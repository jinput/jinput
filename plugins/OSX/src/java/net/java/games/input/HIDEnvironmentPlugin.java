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

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.util.plugins.Plugin;

/** OSX HIDManager implementation of controller environment
* @author gregorypierce
* @version 1.0
*/
public class HIDEnvironmentPlugin extends ControllerEnvironment implements Plugin
{
    
    static 
    {
        System.loadLibrary("hidinput");
    }
    
    private static final int HIDDEVTYPE_MOUSE    =        0x02;
    private static final int HIDDEVTYPE_JOYSTICK =        0x04;
    private static final int HIDDEVTYPE_GAMEPAD  =        0x05;
    private static final int HIDDEVTYPE_KEYBOARD =        0x06;
    
    // Permanent array of controllers
    private Controller[] controllers;
    
    
    /**
    * Returns the direct input instance, or 0 if failed to initialize
     */
    private native void hidInputCreate();
    
    /**
        * Enumerates all devices, calling createDevice for each one
     */
    private native boolean enumDevices(ArrayList list);    
    
    /** Creates new DirectInputEnvironment */
    public HIDEnvironmentPlugin() 
    {
        hidInputCreate();
        enumControllers();
    }
    
    /** Returns a list of all controllers available to this environment,
        * or an empty array if there are no controllers in this environment.
        * @return An array of controllers that may be empty.
        */
    public Controller[] getControllers() 
    {
        return controllers;
    }
    
    private void enumControllers() 
    {
        controllers = new Controller[] {};

        // Create temporary controller array
        ArrayList tempDevices = new ArrayList();
        
        // Eumerate devices
        enumDevices(tempDevices);
        
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
                           int type, String productName, String instanceName, boolean polled) 
    {
        Controller c;

    }

}