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


	public static final int HID_USAGE_KEYBOARD_ERRORROLLOVER	= 0x01;	/* ErrorRollOver */
	public static final int HID_USAGE_KEYBOARD_POSTFAIL	= 0x02;	/* POSTFail */
	public static final int HID_USAGE_KEYBOARD_ERRORUNDEFINED	= 0x03;	/* ErrorUndefined */
	public static final int HID_USAGE_KEYBOARD_A	= 0x04;	/* a or A */
	public static final int HID_USAGE_KEYBOARD_B	= 0x05;	/* b or B */
	public static final int HID_USAGE_KEYBOARD_C	= 0x06;	/* c or C */
	public static final int HID_USAGE_KEYBOARD_D	= 0x07;	/* d or D */
	public static final int HID_USAGE_KEYBOARD_E	= 0x08;	/* e or E */
	public static final int HID_USAGE_KEYBOARD_F	= 0x09;	/* f or F */
	public static final int HID_USAGE_KEYBOARD_G	= 0x0A;	/* g or G */
	public static final int HID_USAGE_KEYBOARD_H	= 0x0B;	/* h or H */
	public static final int HID_USAGE_KEYBOARD_I	= 0x0C;	/* i or I */
	public static final int HID_USAGE_KEYBOARD_J	= 0x0D;	/* j or J */
	public static final int HID_USAGE_KEYBOARD_K	= 0x0E;	/* k or K */
	public static final int HID_USAGE_KEYBOARD_L	= 0x0F;	/* l or L */
	public static final int HID_USAGE_KEYBOARD_M	= 0x10;	/* m or M */
	public static final int HID_USAGE_KEYBOARD_N	= 0x11;	/* n or N */
	public static final int HID_USAGE_KEYBOARD_O	= 0x12;	/* o or O */
	public static final int HID_USAGE_KEYBOARD_P	= 0x13;	/* p or P */
	public static final int HID_USAGE_KEYBOARD_Q	= 0x14;	/* q or Q */
	public static final int HID_USAGE_KEYBOARD_R	= 0x15;	/* r or R */
	public static final int HID_USAGE_KEYBOARD_S	= 0x16;	/* s or S */
	public static final int HID_USAGE_KEYBOARD_T	= 0x17;	/* t or T */
	public static final int HID_USAGE_KEYBOARD_U	= 0x18;	/* u or U */
	public static final int HID_USAGE_KEYBOARD_V	= 0x19;	/* v or V */
	public static final int HID_USAGE_KEYBOARD_W	= 0x1A;	/* w or W */
	public static final int HID_USAGE_KEYBOARD_X	= 0x1B;	/* x or X */
	public static final int HID_USAGE_KEYBOARD_Y	= 0x1C;	/* y or Y */
	public static final int HID_USAGE_KEYBOARD_Z	= 0x1D;	/* z or Z */
	public static final int HID_USAGE_KEYBOARD_1	= 0x1E;	/* 1 or ! */
	public static final int HID_USAGE_KEYBOARD_2	= 0x1F;	/* 2 or @ */
	public static final int HID_USAGE_KEYBOARD_3	= 0x20;	/* 3 or # */
	public static final int HID_USAGE_KEYBOARD_4	= 0x21;	/* 4 or $ */
	public static final int HID_USAGE_KEYBOARD_5	= 0x22;	/* 5 or % */
	public static final int HID_USAGE_KEYBOARD_6	= 0x23;	/* 6 or ^ */
	public static final int HID_USAGE_KEYBOARD_7	= 0x24;	/* 7 or & */
	public static final int HID_USAGE_KEYBOARD_8	= 0x25;	/* 8 or * */
	public static final int HID_USAGE_KEYBOARD_9	= 0x26;	/* 9 or ( */
	public static final int HID_USAGE_KEYBOARD_0	= 0x27;	/* 0 or ) */
	public static final int HID_USAGE_KEYBOARD_ENTER	= 0x28;	/* Return (Enter) */
	public static final int HID_USAGE_KEYBOARD_ESCAPE	= 0x29;	/* Escape */
	public static final int HID_USAGE_KEYBOARD_BACKSPACE	= 0x2A;	/* Delete (Backspace) */
	public static final int HID_USAGE_KEYBOARD_TAB	= 0x2B;	/* Tab */
	public static final int HID_USAGE_KEYBOARD_SPACEBAR	= 0x2C;	/* Spacebar */
	public static final int HID_USAGE_KEYBOARD_HYPHEN	= 0x2D;	/* - or _ */
	public static final int HID_USAGE_KEYBOARD_EQUALSIGN	= 0x2E;	/* = or + */
	public static final int HID_USAGE_KEYBOARD_OPENBRACKET	= 0x2F;	/* [ or { */
	public static final int HID_USAGE_KEYBOARD_CLOSEBRACKET	= 0x30;	/* ] or } */
	public static final int HID_USAGE_KEYBOARD_BACKSLASH	= 0x31;	/* \ or | */
	public static final int HID_USAGE_KEYBOARD_NONUSPOUNT	= 0x32;	/* Non-US # or _ */
	public static final int HID_USAGE_KEYBOARD_SEMICOLON	= 0x33;	/* ; or : */
	public static final int HID_USAGE_KEYBOARD_QUOTE	= 0x34;	/* ' or " */
	public static final int HID_USAGE_KEYBOARD_TILDE	= 0x35;	/* Grave Accent and Tilde */
	public static final int HID_USAGE_KEYBOARD_COMMA	= 0x36;	/* , or < */
	public static final int HID_USAGE_KEYBOARD_PERIOD	= 0x37;	/* . or > */
	public static final int HID_USAGE_KEYBOARD_SLASH	= 0x38;	/* / or ? */
	public static final int HID_USAGE_KEYBOARD_CAPSLOCK	= 0x39;	/* Caps Lock */
	public static final int HID_USAGE_KEYBOARD_F1	= 0x3A;	/* F1 */
	public static final int HID_USAGE_KEYBOARD_F2	= 0x3B;	/* F2 */
	public static final int HID_USAGE_KEYBOARD_F3	= 0x3C;	/* F3 */
	public static final int HID_USAGE_KEYBOARD_F4	= 0x3D;	/* F4 */
	public static final int HID_USAGE_KEYBOARD_F5	= 0x3E;	/* F5 */
	public static final int HID_USAGE_KEYBOARD_F6	= 0x3F;	/* F6 */
	public static final int HID_USAGE_KEYBOARD_F7	= 0x40;	/* F7 */
	public static final int HID_USAGE_KEYBOARD_F8	= 0x41;	/* F8 */
	public static final int HID_USAGE_KEYBOARD_F9	= 0x42;	/* F9 */
	public static final int HID_USAGE_KEYBOARD_F10	= 0x43;	/* F10 */
	public static final int HID_USAGE_KEYBOARD_F11	= 0x44;	/* F11 */
	public static final int HID_USAGE_KEYBOARD_F12	= 0x45;	/* F12 */
	public static final int HID_USAGE_KEYBOARD_PRINTSCREEN	= 0x46;	/* Print Screen */
	public static final int HID_USAGE_KEYBOARD_SCROLLLOCK	= 0x47;	/* Scroll Lock */
	public static final int HID_USAGE_KEYBOARD_PAUSE	= 0x48;	/* Pause */
	public static final int HID_USAGE_KEYBOARD_INSERT	= 0x49;	/* Insert */
	public static final int HID_USAGE_KEYBOARD_HOME	= 0x4A;	/* Home */
	public static final int HID_USAGE_KEYBOARD_PAGEUP	= 0x4B;	/* Page Up */
	public static final int HID_USAGE_KEYBOARD_DELETE	= 0x4C;	/* Delete Forward */
	public static final int HID_USAGE_KEYBOARD_END	= 0x4D;	/* End */
	public static final int HID_USAGE_KEYBOARD_PAGEDOWN	= 0x4E;	/* Page Down */
	public static final int HID_USAGE_KEYBOARD_RIGHTARROW	= 0x4F;	/* Right Arrow */
	public static final int HID_USAGE_KEYBOARD_LEFTARROW	= 0x50;	/* Left Arrow */
	public static final int HID_USAGE_KEYBOARD_DOWNARROW	= 0x51;	/* Down Arrow */
	public static final int HID_USAGE_KEYBOARD_UPARROW	= 0x52;	/* Up Arrow */
	public static final int HID_USAGE_KEYPAD_NUMLOCK	= 0x53;	/* Keypad NumLock or Clear */
	public static final int HID_USAGE_KEYPAD_SLASH	= 0x54;	/* Keypad / */
	public static final int HID_USAGE_KEYPAD_ASTERICK	= 0x55;	/* Keypad * */
	public static final int HID_USAGE_KEYPAD_HYPHEN	= 0x56;	/* Keypad - */
	public static final int HID_USAGE_KEYPAD_PLUS	= 0x57;	/* Keypad + */
	public static final int HID_USAGE_KEYPAD_ENTER	= 0x58;	/* Keypad Enter */
	public static final int HID_USAGE_KEYPAD_1	= 0x59;	/* Keypad 1 or End */
	public static final int HID_USAGE_KEYPAD_2	= 0x5A;	/* Keypad 2 or Down Arrow */
	public static final int HID_USAGE_KEYPAD_3	= 0x5B;	/* Keypad 3 or Page Down */
	public static final int HID_USAGE_KEYPAD_4	= 0x5C;	/* Keypad 4 or Left Arrow */
	public static final int HID_USAGE_KEYPAD_5	= 0x5D;	/* Keypad 5 */
	public static final int HID_USAGE_KEYPAD_6	= 0x5E;	/* Keypad 6 or Right Arrow */
	public static final int HID_USAGE_KEYPAD_7	= 0x5F;	/* Keypad 7 or Home */
	public static final int HID_USAGE_KEYPAD_8	= 0x60;	/* Keypad 8 or Up Arrow */
	public static final int HID_USAGE_KEYPAD_9	= 0x61;	/* Keypad 9 or Page Up */
	public static final int HID_USAGE_KEYPAD_0	= 0x62;	/* Keypad 0 or Insert */
	public static final int HID_USAGE_KEYPAD_PERIOD	= 0x63;	/* Keypad . or Delete */
	public static final int HID_USAGE_KEYBOARD_NONUSBACKSLASH	= 0x64;	/* Non-US \ or | */
	public static final int HID_USAGE_KEYBOARD_APPLICATION	= 0x65;	/* Application */
	public static final int HID_USAGE_KEYBOARD_POWER	= 0x66;	/* Power */
	public static final int HID_USAGE_KEYPAD_EQUALSIGN	= 0x67;	/* Keypad = */
	public static final int HID_USAGE_KEYBOARD_F13	= 0x68;	/* F13 */
	public static final int HID_USAGE_KEYBOARD_F14	= 0x69;	/* F14 */
	public static final int HID_USAGE_KEYBOARD_F15	= 0x6A;	/* F15 */
	public static final int HID_USAGE_KEYBOARD_F16	= 0x6B;	/* F16 */
	public static final int HID_USAGE_KEYBOARD_F17	= 0x6C;	/* F17 */
	public static final int HID_USAGE_KEYBOARD_F18	= 0x6D;	/* F18 */
	public static final int HID_USAGE_KEYBOARD_F19	= 0x6E;	/* F19 */
	public static final int HID_USAGE_KEYBOARD_F20	= 0x6F;	/* F20 */
	public static final int HID_USAGE_KEYBOARD_F21	= 0x70;	/* F21 */
	public static final int HID_USAGE_KEYBOARD_F22	= 0x71;	/* F22 */
	public static final int HID_USAGE_KEYBOARD_F23	= 0x72;	/* F23 */
	public static final int HID_USAGE_KEYBOARD_F24	= 0x73;	/* F24 */
	public static final int HID_USAGE_KEYBOARD_EXECUTE	= 0x74;	/* Execute */
	public static final int HID_USAGE_KEYBOARD_HELP	= 0x75;	/* Help */
	public static final int HID_USAGE_KEYBOARD_MENU	= 0x76;	/* Menu */
	public static final int HID_USAGE_KEYBOARD_SELECT	= 0x77;	/* Select */
	public static final int HID_USAGE_KEYBOARD_STOP	= 0x78;	/* Stop */
	public static final int HID_USAGE_KEYBOARD_AGAIN	= 0x79;	/* Again */
	public static final int HID_USAGE_KEYBOARD_UNDO	= 0x7A;	/* Undo */
	public static final int HID_USAGE_KEYBOARD_CUT	= 0x7B;	/* Cut */
	public static final int HID_USAGE_KEYBOARD_COPY	= 0x7C;	/* Copy */
	public static final int HID_USAGE_KEYBOARD_PASTE	= 0x7D;	/* Paste */
	public static final int HID_USAGE_KEYBOARD_FIND	= 0x7E;	/* Find */
	public static final int HID_USAGE_KEYBOARD_MUTE	= 0x7F;	/* Mute */
	public static final int HID_USAGE_KEYBOARD_VOLUMEUP	= 0x80;	/* Volume Up */
	public static final int HID_USAGE_KEYBOARD_VOLUMEDOWN	= 0x81;	/* Volume Down */
	public static final int HID_USAGE_KEYBOARD_LOCKINGCAPSLOCK	= 0x82;	/* Locking Caps Lock */
	public static final int HID_USAGE_KEYBOARD_LOCKINGNUMLOCK	= 0x83;	/* Locking Num Lock */
	public static final int HID_USAGE_KEYBOARD_LOCKINGSCROLLLOCK	= 0x84;	/* Locking Scroll Lock */
	public static final int HID_USAGE_KEYPAD_COMMA	= 0x85;	/* Keypad Comma */
	public static final int HID_USAGE_KEYPAD_EQUALSSIGNAS400	= 0x86;	/* Keypad Equal Sign for AS/400 */
	public static final int HID_USAGE_KEYBOARD_INTERNATIONAL1	= 0x87;	/* International1 */
	public static final int HID_USAGE_KEYBOARD_INTERNATIONAL2	= 0x88;	/* International2 */
	public static final int HID_USAGE_KEYBOARD_INTERNATIONAL3	= 0x89;	/* International3 */
	public static final int HID_USAGE_KEYBOARD_INTERNATIONAL4	= 0x8A;	/* International4 */
	public static final int HID_USAGE_KEYBOARD_INTERNATIONAL5	= 0x8B;	/* International5 */
	public static final int HID_USAGE_KEYBOARD_INTERNATIONAL6	= 0x8C;	/* International6 */
	public static final int HID_USAGE_KEYBOARD_INTERNATIONAL7	= 0x8D;	/* International7 */
	public static final int HID_USAGE_KEYBOARD_INTERNATIONAL8	= 0x8E;	/* International8 */
	public static final int HID_USAGE_KEYBOARD_INTERNATIONAL9	= 0x8F;	/* International9 */
	public static final int HID_USAGE_KEYBOARD_LANG1	= 0x90;	/* LANG1 */
	public static final int HID_USAGE_KEYBOARD_LANG2	= 0x91;	/* LANG2 */
	public static final int HID_USAGE_KEYBOARD_LANG3	= 0x92;	/* LANG3 */
	public static final int HID_USAGE_KEYBOARD_LANG4	= 0x93;	/* LANG4 */
	public static final int HID_USAGE_KEYBOARD_LANG5	= 0x94;	/* LANG5 */
	public static final int HID_USAGE_KEYBOARD_LANG6	= 0x95;	/* LANG6 */
	public static final int HID_USAGE_KEYBOARD_LANG7	= 0x96;	/* LANG7 */
	public static final int HID_USAGE_KEYBOARD_LANG8	= 0x97;	/* LANG8 */
	public static final int HID_USAGE_KEYBOARD_LANG9	= 0x98;	/* LANG9 */
	public static final int HID_USAGE_KEYBOARD_ALTERNATEERASE	= 0x99;	/* AlternateErase */
	public static final int HID_USAGE_KEYBOARD_SYSREQORATTENTION	= 0x9A;	/* SysReq/Attention */
	public static final int HID_USAGE_KEYBOARD_CANCEL	= 0x9B;	/* Cancel */
	public static final int HID_USAGE_KEYBOARD_CLEAR	= 0x9C;	/* Clear */
	public static final int HID_USAGE_KEYBOARD_PRIOR	= 0x9D;	/* Prior */
	public static final int HID_USAGE_KEYBOARD_RETURN	= 0x9E;	/* Return */
	public static final int HID_USAGE_KEYBOARD_SEPARATOR	= 0x9F;	/* Separator */
	public static final int HID_USAGE_KEYBOARD_OUT	= 0xA0;	/* Out */
	public static final int HID_USAGE_KEYBOARD_OPER	= 0xA1;	/* Oper */
	public static final int HID_USAGE_KEYBOARD_CLEARORAGAIN	= 0xA2;	/* Clear/Again */
	public static final int HID_USAGE_KEYBOARD_CRSELORPROPS	= 0xA3;	/* CrSel/Props */
	public static final int HID_USAGE_KEYBOARD_EXSEL	= 0xA4;	/* ExSel */
	/* 0xA5-0xDF Reserved */
	public static final int HID_USAGE_KEYBOARD_LEFTCONTROL	= 0xE0;	/* Left Control */
	public static final int HID_USAGE_KEYBOARD_LEFTSHIFT	= 0xE1;	/* Left Shift */
	public static final int HID_USAGE_KEYBOARD_LEFTALT	= 0xE2;	/* Left Alt */
	public static final int HID_USAGE_KEYBOARD_LEFTGUI	= 0xE3;	/* Left GUI */
	public static final int HID_USAGE_KEYBOARD_RIGHTCONTROL	= 0xE4;	/* Right Control */
	public static final int HID_USAGE_KEYBOARD_RIGHTSHIFT	= 0xE5;	/* Right Shift */
	public static final int HID_USAGE_KEYBOARD_RIGHTALT	= 0xE6;	/* Right Alt */
	public static final int HID_USAGE_KEYBOARD_RIGHTGUI	= 0xE7;	/* Right GUI */
	/* 0xE8-0xFFFF Reserved */
	public static final int HID_USAGE_KEYBOARD__RESERVED = 0xFFFF;


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

        System.out.println("Creating HID engine");
        hidCreate();

        System.out.println("Enumerating devices");
        enumDevices();
    }

    public void finalize()
    {
        System.out.println("Disposing HID engine");
        hidDispose();
    }

    public Controller[] getControllers()
    {
        return (Controller[])(devices.values().toArray( new Controller[0]));
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
        InputControllerElement element = new InputControllerElement( elementCookie, elementType, usage, usagePage,
                                                                     rawMin, rawMax, scaledMin, scaledMax,
                                                                     dataBitSize, isRelative, isWrapping, isNonLinear,
                                                                     hasPreferredState, hasNullState );


        InputController inputController = (InputController)devices.get( new Long( lpDevice) );
        if ( inputController != null )
        {
            inputController.addControllerElement( element );
        }
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

        //newjni.hidCreate();

        //newjni.enumDevices();


        Controller[] controllers = newjni.getControllers();

        for ( int i = 0; i < controllers.length; i++ )
        {
            System.out.println("Controller [" + controllers[i].getName() +"] enumerated...");
        }

        //newjni.hidDispose();

        System.out.println("Done");
    }

}