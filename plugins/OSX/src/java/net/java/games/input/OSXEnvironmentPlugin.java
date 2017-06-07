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
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/** OSX HIDManager implementation
* @author elias
* @author gregorypierce
* @version 1.0
*/
public final class OSXEnvironmentPlugin extends ControllerEnvironment implements Plugin {
	
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
    							if (lib_path.indexOf(lib_name) != -1) {
									System.load(lib_path);
								} else {
    								System.load(lib_path + File.separator + System.mapLibraryName(lib_name));
								}
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
    	if(osName.equals("Mac OS X")) {
    		// Could check isMacOSXEqualsOrBetterThan in here too.
    		supported = true;
    		loadLibrary("jinput-osx");
    	}
    }

	private final static boolean isMacOSXEqualsOrBetterThan(int major_required, int minor_required) {
		String os_version = System.getProperty("os.version");
		StringTokenizer version_tokenizer = new StringTokenizer(os_version, ".");
		int major;
		int minor;
		try {
			String major_str = version_tokenizer.nextToken();
			String minor_str = version_tokenizer.nextToken();
			major = Integer.parseInt(major_str);
			minor = Integer.parseInt(minor_str);
		} catch (Exception e) {
			logln("Exception occurred while trying to determine OS version: " + e);
			// Best guess, no
			return false;
		}
		return major > major_required || (major == major_required && minor >= minor_required);
	}

	private final Controller[] controllers;

	public OSXEnvironmentPlugin() {
		if(isSupported()) {
			this.controllers = enumerateControllers();
		} else {
			this.controllers = new Controller[0];
		}
	}

	public final Controller[] getControllers() {
		return controllers;
	}

	public boolean isSupported() {
		return supported;
	}

	private final static void addElements(OSXHIDQueue queue, List elements, List components, boolean map_mouse_buttons) throws IOException {
		Iterator it = elements.iterator();
		while (it.hasNext()) {
			OSXHIDElement element = (OSXHIDElement)it.next();
			Component.Identifier id = element.getIdentifier();
			if (id == null)
				continue;
			if (map_mouse_buttons) {
				if (id == Component.Identifier.Button._0) {
					id = Component.Identifier.Button.LEFT;
				} else if (id == Component.Identifier.Button._1) {
					id = Component.Identifier.Button.RIGHT;
				} else if (id == Component.Identifier.Button._2) {
					id = Component.Identifier.Button.MIDDLE;
				}
			}
			OSXComponent component = new OSXComponent(id, element);
			components.add(component);
			queue.addElement(element, component);
		}
	}

	private final static Keyboard createKeyboardFromDevice(OSXHIDDevice device, List elements) throws IOException {
		List components = new ArrayList();
		OSXHIDQueue queue = device.createQueue(AbstractController.EVENT_QUEUE_DEPTH);
		try {
			addElements(queue, elements, components, false);
		} catch (IOException e) {
			queue.release();
			throw e;
		}
		Component[] components_array = new Component[components.size()];
		components.toArray(components_array);
		Keyboard keyboard = new OSXKeyboard(device, queue, components_array, new Controller[]{}, new Rumbler[]{});
		return keyboard;
	}

	private final static Mouse createMouseFromDevice(OSXHIDDevice device, List elements) throws IOException {
		List components = new ArrayList();
		OSXHIDQueue queue = device.createQueue(AbstractController.EVENT_QUEUE_DEPTH);
		try {
			addElements(queue, elements, components, true);
		} catch (IOException e) {
			queue.release();
			throw e;
		}
		Component[] components_array = new Component[components.size()];
		components.toArray(components_array);
		Mouse mouse = new OSXMouse(device, queue, components_array, new Controller[]{}, new Rumbler[]{});
		if (mouse.getPrimaryButton() != null && mouse.getX() != null && mouse.getY() != null) {
			return mouse;
		} else {
			queue.release();
			return null;
		}
	}
	
	private final static AbstractController createControllerFromDevice(OSXHIDDevice device, List elements, Controller.Type type) throws IOException {
		List components = new ArrayList();
		OSXHIDQueue queue = device.createQueue(AbstractController.EVENT_QUEUE_DEPTH);
		try {
			addElements(queue, elements, components, false);
		} catch (IOException e) {
			queue.release();
			throw e;
		}
		Component[] components_array = new Component[components.size()];
		components.toArray(components_array);
		AbstractController controller = new OSXAbstractController(device, queue, components_array, new Controller[]{}, new Rumbler[]{}, type);
		return controller;
	}

	private final static void createControllersFromDevice(OSXHIDDevice device, List controllers) throws IOException {
		UsagePair usage_pair = device.getUsagePair();
		if (usage_pair == null)
			return;
		List elements = device.getElements();
		if (usage_pair.getUsagePage() == UsagePage.GENERIC_DESKTOP && (usage_pair.getUsage() == GenericDesktopUsage.MOUSE ||
					usage_pair.getUsage() == GenericDesktopUsage.POINTER)) {
			Controller mouse = createMouseFromDevice(device, elements);
			if (mouse != null)
				controllers.add(mouse);
		} else if (usage_pair.getUsagePage() == UsagePage.GENERIC_DESKTOP && (usage_pair.getUsage() == GenericDesktopUsage.KEYBOARD ||
					usage_pair.getUsage() == GenericDesktopUsage.KEYPAD)) {
			Controller keyboard = createKeyboardFromDevice(device, elements);
			if (keyboard != null)
				controllers.add(keyboard);
		} else if (usage_pair.getUsagePage() == UsagePage.GENERIC_DESKTOP && usage_pair.getUsage() == GenericDesktopUsage.JOYSTICK) {
			Controller joystick = createControllerFromDevice(device, elements, Controller.Type.STICK);
			if (joystick != null)
				controllers.add(joystick);
		} else if (usage_pair.getUsagePage() == UsagePage.GENERIC_DESKTOP && usage_pair.getUsage() == GenericDesktopUsage.MULTI_AXIS_CONTROLLER) {
			Controller multiaxis = createControllerFromDevice(device, elements, Controller.Type.STICK);
			if (multiaxis != null)
				controllers.add(multiaxis);
		} else if (usage_pair.getUsagePage() == UsagePage.GENERIC_DESKTOP && usage_pair.getUsage() == GenericDesktopUsage.GAME_PAD) {
			Controller game_pad = createControllerFromDevice(device, elements, Controller.Type.GAMEPAD);
			if (game_pad != null)
				controllers.add(game_pad);
		}
	}

	private final static Controller[] enumerateControllers() {
		List controllers = new ArrayList();
		try {
			OSXHIDDeviceIterator it = new OSXHIDDeviceIterator();
			try {
				while (true) {
					OSXHIDDevice device;
					try {
						device = it.next();
						if (device == null)
							break;
						boolean device_used = false;
						try {
							int old_size = controllers.size();
							createControllersFromDevice(device, controllers);
							device_used = old_size != controllers.size();
						} catch (IOException e) {
							logln("Failed to create controllers from device: " + device.getProductName());
						}
						if (!device_used)
							device.release();
					} catch (IOException e) {
						logln("Failed to enumerate device: " + e.getMessage());
					}
				}
			} finally {
				it.close();
			}
		} catch (IOException e) {
			log("Failed to enumerate devices: " + e.getMessage());
			return new Controller[]{};
		}
		Controller[] controllers_array = new Controller[controllers.size()];
		controllers.toArray(controllers_array);
		return controllers_array;
	}
}
