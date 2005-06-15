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

import net.java.games.input.AbstractController;
import net.java.games.input.Component;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author  martak
 * @version 
 */
class DirectInputDevice extends AbstractController {
    
    /**
     * DIDEVTYPE_ constants from dinput.h header file
     ** JPK NOTE: This changed in DI8.  In general this
     * is fragile anda way shoudl be found to tie these mroe directly
     * to the header files.
     */
    /* Mike's <=DX7 types
    private static final int DIDEVTYPEJOYSTICK_UNKNOWN = 1;
    private static final int DIDEVTYPEJOYSTICK_TRADITIONAL = 2;
    private static final int DIDEVTYPEJOYSTICK_FLIGHTSTICK = 3;
    private static final int DIDEVTYPEJOYSTICK_GAMEPAD = 4;
    private static final int DIDEVTYPEJOYSTICK_RUDDER = 5;
    private static final int DIDEVTYPEJOYSTICK_WHEEL = 6;
    private static final int DIDEVTYPEJOYSTICK_HEADTRACKER = 7;
    */
    
    /**
     * Pointer to the IDirectInputDevice for this device
     */
    private long lpDevice;
    
    /**
     * Type of device
     */
    private Type type;
    
    /** 
     * Do we need to poll data?
     */
    private boolean polled = true;
    /**
     * Data when polling, as per the DIJOYSTATE structure in dinput.h;
     * @see DirectInputAxis for a breakdown of this structure
     */
   int[] data = new int[38];
   
   /** Array list of rumblers */
   private ArrayList rumblerList = new ArrayList();
    
    /**
     * Private constructor
     * @param lpDevice A pointer to the IDirectInputDevice for the device.
     * @param subtype The subtype of device, as defined in the DIDEVTYPE
     * constants above
     * @param productName The product name for the device
     * @param instanceName The name of the device
     */
    private DirectInputDevice(long lpDevice, int subtype, String productName,
        String instanceName,boolean polled) {
        super(productName + " " + instanceName);
        this.lpDevice = lpDevice;
        this.polled = polled;
        System.out.println("Creating "+productName+" polling = "+polled);
        switch(subtype) {
            /*
            case DIDEVTYPEJOYSTICK_GAMEPAD:
                type = Type.GAMEPAD; break;
            case DIDEVTYPEJOYSTICK_RUDDER:
                type = Type.RUDDER; break;
            case DIDEVTYPEJOYSTICK_WHEEL:
                type = Type.WHEEL; break;
            case DIDEVTYPEJOYSTICK_HEADTRACKER:
                type = Type.HEADTRACKER; break;
            case DIDEVTYPEJOYSTICK_TRADITIONAL: // fall through
            case DIDEVTYPEJOYSTICK_FLIGHTSTICK: // fall through
             */
            default:
                type = Type.STICK; break;
        }
        components = initDirectInputAxes();
    }
    
    /**
     * Used instead of overriding initAxes because it needs the
     * pointer to the IDirectInputDevice
     */
    private Component[] initDirectInputAxes() {
        ArrayList list = new ArrayList();
        enumObjects(lpDevice, list);
        Component[] ret = new Component[list.size()];
        Iterator it = list.iterator();
        int i = 0;
        while (it.hasNext()) {
            ret[i] = (Component)it.next();
            i++;
        }
        return ret;
    }
    
    /**
     * Callback called by enumObjects to add a new axis into the list
     * @param list This in which to add the new axis
     * @param id The identifier for the axis, based on GUID
     * @param didft The combination of DIDFT_ flags that make up the type and
     * instance number of the axis.
     * @param name The name to call the axis.
     */
    private void addAxis(ArrayList list, Component.Identifier id, int didft,
        String name) {
        list.add(DirectInputAxis.createAxis(this, id, didft, name));
    }
    
    /**
     * Callback called by enumDevice to add a rumbler
     *
     * @param effect the natie effect id
     * @param axisID The axis ID
     */
    private void addRumbler(long effect, Component.Identifier axisID, String axisName) {        
        rumblerList.add(new DirectInputRumbler(this, effect, axisID, axisName));
    }
    
    /** Polls axes for data.  Returns false if the controller is no longer valid.
     * Polling reflects the current state of the device when polled, and is
     * unbuffered.
     * @return False if the co troller is no longer valid, else true.
     */
    public boolean poll() {
        return pollNative(lpDevice, data, polled);
    }
    
    /** Returns the type of Controller.
     * @return The type of the controller.
     */
    public Type getType() {
        return type;
    }
    
    /** Returns the zero-based port number for this Controller.
     * @return The port number.
     */
    public int getPortNumber() {
        // REMIND : We may be able to parse this from the name string
        return 0;
    }
    
    /**
     * Returns the rumbler array
     */
    public Rumbler[] getRumblers() {
        return (Rumbler[]) rumblerList.toArray(new Rumbler[0]);
    }
    
    /**
     * Polls the device; native method.  The data from the poll is stored in
     * the data array.
     */
    private native boolean pollNative(long lpDevice, int[] data, 
        boolean polled);
    
    /**
     * Enumerates the axes on the device
     */
    private native boolean enumObjects(long lpDevice, ArrayList list);
    
    /** Creates a new DirectInputDevice (factory method)
     * This is a function used internally during set up
     * @param polled Whether this device's driver should actually be
     * polled during a call to Poll or whether it is an
     * interrupt driven device that should ignore poll
     * requests.
     * @param lpDevice A pointer to the IDirectInputDevice for the device.
     * @param subtype The subtype of device, as defined in the DIDEVTYPE
     * constants above
     * @param productName The product name for the device
     * @param instanceName The name of the device
     * @return The new DirectInputDevice object
     */
    public static DirectInputDevice createDevice(long lpDevice, int subtype,
        String productName, String instanceName, boolean polled) {
        return new DirectInputDevice(lpDevice, subtype, productName,
            instanceName,polled);
    }
} // class DirectInputDevice
