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
        new Key(Component.Identifier.Key.VOID       ), new Key(Component.Identifier.Key.ESCAPE     ),
        new Key(Component.Identifier.Key._1         ), new Key(Component.Identifier.Key._2         ),
        new Key(Component.Identifier.Key._3         ), new Key(Component.Identifier.Key._4         ),
        new Key(Component.Identifier.Key._5         ), new Key(Component.Identifier.Key._6         ),
        new Key(Component.Identifier.Key._7         ), new Key(Component.Identifier.Key._8         ),
        new Key(Component.Identifier.Key._9         ), new Key(Component.Identifier.Key._0         ),
        new Key(Component.Identifier.Key.MINUS      ), new Key(Component.Identifier.Key.EQUALS     ),
        new Key(Component.Identifier.Key.BACK       ), new Key(Component.Identifier.Key.TAB        ),
        new Key(Component.Identifier.Key.Q          ), new Key(Component.Identifier.Key.W          ),
        new Key(Component.Identifier.Key.E          ), new Key(Component.Identifier.Key.R          ),
        new Key(Component.Identifier.Key.T          ), new Key(Component.Identifier.Key.Y          ),
        new Key(Component.Identifier.Key.U          ), new Key(Component.Identifier.Key.I          ),
        new Key(Component.Identifier.Key.O          ), new Key(Component.Identifier.Key.P          ),
        new Key(Component.Identifier.Key.LBRACKET   ), new Key(Component.Identifier.Key.RBRACKET   ),
        new Key(Component.Identifier.Key.RETURN     ), new Key(Component.Identifier.Key.LCONTROL   ),
        new Key(Component.Identifier.Key.A          ), new Key(Component.Identifier.Key.S          ),
        new Key(Component.Identifier.Key.D          ), new Key(Component.Identifier.Key.F          ),
        new Key(Component.Identifier.Key.G          ), new Key(Component.Identifier.Key.H          ),
        new Key(Component.Identifier.Key.J          ), new Key(Component.Identifier.Key.K          ),
        new Key(Component.Identifier.Key.L          ), new Key(Component.Identifier.Key.SEMICOLON  ),
        new Key(Component.Identifier.Key.APOSTROPHE ), new Key(Component.Identifier.Key.GRAVE      ),
        new Key(Component.Identifier.Key.LSHIFT     ), new Key(Component.Identifier.Key.BACKSLASH  ),
        new Key(Component.Identifier.Key.Z          ), new Key(Component.Identifier.Key.X          ),
        new Key(Component.Identifier.Key.C          ), new Key(Component.Identifier.Key.V          ),
        new Key(Component.Identifier.Key.B          ), new Key(Component.Identifier.Key.N          ),
        new Key(Component.Identifier.Key.M          ), new Key(Component.Identifier.Key.COMMA      ),
        new Key(Component.Identifier.Key.PERIOD     ), new Key(Component.Identifier.Key.SLASH      ),
        new Key(Component.Identifier.Key.RSHIFT     ), new Key(Component.Identifier.Key.MULTIPLY   ),
        new Key(Component.Identifier.Key.LALT       ), new Key(Component.Identifier.Key.SPACE      ),
        new Key(Component.Identifier.Key.CAPITAL    ), new Key(Component.Identifier.Key.F1         ),
        new Key(Component.Identifier.Key.F2         ), new Key(Component.Identifier.Key.F3         ),
        new Key(Component.Identifier.Key.F4         ), new Key(Component.Identifier.Key.F5         ),
        new Key(Component.Identifier.Key.F6         ), new Key(Component.Identifier.Key.F7         ),
        new Key(Component.Identifier.Key.F8         ), new Key(Component.Identifier.Key.F9         ),
        new Key(Component.Identifier.Key.F10        ), new Key(Component.Identifier.Key.NUMLOCK    ),
        new Key(Component.Identifier.Key.SCROLL     ), new Key(Component.Identifier.Key.NUMPAD7    ),
        new Key(Component.Identifier.Key.NUMPAD8    ), new Key(Component.Identifier.Key.NUMPAD9    ),
        new Key(Component.Identifier.Key.SUBTRACT   ), new Key(Component.Identifier.Key.NUMPAD4    ),
        new Key(Component.Identifier.Key.NUMPAD5    ), new Key(Component.Identifier.Key.NUMPAD6    ),
        new Key(Component.Identifier.Key.ADD        ), new Key(Component.Identifier.Key.NUMPAD1    ),
        new Key(Component.Identifier.Key.NUMPAD2    ), new Key(Component.Identifier.Key.NUMPAD3    ),
        new Key(Component.Identifier.Key.NUMPAD0    ), new Key(Component.Identifier.Key.DECIMAL    ),
        new Key(Component.Identifier.Key.F11        ), new Key(Component.Identifier.Key.F12        ),
        new Key(Component.Identifier.Key.F13        ), new Key(Component.Identifier.Key.F14        ),
        new Key(Component.Identifier.Key.F15        ), new Key(Component.Identifier.Key.KANA       ),
        new Key(Component.Identifier.Key.CONVERT    ), new Key(Component.Identifier.Key.NOCONVERT  ),
        new Key(Component.Identifier.Key.YEN        ), new Key(Component.Identifier.Key.NUMPADEQUAL),
        new Key(Component.Identifier.Key.CIRCUMFLEX ), new Key(Component.Identifier.Key.AT         ),
        new Key(Component.Identifier.Key.COLON      ), new Key(Component.Identifier.Key.UNDERLINE  ),
        new Key(Component.Identifier.Key.KANJI      ), new Key(Component.Identifier.Key.STOP       ),
        new Key(Component.Identifier.Key.AX         ), new Key(Component.Identifier.Key.UNLABELED  ),
        new Key(Component.Identifier.Key.NUMPADENTER), new Key(Component.Identifier.Key.RCONTROL   ),
        new Key(Component.Identifier.Key.NUMPADCOMMA), new Key(Component.Identifier.Key.DIVIDE     ),
        new Key(Component.Identifier.Key.SYSRQ      ), new Key(Component.Identifier.Key.RALT       ),
        new Key(Component.Identifier.Key.PAUSE      ), new Key(Component.Identifier.Key.HOME       ),
        new Key(Component.Identifier.Key.UP         ), new Key(Component.Identifier.Key.PAGEUP	   ),
        new Key(Component.Identifier.Key.LEFT       ), new Key(Component.Identifier.Key.RIGHT      ),
        new Key(Component.Identifier.Key.END        ), new Key(Component.Identifier.Key.DOWN       ),
        new Key(Component.Identifier.Key.PAGEDOWN   ), new Key(Component.Identifier.Key.INSERT     ),
        new Key(Component.Identifier.Key.DELETE     ), new Key(Component.Identifier.Key.LWIN       ),
        new Key(Component.Identifier.Key.RWIN       ), new Key(Component.Identifier.Key.APPS       ),
        new Key(Component.Identifier.Key.POWER      ), new Key(Component.Identifier.Key.SLEEP      ),
    };
        
    /**
     * Creates a new standard keyboard object with the default keys
     * for a standard keyboard.
     */
    protected StandardKeyboard(String name) {
        super(name);
        components = standardKeys;
    }
} // class StandardKeyboard
