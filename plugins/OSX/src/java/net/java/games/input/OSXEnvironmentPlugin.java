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


    public static final int HID_DEVICE_MOUSE                        = 0x02;
    public static final int HID_DEVICE_JOYSTICK                     = 0x04;
    public static final int HID_DEVICE_GAMEPAD                      = 0x05;
    public static final int HID_DEVICE_KEYBOARD                     = 0x06;


    public static final int HID_USAGE_POINTER                       = 0x01;
    public static final int HID_USAGE_XAXIS                         = 0x30;
    public static final int HID_USAGE_YAXIS                         = 0x31;
    public static final int HID_USAGE_ZAXIS                         = 0x32;
    public static final int HID_USAGE_XAXIS_ROTATION                = 0x33;
    public static final int HID_USAGE_YAXIS_ROTATION                = 0x34;
    public static final int HID_USAGE_ZAXIS_ROTATION                = 0x35;
    public static final int HID_USAGE_SLIDER                        = 0x36;
    public static final int HID_USAGE_DIAL                          = 0x37;
    public static final int HID_USAGE_WHEEL                         = 0x38;
    public static final int HID_USAGE_HAT                           = 0x39;
    public static final int HID_USAGE_DPAD_UP                       = 0x90;
    public static final int HID_USAGE_DPAD_DOWN                     = 0x91;
    public static final int HID_USAGE_DPAD_LEFT                     = 0x92;
    public static final int HID_USAGE_DPAD_RIGHT                    = 0x93;

    public static final int HID_USAGEPAGE_UNDEFINED	                = 0x00;
    public static final int HID_USAGEPAGE_GENERICDESKTOP	        = 0x01;
    public static final int HID_USAGEPAGE_SIMULATION                = 0x02;
    public static final int HID_USAGEPAGE_VR	                    = 0x03;
    public static final int HID_USAGEPAGE_SPORT	                    = 0x04;
    public static final int HID_USAGEPAGE_GAME	                    = 0x05;
    public static final int HID_USAGEPAGE_KEYBOARD	                = 0x07;	/* USB Device Class Definition for Human Interface Devices (HID). Note: the usage type for all key codes is Selector (Sel). */
    public static final int HID_USAGEPAGE_LED	                    = 0x08;
    public static final int HID_USAGEPAGE_BUTTON	                = 0x09;
    public static final int HID_USAGEPAGE_ORDINAL	                = 0x0A;
    public static final int HID_USAGEPAGE_TELEPHONY	                = 0x0B;
    public static final int HID_USAGEPAGE_CONSUMER	                = 0x0C;
    public static final int HID_USAGEPAGE_DIGITIZER	                = 0x0D;
    public static final int HID_USAGEPAGE_PID	                    = 0x0F;	/* USB Physical Interface Device definitions for force feedback and related devices. */
    public static final int HID_USAGEPAGE_UNICODE	                = 0x10;
    public static final int HID_USAGEPAGE_ALPHANUMERIC_DISPLAY      = 0x14;
    public static final int HID_USAGEPAGE_POWERDEVICE               = 0x84; 				/* Power Device Page */
    public static final int HID_USAGEPAGE_BATTERY_SYSTEM            = 0x85; 				/* Battery System Page */
    public static final int HID_USAGEPAGE_BARCODE_SCANNER           = 0x8C;	/* (Point of Sale) USB Device Class Definition for Bar Code Scanner Devices */
    public static final int HID_USAGEPAGE_SCALE                     = 0x8D;	/* (Point of Sale) USB Device Class Definition for Scale Devices */
    public static final int HID_USAGEPAGE_CAMERA_CONTROL            = 0x90;	/* USB Device Class Definition for Image Class Devices */
    public static final int HID_USAGEPAGE_ARCADE                    = 0x91;	/* OAAF Definitions for arcade and coinop related Devices */
    public static final int HID_USAGEPAGE_VENDOR_DEFINED_START      = 0xFF00;


	public static final int HID_ELEMENTTYPE_INPUT_MISC              = 1;
	public static final int HID_ELEMENTTYPE_INPUT_BUTTON            = 2;
	public static final int HID_ELEMENTTYPE_INPUT_AXIS              = 3;
	public static final int HID_ELEMENTTYPE_INPUT_SCANCODES         = 4;
	public static final int HID_ELEMENTTYPE_OUTPUT                  = 129;
	public static final int HID_ELEMENTTYPE_FEATURE                 = 257;
	public static final int HID_ELEMENTTYPE_COLLECTION              = 513;


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
    public native void closeDevice( long lpDevice, long lpQueue );


    /**
     * Polls a device and returns the element top most on the input queue. The elements that
     * are returned are only those that have had their hidCookies registered with registerDeviceElement.
     * @param lpQueue
     * @return
     */
    public native int pollDevice( long lpQueue );
    public native int pollElement( long lpDevice, long hidCookie );
    public native void registerDeviceElement( long lpQueue, long hidCookie );
    public native void deregisterDeviceElement( long lpQueue, long hidCookie );


    private HashMap devices = new HashMap();


    public OSXEnvironmentPlugin()
    {
        System.out.println("net.java.games.input.OSXEnvironmentPlugin instance created");
    }

    public Controller[] getControllers()
    {
        return (Controller[])devices.values().toArray();
    }

    public Controller createController( long lpDevice, String productName, int usage )
    {

        switch (usage)
        {
            case (HID_DEVICE_MOUSE):
                System.out.println("Found mouse [" + productName + "] device address [" + lpDevice + "]");
                return new OSXMouse( this, lpDevice, productName );
            case (HID_DEVICE_JOYSTICK):
                System.out.println("Found joystick [" + productName + "] device address [" + lpDevice + "]");
                return new OSXJoystick( this, lpDevice, productName );
            case (HID_DEVICE_GAMEPAD):
                System.out.println("Found gamepad [" + productName + "] device address [" + lpDevice + "]");
                return new OSXGamepad( this, lpDevice, productName );
            case (HID_DEVICE_KEYBOARD):
                System.out.println("Found keyboard [" + productName + "] device address [" + lpDevice + "]");
                return new OSXKeyboard( this, lpDevice, productName );

            default:
                System.out.println("Found device of unknown or unsupported type. Usage[" + usage + "], ProductName[" + productName + "] - ignoring");
                return null;
        }
    }

    /**
     * Add a controller to the device list
     * @param lpDevice
     * @param productName
     * @param usage
     */
    private void addController( long lpDevice, String productName, int usage )
    {
        Controller controller = null;

        controller = createController( lpDevice, productName, usage );

        if ( controller != null )
        {
            devices.put( new Long(lpDevice), controller );
        }
    }

    /**
     * Adds an InputControllerElement to a device. This method is invoked from native code.
     * @param lpDevice
     * @param elementCookie
     * @param elementType
     * @param usage
     * @param usagePage
     * @param rawMin
     * @param rawMax
     * @param scaledMin
     * @param scaledMax
     * @param dataBitSize
     * @param isRelative
     * @param isWrapping
     * @param isNonLinear
     * @param hasPreferredState
     * @param hasNullState
     */
    private void addControllerElement(  long lpDevice,
                                        long elementCookie,
                                        int elementType,
                                        int usage,
                                        int usagePage,
                                        int rawMin,
                                        int rawMax,
                                        int scaledMin,
                                        int scaledMax,
                                        int dataBitSize,
                                        boolean isRelative,
                                        boolean isWrapping,
                                        boolean isNonLinear,
                                        boolean hasPreferredState,
                                        boolean hasNullState)
    {
        InputControllerElement element = new InputControllerElement( elementCookie, usagePage, usage, usagePage,
                                                                     rawMin, rawMax, scaledMin, scaledMax,
                                                                     dataBitSize, isRelative, isWrapping, isNonLinear,
                                                                     hasPreferredState, hasNullState );


        InputController inputController = (InputController)devices.get( new Long( lpDevice) );
        inputController.addControllerElement( element );
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