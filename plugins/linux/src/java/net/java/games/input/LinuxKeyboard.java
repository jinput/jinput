/**
 * Copyright (C) 2003 Jeremy Booth (jeremy@newdawnsoftware.com)
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this 
 * list of conditions and the following disclaimer. Redistributions in binary 
 * form must reproduce the above copyright notice, this list of conditions and 
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. 
 * The name of the author may not be used to endorse or promote products derived
 * from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO 
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
 */
package net.java.games.input;

/** Class that represents a keyboard under linux
 * @author Jeremy Booth (jeremy@newdawnsoftware.com)
 */
public class LinuxKeyboard extends StandardKeyboard {
    
    /** Values for the keys
     */    
    private int keyData[];
    /** Needed for the polling methods
     */    
    private int dummyRelAxesData[];
    /** Needed for the polling methods
     */    
    private int dummyAbsAxesData[];
    /** Map of native key numbers from jinput key id key indexes.
     */    
    private int keyMap[];
    /** List of keys this keyboard supports
     */    
    private int supportedKeys[];
    /** Number of keys this keyboard has
     */    
    private int numKeys;
    /** Port type that this keyboard is connected to.
     */    
    private PortType portType;
    /** The native device id
     */    
    private int nativeID;
    
    /** Creates a new instance of LinuxKeyboard
     * @param nativeID Native device id
     * @param name Name of this keybaord
     * @param numButtons Number of keys
     * @param numRelAxes Number of relative axes (you never know)
     * @param numAbsAxes Number of absolute axes (you never know)
     */
    public LinuxKeyboard(int nativeID, String name, int numButtons, int numRelAxes, int numAbsAxes) {
        super(name);
        
        throw new RuntimeException("Error, should not get here");
        
        /*children = NO_CONTROLLERS;
        rumblers = NO_RUMBLERS;
        
        this.nativeID = nativeID;
        
        portType = LinuxNativeTypesMap.getPortType(getNativePortType(nativeID));
        
        dummyRelAxesData = new int[numRelAxes];
        dummyAbsAxesData = new int[numAbsAxes];
        
        this.numKeys = numButtons;
        keyData = new int[numButtons+1];
        supportedKeys = new int[numButtons+1];
        keyMap = new int[KeyID.LAST.getKeyIndex()];
        
        getSupportedButtons(supportedKeys);
        supportedKeys[numKeys] = NativeDefinitions.KEY_UNKNOWN;
        
        setupKeyMap();
        renameKeys();*/
    }
    
    /** Returns whether or not the given key has been pressed since the last
     * call to poll.  This is called from a key's getPollData method.
     * @param key The key to check
     * @return the value fo the key
     */
    protected boolean isKeyPressed(Key key) {
        /*if(((Keyboard.KeyID) key.getIdentifier()).getKeyIndex() == StandardKeyboard.KeyID.ESCAPE.getKeyIndex()) {
            System.out.println("Asked if key " + key + " was pressed");
            System.out.println("key id " + key.getIdentifier());
            System.out.println("keyIndex " + ((Keyboard.KeyID) key.getIdentifier()).getKeyIndex());
            System.out.println("keyMap for index is " + keyMap[((Keyboard.KeyID) key.getIdentifier()).getKeyIndex()]);
            System.out.println("name for supportedKeys index is " + LinuxNativeTypesMap.getButtonName(supportedKeys[keyMap[((Keyboard.KeyID) key.getIdentifier()).getKeyIndex()]]));
            System.out.println("id for supportedKeys index is " + LinuxNativeTypesMap.getButtonID(supportedKeys[keyMap[((Keyboard.KeyID) key.getIdentifier()).getKeyIndex()]]));
            System.out.flush();
        }*/
        if(keyData[keyMap[((Keyboard.KeyID) key.getIdentifier()).getKeyIndex()]] > 0) {
            return true;
        }
        return false;
    }
    
    /** Polls axes for data.  Returns false if the controller is no longer valid.
     * Polling reflects the current state of the device when polled.
     * @return False if this device is invalid.
     */
    public boolean poll() {
        int retval = nativePoll(nativeID, keyData, dummyRelAxesData, dummyAbsAxesData);
        if(retval>=0) return true;
        return false;
    }
    
    /** Goes through every key to initialise the key map
     */    
    private void setupKeyMap() {
        for(int i=0;i<KeyID.LAST.getKeyIndex();i++) {
            keyMap[i] = numKeys;
        }
        for(int i=0;i<numKeys;i++) {
            int tempNativeID = supportedKeys[i];
            Keyboard.KeyID tempKeyID = StandardKeyboard.KeyID.VOID;
            try {
                tempKeyID = (Keyboard.KeyID)LinuxNativeTypesMap.getButtonID(tempNativeID);                
            } catch (ClassCastException e) {
                System.out.println("LinuxNativeTypesMap.getButtonID() returned " + LinuxNativeTypesMap.getButtonID(tempNativeID).getClass().toString());
            }            
            if(tempKeyID.getKeyIndex() < keyMap.length) {
                keyMap[tempKeyID.getKeyIndex()] = i;
                //System.out.println("keyMap[" + (tempKeyID.getKeyIndex()) + "] (" + tempKeyID + ") set to index " + i + " (" + LinuxNativeTypesMap.getButtonName(supportedKeys[i]) + ")");
            } else {
                //System.out.println("Linux key " + LinuxNativeTypesMap.getButtonName(tempNativeID) + " isn't supported by jinput");
            }
        }
    }
    
    /** Renames all the keys based on what information we have about them (number/name)
     */    
    private void renameKeys() {
        Axis tempAxes[] = getAxes();
        // Do this one by hand as it's a special case
        //((AbstractAxis)tempAxes[0]).setName("Unknown");
        for(int i=0;i<tempAxes.length;i++) {
            Axis tempAxis = tempAxes[i];
            int nativeKeyID = supportedKeys[keyMap[((Keyboard.KeyID) tempAxis.getIdentifier()).getKeyIndex()]];
            //System.out.println("key " + tempAxis + " map: " + nativeKeyID);
            if(nativeKeyID != NativeDefinitions.KEY_UNKNOWN) {
                String tempName = LinuxNativeTypesMap.getButtonName(nativeKeyID);
                ((AbstractAxis)tempAxis).setName(tempName);

                /*System.out.println("axis id is " + (Keyboard.KeyID) tempAxis.getIdentifier());
                System.out.println("keyMap[id] is " + keyMap[((Keyboard.KeyID) tempAxis.getIdentifier()).getKeyIndex()]);
                System.out.println("nativeKeyID is: " + nativeKeyID);             
                System.out.println("Set name of key " + ((Keyboard.KeyID) tempAxis.getIdentifier()).getKeyIndex() + " to " + tempName);*/
            }
        }
    }
    
    /** Gets all the supported keys for this device
     * @param supportedButtons The array if key types to populate
     */    
    private void getSupportedButtons(int supportedButtons[]) {
        getNativeSupportedButtons(nativeID, supportedButtons);
    }

    /** Gets the supported key types for a particular native device
     * @param deviceID The device ID
     * @param supportedButtons The array to populate with teh supported key ids
     */    
    private native void getNativeSupportedButtons(int deviceID, int supportedButtons[]);
    /** Calls the native library to poll the device
     * @param deviceID The device ID
     * @param buttonData Aray to populate with button values
     * @param relAxesData Array to populate with relative axis values
     * @param absAxesData Array to populate with absolute axes values
     * @return <0 if soething went wrong
     */    
    private native int nativePoll(int deviceID, int buttonData[], int relAxesData[], int absAxesData[]);
    /** Gets the port type from the native library for a particular keyboard
     * @param deviceID The keybaord id
     * @return native port ype
     */    
    private native int getNativePortType(int deviceID);

    /** Linux specific key ID's
     * @author Jeremy Booth (jeremy@newdawnsoftware.com)
     */    
    public static class KeyID extends StandardKeyboard.KeyID {
        
        /** The Page up key id
         */        
        public static final KeyID PAGEUP = new KeyID(StandardKeyboard.KeyID.LAST.getKeyIndex()+1, "Page up");
        /** Page down key id
         */        
        public static final KeyID PAGEDOWN = new KeyID(StandardKeyboard.KeyID.LAST.getKeyIndex()+1, "Page down");
        /** The last defined key ID
         */        
        protected static final KeyID LAST = PAGEDOWN;
        
        /** The name of this key ID
         */        
        String name;
        
        /** Construct a new key ID from the passed arguments
         * @param keyid The native ID of the key
         * @param name The name fo the key
         */        
        public KeyID(int keyid, String name) {
            super(keyid);
            this.name = name;
        }
        
        /** Returns a string representing this key ID
         * @return String representing this key id
         */        
        public String toString() {
            return super.toString() + " (" + name + ")";
        }
    }    
}
