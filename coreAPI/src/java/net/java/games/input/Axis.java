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
 * An axis is a single button, slider, or dial, which has a single range.  An
 * axis can hold information for motion (linear or rotational), velocity,
 * force, or acceleration.
 */
public interface Axis {

    /**
     * Returns the identifier of the axis.
     */
    public abstract Identifier getIdentifier();

    /**
     * Returns <code>true</code> if data returned from <code>poll</code>
     * is relative to the last call, or <code>false</code> if data
     * is absolute.
     */
    public abstract boolean isRelative();

    /**
     * Returns whether or not the axis is analog, or false if it is digital.
     */
    public abstract boolean isAnalog();

    /**
     * Returns whether or not data polled from this axis is normalized
     * between the values of -1.0f and 1.0f.
     * @see #getPollData
     */
    public abstract boolean isNormalized();

    /**
     * Returns whether or not this axis is ready to receive polling data.
     * @see #getPollData
     * @see Controller#poll
     * @see #setPolling
     */
    public abstract boolean isPolling();

    /**
     * Sets whether or not the axis should receive polling data.
     * @see #getPollData
     * @see Controller#poll
     * @see #isPolling
     */
    public abstract void setPolling(boolean polling);

    /**
     * Returns the suggested dead zone for this axis.  Dead zone is the
     * amount polled data can vary before considered a significant change
     * in value.  An application can safely ignore changes less than this
     * value in the positive or negative direction.
     * @see #getPollData
     */
    public abstract float getDeadZone();

    /**
     * Returns the data from the last time the control has been polled.
     * If this axis is a button, the value returned will be either 0.0f or 1.0f.
     * If this axis is normalized, the value returned will be between -1.0f and
     * 1.0f.
     * @see Controller#poll
     */
    public abstract float getPollData();

    /**
     * Returns a human-readable name for this axis.
     */
    public abstract String getName();
    
    /**
     * Identifiers for different Axes.
     */
    public static class Identifier {
        
        /**
         * Name of axis type
         */
        private final String name;
        
        /**
         * Protected constructor
         */
        protected Identifier(String name) {
            this.name = name;
        }
        
        /**
         * Returns a non-localized string description of this axis type.
         */
        public String getName() {
            return name;
        }
        
        /**
         * Returns a non-localized string description of this axis type.
         */
        public String toString() {
            return name;
        }
        
        /**
         * An axis for specifying vertical data.
         */
        public static final Identifier X = new Identifier("x");

        /**
         * An axis for specifying horizontal data.
         */
        public static final Identifier Y = new Identifier("y");

        /**
         * An axis for specifying third dimensional up/down
         * data, or linear data in any direction that is
         * neither horizontal nor vertical.
         */
        public static final Identifier Z = new Identifier("z");

        /**
         * An axis for specifying left-right rotational data.
         */
        public static final Identifier RX = new Identifier("rx");

        /**
         * An axis for specifying forward-back rotational data.
         */
        public static final Identifier RY = new Identifier("ry");

        /**
         * An axis for specifying up-down rotational data
         * (rudder control).
         */
        public static final Identifier RZ = new Identifier("rz");

        /**
         * An axis for a button or key.
         */
        public static final Identifier BUTTON = new Identifier("button");

        /**
         * An axis for a slider or mouse wheel.
         */
        public static final Identifier SLIDER = new Identifier("slider");

        /**
         * An axis for a point-of-view control.
         */
        public static final Identifier POV = new Identifier("pov");
        
        /**
         * An axis for specifying vertical velocity data.
         */
        public static final Identifier X_VELOCITY =
            new Identifier("x-velocity");

        /**
         * An axis for specifying horizontal velocity data.
         */
        public static final Identifier Y_VELOCITY =
            new Identifier("y-velocity");

        /**
         * An axis for specifying third dimensional up/down velocity
         * data.
         */
        public static final Identifier Z_VELOCITY =
            new Identifier("z-velocity");

        /**
         * An axis for specifying left-right angular velocity data.
         */
        public static final Identifier RX_VELOCITY =
            new Identifier("rx-velocity");

        /**
         * An axis for specifying forward-back angular velocity data.
         */
        public static final Identifier RY_VELOCITY =
            new Identifier("ry-velocity");

        /**
         * An axis for specifying up-down angular velocity data.
         */
        public static final Identifier RZ_VELOCITY =
            new Identifier("rz-velocity");

        /**
         * An axis for slider or mouse wheel velocity data.
         */
        public static final Identifier SLIDER_VELOCITY =
            new Identifier("slider-velocity");
        
        /**
         * An axis for specifying vertical acceleration data.
         */
        public static final Identifier X_ACCELERATION =
            new Identifier("x-acceleration");

        /**
         * An axis for specifying horizontal acceleration data.
         */
        public static final Identifier Y_ACCELERATION =
            new Identifier("y-acceleration");

        /**
         * An axis for specifying third dimensional up/down acceleration
         * data.
         */
        public static final Identifier Z_ACCELERATION =
            new Identifier("z-acceleration");

        /**
         * An axis for specifying left-right angular acceleration data.
         */
        public static final Identifier RX_ACCELERATION =
            new Identifier("rx-acceleration");

        /**
         * An axis for specifying forward-back angular acceleration data.
         */
        public static final Identifier RY_ACCELERATION =
            new Identifier("ry-acceleration");

        /**
         * An axis for specifying up-down angular acceleration data.
         */
        public static final Identifier RZ_ACCELERATION =
            new Identifier("rz-acceleration");

        /**
         * An axis for slider or mouse wheel acceleration data.
         */
        public static final Identifier SLIDER_ACCELERATION =
            new Identifier("slider-acceleration");

        /**
         * An axis for specifying vertical force data.
         */
        public static final Identifier X_FORCE =
            new Identifier("x-force");

        /**
         * An axis for specifying horizontal force data.
         */
        public static final Identifier Y_FORCE =
            new Identifier("y-force");

        /**
         * An axis for specifying third dimensional up/down force
         * data.
         */
        public static final Identifier Z_FORCE =
            new Identifier("z-force");

        /**
         * An axis for specifying left-right angular force (torque) data.
         */
        public static final Identifier RX_FORCE =
            new Identifier("rx-force");

        /**
         * An axis for specifying forward-back angular force (torque) data.
         */
        public static final Identifier RY_FORCE =
            new Identifier("ry-force");

        /**
         * An axis for specifying up-down angular force (torque) data.
         */
        public static final Identifier RZ_FORCE =
            new Identifier("rz-force");

        /**
         * An axis for slider force data.
         */
        public static final Identifier SLIDER_FORCE =
            new Identifier("slider-force");
    } // class Axis.Identifier
    
    /**
     * POV enum for different positions.
     */
    public static class POV {
        /**
        * Standard value for center HAT position
        */
        public static final float OFF = 0.0f;
        /**
        * Synonmous with OFF
        */
        public static final float CENTER = OFF;
       /**
        * Standard value for up-left HAT position
        */
        public static final float UP_LEFT = 0.125f;
       /**
        * Standard value for up HAT position
        */
        public static final float UP = 0.25f;
       /**
        * Standard value for up-right HAT position
        */
        public static final float UP_RIGHT = 0.375f;
        /**
        * Standard value for right HAT position
        */
        public static final float RIGHT = 0.50f;
       /**
        * Standard value for down-right HAT position
        */
        public static final float DOWN_RIGHT = 0.625f;
        /**
        * Standard value for down HAT position
        */
        public static final float DOWN = 0.75f;
       /**
        * Standard value for down-left HAT position
        */
        public static final float DOWN_LEFT = 0.875f;
        /**
        * Standard value for left HAT position
        */
        public static final float LEFT = 1.0f;
    } // class Axis.POV
} // interface Axis
