/*
 * %W% %E%
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
/*****************************************************************************
 * Copyright (c) 2003 Sun Microsystems, Inc.  All Rights Reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistribution of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materails provided with the distribution.
 *
 * Neither the name Sun Microsystems, Inc. or the names of the contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANT OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMEN, ARE HEREBY EXCLUDED.  SUN MICROSYSTEMS, INC. ("SUN") AND
 * ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS
 * A RESULT OF USING, MODIFYING OR DESTRIBUTING THIS SOFTWARE OR ITS 
 * DERIVATIVES.  IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES.  HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OUR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed or intended for us in
 * the design, construction, operation or maintenance of any nuclear facility
 *
 *****************************************************************************/
package net.java.games.input;

import net.java.games.util.plugins.Plugin;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

/** DirectInput implementation of controller environment
 * @author martak
 * @author elias
 * @version 1.0
 */
public final class DirectInputEnvironmentPlugin extends ControllerEnvironment implements Plugin {
	
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
							if (lib_path != null) {
								String[] lib_path_parts = lib_path.split(";");
								boolean found = false;
								for (int i = 0; i < lib_path_parts.length; i++) {
									if (lib_path_parts[i].indexOf(lib_name) != -1) {
										System.load(lib_path);
										found = true;
										break;
									}
								}
								if (!found)
									System.load(lib_path + File.separator + System.mapLibraryName(lib_name));
							} else {
								System.loadLibrary(lib_name);
							}
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
			if("x86".equals(getPrivilegedProperty("os.arch"))) {
				loadLibrary("jinput-dx8");
			} else {
				loadLibrary("jinput-dx8_64");
			}
		}
	}

	private final Controller[] controllers;
	private final List active_devices = new ArrayList();
	private final DummyWindow window;

	/** Creates new DirectInputEnvironment */
	public DirectInputEnvironmentPlugin() {		
		DummyWindow window = null;
		Controller[] controllers = new Controller[]{};
		if(isSupported()) {
			try {
				window = new DummyWindow();
				try {
					controllers = enumControllers(window);
				} catch (IOException e) {
					window.destroy();
					throw e;
				}
			} catch (IOException e) {
				logln("Failed to enumerate devices: " + e.getMessage());
			}
			this.window = window;
			this.controllers = controllers;
			AccessController.doPrivileged(
					new PrivilegedAction() {
						public final Object run() {
							Runtime.getRuntime().addShutdownHook(new ShutdownHook());
							return null;
						}
					});
		} else {
			// These are final fields, so can't set them, then over ride 
			// them if we are supported.
			this.window = null;
			this.controllers = controllers;
		}
	}

	public final Controller[] getControllers() {
		return controllers;
	}

	private final Component[] createComponents(IDirectInputDevice device, boolean map_mouse_buttons) {
		List device_objects = device.getObjects();
		List controller_components = new ArrayList();
		for (int i = 0; i < device_objects.size(); i++) {
			DIDeviceObject device_object = (DIDeviceObject)device_objects.get(i);
			Component.Identifier identifier = device_object.getIdentifier();
			if (identifier == null)
				continue;
			if (map_mouse_buttons && identifier instanceof Component.Identifier.Button) {
				identifier = DIIdentifierMap.mapMouseButtonIdentifier((Component.Identifier.Button)identifier);
			}
			DIComponent component = new DIComponent(identifier, device_object);
			controller_components.add(component);
			device.registerComponent(device_object, component);
		}
		Component[] components = new Component[controller_components.size()];
		controller_components.toArray(components);
		return components;
	}
		
	private final Mouse createMouseFromDevice(IDirectInputDevice device) {
		Component[] components = createComponents(device, true);
		Mouse mouse = new DIMouse(device, components, new Controller[]{}, device.getRumblers());
		if (mouse.getX() != null && mouse.getY() != null && mouse.getPrimaryButton() != null)
			return mouse;
		else
			return null;
	}

	private final AbstractController createControllerFromDevice(IDirectInputDevice device, Controller.Type type) {
		Component[] components = createComponents(device, false);
		AbstractController controller = new DIAbstractController(device, components, new Controller[]{}, device.getRumblers(), type);
		return controller;
	}

	private final Keyboard createKeyboardFromDevice(IDirectInputDevice device) {
		Component[] components = createComponents(device, false);
		return new DIKeyboard(device, components, new Controller[]{}, device.getRumblers());
	}

	private final Controller createControllerFromDevice(IDirectInputDevice device) {
		switch (device.getType()) {
			case IDirectInputDevice.DI8DEVTYPE_MOUSE:
				return createMouseFromDevice(device);
			case IDirectInputDevice.DI8DEVTYPE_KEYBOARD:
				return createKeyboardFromDevice(device);
			case IDirectInputDevice.DI8DEVTYPE_GAMEPAD:
				return createControllerFromDevice(device, Controller.Type.GAMEPAD);
			case IDirectInputDevice.DI8DEVTYPE_DRIVING:
				return createControllerFromDevice(device, Controller.Type.WHEEL);
			case IDirectInputDevice.DI8DEVTYPE_1STPERSON:
				/* Fall through */
			case IDirectInputDevice.DI8DEVTYPE_FLIGHT:
				/* Fall through */
			case IDirectInputDevice.DI8DEVTYPE_JOYSTICK:
				return createControllerFromDevice(device, Controller.Type.STICK);
			default:
				return createControllerFromDevice(device, Controller.Type.UNKNOWN);
		}
	}
	
	private final Controller[] enumControllers(DummyWindow window) throws IOException {
		List controllers = new ArrayList();
		IDirectInput dinput = new IDirectInput(window);
		try {
			List devices = dinput.getDevices();
			for (int i = 0; i < devices.size(); i++) {
				IDirectInputDevice device = (IDirectInputDevice)devices.get(i);
				Controller controller = createControllerFromDevice(device);
				if (controller != null) {
					controllers.add(controller);
					active_devices.add(device);
				} else
					device.release();
			}
		} finally {
			dinput.release();
		}
		Controller[] controllers_array = new Controller[controllers.size()];
		controllers.toArray(controllers_array);
		return controllers_array;
	}

	private final class ShutdownHook extends Thread {
		public final void run() {
			/* Release the devices to kill off active force feedback effects */
			for (int i = 0; i < active_devices.size(); i++) {
				IDirectInputDevice device = (IDirectInputDevice)active_devices.get(i);
				device.release();
			}
			/* We won't release the window since it is
			 * owned by the thread that created the environment.
			 */
		}
	}

	public boolean isSupported() {
		return supported;
	}
} // class DirectInputEnvironment
