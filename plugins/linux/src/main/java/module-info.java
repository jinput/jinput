import net.java.games.input.AbstractControllerEnvironment;
import net.java.games.input.linux.LinuxEnvironmentPlugin;

open module net.java.games.input.linux {
	requires transitive net.java.games.input;
	requires java.logging;
	provides AbstractControllerEnvironment with LinuxEnvironmentPlugin;
}
