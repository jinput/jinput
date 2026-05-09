import net.java.games.input.AbstractControllerEnvironment;
import net.java.games.input.awt.AWTEnvironmentPlugin;

open module net.java.games.input.awt {
	exports net.java.games.input.awt;
	requires transitive net.java.games.input;
	requires java.desktop;
	provides AbstractControllerEnvironment with AWTEnvironmentPlugin;
}
