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

import java.util.HashMap;

/** Class that represents a keyboard under linux
 * @author Jeremy Booth (jeremy@newdawnsoftware.com)
 */
public class LinuxKeyboard extends StandardKeyboard {
    
    private HashMap keyMap = new HashMap();
    private LinuxDevice realController;
    
    /** Creates a new instance of LinuxKeyboard
     * @param nativeID Native device id
     * @param name Name of this keybaord
     * @param numButtons Number of keys
     * @param numRelAxes Number of relative axes (you never know)
     * @param numAbsAxes Number of absolute axes (you never know)
     */
    public LinuxKeyboard(LinuxDevice realController) {
        super(realController.getName());
	this.realController = realController;
        
        setupKeyMap();
    }
    
    /** Returns whether or not the given key has been pressed since the last
     * call to poll.  This is called from a key's getPollData method.
     * @param key The key to check
     * @return the value fo the key
     */
    protected boolean isKeyPressed(Key key) {
        Axis button = (Axis)keyMap.get(key.getIdentifier());
	if(button.getPollData()!=0) return true; else return false;
    }
    
    /** Polls axes for data.  Returns false if the controller is no longer valid.
     * Polling reflects the current state of the device when polled.
     * @return False if this device is invalid.
     */
    public boolean poll() {
        return realController.poll();
    }
    
    /** Goes through every key to initialise the key map
     */    
    private void setupKeyMap() {
    	Axis[] allButtons = realController.getButtons();
	for(int i=0;i<allButtons.length;i++) {
	    Axis tempButton = allButtons[i];
	    keyMap.put(tempButton.getIdentifier(), tempButton);
	}
    }
    
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
