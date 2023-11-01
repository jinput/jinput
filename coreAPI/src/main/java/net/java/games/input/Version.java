package net.java.games.input;

/** A wrapper for {@link PomProperties#getVersion()}, to ensure backwards compatibility with older versions of JInput. */
public class Version {
    /** Private constructor to prevent instantiation. */
    private Version() {}

    /**
     * <p>Retrieves the version of this implementation.</p>
     *
     * <p>See {@link PomProperties#getVersion()}.</p>
     *
     * @return The version of this implementation.
     */
    public static String getVersion() {
        return PomProperties.getVersion();
    }
}
