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

import net.java.games.input.AbstractAxis;
import net.java.games.input.Axis;
import net.java.games.input.Mouse;

/**
 * DirectInput mouse implementation.
 * @author  martak
 * @version 
 */
class DirectInputMouse extends Mouse {

    /**
     * DIDEVTYPE_ constants from dinput.h header file
     */
    private static final int DIDEVTYPEMOUSE_UNKNOWN = 1;
    private static final int DIDEVTYPEMOUSE_TRADITIONAL = 2;
    private static final int DIDEVTYPEMOUSE_FINGERSTICK = 3;
    private static final int DIDEVTYPEMOUSE_TOUCHPAD = 4;
    private static final int DIDEVTYPEMOUSE_TRACKBALL = 5;

    /**
     * Pointer to the IDirectInputDevice for this device
     */
    private long lpDevice;

    /**
     * Type of mouse
     */
    private Type type;
    
    /**
     * Mouse data
     */
    private byte[] mouseData = new byte[16];
    
    /**
     * Private constructor
     * @param lpDevice A pointer to the IDirectInputDevice for the device.
     * @param subtype The subtype of mouse, as defined in the DIDEVTYPE
     * constants above
     * @param productName The product name for the device
     * @param instanceName The name of the device
     */
    private DirectInputMouse(long lpDevice, int subtype, String productName,
        String instanceName) {
        super(productName + " " + instanceName);
        buttons = new ButtonsImpl();
        ball = new BallImpl();
        this.lpDevice = lpDevice;
        switch(subtype) {
            case DIDEVTYPEMOUSE_FINGERSTICK:
                type = Type.FINGERSTICK; break;
            case DIDEVTYPEMOUSE_TOUCHPAD:
                type = Type.TRACKPAD; break;
            case DIDEVTYPEMOUSE_TRACKBALL:
                type = Type.TRACKBALL; break;
            case DIDEVTYPEMOUSE_TRADITIONAL: // fall through
            case DIDEVTYPEMOUSE_UNKNOWN: // fall through
            default:
                type = Type.MOUSE; break;
        }
        renameAxes(lpDevice);
    }

    /**
     * Callback to rename a given axis by type, name
     */
    private void renameAxis(Axis.Identifier id, String name) {
        AbstractAxis axis;
        if (id instanceof ButtonID) {
            axis = (AbstractAxis)getButtons().getAxis(id);
        } else {
            axis = (AbstractAxis)getBall().getAxis(id);
        }
        axis.setName(name);
        //System.out.println("Renaming " + name);
    }

    /** Polls axes for data.  Returns false if the controller is no longer valid.
     * Polling reflects the current state of the device when polled.
     * @return false if the mosue is no lonegr valid, true otherwise.
     */
    public boolean poll() {
        return pollNative(lpDevice, mouseData);
    }
    
    /** Returns the type of the Controller.
     * @return The device type of the controller (logically this
     * shoudl be some form of "mouse" .)
     */
    public Type getType() {
        return type;
    }

    /** Creates a new DirectInputMouse (factory method)
     * This is a function used internally during set up.
     * @param lpDevice A pointer to the IDirectInputDevice for the device.
     * @param subtype The subtype of mouse, as defined in the DIDEVTYPE
     * constants above
     * @param productName The product name for the keyboard
     * @param instanceName The name of the keyboard
     * @return The new DirectInputMouse object.
     */
    public static DirectInputMouse createMouse(long lpDevice, int subtype,
        String productName, String instanceName) {
        return new DirectInputMouse(lpDevice, subtype, productName,
            instanceName);
    }
    
    /**
     * Implementation class representing the mouse ball
     */
    class BallImpl extends Ball {
        
        /**
         * Public constructor
         */
        public BallImpl() {
            super(DirectInputMouse.this.getName() + " ball");
            x = new BallAxis(Axis.Identifier.X);
            y = new BallAxis(Axis.Identifier.Y);
            wheel = new BallAxis(Axis.Identifier.SLIDER);
        }
    } // class DirectInputMouse.BallImpl
    
    /**
     * Implementation class representing the mouse buttons
     */
    class ButtonsImpl extends Buttons {
        
        /**
         * Public constructor
         */
        public ButtonsImpl() {
            super(DirectInputMouse.this.getName() + " buttons");
            left = new ButtonImpl(ButtonID.LEFT);
            right = new ButtonImpl(ButtonID.RIGHT);
            middle = new ButtonImpl(ButtonID.MIDDLE);
        }
    } // class DirectInputMouse.ButtonsImpl
    
    /**
     * Polls the device; native method
     */
    private native boolean pollNative(long lpDevice, byte[] mouseData);
    
    /**
     * Renames the axes with the name provided by DirectInput
     */
    private native boolean renameAxes(long lpDevice);
    
    /**
     * Mouse button axis implementation
     */
    class ButtonImpl extends Button {
        
        /**
         * Index into the mouseData array
         */
        private final int index;
        
        /** Public constructor
         * @param id An ID of a button to create an obejct to represent.
         *
         */
        public ButtonImpl(ButtonID id) {
            super(id.getName(), id);
            if (id == ButtonID.LEFT) {
                index = 12;
            } else if (id == ButtonID.RIGHT) {
                index = 13;
            } else if (id == ButtonID.MIDDLE) {
                index = 14;
            } else {
                throw new RuntimeException("Unknown button");
            }
        }
        
        /** Returns the data from the last time the control has been polled.
         * If this axis is a button, the value returned will be either 0.0f or 1.0f.
         * If this axis is normalized, the value returned will be between -1.0f and
         * 1.0f.
         * @return  state of controller. (Note: DX8 mice actually
         * queue state so what is returned is the next state,
         * not necessarily the most current one.)
         */
        public float getPollData() {
            // Mouse button
            byte data = mouseData[index];
            if ((data & 0x80) != 0) {
                return 1.0f;
            } else {
                return 0.0f;
            }
        }
        
        /** Returns <code>true</code> if data returned from <code>poll</code>
         * is relative to the last call, or <code>false</code> if data
         * is absolute.
         * @return true if data is relative, otherwise false.
         */
        public boolean isRelative() {
            return false;
        }
    } // class DirectInputMouse.ButtonImpl

    /**
     * Mouse ball axis implementation
     */
    class BallAxis extends AbstractAxis {
        
        /**
         * Starting index into the mouseData array
         */
        private final int index;
        
        /** Public constructor
         * @param id  An ID for a mouse axis to create an object to represent.
         */
        public BallAxis(Identifier id) {
            super(id.getName(), id);
            if (id == Identifier.X) {
                index = 0;
            } else if (id == Identifier.Y) {
                index = 4;
            } else if (id == Identifier.SLIDER) {
                index = 8;
            } else {
                throw new RuntimeException("Unknown mouse axis");
            }
        }
        
        /** Returns the data from the last time the control has been polled.
         * If this axis is a button, the value returned will be either 0.0f or 1.0f.
         * If this axis is normalized, the value returned will be between -1.0f and
         * 1.0f.
         * @return  data.  (Note that mice queue state in DX8 so what
         * is returned is the next stae in the queue, not
         * necessarily the most current one.)
         */
        public float getPollData() {
            int data = ((int)mouseData[index] << 12) |
                ((int)mouseData[index + 1] << 8) |
                ((int)mouseData[index + 2] << 4) |
                ((int)mouseData[index + 3]);
            if (data == -1) {
                return -1.0f;
            } else if (data >= 1) {
                return 1.0f;
            } else {
                return 0.0f;
            }
        }
        
        /** Returns <code>true</code> if data returned from <code>poll</code>
         * is relative to the last call, or <code>false</code> if data
         * is absolute.
         * @return true if relative, otherwise false.
         */
        public boolean isRelative() {
            return true;
        }
        
        /** Returns whether or not the axis is analog, or false if it is digital.
         * @return true if analog, false if digital
         */
        public boolean isAnalog() {
            return true;
        }
    } // class DirectInputMouse.MouseBallAxis
} // class DirectInputMouse
