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
public interface Component {

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
        
        public static class Axis extends Identifier {
        
            /**
             * @param name
             */
            protected Axis(String name) {
                super(name);
            }

            /**
             * An axis for specifying vertical data.
             */
            public static final Axis X = new Axis("x");
            
            /**
             * An axis for specifying horizontal data.
             */
            public static final Axis Y = new Axis("y");
            
            /**
             * An axis for specifying third dimensional up/down
             * data, or linear data in any direction that is
             * neither horizontal nor vertical.
             */
            public static final Axis Z = new Axis("z");
            
            /**
             * An axis for specifying left-right rotational data.
             */
            public static final Axis RX = new Axis("rx");
            
            /**
             * An axis for specifying forward-back rotational data.
             */
            public static final Axis RY = new Axis("ry");
            
            /**
             * An axis for specifying up-down rotational data
             * (rudder control).
             */
            public static final Axis RZ = new Axis("rz");

            /**
             * An axis for a slider or mouse wheel.
             */
            public static final Axis SLIDER = new Axis("slider");

            /**
             * An axis for slider or mouse wheel acceleration data.
             */
            public static final Axis SLIDER_ACCELERATION = new Axis("slider-acceleration");

            /**
             * An axis for slider force data.
             */
            public static final Axis SLIDER_FORCE = new Axis("slider-force");

            /**
             * An axis for slider or mouse wheel velocity data.
             */
            public static final Axis SLIDER_VELOCITY = new Axis("slider-velocity");

            /**
             * An axis for specifying vertical acceleration data.
             */
            public static final Axis X_ACCELERATION = new Axis("x-acceleration");

            /**
             * An axis for specifying vertical force data.
             */
            public static final Axis X_FORCE = new Axis("x-force");

            /**
             * An axis for specifying vertical velocity data.
             */
            public static final Axis X_VELOCITY = new Axis("x-velocity");

            /**
             * An axis for specifying horizontal acceleration data.
             */
            public static final Axis Y_ACCELERATION = new Axis("y-acceleration");

            /**
             * An axis for specifying horizontal force data.
             */
            public static final Axis Y_FORCE = new Axis("y-force");

            /**
             * An axis for specifying horizontal velocity data.
             */
            public static final Axis Y_VELOCITY = new Axis("y-velocity");

            /**
             * An axis for specifying third dimensional up/down acceleration data.
             */
            public static final Axis Z_ACCELERATION = new Axis("z-acceleration");

            /**
             * An axis for specifying third dimensional up/down force data.
             */
            public static final Axis Z_FORCE = new Axis("z-force");

            /**
             * An axis for specifying third dimensional up/down velocity data.
             */
            public static final Axis Z_VELOCITY = new Axis("z-velocity");

            /**
             * An axis for specifying left-right angular acceleration data.
             */
            public static final Axis RX_ACCELERATION = new Axis("rx-acceleration");

            /**
             * An axis for specifying left-right angular force (torque) data.
             */
            public static final Axis RX_FORCE = new Axis("rx-force");

            /**
             * An axis for specifying left-right angular velocity data.
             */
            public static final Axis RX_VELOCITY = new Axis("rx-velocity");

            /**
             * An axis for specifying forward-back angular acceleration data.
             */
            public static final Axis RY_ACCELERATION = new Axis("ry-acceleration");

            /**
             * An axis for specifying forward-back angular force (torque) data.
             */
            public static final Axis RY_FORCE = new Axis("ry-force");

            /**
             * An axis for specifying forward-back angular velocity data.
             */
            public static final Axis RY_VELOCITY = new Axis("ry-velocity");

            /**
             * An axis for specifying up-down angular acceleration data.
             */
            public static final Axis RZ_ACCELERATION = new Axis("rz-acceleration");

            /**
             * An axis for specifying up-down angular force (torque) data.
             */
            public static final Axis RZ_FORCE = new Axis("rz-force");

            /**
             * An axis for specifying up-down angular velocity data.
             */
            public static final Axis RZ_VELOCITY = new Axis("rz-velocity");
        
            /**
             * An axis for a point-of-view control.
             */
            public static final Axis POV = new Axis("pov");

        }
        
        public static class Button extends Identifier {
            
            public Button(String name) {
                super(name);
            }
            
            /** First device button
             */        
            public static final Button _0 = new Button("0");
            
            /** Second device button
             */        
            public static final Button _1 = new Button("1");
            
            /** Thrid device button
             */        
            public static final Button _2 = new Button("2");
            
            /** Fourth device button
             */        
            public static final Button _3 = new Button("3");
            
            /** Fifth device button
             */        
            public static final Button _4 = new Button("4");
            
            /** Sixth device button
             */        
            public static final Button _5 = new Button("5");
            
            /** Seventh device button
             */        
            public static final Button _6 = new Button("6");
            
            /** Eighth device button
             */        
            public static final Button _7 = new Button("7");
            
            /** Ninth device button
             */        
            public static final Button _8 = new Button("8");
            
            /** 10th device button
             */        
            public static final Button _9 = new Button("9");
            public static final Button _10 = new Button("10");
            public static final Button _11 = new Button("11");
            public static final Button _12 = new Button("12");
            public static final Button _13 = new Button("13");
            public static final Button _14 = new Button("14");
            public static final Button _15 = new Button("15");
            public static final Button _16 = new Button("16");
            public static final Button _17 = new Button("17");
            public static final Button _18 = new Button("18");
            public static final Button _19 = new Button("19");
            public static final Button _20 = new Button("20");
            public static final Button _21 = new Button("21");
            public static final Button _22 = new Button("22");
            public static final Button _23 = new Button("23");
            public static final Button _24 = new Button("24");
            public static final Button _25 = new Button("25");
            public static final Button _26 = new Button("26");
            public static final Button _27 = new Button("27");
            public static final Button _28 = new Button("28");
            public static final Button _29 = new Button("29");
            public static final Button _30 = new Button("30");
            public static final Button _31 = new Button("31");
            
            /** Joystick trigger button
             */        
            public static final Button TRIGGER = new Button("Trigger");
            
            /** Joystick thumb button
             */        
            public static final Button THUMB = new Button("Thumb");
            
            /** Second joystick thumb button
             */        
            public static final Button THUMB2 = new Button("Thumb 2");
            
            /** Joystick top button
             */        
            public static final Button TOP = new Button("Top");
            
            /** Second joystick top button
             */        
            public static final Button TOP2 = new Button("Top 2");
            
            /** The joystick button you play with with you little finger (Pinkie on *that* side
             * of the pond :P)
             */        
            public static final Button PINKIE = new Button("Pinkie");
            
            /** Joystick button on the base of the device
             */        
            public static final Button BASE = new Button("Base");
            
            /** Second joystick button on the base of the device
             */        
            public static final Button BASE2 = new Button("Base 2");
            
            /** Thrid joystick button on the base of the device
             */        
            public static final Button BASE3 = new Button("Base 3");
            
            /** Fourth joystick button on the base of the device
             */        
            public static final Button BASE4 = new Button("Base 4");
            
            /** Fifth joystick button on the base of the device
             */        
            public static final Button BASE5 = new Button("Base 5");
            
            /** Sixth joystick button on the base of the device
             */        
            public static final Button BASE6 = new Button("Base 6");
            
            /** erm, dunno, but it's in the defines so it might exist.
             */        
            public static final Button DEAD = new Button("Dead");
            
            /** 'A' button on a gamepad
             */        
            public static final Button A = new Button("A");
            
            /** 'B' button on a gamepad
             */        
            public static final Button B = new Button("B");
            
            /** 'C' button on a gamepad
             */        
            public static final Button C = new Button("C");
            
            /** 'X' button on a gamepad
             */        
            public static final Button X = new Button("X");
            
            /** 'Y' button on a gamepad
             */        
            public static final Button Y = new Button("Y");
            
            /** 'Z' button on a gamepad
             */        
            public static final Button Z = new Button("Z");
            
            /** Left thumb button on a gamepad
             */        
            public static final Button LEFT_THUMB = new Button("Left Thumb");
            
            /** Right thumb button on a gamepad
             */        
            public static final Button RIGHT_THUMB = new Button("Right Thumb");
            
            /** Second left thumb button on a gamepad
             */        
            public static final Button LEFT_THUMB2 = new Button("Left Thumb 2");
            
            /** Second right thumb button on a gamepad
             */        
            public static final Button RIGHT_THUMB2 = new Button("Right Thumb 2");
            
            /** 'Select' button on a gamepad
             */        
            public static final Button SELECT = new Button("Select");
            
            /** 'Mode' button on a gamepad
             */        
            public static final Button MODE = new Button("Mode");
            
            /** Another left thumb button on a gamepad (how many thumbs do you have??)
             */        
            public static final Button LEFT_THUMB3 = new Button("Left Thumb 3");
            
            /** Another right thumb button on a gamepad
             */        
            public static final Button RIGHT_THUMB3 = new Button("Right Thumb 3");
            
            /** Digitiser pen tool button
             */        
            public static final Button TOOL_PEN = new Button("Pen");
            
            /** Digitiser rubber (eraser) tool button
             */        
            public static final Button TOOL_RUBBER = new Button("Rubber");
            
            /** Digitiser brush tool button
             */        
            public static final Button TOOL_BRUSH = new Button("Brush");
            
            /** Digitiser pencil tool button
             */        
            public static final Button TOOL_PENCIL = new Button("Pencil");
            
            /** Digitiser airbrush tool button
             */        
            public static final Button TOOL_AIRBRUSH = new Button("Airbrush");
            
            /** Digitiser finger tool button
             */        
            public static final Button TOOL_FINGER = new Button("Finger");
            
            /** Digitiser mouse tool button
             */        
            public static final Button TOOL_MOUSE = new Button("Mouse");
            
            /** Digitiser lens tool button
             */        
            public static final Button TOOL_LENS = new Button("Lens");
            
            /** Digitiser touch button
             */        
            public static final Button TOUCH = new Button("Touch");
            
            /** Digitiser stylus button
             */        
            public static final Button STYLUS = new Button("Stylus");
            
            /** Second digitiser stylus button
             */        
            public static final Button STYLUS2 = new Button("Stylus 2");
            
            /**
             * An unknown button
             */
            public static final Button UNKNOWN = new Button("Unknown");

            /**
             * Returns the back mouse button.
             */
            public static final Button BACK = new Button("Back");

            /**
             * Returns the extra mouse button.
             */
            public static final Button EXTRA = new Button("Extra");

            /**
             * Returns the forward mouse button.
             */
            public static final Button FORWARD = new Button("Forward");

            /**
             * The primary or leftmost mouse button.
             */
            public static final Button LEFT = new Button("Left");

            /**
             * Returns the middle mouse button, not present if the mouse has fewer than three buttons.
             */
            public static final Button MIDDLE = new Button("Middle");

            /**
             * The secondary or rightmost mouse button, not present if the mouse is a single-button mouse.
             */
            public static final Button RIGHT = new Button("Right");

            /**
             * Returns the side mouse button.
             */
            public static final Button SIDE = new Button("Side");
 
        }

        /**
         * KeyIDs for standard PC (LATIN-1) keyboards
         */
        public static class Key extends Identifier {
            
            private int keyID;
            
            /**
             * Protected constructor
             */
            protected Key(String name, int keyID) {
                super(name);
                this.keyID = keyID;
            }
            
            protected Key(int keyID) {
                this("Key " + keyID, keyID);
            }
            
            public int getKeyIndex() {
                return keyID;
            }
            
            /**
             * Standard keyboard (LATIN-1) keys
             * UNIX X11 keysym values are listed to the right
             */
            public static final Key VOID        = new Key("Void", 0); // MS 0x00 UNIX 0xFFFFFF
            public static final Key ESCAPE      = new Key("Escape", 1); // MS 0x01 UNIX 0xFF1B
            public static final Key _1          = new Key("1", 2); // MS 0x02 UNIX 0x031 EXCLAM 0x021
            public static final Key _2          = new Key("2", 3); // MS 0x03 UNIX 0x032 AT 0x040
            public static final Key _3          = new Key("3", 4); // MS 0x04 UNIX 0x033 NUMBERSIGN 0x023
            public static final Key _4          = new Key("4", 5); // MS 0x05 UNIX 0x034 DOLLAR 0x024
            public static final Key _5          = new Key("5", 6); // MS 0x06 UNIX 0x035 PERCENT 0x025
            public static final Key _6          = new Key("6", 7); // MS 0x07 UNIX 0x036 CIRCUMFLEX 0x05e
            public static final Key _7          = new Key("7", 8); // MS 0x08 UNIX 0x037 AMPERSAND 0x026
            public static final Key _8          = new Key("8", 9); // MS 0x09 UNIX 0x038 ASTERISK 0x02a
            public static final Key _9          = new Key("9", 10); // MS 0x0A UNIX 0x039 PARENLEFT 0x028
            public static final Key _0          = new Key("0", 11); // MS 0x0B UNIX 0x030 PARENRIGHT 0x029
            public static final Key MINUS       = new Key("-", 12); // MS 0x0C UNIX 0x02d UNDERSCORE 0x05f
            public static final Key EQUALS      = new Key("=", 13); // MS 0x0D UNIX 0x03d PLUS 0x02b
            public static final Key BACK        = new Key("Back", 14); // MS 0x0E UNIX 0xFF08
            public static final Key TAB         = new Key("Tab", 15); // MS 0x0F UNIX 0xFF09
            public static final Key Q           = new Key("Q", 16); // MS 0x10 UNIX 0x071 UPPER 0x051
            public static final Key W           = new Key("W", 17); // MS 0x11 UNIX 0x077 UPPER 0x057
            public static final Key E           = new Key("E", 18); // MS 0x12 UNIX 0x065 UPPER 0x045
            public static final Key R           = new Key("R", 19); // MS 0x13 UNIX 0x072 UPPER 0x052
            public static final Key T           = new Key("T", 20); // MS 0x14 UNIX 0x074 UPPER 0x054
            public static final Key Y           = new Key("Y", 21); // MS 0x15 UNIX 0x079 UPPER 0x059
            public static final Key U           = new Key("U", 22); // MS 0x16 UNIX 0x075 UPPER 0x055
            public static final Key I           = new Key("I", 23); // MS 0x17 UNIX 0x069 UPPER 0x049
            public static final Key O           = new Key("O", 24); // MS 0x18 UNIX 0x06F UPPER 0x04F
            public static final Key P           = new Key("P", 25); // MS 0x19 UNIX 0x070 UPPER 0x050
            public static final Key LBRACKET    = new Key("[", 26); // MS 0x1A UNIX 0x05b BRACE 0x07b
            public static final Key RBRACKET    = new Key("]", 27); // MS 0x1B UNIX 0x05d BRACE 0x07d
            public static final Key RETURN      = new Key("Return", 28); // MS 0x1C UNIX 0xFF0D
            public static final Key LCONTROL    = new Key("Left Control", 29); // MS 0x1D UNIX 0xFFE3
            public static final Key A           = new Key("A", 30); // MS 0x1E UNIX 0x061 UPPER 0x041
            public static final Key S           = new Key("S", 31); // MS 0x1F UNIX 0x073 UPPER 0x053
            public static final Key D           = new Key("D", 32); // MS 0x20 UNIX 0x064 UPPER 0x044
            public static final Key F           = new Key("F", 33); // MS 0x21 UNIX 0x066 UPPER 0x046
            public static final Key G           = new Key("G", 34); // MS 0x22 UNIX 0x067 UPPER 0x047
            public static final Key H           = new Key("H", 35); // MS 0x23 UNIX 0x068 UPPER 0x048
            public static final Key J           = new Key("J", 36); // MS 0x24 UNIX 0x06A UPPER 0x04A
            public static final Key K           = new Key("K", 37); // MS 0x25 UNIX 0x06B UPPER 0x04B
            public static final Key L           = new Key("L", 38); // MS 0x26 UNIX 0x06C UPPER 0x04C
            public static final Key SEMICOLON   = new Key(";", 39); // MS 0x27 UNIX 0x03b COLON 0x03a
            public static final Key APOSTROPHE  = new Key("'", 40); // MS 0x28 UNIX 0x027 QUOTEDBL 0x022
            public static final Key GRAVE       = new Key("~", 41); // MS 0x29 UNIX 0x060 TILDE 0x07e
            public static final Key LSHIFT      = new Key("Left Shift", 42); // MS 0x2A UNIX 0xFFE1
            public static final Key BACKSLASH   = new Key("\\", 43); // MS 0x2B UNIX 0x05c BAR 0x07c
            public static final Key Z           = new Key("Z", 44); // MS 0x2C UNIX 0x07A UPPER 0x05A
            public static final Key X           = new Key("X", 45); // MS 0x2D UNIX 0x078 UPPER 0x058
            public static final Key C           = new Key("C", 46); // MS 0x2E UNIX 0x063 UPPER 0x043
            public static final Key V           = new Key("V", 47); // MS 0x2F UNIX 0x076 UPPER 0x056
            public static final Key B           = new Key("B", 48); // MS 0x30 UNIX 0x062 UPPER 0x042
            public static final Key N           = new Key("N", 49); // MS 0x31 UNIX 0x06E UPPER 0x04E
            public static final Key M           = new Key("M", 50); // MS 0x32 UNIX 0x06D UPPER 0x04D
            public static final Key COMMA       = new Key(",", 51); // MS 0x33 UNIX 0x02c LESS 0x03c
            public static final Key PERIOD      = new Key(".", 52); // MS 0x34 UNIX 0x02e GREATER 0x03e
            public static final Key SLASH       = new Key("/", 53); // MS 0x35 UNIX 0x02f QUESTION 0x03f
            public static final Key RSHIFT      = new Key("Right Shift", 54); // MS 0x36 UNIX 0xFFE2
            public static final Key MULTIPLY    = new Key("Multiply", 55); // MS 0x37 UNIX 0xFFAA
            public static final Key LALT        = new Key("Left Alt", 56); // MS 0x38 UNIX 0xFFE9
            public static final Key SPACE       = new Key(" ", 57); // MS 0x39 UNIX 0x020
            public static final Key CAPITAL     = new Key("Caps Lock", 58); // MS 0x3A UNIX 0xFFE5 SHIFTLOCK 0xFFE6
            public static final Key F1          = new Key("F1", 59); // MS 0x3B UNIX 0xFFBE
            public static final Key F2          = new Key("F2", 60); // MS 0x3C UNIX 0xFFBF
            public static final Key F3          = new Key("F3", 61); // MS 0x3D UNIX 0xFFC0
            public static final Key F4          = new Key("F4", 62); // MS 0x3E UNIX 0xFFC1
            public static final Key F5          = new Key("F5", 63); // MS 0x3F UNIX 0xFFC2
            public static final Key F6          = new Key("F6", 64); // MS 0x40 UNIX 0xFFC3
            public static final Key F7          = new Key("F7", 65); // MS 0x41 UNIX 0xFFC4
            public static final Key F8          = new Key("F8", 66); // MS 0x42 UNIX 0xFFC5
            public static final Key F9          = new Key("F9", 67); // MS 0x43 UNIX 0xFFC6
            public static final Key F10         = new Key("F10", 68); // MS 0x44 UNIX 0xFFC7
            public static final Key NUMLOCK     = new Key("Num Lock", 69); // MS 0x45 UNIX 0xFF7F
            public static final Key SCROLL      = new Key("Scroll Lock", 70); // MS 0x46 UNIX 0xFF14
            public static final Key NUMPAD7     = new Key("Num 7", 71); // MS 0x47 UNIX 0xFFB7 HOME 0xFF95
            public static final Key NUMPAD8     = new Key("Num 8", 72); // MS 0x48 UNIX 0xFFB8 UP 0xFF97
            public static final Key NUMPAD9     = new Key("Num 9", 73); // MS 0x49 UNIX 0xFFB9 PRIOR 0xFF9A
            public static final Key SUBTRACT    = new Key("Num -", 74); // MS 0x4A UNIX 0xFFAD
            public static final Key NUMPAD4     = new Key("Num 4", 75); // MS 0x4B UNIX 0xFFB4 LEFT 0xFF96
            public static final Key NUMPAD5     = new Key("Num 5", 76); // MS 0x4C UNIX 0xFFB5
            public static final Key NUMPAD6     = new Key("Num 6", 77); // MS 0x4D UNIX 0xFFB6 RIGHT 0xFF98
            public static final Key ADD         = new Key("Num +", 78); // MS 0x4E UNIX 0xFFAB
            public static final Key NUMPAD1     = new Key("Num 1", 79); // MS 0x4F UNIX 0xFFB1 END 0xFF9C
            public static final Key NUMPAD2     = new Key("Num 2", 80); // MS 0x50 UNIX 0xFFB2 DOWN 0xFF99
            public static final Key NUMPAD3     = new Key("Num 3", 81); // MS 0x51 UNIX 0xFFB3 NEXT 0xFF9B
            public static final Key NUMPAD0     = new Key("Num 0", 82); // MS 0x52 UNIX 0xFFB0 INSERT 0xFF9E
            public static final Key DECIMAL     = new Key("Num .", 83); // MS 0x53 UNIX 0xFFAE DELETE 0xFF9F
            public static final Key F11         = new Key("F11", 84); // MS 0x57 UNIX 0xFFC8
            public static final Key F12         = new Key("F12", 85); // MS 0x58 UNIX 0xFFC9
            public static final Key F13         = new Key("F13", 86); // MS 0x64 UNIX 0xFFCA
            public static final Key F14         = new Key("F14", 87); // MS 0x65 UNIX 0xFFCB
            public static final Key F15         = new Key("F15", 88); // MS 0x66 UNIX 0xFFCC
            public static final Key KANA        = new Key(89); // MS 0x70 UNIX 0xFF2D
            public static final Key CONVERT     = new Key(90); // MS 0x79 Japanese keyboard
            public static final Key NOCONVERT   = new Key(91); // MS 0x7B Japanese keyboard
            public static final Key YEN         = new Key(92); // MS 0x7D UNIX 0x0a5
            public static final Key NUMPADEQUAL = new Key("Num =", 93); // MS 0x8D UNIX 0xFFBD
            public static final Key CIRCUMFLEX  = new Key(94); // MS 0x90 Japanese keyboard
            public static final Key AT          = new Key(95); // MS 0x91 UNIX 0x040
            public static final Key COLON       = new Key(96); // MS 0x92 UNIX 0x03a
            public static final Key UNDERLINE   = new Key(97); // MS 0x93 NEC PC98
            public static final Key KANJI       = new Key(98); // MS 0x94 UNIX 0xFF21
            public static final Key STOP        = new Key(99); // MS 0x95 UNIX 0xFF69
            public static final Key AX          = new Key(100); // MS 0x96 Japan AX
            public static final Key UNLABELED   = new Key(101); // MS 0x97 J3100
            public static final Key NUMPADENTER = new Key("Num Enter", 102); // MS 0x9C UNIX 0xFF8D
            public static final Key RCONTROL    = new Key("Right Control", 103); // MS 0x9D UNIX 0xFFE4
            public static final Key NUMPADCOMMA = new Key("Num ,", 104); // MS 0xB3 UNIX 0xFFAC
            public static final Key DIVIDE      = new Key("Num /", 105); // MS 0xB5 UNIX 0xFFAF
            public static final Key SYSRQ       = new Key(106); // MS 0xB7 UNIX 0xFF15 PRINT 0xFF61
            public static final Key RALT        = new Key("Right Alt", 107); // MS 0xB8 UNIX 0xFFEA
            public static final Key PAUSE       = new Key("Pause", 108); // MS 0xC5 UNIX 0xFF13 BREAK 0xFF6B
            public static final Key HOME        = new Key("Home", 109); // MS 0xC7 UNIX 0xFF50
            public static final Key UP          = new Key("Up", 110); // MS 0xC8 UNIX 0xFF52
            public static final Key PAGEUP      = new Key("Pg Up", 111); // MS 0xC9 UNIX 0xFF55
            public static final Key LEFT        = new Key("Left", 112); // MS 0xCB UNIX 0xFF51
            public static final Key RIGHT       = new Key("Right", 113); // MS 0xCD UNIX 0xFF53
            public static final Key END         = new Key("End", 114); // MS 0xCF UNIX 0xFF57
            public static final Key DOWN        = new Key("Down", 115); // MS 0xD0 UNIX 0xFF54
            public static final Key PAGEDOWN    = new Key("Pg Down", 116); // MS 0xD1 UNIX 0xFF56
            public static final Key INSERT      = new Key("Insert", 117); // MS 0xD2 UNIX 0xFF63
            public static final Key DELETE      = new Key("Delete", 118); // MS 0xD3 UNIX 0xFFFF
            public static final Key LWIN        = new Key("Left Windows", 119); // MS 0xDB UNIX META 0xFFE7 SUPER 0xFFEB HYPER 0xFFED
            public static final Key RWIN        = new Key("Right Windows", 120); // MS 0xDC UNIX META 0xFFE8 SUPER 0xFFEC HYPER 0xFFEE
            public static final Key APPS        = new Key(121); // MS 0xDD UNIX 0xFF67
            public static final Key POWER       = new Key("Power", 122); // MS 0xDE Sun 0x1005FF76 SHIFT 0x1005FF7D
            public static final Key SLEEP       = new Key("Sleep", 123); // MS 0xDF No UNIX keysym
            public static final Key UNKNOWN     = new Key("Unknown", 0);
            protected static final Key FIRST = VOID;
            protected static final Key LAST = SLEEP;
        } // class StandardKeyboard.KeyID

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
