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

/**
 * A Controller represents a physical device, such as a keyboard, mouse,
 * or joystick, or a logical grouping of related controls, such as a button
 * pad or mouse ball.  A controller can be composed of multiple controllers.
 * For example, the ball of a mouse and its buttons are two separate
 * controllers.
 */
public interface Controller {
    
    /**
     * Returns the controllers connected to make up this controller, or
     * an empty array if this controller contains no child controllers.
     * The objects in the array are returned in order of assignment priority
     * (primary stick, secondary buttons, etc.).
     */
    public abstract Controller[] getControllers();

    /**
     * Returns the type of the Controller.
     */
    public abstract Type getType();

    /**
     * Returns the axes on this controller, in order of assignment priority.
     * For example, the button controller on a mouse returns an array containing
     * the primary or leftmost mouse button, followed by the secondary or
     * rightmost mouse button (if present), followed by the middle mouse button
     * (if present).
     * The array returned is an empty array if this controller contains no axes
     * (such as a logical grouping of child controllers).
     */
    public abstract Axis[] getAxes();

    /**
     * Returns a single axis based on its type, or null
     * if no axis with the specified type could be found.
     */
    public abstract Axis getAxis(Axis.Identifier id);

    /**
     * Returns the rumblers for sending feedback to this controller, or an
     * empty array if there are no rumblers on this controller.
     */
    public abstract Rumbler[] getRumblers();

    /**
     * Polls axes for data.  Returns false if the controller is no longer valid.
     * Polling reflects the current state of the device when polled.
     */
    public abstract boolean poll();

    /**
     * Returns the port type for this Controller.
     */
    public abstract PortType getPortType();

    /**
     * Returns the zero-based port number for this Controller.
     */
    public abstract int getPortNumber();

    /**
     * Returns a human-readable name for this Controller.
     */
    public abstract String getName();
    
    /**
     * Types of controller objects.
     */
    public static class Type {
        
        /**
         * Name of controller type
         */
        private final String name;
        
        /**
         * Protected constructor
         */
        protected Type(String name) {
            this.name = name;
        }
        
        /**
         * Returns a non-localized string description of this controller type.
         */
        public String toString() {
            return name;
        }
        
         /**
         * Mouse controller.
         */
        public static final Type UNKNOWN = new Type("unknown");
        
        /**
         * Mouse controller.
         */
        public static final Type MOUSE = new Type("mouse");

        /**
         * A mouse ball or the ball part of a trackball controller.
         * Note that a mouse wheel is considered part of a ball controller.
         */
        public static final Type BALL = new Type("ball");

        /**
         * A group of buttons on a pad (mouse buttons, for
         * example) or a keyboard.
         */
        public static final Type BUTTONS = new Type("buttons");
        /**
         * A keyboard controller (same as BUTTONS)
         * @see #BUTTONS
         */
        public static final Type KEYBOARD = BUTTONS;
        
        /**
         * Fingerstick controller; note that this may be sometimes treated as a
         * type of mouse or stick.
         */
        public static final Type FINGERSTICK = new Type("fingerstick");

        /**
         * Gamepad controller.
         */
        public static final Type GAMEPAD = new Type("gamepad");

        /**
         * Headtracker controller.
         */
        public static final Type HEADTRACKER = new Type("headtracker");

        /**
         * Rudder controller.
         */
        public static final Type RUDDER = new Type("rudder");

        /**
         * Stick controller, such as a joystick or flightstick.
         */
        public static final Type STICK = new Type("stick");

        /**
         * A trackball controller; note that this may sometimes be treated as a
         * type of mouse.
         */
        public static final Type TRACKBALL = new Type("trackball");

        /**
         * A trackpad, such as a tablet, touchpad, or glidepad;
         * note that this may sometimes be treated as a type of mouse.
         */
        public static final Type TRACKPAD = new Type("trackpad");

        /**
         * A wheel controller, such as a steering wheel (note
         * that a mouse wheel is considered part of a ball control, not a
         * wheel controller).
         */
        public static final Type WHEEL = new Type("wheel");
    } // class Controller.Type
    
    /**
     * Common controller port types.
     */
    public static final class PortType {
        
        /**
         * Name of port type
         */
        private final String name;
        
        /**
         * Protected constructor
         */
        protected PortType(String name) {
            this.name = name;
        }
        
        /**
         * Returns a non-localized string description of this port type.
         */
        public String toString() {
            return name;
        }
        
        /**
         * Unknown port type
         */
        public static final PortType UNKNOWN = new PortType("Unknown");

        /**
         * USB port
         */
        public static final PortType USB = new PortType("USB port");

        /**
         * Standard game port
         */
        public static final PortType GAME = new PortType("Game port");

        /**
         * Network port
         */
        public static final PortType NETWORK = new PortType("Network port");

        /**
         * Serial port
         */
        public static final PortType SERIAL = new PortType("Serial port");
    } // class Controller.PortType
} // interface Controller
