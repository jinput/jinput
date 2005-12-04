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

import net.java.games.input.AbstractComponent;
import net.java.games.input.Component;

/**
 *
 * @author  martak
 * @version 
 */
class DirectInputAxis extends AbstractComponent {

    /**
     * DIDFT_ constants and macros defined in dinput.h
     */
    private static final int DIDFT_ALL = 0x00000000;
    private static final int DIDFT_RELAXIS = 0x00000001;
    private static final int DIDFT_ABSAXIS = 0x00000002;
    private static final int DIDFT_AXIS = 0x00000003;
    private static final int DIDFT_PSHBUTTON = 0x00000004;
    private static final int DIDFT_TGLBUTTON = 0x00000008;
    private static final int DIDFT_BUTTON = 0x0000000C;
    private static final int DIDFT_POV = 0x00000010;
    private static final int DIDFT_COLLECTION = 0x00000040;
    private static final int DIDFT_NODATA = 0x00000080;
    private static final int DIDFT_ANYINSTANCE = 0x00FFFF00;
    private static final int DIDFT_INSTANCEMASK = DIDFT_ANYINSTANCE;
    private static final int DIDFT_FFACTUATOR = 0x01000000;
    private static final int DIDFT_FFEFFECTTRIGGER = 0x02000000;
    private static final int DIDFT_OUTPUT = 0x10000000;
    private static final int DIDFT_NOCOLLECTION = 0x00FFFF00;
    private static int DIDFT_MAKEINSTANCE(int n) {
        return ((n&0xffff) << 8);
    }
    private static int DIDFT_GETTYPE(int n) {
        return (n&0xFF);
    }
    private static int DIDFT_GETINSTANCE(int n) {
        return ((n >> 8)&0xffff);
    }
    private static int DIDFT_ENUMCOLLECTION(int n) {
        return ((n&0xFFFF) << 8);
    }
    
    private DirectInputDevice device;
    /**
     * DIJOYSTATE structure defined in dinput.h:
     *
     * <pre>
     * typedef struct DIJOYSTATE { 
     *      LONG    lX; 
     *      LONG    lY; 
     *      LONG    lZ; 
     *      LONG    lRx; 
     *      LONG    lRy; 
     *      LONG    lRz; 
     *      LONG    rglSlider[2];
     *      DWORD   rgdwPOV[4];
     *      BYTE    rgbButtons[32];
     *  } DIJOYSTATE, *LPDIJOYSTATE;
     * 
     * 80 bytes total
     * </pre>
     */
    private int offset;
    private int type;
    private int instance;
    private int bitmask = 0xffffffff;
    private int bitshift = 0;
    
    private DirectInputAxis(DirectInputDevice device, Component.Identifier id,
        int didft, String name) {
        super(name, id);
        this.device = device;
        this.type = DIDFT_GETTYPE(didft);
        this.instance = DIDFT_GETINSTANCE(didft);
        if (id == Component.Identifier.Axis.X) {
            offset = 0;
        } else if (id == Component.Identifier.Axis.Y) {
            offset = 1;
        } else if (id == Component.Identifier.Axis.Z) {
            offset = 2;
        } else if (id == Component.Identifier.Axis.RX) {
            offset = 3;
        } else if (id == Component.Identifier.Axis.RY) {
            offset = 4;
        } else if (id == Component.Identifier.Axis.RZ) {
            offset = 5;
        } else if (id == Component.Identifier.Axis.SLIDER) {
            //System.out.println("Slider on "+name+" instance = "+instance);
            offset = 6 + (instance>>2);
        } else if (id == Component.Identifier.Axis.POV) {
            //System.out.println("POV on "+name+" instance = "+instance);
            offset = 8 + instance;
        } else if (id instanceof Component.Identifier.Button) {
            //System.out.println("Button on "+name+" instance = "+instance);
            offset = 12 + (instance/4);
            bitshift = (instance%4)*8;
            bitmask=0xff;
            
            //Nasty cludge to get some similarities across platforms.
            if(name.contains("A Button")) {
                this.id = Component.Identifier.Button.A;
            } else if(name.contains("B Button")) {
                this.id = Component.Identifier.Button.B;
            } else if(name.contains("C Button")) {
                this.id = Component.Identifier.Button.C;
            } else if(name.contains("X Button")) {
                this.id = Component.Identifier.Button.X;
            } else if(name.contains("Y Button")) {
                this.id = Component.Identifier.Button.Y;
            } else if(name.contains("Z Button")) {
                this.id = Component.Identifier.Button.Z;
            }
        }
    }

    /** Returns the data from the last time the control has been polled.
     * If this axis is a button, the value returned will be either 0.0f or 1.0f.
     * If this axis is normalized, the value returned will be between -1.0f and
     * 1.0f.
     * @return A float between -1.0 and 1.0
     */
    public float getPollData() {
        int data = 0;
        try {
            data = ((device.data[offset] >> bitshift)&bitmask);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Tried to get data for axis: " + this.getName() + ", device.data[" + offset + "] does not exists as device.data is only " + device.data.length + " long.");
        }
        if ((type&DIDFT_BUTTON) != 0 ) {
            return (float)((data&0x80)>>7);
        }  else if ((type&DIDFT_AXIS)!=0){ // all axes are set for -32768 to 32738
            return ((float)data)/32768;
        } else if ((type&DIDFT_POV)!=0) {
            if (data == -1) {
                return Component.POV.OFF;
            } else if (data == 0.0) {
                return Component.POV.UP;
            } else if (data == 4500) {
                return Component.POV.UP_RIGHT;
            } else if (data == 9000) {
                return Component.POV.RIGHT;
            } else if (data == 13500) {
                return Component.POV.DOWN_RIGHT;
            } else if (data == 18000) {
                return Component.POV.DOWN;
            } else if (data == 22500) {
                return Component.POV.DOWN_LEFT;
            } else if (data == 27000) {
                return Component.POV.LEFT;
            } else if (data == 31500) {
                return Component.POV.UP_LEFT;
            } else {
                System.err.print("Unexpected value for DX8 HAT: "+data);
                return Component.POV.OFF;
            }
        } else { // return raw value
            return (float)data;
        }
          
    }
    
    /** Returns <code>true</code> if data returned from <code>poll</code>
     * is relative to the last call, or <code>false</code> if data
     * is absolute.
     * @return true if data has chnaged since last poll, else false
     */
    public boolean isRelative() {
        return (type & DIDFT_RELAXIS) != 0;
    }
    
    /** Returns whether or not the axis is analog, or false if it is digital.
     * @return true if analog, false if digital
     */
    public boolean isAnalog() {
        return (type & DIDFT_AXIS) != 0;
    }

    /** Returns whether or not data polled from this axis is normalized
     * between the values of -1.0f and 1.0f.
     * @return true if data is normalized, false if not.
     */
    public boolean isNormalized() {
        return (type & (DIDFT_BUTTON|DIDFT_AXIS|DIDFT_POV)) != 0;
    }

    /** Creates a new DirectInputAxis (factory method).
     * This is a function used internally during set up
     * @return The new DirectInputAxis object.
     * @param device The device to attach this axis to.
     *
     * @param didft The identifier for the axis as provided by DX8.
     * @param name A name to give the new axis.
     * @param id The identifier for the device
     */
    public static DirectInputAxis createAxis(DirectInputDevice device,
        Component.Identifier id, int didft, String name) {
        return new DirectInputAxis(device, id, didft, name);
    }
}
