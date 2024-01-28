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
import java.util.ArrayList;
import java.util.List;

/** DirectInput implementation of controller environment
 * @author martak
 * @author elias
 * @version 1.0
 */
public final class RawInputEnvironmentPlugin extends ControllerEnvironment implements Plugin {
	
	private static boolean supported = false;

	/**
	 * Static utility method for loading native libraries.
	 * It will try to load from either the path given by
	 * the net.java.games.input.librarypath property
	 * or through System.loadLibrary().
	 * 
	 */
	static void loadLibrary(final String lib_name) {
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
	}
    
	static {
		String osName = System.getProperty("os.name", "").trim();
		if(osName.startsWith("Windows")) {
			supported = true;
			if("x86".equals(System.getProperty("os.arch"))) {
				loadLibrary("jinput-raw");
			} else {
				loadLibrary("jinput-raw_64");
			}
		}
	}

    private final Controller[] controllers;

	/** Creates new DirectInputEnvironment */
	public RawInputEnvironmentPlugin() {
		RawInputEventQueue queue;
		Controller[] controllers = new Controller[]{};
		if(isSupported()) {
			try {
				queue = new RawInputEventQueue();
				controllers = enumControllers(queue);
			} catch (IOException e) {
				log("Failed to enumerate devices: " + e.getMessage());
			}
		}
		this.controllers = controllers;
	}

	public Controller[] getControllers() {
		return controllers;
	}

	private static SetupAPIDevice lookupSetupAPIDevice(String device_name, List<SetupAPIDevice> setupapi_devices) {
		/* First, replace # with / in the device name, since that
		 * seems to be the format in raw input device name
		 */
		device_name = device_name.replaceAll("#", "\\\\").toUpperCase();
		for (int i = 0; i < setupapi_devices.size(); i++) {
			SetupAPIDevice device = setupapi_devices.get(i);
			if (device_name.contains(device.getInstanceId().toUpperCase()))
				return device;
		}
		return null;
	}
	
	private static void createControllersFromDevices(RawInputEventQueue queue, List<Controller> controllers, List<RawDevice> devices, List<SetupAPIDevice> setupapi_devices) throws IOException {
		List<RawDevice> active_devices = new ArrayList<>();
		for (int i = 0; i < devices.size(); i++) {
			RawDevice device = devices.get(i);
			SetupAPIDevice setupapi_device = lookupSetupAPIDevice(device.getName(), setupapi_devices);
			if (setupapi_device == null) {
				/* Either the device is an RDP or we failed to locate the
				 * SetupAPI device that matches
				 */
				continue;
			}
			RawDeviceInfo info = device.getInfo();
			Controller controller = info.createControllerFromDevice(device, setupapi_device);
			if (controller != null) {
				controllers.add(controller);
				active_devices.add(device);
			}
		}
		queue.start(active_devices);
	}

	private static native void enumerateDevices(RawInputEventQueue queue, List<RawDevice> devices) throws IOException;

	private Controller[] enumControllers(RawInputEventQueue queue) throws IOException {
		List<Controller> controllers = new ArrayList<>();
		List<RawDevice> devices = new ArrayList<>();
		enumerateDevices(queue, devices);
		List<SetupAPIDevice> setupapi_devices = enumSetupAPIDevices();
		createControllersFromDevices(queue, controllers, devices, setupapi_devices);
		Controller[] controllers_array = new Controller[controllers.size()];
		controllers.toArray(controllers_array);
		return controllers_array;
	}

	public boolean isSupported() {
		return supported;
	}

	/*
	 * The raw input API, while being able to access
	 * multiple mice and keyboards, is a bit raw (hah)
	 * since it lacks some important features:
	 *
	 * 1. The list of keyboards and the list of mice
	 *    both include useless Terminal Server
	 *    devices (RDP_MOU and RDP_KEY) that we'd
	 *    like to skip.
	 * 2. The device names returned by GetRawInputDeviceInfo()
	 *    are not for display, but instead synthesized
	 *    from a combination of a device instance id
	 *    and a GUID.
	 *
	 * A solution to both problems is the SetupAPI that allows
	 * us to enumerate all keyboard and mouse devices and fetch their
	 * descriptive names and at the same time filter out the unwanted
	 * RDP devices.
	 */
	private static List<SetupAPIDevice> enumSetupAPIDevices() throws IOException {
		List<SetupAPIDevice> devices = new ArrayList<>();
		nEnumSetupAPIDevices(getKeyboardClassGUID(), devices);
		nEnumSetupAPIDevices(getMouseClassGUID(), devices);
		return devices;
	}
	private static native void nEnumSetupAPIDevices(byte[] guid, List<SetupAPIDevice> devices) throws IOException;

	private static native byte[] getKeyboardClassGUID();
	private static native byte[] getMouseClassGUID();

} // class DirectInputEnvironment
