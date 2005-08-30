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
    
    /** List of controllers
     */    
    private Controller[] controllers;
    
    /** Creates a new instance of LinuxEnvironmentPlugin */
    public LinuxEnvironmentPlugin() {
        if(JInputLibrary.isSupported()) {
            LinuxNativeTypesMap.init();
            JInputLibrary.init();
	        createControllers();
        } else {
            controllers = new Controller[0];
        }
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
        int numDevices = JInputLibrary.getNumberOfDevices();
        
        Controller[] tempControllers = new Controller[numDevices];
        
        int numRealDevices = 0;
        Controller tempController;
        for(int i=0;i<numDevices;i++) {
            tempController = createDevice(i);
            if(tempController!=null) {
                if(tempController.getComponents().length>0 || tempController.getControllers().length>0) {
                    tempControllers[numRealDevices] = tempController;
                    numRealDevices++;
                }
            }
        }
        
        controllers = new Controller[numRealDevices];
        
        for(int i=0;i<numRealDevices;i++) {
            controllers[i] = tempControllers[i];
        }
    }
    
    /** Create a particular device
     * @param deviceNumber The device ID
     * @return The new device
     */    
    private Controller createDevice(int deviceNumber) {        
        String name = JInputLibrary.getDeviceName(deviceNumber);
        int numAbsAxes = JInputLibrary.getNumAbsAxes(deviceNumber);
        int numRelAxes = JInputLibrary.getNumRelAxes(deviceNumber);
        int numButtons = JInputLibrary.getNumButtons(deviceNumber);
        Controller device = null;
        
        System.out.println("Java working on device " + name);
        
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
            device = new LinuxDevice(deviceNumber, name, numButtons, numRelAxes, numAbsAxes, Controller.Type.KEYBOARD);
        } else if((joystickCharacteristic > keyboardCharacteristic) && (joystickCharacteristic > mouseCharacteristic)) {
            device = new LinuxDevice(deviceNumber, name, numButtons, numRelAxes, numAbsAxes);
        } else {
            //Dunno what this is, but try it anyway
            device = new LinuxDevice(deviceNumber, name, numButtons, numRelAxes, numAbsAxes);
        }
        return device;
    }
    
}
