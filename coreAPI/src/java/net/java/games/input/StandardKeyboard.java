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
 * Identifiers for physical keys for standard PC (LATIN-1) keyboards.
 */
public abstract class StandardKeyboard extends Keyboard {
    
    private Key[] standardKeys = {
        new Key(KeyID.VOID       ), new Key(KeyID.ESCAPE     ),
        new Key(KeyID._1         ), new Key(KeyID._2         ),
        new Key(KeyID._3         ), new Key(KeyID._4         ),
        new Key(KeyID._5         ), new Key(KeyID._6         ),
        new Key(KeyID._7         ), new Key(KeyID._8         ),
        new Key(KeyID._9         ), new Key(KeyID._0         ),
        new Key(KeyID.MINUS      ), new Key(KeyID.EQUALS     ),
        new Key(KeyID.BACK       ), new Key(KeyID.TAB        ),
        new Key(KeyID.Q          ), new Key(KeyID.W          ),
        new Key(KeyID.E          ), new Key(KeyID.R          ),
        new Key(KeyID.T          ), new Key(KeyID.Y          ),
        new Key(KeyID.U          ), new Key(KeyID.I          ),
        new Key(KeyID.O          ), new Key(KeyID.P          ),
        new Key(KeyID.LBRACKET   ), new Key(KeyID.RBRACKET   ),
        new Key(KeyID.RETURN     ), new Key(KeyID.LCONTROL   ),
        new Key(KeyID.A          ), new Key(KeyID.S          ),
        new Key(KeyID.D          ), new Key(KeyID.F          ),
        new Key(KeyID.G          ), new Key(KeyID.H          ),
        new Key(KeyID.J          ), new Key(KeyID.K          ),
        new Key(KeyID.L          ), new Key(KeyID.SEMICOLON  ),
        new Key(KeyID.APOSTROPHE ), new Key(KeyID.GRAVE      ),
        new Key(KeyID.LSHIFT     ), new Key(KeyID.BACKSLASH  ),
        new Key(KeyID.Z          ), new Key(KeyID.X          ),
        new Key(KeyID.C          ), new Key(KeyID.V          ),
        new Key(KeyID.B          ), new Key(KeyID.N          ),
        new Key(KeyID.M          ), new Key(KeyID.COMMA      ),
        new Key(KeyID.PERIOD     ), new Key(KeyID.SLASH      ),
        new Key(KeyID.RSHIFT     ), new Key(KeyID.MULTIPLY   ),
        new Key(KeyID.LALT       ), new Key(KeyID.SPACE      ),
        new Key(KeyID.CAPITAL    ), new Key(KeyID.F1         ),
        new Key(KeyID.F2         ), new Key(KeyID.F3         ),
        new Key(KeyID.F4         ), new Key(KeyID.F5         ),
        new Key(KeyID.F6         ), new Key(KeyID.F7         ),
        new Key(KeyID.F8         ), new Key(KeyID.F9         ),
        new Key(KeyID.F10        ), new Key(KeyID.NUMLOCK    ),
        new Key(KeyID.SCROLL     ), new Key(KeyID.NUMPAD7    ),
        new Key(KeyID.NUMPAD8    ), new Key(KeyID.NUMPAD9    ),
        new Key(KeyID.SUBTRACT   ), new Key(KeyID.NUMPAD4    ),
        new Key(KeyID.NUMPAD5    ), new Key(KeyID.NUMPAD6    ),
        new Key(KeyID.ADD        ), new Key(KeyID.NUMPAD1    ),
        new Key(KeyID.NUMPAD2    ), new Key(KeyID.NUMPAD3    ),
        new Key(KeyID.NUMPAD0    ), new Key(KeyID.DECIMAL    ),
        new Key(KeyID.F11        ), new Key(KeyID.F12        ),
        new Key(KeyID.F13        ), new Key(KeyID.F14        ),
        new Key(KeyID.F15        ), new Key(KeyID.KANA       ),
        new Key(KeyID.CONVERT    ), new Key(KeyID.NOCONVERT  ),
        new Key(KeyID.YEN        ), new Key(KeyID.NUMPADEQUAL),
        new Key(KeyID.CIRCUMFLEX ), new Key(KeyID.AT         ),
        new Key(KeyID.COLON      ), new Key(KeyID.UNDERLINE  ),
        new Key(KeyID.KANJI      ), new Key(KeyID.STOP       ),
        new Key(KeyID.AX         ), new Key(KeyID.UNLABELED  ),
        new Key(KeyID.NUMPADENTER), new Key(KeyID.RCONTROL   ),
        new Key(KeyID.NUMPADCOMMA), new Key(KeyID.DIVIDE     ),
        new Key(KeyID.SYSRQ      ), new Key(KeyID.RALT       ),
        new Key(KeyID.PAUSE      ), new Key(KeyID.HOME       ),
        new Key(KeyID.UP         ), new Key(KeyID.PRIOR      ),
        new Key(KeyID.LEFT       ), new Key(KeyID.RIGHT      ),
        new Key(KeyID.END        ), new Key(KeyID.DOWN       ),
        new Key(KeyID.NEXT       ), new Key(KeyID.INSERT     ),
        new Key(KeyID.DELETE     ), new Key(KeyID.LWIN       ),
        new Key(KeyID.RWIN       ), new Key(KeyID.APPS       ),
        new Key(KeyID.POWER      ), new Key(KeyID.SLEEP      ),
    };
        
    /**
     * Creates a new standard keyboard object with the default keys
     * for a standard keyboard.
     */
    protected StandardKeyboard(String name) {
        super(name);
        axes = standardKeys;
    }
    
    /**
     * KeyIDs for standard PC (LATIN-1) keyboards
     */
    public static class KeyID extends Keyboard.KeyID {
        /**
         * Protected constructor
         */
        protected KeyID(int keyID) {
            super(keyID);
        }
        /**
         * Standard keyboard (LATIN-1) keys
         * UNIX X11 keysym values are listed to the right
         */
        public static final KeyID VOID        = new KeyID(0); // MS 0x00 UNIX 0xFFFFFF
        public static final KeyID ESCAPE      = new KeyID(1); // MS 0x01 UNIX 0xFF1B
        public static final KeyID _1          = new KeyID(2); // MS 0x02 UNIX 0x031 EXCLAM 0x021
        public static final KeyID _2          = new KeyID(3); // MS 0x03 UNIX 0x032 AT 0x040
        public static final KeyID _3          = new KeyID(4); // MS 0x04 UNIX 0x033 NUMBERSIGN 0x023
        public static final KeyID _4          = new KeyID(5); // MS 0x05 UNIX 0x034 DOLLAR 0x024
        public static final KeyID _5          = new KeyID(6); // MS 0x06 UNIX 0x035 PERCENT 0x025
        public static final KeyID _6          = new KeyID(7); // MS 0x07 UNIX 0x036 CIRCUMFLEX 0x05e
        public static final KeyID _7          = new KeyID(8); // MS 0x08 UNIX 0x037 AMPERSAND 0x026
        public static final KeyID _8          = new KeyID(9); // MS 0x09 UNIX 0x038 ASTERISK 0x02a
        public static final KeyID _9          = new KeyID(10); // MS 0x0A UNIX 0x039 PARENLEFT 0x028
        public static final KeyID _0          = new KeyID(11); // MS 0x0B UNIX 0x030 PARENRIGHT 0x029
        public static final KeyID MINUS       = new KeyID(12); // MS 0x0C UNIX 0x02d UNDERSCORE 0x05f
        public static final KeyID EQUALS      = new KeyID(13); // MS 0x0D UNIX 0x03d PLUS 0x02b
        public static final KeyID BACK        = new KeyID(14); // MS 0x0E UNIX 0xFF08
        public static final KeyID TAB         = new KeyID(15); // MS 0x0F UNIX 0xFF09
        public static final KeyID Q           = new KeyID(16); // MS 0x10 UNIX 0x071 UPPER 0x051
        public static final KeyID W           = new KeyID(17); // MS 0x11 UNIX 0x077 UPPER 0x057
        public static final KeyID E           = new KeyID(18); // MS 0x12 UNIX 0x065 UPPER 0x045
        public static final KeyID R           = new KeyID(19); // MS 0x13 UNIX 0x072 UPPER 0x052
        public static final KeyID T           = new KeyID(20); // MS 0x14 UNIX 0x074 UPPER 0x054
        public static final KeyID Y           = new KeyID(21); // MS 0x15 UNIX 0x079 UPPER 0x059
        public static final KeyID U           = new KeyID(22); // MS 0x16 UNIX 0x075 UPPER 0x055
        public static final KeyID I           = new KeyID(23); // MS 0x17 UNIX 0x069 UPPER 0x049
        public static final KeyID O           = new KeyID(24); // MS 0x18 UNIX 0x06F UPPER 0x04F
        public static final KeyID P           = new KeyID(25); // MS 0x19 UNIX 0x070 UPPER 0x050
        public static final KeyID LBRACKET    = new KeyID(26); // MS 0x1A UNIX 0x05b BRACE 0x07b
        public static final KeyID RBRACKET    = new KeyID(27); // MS 0x1B UNIX 0x05d BRACE 0x07d
        public static final KeyID RETURN      = new KeyID(28); // MS 0x1C UNIX 0xFF0D
        public static final KeyID LCONTROL    = new KeyID(29); // MS 0x1D UNIX 0xFFE3
        public static final KeyID A           = new KeyID(30); // MS 0x1E UNIX 0x061 UPPER 0x041
        public static final KeyID S           = new KeyID(31); // MS 0x1F UNIX 0x073 UPPER 0x053
        public static final KeyID D           = new KeyID(32); // MS 0x20 UNIX 0x064 UPPER 0x044
        public static final KeyID F           = new KeyID(33); // MS 0x21 UNIX 0x066 UPPER 0x046
        public static final KeyID G           = new KeyID(34); // MS 0x22 UNIX 0x067 UPPER 0x047
        public static final KeyID H           = new KeyID(35); // MS 0x23 UNIX 0x068 UPPER 0x048
        public static final KeyID J           = new KeyID(36); // MS 0x24 UNIX 0x06A UPPER 0x04A
        public static final KeyID K           = new KeyID(37); // MS 0x25 UNIX 0x06B UPPER 0x04B
        public static final KeyID L           = new KeyID(38); // MS 0x26 UNIX 0x06C UPPER 0x04C
        public static final KeyID SEMICOLON   = new KeyID(39); // MS 0x27 UNIX 0x03b COLON 0x03a
        public static final KeyID APOSTROPHE  = new KeyID(40); // MS 0x28 UNIX 0x027 QUOTEDBL 0x022
        public static final KeyID GRAVE       = new KeyID(41); // MS 0x29 UNIX 0x060 TILDE 0x07e
        public static final KeyID LSHIFT      = new KeyID(42); // MS 0x2A UNIX 0xFFE1
        public static final KeyID BACKSLASH   = new KeyID(43); // MS 0x2B UNIX 0x05c BAR 0x07c
        public static final KeyID Z           = new KeyID(44); // MS 0x2C UNIX 0x07A UPPER 0x05A
        public static final KeyID X           = new KeyID(45); // MS 0x2D UNIX 0x078 UPPER 0x058
        public static final KeyID C           = new KeyID(46); // MS 0x2E UNIX 0x063 UPPER 0x043
        public static final KeyID V           = new KeyID(47); // MS 0x2F UNIX 0x076 UPPER 0x056
        public static final KeyID B           = new KeyID(48); // MS 0x30 UNIX 0x062 UPPER 0x042
        public static final KeyID N           = new KeyID(49); // MS 0x31 UNIX 0x06E UPPER 0x04E
        public static final KeyID M           = new KeyID(50); // MS 0x32 UNIX 0x06D UPPER 0x04D
        public static final KeyID COMMA       = new KeyID(51); // MS 0x33 UNIX 0x02c LESS 0x03c
        public static final KeyID PERIOD      = new KeyID(52); // MS 0x34 UNIX 0x02e GREATER 0x03e
        public static final KeyID SLASH       = new KeyID(53); // MS 0x35 UNIX 0x02f QUESTION 0x03f
        public static final KeyID RSHIFT      = new KeyID(54); // MS 0x36 UNIX 0xFFE2
        public static final KeyID MULTIPLY    = new KeyID(55); // MS 0x37 UNIX 0xFFAA
        public static final KeyID LALT        = new KeyID(56); // MS 0x38 UNIX 0xFFE9
        public static final KeyID SPACE       = new KeyID(57); // MS 0x39 UNIX 0x020
        public static final KeyID CAPITAL     = new KeyID(58); // MS 0x3A UNIX 0xFFE5 SHIFTLOCK 0xFFE6
        public static final KeyID F1          = new KeyID(59); // MS 0x3B UNIX 0xFFBE
        public static final KeyID F2          = new KeyID(60); // MS 0x3C UNIX 0xFFBF
        public static final KeyID F3          = new KeyID(61); // MS 0x3D UNIX 0xFFC0
        public static final KeyID F4          = new KeyID(62); // MS 0x3E UNIX 0xFFC1
        public static final KeyID F5          = new KeyID(63); // MS 0x3F UNIX 0xFFC2
        public static final KeyID F6          = new KeyID(64); // MS 0x40 UNIX 0xFFC3
        public static final KeyID F7          = new KeyID(65); // MS 0x41 UNIX 0xFFC4
        public static final KeyID F8          = new KeyID(66); // MS 0x42 UNIX 0xFFC5
        public static final KeyID F9          = new KeyID(67); // MS 0x43 UNIX 0xFFC6
        public static final KeyID F10         = new KeyID(68); // MS 0x44 UNIX 0xFFC7
        public static final KeyID NUMLOCK     = new KeyID(69); // MS 0x45 UNIX 0xFF7F
        public static final KeyID SCROLL      = new KeyID(70); // MS 0x46 UNIX 0xFF14
        public static final KeyID NUMPAD7     = new KeyID(71); // MS 0x47 UNIX 0xFFB7 HOME 0xFF95
        public static final KeyID NUMPAD8     = new KeyID(72); // MS 0x48 UNIX 0xFFB8 UP 0xFF97
        public static final KeyID NUMPAD9     = new KeyID(73); // MS 0x49 UNIX 0xFFB9 PRIOR 0xFF9A
        public static final KeyID SUBTRACT    = new KeyID(74); // MS 0x4A UNIX 0xFFAD
        public static final KeyID NUMPAD4     = new KeyID(75); // MS 0x4B UNIX 0xFFB4 LEFT 0xFF96
        public static final KeyID NUMPAD5     = new KeyID(76); // MS 0x4C UNIX 0xFFB5
        public static final KeyID NUMPAD6     = new KeyID(77); // MS 0x4D UNIX 0xFFB6 RIGHT 0xFF98
        public static final KeyID ADD         = new KeyID(78); // MS 0x4E UNIX 0xFFAB
        public static final KeyID NUMPAD1     = new KeyID(79); // MS 0x4F UNIX 0xFFB1 END 0xFF9C
        public static final KeyID NUMPAD2     = new KeyID(80); // MS 0x50 UNIX 0xFFB2 DOWN 0xFF99
        public static final KeyID NUMPAD3     = new KeyID(81); // MS 0x51 UNIX 0xFFB3 NEXT 0xFF9B
        public static final KeyID NUMPAD0     = new KeyID(82); // MS 0x52 UNIX 0xFFB0 INSERT 0xFF9E
        public static final KeyID DECIMAL     = new KeyID(83); // MS 0x53 UNIX 0xFFAE DELETE 0xFF9F
        public static final KeyID F11         = new KeyID(84); // MS 0x57 UNIX 0xFFC8
        public static final KeyID F12         = new KeyID(85); // MS 0x58 UNIX 0xFFC9
        public static final KeyID F13         = new KeyID(86); // MS 0x64 UNIX 0xFFCA
        public static final KeyID F14         = new KeyID(87); // MS 0x65 UNIX 0xFFCB
        public static final KeyID F15         = new KeyID(88); // MS 0x66 UNIX 0xFFCC
        public static final KeyID KANA        = new KeyID(89); // MS 0x70 UNIX 0xFF2D
        public static final KeyID CONVERT     = new KeyID(90); // MS 0x79 Japanese keyboard
        public static final KeyID NOCONVERT   = new KeyID(91); // MS 0x7B Japanese keyboard
        public static final KeyID YEN         = new KeyID(92); // MS 0x7D UNIX 0x0a5
        public static final KeyID NUMPADEQUAL = new KeyID(93); // MS 0x8D UNIX 0xFFBD
        public static final KeyID CIRCUMFLEX  = new KeyID(94); // MS 0x90 Japanese keyboard
        public static final KeyID AT          = new KeyID(95); // MS 0x91 UNIX 0x040
        public static final KeyID COLON       = new KeyID(96); // MS 0x92 UNIX 0x03a
        public static final KeyID UNDERLINE   = new KeyID(97); // MS 0x93 NEC PC98
        public static final KeyID KANJI       = new KeyID(98); // MS 0x94 UNIX 0xFF21
        public static final KeyID STOP        = new KeyID(99); // MS 0x95 UNIX 0xFF69
        public static final KeyID AX          = new KeyID(100); // MS 0x96 Japan AX
        public static final KeyID UNLABELED   = new KeyID(101); // MS 0x97 J3100
        public static final KeyID NUMPADENTER = new KeyID(102); // MS 0x9C UNIX 0xFF8D
        public static final KeyID RCONTROL    = new KeyID(103); // MS 0x9D UNIX 0xFFE4
        public static final KeyID NUMPADCOMMA = new KeyID(104); // MS 0xB3 UNIX 0xFFAC
        public static final KeyID DIVIDE      = new KeyID(105); // MS 0xB5 UNIX 0xFFAF
        public static final KeyID SYSRQ       = new KeyID(106); // MS 0xB7 UNIX 0xFF15 PRINT 0xFF61
        public static final KeyID RALT        = new KeyID(107); // MS 0xB8 UNIX 0xFFEA
        public static final KeyID PAUSE       = new KeyID(108); // MS 0xC5 UNIX 0xFF13 BREAK 0xFF6B
        public static final KeyID HOME        = new KeyID(109); // MS 0xC7 UNIX 0xFF50
        public static final KeyID UP          = new KeyID(110); // MS 0xC8 UNIX 0xFF52
        public static final KeyID PRIOR       = new KeyID(111); // MS 0xC9 UNIX 0xFF55
        public static final KeyID LEFT        = new KeyID(112); // MS 0xCB UNIX 0xFF51
        public static final KeyID RIGHT       = new KeyID(113); // MS 0xCD UNIX 0xFF53
        public static final KeyID END         = new KeyID(114); // MS 0xCF UNIX 0xFF57
        public static final KeyID DOWN        = new KeyID(115); // MS 0xD0 UNIX 0xFF54
        public static final KeyID NEXT        = new KeyID(116); // MS 0xD1 UNIX 0xFF56
        public static final KeyID INSERT      = new KeyID(117); // MS 0xD2 UNIX 0xFF63
        public static final KeyID DELETE      = new KeyID(118); // MS 0xD3 UNIX 0xFFFF
        public static final KeyID LWIN        = new KeyID(119); // MS 0xDB UNIX META 0xFFE7 SUPER 0xFFEB HYPER 0xFFED
        public static final KeyID RWIN        = new KeyID(120); // MS 0xDC UNIX META 0xFFE8 SUPER 0xFFEC HYPER 0xFFEE
        public static final KeyID APPS        = new KeyID(121); // MS 0xDD UNIX 0xFF67
        public static final KeyID POWER       = new KeyID(122); // MS 0xDE Sun 0x1005FF76 SHIFT 0x1005FF7D
        public static final KeyID SLEEP       = new KeyID(123); // MS 0xDF No UNIX keysym
        protected static final KeyID FIRST = VOID;
        protected static final KeyID LAST = SLEEP;
    } // class StandardKeyboard.KeyID
} // class StandardKeyboard
