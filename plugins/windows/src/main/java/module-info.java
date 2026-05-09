import net.java.games.input.AbstractControllerEnvironment;
import net.java.games.input.windows.DirectAndRawInputEnvironmentPlugin;
import net.java.games.input.windows.DirectInputEnvironmentPlugin;
import net.java.games.input.windows.RawInputEnvironmentPlugin;

open module net.java.games.input.windows {
	exports net.java.games.input.windows;
	requires transitive net.java.games.input;
	requires java.logging;
	provides AbstractControllerEnvironment with DirectAndRawInputEnvironmentPlugin, DirectInputEnvironmentPlugin, RawInputEnvironmentPlugin;
}
