#!/usr/bin/env bash
#
# Build the JInput Linux native plugin and install it into a JMRI
# installation as libjinput-linux64.so.
#
# Run from the root of a checked-out https://github.com/jinput/jinput clone:
#   ./build-and-install-jmri.sh
#
# Override the destination if needed:
#   JMRI_LIB_DIR=/opt/JMRI/lib/linux/aarch64 ./build-and-install-jmri.sh
#
set -euo pipefail

# --- config ----------------------------------------------------------------
JMRI_LIB_DIR="${JMRI_LIB_DIR:-/home/pi/JMRI/lib/linux/aarch64}"
TARGET_NAME="libjinput-linux64.so"
# --------------------------------------------------------------------------

# 1. Sanity: must be at the root of a jinput checkout.
if [[ ! -f pom.xml || ! -d plugins/linux ]]; then
    echo "ERROR: run this from the root of a jinput checkout" >&2
    echo "       (current dir: $(pwd))" >&2
    exit 1
fi

# 2. Sanity: required tools.
for tool in mvn cc strip uname java; do
    if ! command -v "$tool" >/dev/null 2>&1; then
        echo "ERROR: required tool '$tool' not found in PATH" >&2
        echo "       install with: sudo apt install build-essential maven openjdk-21-jdk-headless" >&2
        exit 1
    fi
done

ARCH="$(uname -m)"
echo ">>> Detected arch: ${ARCH}"
echo ">>> JMRI lib dir:  ${JMRI_LIB_DIR}"

# 3. Make sure the destination exists and is writable; re-exec under sudo if not.
if [[ ! -d "$JMRI_LIB_DIR" ]]; then
    echo "ERROR: destination does not exist: $JMRI_LIB_DIR" >&2
    echo "       (is JMRI installed? edit JMRI_LIB_DIR in this script or pass it as env var)" >&2
    exit 1
fi

if [[ ! -w "$JMRI_LIB_DIR" && $EUID -ne 0 ]]; then
    echo ">>> $JMRI_LIB_DIR is not writable by $(whoami); re-running install step under sudo"
    NEED_SUDO=1
else
    NEED_SUDO=0
fi

# 4. Build with the install-jmri profile (which we added to plugins/linux/pom.xml).
echo ">>> Building JInput Linux native plugin..."
mvn --batch-mode \
    --also-make \
    --projects plugins/linux \
    -P install-jmri \
    -Djmri.lib.dir="$JMRI_LIB_DIR" \
    clean compile

# 5. Verify the artifact actually got produced.
BUILT_SO="$(ls -1 plugins/linux/target/natives/libjinput-linux*.so 2>/dev/null | head -n1 || true)"
if [[ -z "$BUILT_SO" ]]; then
    echo "ERROR: build finished but no .so was produced under plugins/linux/target/natives/" >&2
    exit 1
fi
echo ">>> Built: $BUILT_SO"

# 6. If the Maven profile copy was blocked by permissions, do the copy via sudo now.
DEST="$JMRI_LIB_DIR/$TARGET_NAME"
if [[ ! -f "$DEST" || "$BUILT_SO" -nt "$DEST" ]]; then
    echo ">>> Copying to $DEST"
    if [[ $NEED_SUDO -eq 1 ]]; then
        sudo install -m 0644 "$BUILT_SO" "$DEST"
    else
        install -m 0644 "$BUILT_SO" "$DEST"
    fi
else
    echo ">>> $DEST is already up to date (Maven profile installed it)"
fi

# 7. Final sanity check.
if [[ -f "$DEST" ]]; then
    echo ""
    echo "SUCCESS: $(file "$DEST")"
    echo ""
    echo "Restart JMRI to pick up the new library."
    echo "If JMRI was running during this build, the old .so is still mapped"
    echo "in the existing JVM until you exit and relaunch."
else
    echo "ERROR: install step did not produce $DEST" >&2
    exit 1
fi

