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

import net.java.games.util.plugins.Plugin;

/** Environment plugin for linux
 * @author Jeremy Booth (jeremy@newdawnsoftware.com)
 */
public class LinuxEnvironmentPlugin extends ControllerEnvironment implements Plugin {
    
    static {
        System.loadLibrary("jinput");
    }

    /** List of controllers
     */    
    private Controller[] controllers;
    
    /** Creates a new instance of LinuxEnvironmentPlugin */
    public LinuxEnvironmentPlugin() {
        LinuxNativeTypesMap.init();
        init();
        createControllers();
    }
    
    /** Returns a list of all controllers available to this environment,
     * or an empty array if there are no controllers in this environment.
     * @return Returns a list of all controllers available to this environment,
     * or an empty array if there are no controllers in this environment.
     */
    public Controller[] getControllers() {
        return controllers;
    }
    
    /** Create the controllers
     */    
    private void createControllers() {
        int numDevices = getNumberOfDevices();
        
        controllers = new Controller[numDevices];
        
        for(int i=0;i<numDevices;i++) {
            controllers[i] = createDevice(i);
        }
    }
    
    /** Create a particular device
     * @param deviceNumber The device ID
     * @return The new device
     */    
    private Controller createDevice(int deviceNumber) {
        String name = getDeviceName(deviceNumber);
        int numAbsAxes = getNumAbsAxes(deviceNumber);
        int numRelAxes = getNumRelAxes(deviceNumber);
        int numButtons = getNumButtons(deviceNumber);
        Controller device = null;
        
        int mouseCharacteristic = 0;
        int keyboardCharacteristic = 0;
        int joystickCharacteristic = 0;
        
        // we are going to try and guess what type of controller it is now
        if(name.toLowerCase().indexOf("mouse")>=0) {
            mouseCharacteristic++;
        }
        if(name.toLowerCase().indexOf("keyboard")>=0) {
            keyboardCharacteristic++;
        }
        if(name.toLowerCase().indexOf("joystick")>=0) {
            joystickCharacteristic++;
        }
        
        if(numRelAxes>=2) {
            mouseCharacteristic++;
        } else {
            mouseCharacteristic--;
        }
        if(numAbsAxes>=2) {
            joystickCharacteristic++;
        } else {
            joystickCharacteristic--;
        }
        if(numButtons>64) {
            keyboardCharacteristic++;
        } else {
            keyboardCharacteristic--;
        }
        
        if((mouseCharacteristic > keyboardCharacteristic) && (mouseCharacteristic > joystickCharacteristic)) {
            device = new LinuxMouse(new LinuxDevice(deviceNumber, name, numButtons, numRelAxes, numAbsAxes));
        } else if((keyboardCharacteristic > mouseCharacteristic) && (keyboardCharacteristic > joystickCharacteristic)) {
            device = new LinuxKeyboard(deviceNumber, name, numButtons, numRelAxes, numAbsAxes);
        } else if((joystickCharacteristic > keyboardCharacteristic) && (joystickCharacteristic > mouseCharacteristic)) {
            device = new LinuxDevice(deviceNumber, name, numButtons, numRelAxes, numAbsAxes);
        } else {
            //Dunno what this is, but try it anyway
            device = new LinuxDevice(deviceNumber, name, numButtons, numRelAxes, numAbsAxes);
        }
        return device;
    }
    
    /** Get the name of a device from the native library
     * @param deviceID The device id
     * @return The devices name
     */    
    private native String getDeviceName(int deviceID);
    /** Get the number of absolute axes for the requested device
     * @param deviceID The device ID
     * @return The number of abs axes
     */    
    private native int getNumAbsAxes(int deviceID);
    /** Get the nmber or relative axes from the native library
     * @param deviceID The native device ID
     * @return The number of raltive axes for the device
     */    
    private native int getNumRelAxes(int deviceID);
    /** Gets the number of buttons for the requested devce from the native library
     * @param deviceID The device ID
     * @return The number of buttons
     */    
    private native int getNumButtons(int deviceID);
    /** Initialises the native library
     * @return <0 if something went wrong
     */    
    private native int init();
    /** Gets the number of devices the native library found
     * @return Th number of devices
     */    
    private native int getNumberOfDevices();
    
}
