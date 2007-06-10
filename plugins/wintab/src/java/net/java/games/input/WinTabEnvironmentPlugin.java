package net.java.games.input;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

import net.java.games.util.plugins.Plugin;

public class WinTabEnvironmentPlugin extends ControllerEnvironment implements Plugin {
	private static boolean supported = false;
	
	/**
	 * Static utility method for loading native libraries.
	 * It will try to load from either the path given by
	 * the net.java.games.input.librarypath property
	 * or through System.loadLibrary().
	 * 
	 */
	static void loadLibrary(final String lib_name) {
		AccessController.doPrivileged(
				new PrivilegedAction() {
					public final Object run() {
						String lib_path = System.getProperty("net.java.games.input.librarypath");
						if (lib_path != null)
							System.load(lib_path + File.separator + System.mapLibraryName(lib_name));
						else
							System.loadLibrary(lib_name);
						return null;
					}
				});
	}
    
	static String getPrivilegedProperty(final String property) {
	       return (String)AccessController.doPrivileged(new PrivilegedAction() {
	                public Object run() {
	                    return System.getProperty(property);
	                }
	            });
		}
		

	static String getPrivilegedProperty(final String property, final String default_value) {
       return (String)AccessController.doPrivileged(new PrivilegedAction() {
                public Object run() {
                    return System.getProperty(property, default_value);
                }
            });
	}
		
    static {
    	String osName = getPrivilegedProperty("os.name", "").trim();
    	if(osName.startsWith("Windows")) {
    		supported = true;
    		loadLibrary("jinput-wintab");
    	}
	}

	private final Controller[] controllers;
	private final List active_devices = new ArrayList();
	private final DummyWindow window;
	private final WinTabContext winTabContext;

	/** Creates new DirectInputEnvironment */
	public WinTabEnvironmentPlugin() {
		if(isSupported()) {
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
				logln("Failed to enumerate devices: " + e.getMessage());
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
		} else {
			winTabContext = null;
			controllers = new Controller[]{};
			window = null;
		}
	}
	
	public boolean isSupported() {
		return supported;
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
