package net.java.games.input;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

import net.java.games.util.plugins.Plugin;

public class WinTabEnvironmentPlugin extends ControllerEnvironment implements Plugin {
	static {
		DefaultControllerEnvironment.loadLibrary("jinput-wintab");
	}

	private final Controller[] controllers;
	private final List active_devices = new ArrayList();
	private final DummyWindow window;
	private final WinTabContext winTabContext;

	/** Creates new DirectInputEnvironment */
	public WinTabEnvironmentPlugin() {
		DummyWindow window = null;
		WinTabContext winTabContext = null;
		Controller[] controllers = new Controller[]{};
		try {
			window = new DummyWindow();
			winTabContext = new WinTabContext(window);
			try {
				winTabContext.open();
				controllers = winTabContext.getControllers();
			} catch (Exception e) {
				window.destroy();
				throw e;
			}
		} catch (Exception e) {
			ControllerEnvironment.logln("Failed to enumerate devices: " + e.getMessage());
			e.printStackTrace();
		}
		this.window = window;
		this.controllers = controllers;
		this.winTabContext = winTabContext;
		AccessController.doPrivileged(
				new PrivilegedAction() {
					public final Object run() {
						Runtime.getRuntime().addShutdownHook(new ShutdownHook());
						return null;
					}
				});
	}
	
	public Controller[] getControllers() {
		return controllers;
	}

	private final class ShutdownHook extends Thread {
		public final void run() {
			/* Release the devices to kill off active force feedback effects */
			for (int i = 0; i < active_devices.size(); i++) {
				// TODO free the devices
			}
			//Close the context
			winTabContext.close();
			/* We won't release the window since it is
			 * owned by the thread that created the environment.
			 */
		}
	}
}
