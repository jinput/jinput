import net.java.games.input.AbstractControllerEnvironment;
import net.java.games.input.osx.OSXEnvironmentPlugin;

open module net.java.games.input.osx {
	requires transitive net.java.games.input;
	requires java.logging;
	provides AbstractControllerEnvironment with OSXEnvironmentPlugin;
}
