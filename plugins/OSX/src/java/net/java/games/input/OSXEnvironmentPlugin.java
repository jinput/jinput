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
import java.util.HashMap;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.util.plugins.Plugin;

/** OSX HIDManager implementation of controller environment
* @author gregorypierce
* @version 1.0
*/
public class OSXEnvironmentPlugin extends ControllerEnvironment implements Plugin
{
    static
    {
        System.loadLibrary("jinput");
    }

    public native void hidCreate();
    public native void hidDispose();
    public native void enumDevices();

    /**
    * Opens an input device and returns the address of the input queue for that device
    */
    public native long openDevice( long lpDevice, int queueDepth );
    public native void closeDevice( long lpDevice, long lpInputQueue );
    public native void pollDevice( long lpInputQueue );

    private static final int HID_DEVICE_MOUSE = 0x02;
    private static final int HID_DEVICE_JOYSTICK = 0x04;
    private static final int HID_DEVICE_GAMEPAD = 0x05;
    private static final int HID_DEVICE_KEYBOARD = 0x06;


    private HashMap devices = new HashMap();


    public OSXEnvironmentPlugin()
    {
        System.out.println("net.java.games.input.OSXEnvironmentPlugin instance created");
    }

    public Controller[] getControllers()
    {
        return (Controller[])devices.values().toArray();
    }

    public InputController createController( long lpDevice,
                                             String productName,
                                             int usage )
    {

        switch (usage)
        {
            case (HID_DEVICE_MOUSE):
                System.out.println("Found mouse [" + productName + "]");
                return new OSXMouse( this, lpDevice, productName, usage );
            case (HID_DEVICE_JOYSTICK):
                System.out.println("Found joystick [" + productName + "]");
                return new OSXJoystick( this, lpDevice, productName, usage );
            case (HID_DEVICE_GAMEPAD):
                System.out.println("Found gamepad [" + productName + "]");
                return new OSXGamepad( this, lpDevice, productName, usage );
            case (HID_DEVICE_KEYBOARD):
                System.out.println("Found keyboard [" + productName + "]");
                return new OSXKeyboard( this, lpDevice, productName, usage );
            default:
                System.out.println("Found device of unknown type [" + usage + "],[" + productName + "] - ignoring");
                return null;
        }
    }

    private void addController( long lpDevice,
                                 String productName,
                                 int usage )
    {
        InputController controller = null;

        controller = createController( lpDevice, productName, usage );

        if ( controller != null )
        {
            devices.put( productName, controller );
        }
    }

    private void addControllerElement(  long lpDevice,
                                        long hidCookie,
                                        int elementType,
                                        String elementName,
                                        int rawMin,
                                        int rawMax,
                                          int scaledMin,
                                          int scaledMax,
                                          int dataBitSize,
                                          boolean relative,
                                          boolean wrapping,
                                          boolean nonLinear,
                                          boolean hasPreferredState,
                                          boolean hasNullState)
    {
        InputControllerElement element = new InputControllerElement( hidCookie, elementType, elementName,
                                                                     rawMin, rawMax, scaledMin, scaledMax,
                                                                     dataBitSize, relative, wrapping, nonLinear,
                                                                     hasPreferredState, hasNullState );
    }



    public void testDevice( long lpDevice )
    {
        System.out.println("Opening device ");
        long lpQueue = openDevice( lpDevice, 32 );

        for ( int i = 0; i < 50; i++ )
        {
            try
            {
                pollDevice( lpQueue );
                Thread.sleep(10);
            }
            catch( Exception e )
            {
                System.out.println("Interrupted" + e );
            }
        }

        System.out.println("Closing device");
        closeDevice( lpDevice, lpQueue );
    }

    public static void main (String args[])
    {
        System.out.println("Started net.java.games.input.OSXEnvironmentPlugin");
        OSXEnvironmentPlugin newjni = new OSXEnvironmentPlugin();

        System.out.println("Creating HID engine");
        newjni.hidCreate();

        System.out.println("Enumerating devices");
        newjni.enumDevices();

        System.out.println("Disposing HID engine");
        newjni.hidDispose();

        System.out.println("Done");
    }

}