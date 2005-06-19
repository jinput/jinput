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

import java.util.ArrayList;
import java.util.Iterator;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
//import sun.security.action.LoadLibraryAction;
import net.java.games.util.plugins.Plugin;

/** DirectInput implementation of controller environment
 * @author martak
 * @version 1.0
 */
public class DirectInputEnvironmentPlugin extends ControllerEnvironment 
    implements Plugin
{

    static {
        /** Mikes old code, causes it to be laoded by wrong loader
        java.security.AccessController.doPrivileged(
            new LoadLibraryAction("jinput"));
         */
        if(isSupported()) {
            System.loadLibrary("jinput-dxplugin");
        }
    }

    /**
     * DIDEVTYPE_ constants from dinput.h header file
     * JPK NOTE: changed in DX8 so had to be changed.  This is
     * fragile, we should find a way to set them from
     * the C side sowe can stay up to date with header
     */
    /** DX7 and earlier 
    private static final int DIDEVTYPE_DEVICE = 1;
    private static final int DIDEVTYPE_MOUSE = 2;
    private static final int DIDEVTYPE_KEYBOARD = 3;
    private static final int DIDEVTYPE_JOYSTICK = 4;
    private static final int DIDEVTYPE_HID = 0x00010000;
    private static int GET_DIDEVICE_TYPE(int dwDevType) {
        return (int)((byte)dwDevType);
    }
    private static int GET_DIDEVICE_SUBTYPE(int dwDevType) {
        return (int)((byte)((((short)dwDevType) >> 8) & 0xFF));
    }
    **/
    /* DX8 and 9 */
    private static final int DI8DEVTYPE_DEVICE   =        0x11;
    private static final int DI8DEVTYPE_MOUSE    =        0x12;
    private static final int DI8DEVTYPE_KEYBOARD =        0x13;
    private static final int DI8DEVTYPE_JOYSTICK =        0x14;
    private static final int DI8DEVTYPE_GAMEPAD  =        0x15;
    private static final int DI8DEVTYPE_DRIVING  =        0x16;
    private static final int DI8DEVTYPE_FLIGHT   =        0x17;
    private static final int DI8DEVTYPE_1STPERSON  =      0x18;
    private static final int DI8DEVTYPE_DEVICECTRL =      0x19;
    private static final int DI8DEVTYPE_SCREENPOINTER =   0x1A;
    private static final int DI8DEVTYPE_REMOTE        =   0x1B;
    private static final int DI8DEVTYPE_SUPPLEMENTAL  =   0x1C;
    
    private static int GET_DIDEVICE_TYPE(int dwDevType) {
        return (dwDevType&0xFF);
    }
    private static int GET_DIDEVICE_SUBTYPE(int dwDevType) {
        return (int)((byte)((((short)dwDevType) >> 8) & 0xFF));
    }

    // Pointer to DirectInput instance
    private long lpDirectInput;
    // Permanent array of controllers
    private Controller[] controllers;
    
    /** Creates new DirectInputEnvironment */
    public DirectInputEnvironmentPlugin() {
        if(isSupported()) {
            lpDirectInput = directInputCreate();
            enumControllers();
        } else {
            controllers = new Controller[0];
        }
    }
    
    public static boolean isSupported() {
        System.out.println("OS name is: " + System.getProperty("os.name"));
        if(System.getProperty("os.name").indexOf("Windows")!=-1) {
            System.out.println("DX8 plugin is supported");
            return true;
        }
        System.out.println("DX8 plugin is not supported");
        return false;
    }

    /** Returns a list of all controllers available to this environment,
     * or an empty array if there are no controllers in this environment.
     * @return An array of controllers that may be empty.
     */
    public Controller[] getControllers() {
        return controllers;
    }
    
    private void enumControllers() {
        // If direct input fails, create an empty array
        if (lpDirectInput == 0) {
            controllers = new Controller[] {};
            return;
        }
        // Create temporary controller array
        ArrayList tempDevices = new ArrayList();
        // Eumerate devices
        enumDevices(lpDirectInput, tempDevices);
        // Set up permanent controller array
        controllers = new Controller[tempDevices.size()];
        Iterator it = tempDevices.iterator();
        int i = 0;
        while (it.hasNext()) {
            controllers[i] = (Controller)it.next();
            i++;
        }
    }
    
    /**
     * Creates a new device, adding it to the list supplied.
     * @param lpDevice A pointer to the IDirectInputDevice for the device.
     * @param type The type of device to create, as defined by the constants
     * in dinput.h (see DI8DEVTYPE constants above).
     * @param productName The product name for the device
     * @param instanceName The name of the device
     */
    private void addDevice(ArrayList list, long lpDevice,
        int type, String productName, String instanceName, boolean polled) {
        Controller c;
        int category = GET_DIDEVICE_TYPE(type);
        int subtype = GET_DIDEVICE_SUBTYPE(type);
        //System.out.println("Category = "+category);
        if (category == DI8DEVTYPE_MOUSE) {
            c = DirectInputMouse.createMouse(lpDevice, subtype, productName,
                instanceName);
        } else if (category == DI8DEVTYPE_KEYBOARD) {
            c = DirectInputKeyboard.createKeyboard(lpDevice, subtype,
                productName, instanceName);
        } else {
            // commented out the assert as we have already got devices that are
            // gamepad type, but wr are still going to create them as
            // DirectInputDevices
            //assert category == DI8DEVTYPE_JOYSTICK;
            c = DirectInputDevice.createDevice(lpDevice, subtype, productName,
                instanceName,polled);
        }
        if (c != null) {
            list.add(c);
        }
    }

    /**
     * Returns the direct input instance, or 0 if failed to initialize
     */
    private native long directInputCreate();

    /**
     * Enumerates all devices, calling createDevice for each one
     */
    private native boolean enumDevices(long lpDirectInput, ArrayList list);
} // class DirectInputEnvironment
