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
import net.java.games.input.StandardKeyboard;

/**
 * DirectInput keyboard implementation.
 * @author  martak
 * @version 
 */
class DirectInputKeyboard extends StandardKeyboard {

    /**
     * DIDEVTYPE_ constants from dinput.h header file
     */
    private static final int DIDEVTYPEKEYBOARD_UNKNOWN = 0;
    private static final int DIDEVTYPEKEYBOARD_PCXT = 1;
    private static final int DIDEVTYPEKEYBOARD_OLIVETTI = 2;
    private static final int DIDEVTYPEKEYBOARD_PCAT = 3;
    private static final int DIDEVTYPEKEYBOARD_PCENH = 4;
    private static final int DIDEVTYPEKEYBOARD_NOKIA1050 = 5;
    private static final int DIDEVTYPEKEYBOARD_NOKIA9140 = 6;
    private static final int DIDEVTYPEKEYBOARD_NEC98 = 7;
    private static final int DIDEVTYPEKEYBOARD_NEC98LAPTOP = 8;
    private static final int DIDEVTYPEKEYBOARD_NEC98106 = 9;
    private static final int DIDEVTYPEKEYBOARD_JAPAN106 = 10;
    private static final int DIDEVTYPEKEYBOARD_JAPANAX = 11;
    private static final int DIDEVTYPEKEYBOARD_J3100 = 12;
    
    /**
     * Key index crosstable; maps indices into the data array to virtual keys
     * in the key array in StandardKeyboard.
     * For example, the key F11 is at index 84 in the array of keys in
     * StandardKeyboard, yet it is data element 87 (0x57) in our data array.
     * <p>
     * To access the data element of a particular key (F11), we use the
     * crosstable as follows:
     * <p><code>
     * KeyID f11 = StandardKey.KeyID.F11;
     * int keyIndex = f11.getKeyIndex(); // returns 84
     * int crossIndex = CROSSTABLE[keyIndex]; // returns 0x57 (87)
     * </code>
     * To find a key given the data element index (87), we do a simple search
     * through the crosstable starting at that index (index 87/0x57 = 0x65) and
     * looking backwards until we find our key itself, or a lower number
     * (meaning the key was not found).  We can only take advantage of this
     * algorithm only because on Windows, our crosstable is in increasing order.
     */
    private final static int[] CROSSTABLE = {
        0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, // _9
        0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, // Y
        0x16, 0x17, 0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F, 0x20, // D
        0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B, // BACKSLASH
        0x2C, 0x2D, 0x2E, 0x2F, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, // RSHIFT
        0x37, 0x38, 0x39, 0x3A, 0x3B, 0x3C, 0x3D, 0x3E, 0x3F, 0x40, 0x41, // F7
        0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4A, 0x4B, 0x4C, // NUMPAD5
        0x4D, 0x4E, 0x4F, 0x50, 0x51, 0x52, 0x53, 0x57, 0x58, 0x64, 0x65, // F14
        0x66, 0x70, 0x79, 0x7B, 0x7D, 0x8D, 0x90, 0x91, 0x92, 0x93, 0x94, // KANJI
        0x95, 0x96, 0x97, 0x9C, 0x9D, 0xB3, 0xB5, 0xB7, 0xB8, 0xC5, 0xC7, // HOME
        0xC8, 0xC9, 0xCB, 0xCD, 0xCF, 0xD0, 0xD1, 0xD2, 0xD3, 0xDB, 0xDC, // RWIN
        0xDD, 0xDE, 0xDF // SLEEP
    };

    /**
     * Pointer to the IDirectInputDevice for this device
     */
    private long lpDevice;
    
    /**
     * Subtype of keyboard, defined by DIDEVTYPE constants
     */
    private int subtype;
    
    /**
     * Polling key data
     */
    private byte[] keyData = new byte[256];
    
    /**
     * Private constructor
     * @param lpDevice A pointer to the IDirectInputDevice for the device.
     * @param subtype The subtype of keyboard, as defined in the DIDEVTYPE
     * constants above
     * @param productName The product name for the device
     * @param instanceName The name of the device
     */
    private DirectInputKeyboard(long lpDevice, int subtype, String productName,
        String instanceName) {
        super(productName + " (" + instanceName + ")");
        this.lpDevice = lpDevice;
        this.subtype = subtype;
    }
    
    /**
     * Callback to rename a given key by index, name
     * @param index the index in the data array
     * @param name the name of the key
     */
    private void renameKey(int index, String name) {
        int keyIndex = index;
        if (keyIndex > CROSSTABLE.length) {
            keyIndex = CROSSTABLE.length - 1;
        }
        for (; CROSSTABLE[keyIndex] > index; keyIndex--)
            ;
        if (CROSSTABLE[keyIndex] == index) {
            Axis[] axes = getAxes();
            AbstractAxis key = (AbstractAxis)axes[index];
            if (name != null && name.length() > 0) {
                //System.out.println("Renaming key " + key.getName() +
                //    " to " + name + " index=" +
                //    index + " keyIndex=" + keyIndex + " CROSSTAB=" +
                //    CROSSTABLE[keyIndex]);
                key.setName(name);
            }
        } else {
            //System.out.println("Key not found " + name + " index=" + index +
            //    " keyIndex=" + keyIndex + " CROSSTAB=" +
            //    CROSSTABLE[keyIndex]);
        }
    }

    /** Polls axes for data.  Returns false if the controller is no longer valid.
     * Polling reflects the current state of the device when polled.
     * @return False if the KB is no longer valid, true otherwise.
     */
    public boolean poll() {
        return pollNative(lpDevice, keyData);
    }

    /** Returns whether or not the given key has been pressed since the last
     * call to poll.
     * @param key The key whose state to check.
     * @return true if this key has changed state since last read of its state, false otherwise.
     */
    protected boolean isKeyPressed(Key key) {
        KeyID id = (KeyID)key.getIdentifier();
        int keyIndex = id.getKeyIndex();
        int crossIndex = CROSSTABLE[keyIndex];
        return ((keyData[crossIndex] & 0x80) != 0);
    }
    
    /**
     * Polls the device; native method
     */
    private native boolean pollNative(long lpDevice, byte[] keyData);
    
    /**
     * Renames the keys with the name provided by DirectInput
     */
    private native boolean renameKeys(long lpDevice);
    
    /** Creates a new DirectInputKeyboard (factory method)
     * This is a function used internally during set up.
     * @param lpDevice A pointer to the IDirectInputDevice for the device.
     * @param subtype The subtype of keyboard, as defined in the DIDEVTYPE
     * constants above
     * @param productName The product name for the keyboard
     * @param instanceName The name of the keyboard
     * @return The new DirectInputKeyboard object.
     */
    public static DirectInputKeyboard createKeyboard(long lpDevice,
        int subtype, String productName, String instanceName) {
        DirectInputKeyboard ret = new DirectInputKeyboard(lpDevice, subtype,
            productName, instanceName);
        ret.renameKeys(lpDevice);
        return ret;
    }
} // class DirectInputKeyboard
