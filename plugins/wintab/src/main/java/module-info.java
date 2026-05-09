import net.java.games.input.ControllerEnvironment;
import net.java.games.input.wintab.WinTabEnvironmentPlugin;

open module net.java.games.input.wintab {
	requires transitive net.java.games.input;
	requires transitive net.java.games.input.windows;
	provides ControllerEnvironment with WinTabEnvironmentPlugin;
}
