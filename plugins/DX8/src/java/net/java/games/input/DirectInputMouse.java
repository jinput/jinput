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
    private byte[] mouseData = new byte[20];
    
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
        this.lpDevice = lpDevice;
        buttons = new ButtonsImpl();
        ball = new BallImpl();
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
    private void renameAxis(Component.Identifier id, String name) {
        AbstractComponent axis;
        if (id instanceof Component.Identifier.Button) {
            axis = (AbstractComponent)getButtons().getComponent(id);
        } else {
            axis = (AbstractComponent)getBall().getComponent(id);
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

        private int numAxes;        

        /**
         * Public constructor
         */
        public BallImpl() {
            super(DirectInputMouse.this.getName() + " ball");
            numAxes = getNumAxes(lpDevice);
            x = new BallAxis(Component.Identifier.Axis.X);
            y = new BallAxis(Component.Identifier.Axis.Y);
            if(numAxes > 2) {
                wheel = new BallAxis(Component.Identifier.Axis.SLIDER);
            }
        }
    } // class DirectInputMouse.BallImpl
    
    /**
     * Implementation class representing the mouse buttons
     */
    class ButtonsImpl extends Buttons {
   
        private int numButtons;        

        /**
         * Public constructor
         */
        public ButtonsImpl() {
            super(DirectInputMouse.this.getName() + " buttons");
            numButtons = getNumButtons(lpDevice);
            left = new ButtonImpl(Component.Identifier.Button.LEFT);
            right = new ButtonImpl(Component.Identifier.Button.RIGHT);
            if(numButtons>2) {
                middle = new ButtonImpl(Component.Identifier.Button.MIDDLE);
            }
            if(numButtons>3) {
                side = new ButtonImpl(Component.Identifier.Button.SIDE);
            }
            if(numButtons>4) {
                extra = new ButtonImpl(Component.Identifier.Button.EXTRA);
            }
            if(numButtons>5) {
                forward = new ButtonImpl(Component.Identifier.Button.FORWARD);
            }
            if(numButtons>6) {
                back = new ButtonImpl(Component.Identifier.Button.BACK);
            }
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

    // another Endolf special
    /**
     * Gets the number of buttons the mouse supports
     */
    private native int getNumButtons(long lpDevice);
    /**
     * Gets the number of axes the mouse supports
     */
    private native int getNumAxes(long lpDevice);
    
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
        public ButtonImpl(Component.Identifier.Button id) {
            super(id.getName(), id);
            if (id == Component.Identifier.Button.LEFT) {
                index = 12;
            } else if (id == Component.Identifier.Button.RIGHT) {
                index = 13;
            } else if (id == Component.Identifier.Button.MIDDLE) {
                index = 14;
            } else if (id == Component.Identifier.Button.SIDE) {
                index = 15;
            } else if (id == Component.Identifier.Button.EXTRA) {
                index = 16;
            } else if (id == Component.Identifier.Button.FORWARD) {
                index = 17;
            } else if (id == Component.Identifier.Button.BACK) {
                index = 18;
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
    class BallAxis extends AbstractComponent {
        
        /**
         * Starting index into the mouseData array
         */
        private final int index;
        
        /** Public constructor
         * @param id  An ID for a mouse axis to create an object to represent.
         */
        public BallAxis(Identifier id) {
            super(id.getName(), id);
            if (id == Identifier.Axis.X) {
                index = 0;
            } else if (id == Identifier.Axis.Y) {
                index = 4;
            } else if (id == Identifier.Axis.SLIDER) {
                index = 8;
            } else {
                throw new RuntimeException("Unknown mouse axis");
            }
        }
        
        // Endolf changed this comment, we *are* a mouse axis if we are in here,
        // and mouse axes no longer are normalised at all
        /** Returns the data from the last time the control has been polled.
         *
         * @return data The total mouse axis change since the last poll
         */
        public float getPollData() {
            int data = ((int)mouseData[index+3] << 24) |
                ((int)mouseData[index + 2] << 16) |
                ((int)mouseData[index + 1] << 8) |
                ((int)mouseData[index]);
            return (float)data;
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

        /** Returns whether or not data polled from this axis is normalized between the values of -1.0f and 1.0f.
         *
         * @return true if normalized, otherwise false.
         */
        public boolean isNormalized() {
            return false;
        }

    } // class DirectInputMouse.MouseBallAxis
} // class DirectInputMouse
