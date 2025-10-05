open module net.java.games.input.example {
	exports net.java.games.input.example;
	requires java.logging;
	requires transitive net.java.games.input;
	requires static net.java.games.input.linux;
	requires static net.java.games.input.osx;
	requires static net.java.games.input.windows;
	requires static net.java.games.input.wintab;
}
