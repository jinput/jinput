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
 * A Keyboard is a type of controller consisting of a single controller,
 * they keypad, which contains several axes (the keys).  By default, all keys
 * are set to receive polling data.
 */
public abstract class Keyboard extends AbstractController {
    
    /**
     * Protected constructor.
     * Subclasses should initialize the array of axes to an array of keys.
     * @param name The name of the keyboard
     */
    protected Keyboard(String name) {
        super(name);
    }
    
    /**
     * Returns the type of the Controller.
     */
    public Type getType() {
        return Type.KEYBOARD;
    }

    /**
     * Returns the component corresponding to a particular key on the keypad,
     * or null if a key with the specified ID could not be found.
     */
    public Component getComponent(Component.Identifier id) {
        assert components != null;
        // Default implementation uses indices to lookup keys
        // in the array of axes
        if(id instanceof Component.Identifier.Key) {
	        int index = ((Component.Identifier.Key)id).getKeyIndex();
	        assert components.length > index;
	        return components[index];
        }
        return null;
    }
    
    /**
     * Returns whether or not the given key has been pressed since the last
     * call to poll.  This is called from a key's getPollData method.
     */
    protected abstract boolean isKeyPressed(Key key);
    
    /**
     * Axis representing a single key.  By default, all keys are set to receive
     * polling data.
     */
    public class Key extends AbstractComponent {
        
        /**
         * Key identifier
         */
        private final Component.Identifier.Key keyID;
        
        /**
         * Construct a new key object
         */
        public Key(Component.Identifier.Key keyID) {
            super(keyID.toString(), keyID);
            this.keyID = keyID;
        }
    
        /**
         * Returns <code>true</code> if data returned from <code>poll</code>
         * is relative to the last call, or <code>false</code> if data
         * is absolute.
         * @return false by default, can be overridden
         */
        public final boolean isRelative() {
            return false;
        }
        
        /**
         * Returns the data from the last time the control has been polled.
         * The value returned will be either 0.0f or 1.0f.  The result is always
         * 0.0f if polling is turned off.
         */
        public float getPollData() {
            if (!isPolling()) {
                return 0.0f;
            }
            return (isKeyPressed(this) ? 1.0f : 0.0f);
        }
    } // class Keyboard.Key
} // class Keyboard
