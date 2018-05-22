/**
 * Copyright (C) 2006 Jeremy Booth (jeremy@newdawnsoftware.com)
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this 
 * list of conditions and the following disclaimer. Redistributions in binary 
 * form must reproduce the above copyright notice, this list of conditions and 
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. 
 * The name of the author may not be used to endorse or promote products derived
 * from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO 
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
 */
package net.java.games.input;

import java.io.File;
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
					    try {
    						String lib_path = System.getProperty("net.java.games.input.librarypath");
    						if (lib_path != null)
    							System.load(lib_path + File.separator + System.mapLibraryName(lib_name));
    						else
    							System.loadLibrary(lib_name);
					    } catch (UnsatisfiedLinkError e) {
					        e.printStackTrace();
					        supported = false;
					    }
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
