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

import java.util.ArrayList;

/**
 * Represents a device that is not a keyboard or mouse.
 * @author Jeremy Booth (jeremy@newdawnsoftware.com)
 */
public class LinuxDevice extends AbstractController {
    
    /** The name of the device
     */    
    private String name;
    /** The native ID of the device
     */    
    private int nativeID;
    /** The port type the device is attached to.
     */    
    private PortType portType;
    /** List of buttons the device has
     */    
    private LinuxAxis[] buttons;
    /** List of relative axes the device has
     */    
    private LinuxAxis[] relAxes;
    /** List of absolute axes the device has
     */    
    private LinuxAxis[] absAxes;
    /** List of coolie hats the device has
     */    
    private LinuxHat[] hats;
    /** The id's of the coolie hat axes
     */    
    private int hatAxisIDs[] = new int[8];
    /** The number of buttons the device has
     */    
    private int numButtons;
    /** The number of relative axes the device has
     */    
    private int numRelAxes;
    /** The number of absolute axes the device has
     */    
    private int numAbsAxes;
    /** The number of coolie hats the device has
     */    
    private int numHats=0;
    /** The native button values.
     */    
    private int[] buttonData;
    /** The native relative axes values
     */    
    private int[] relAxesData;
    /** The native absolute axis values
     */    
    private int[] absAxesData;
    /** A guess at the device type.
     */    
    private Type typeGuess = Type.UNKNOWN;
    /** An array of the list of axes this device has
     */    
    private ArrayList axesArray = new ArrayList();
    
	/** Creates a new instance of LinuxDevice
	 * @param nativeID The native ID of this device
	 * @param name The name of this device
	 * @param numButtons The number of buttons the devices has
	 * @param numRelAxes The number of raltive axes this device has
	 * @param numAbsAxes The number of absolute axes this device has
	 */
	public LinuxDevice(int nativeID, String name, int numButtons, int numRelAxes, int numAbsAxes, Controller.Type type) {
		this(nativeID, name, numButtons, numRelAxes, numAbsAxes);
		typeGuess = type;
	}

    /** Creates a new instance of LinuxDevice
     * @param nativeID The native ID of this device
     * @param name The name of this device
     * @param numButtons The number of buttons the devices has
     * @param numRelAxes The number of raltive axes this device has
     * @param numAbsAxes The number of absolute axes this device has
     */
    public LinuxDevice(int nativeID, String name, int numButtons, int numRelAxes, int numAbsAxes) {
        super(name);
        
        children = NO_CONTROLLERS;
        rumblers = NO_RUMBLERS;

        this.nativeID = nativeID;
        
        portType = LinuxNativeTypesMap.getPortType(getNativePortType(nativeID));
        
        for(int i=0;i<8;i++) {
            hatAxisIDs[i] = -1;
        }
        
        this.numButtons = numButtons;
        this.numRelAxes = numRelAxes;
        this.numAbsAxes = numAbsAxes;
        this.numHats = 0;
        
        buttonData = new int[numButtons];
        relAxesData = new int[numRelAxes];
        absAxesData = new int[numAbsAxes];
        
        createButtons(numButtons);
        createRelAxes(numRelAxes);
        createAbsAxes(numAbsAxes);
        createHats();
        
        /*ArrayList tempAxes = new ArrayList();
        for(int i=0;i<numButtons;i++) {
            Axis tempAxis = buttons[i];
            if(tempAxis!=null) {
                tempAxes.add(tempAxis);
            }
        }
        for(int i=0;i<numRelAxes;i++) {
            Axis tempAxis = relAxes[i];
            if(tempAxis!=null) {
                tempAxes.add(tempAxis);
            }
        }
        for(int i=0;i<numAbsAxes;i++) {
            Axis tempAxis = absAxes[i];
            if(tempAxis!=null) {
                tempAxes.add(tempAxis);
            }
        }
        
        axes = (Axis[]) tempAxes.toArray(axes);*/
        for(int i=0;i<numAbsAxes;i++) {
            if(absAxes[i]!=null) {
                axesArray.add(absAxes[i]);
            }
        }
        for(int i=0;i<numRelAxes;i++) {
            if(relAxes[i]!=null) {
                axesArray.add(relAxes[i]);
            }
        }
        for(int i=0;i<numHats;i++) {
            if(hats[i]!=null) {
                axesArray.add(hats[i]);
            }
        }
        for(int i=0;i<numButtons;i++) {
            if(buttons[i]!=null) {
                axesArray.add(buttons[i]);
            }
        }
        components = (Component[]) axesArray.toArray(components);
        
        guessType();
    }
    
    /*public Axis[] getAxes(){
        Axis retval[] = new Axis[0];
        retval = (Axis []) axesArray.toArray(retval);
        return retval;
    }*/
    
    /**
     * Returns the port type that this device is connected to.
     * @return The port type
     */    
    public PortType getPortType() {
        return portType;
    }

    /**
     * Returns the type of the Controller.
     * @return The controller type.
     */
    public Type getType() {
    	return typeGuess;
    }
    
    /** Create the buttons for the device
     * @param numButtons The number of buttons the device has
     */    
    private void createButtons(int numButtons) {
        
	int supportedButtons[] = new int[numButtons];
	buttons = new LinuxAxis[numButtons];
	if(numButtons>0) {
            getSupportedButtons(supportedButtons);
            for(int i=0;i<numButtons;i++) {
                buttons[i] = createButton(i, supportedButtons[i]);
                //axesArray.add(buttons[i]);
            }
	}
    }
    
    /** Create the relative axes for the device
     * @param numRelAxes The number of relative axes the device has
     */    
    private void createRelAxes(int numRelAxes) {
        int supportedRelAxes[] = new int[numRelAxes];
        getSupportedRelAxes(supportedRelAxes);
        relAxes = new LinuxAxis[numRelAxes];
        for(int i=0;i<numRelAxes;i++) {
            relAxes[i] = createRelAxis(i, supportedRelAxes[i]);
            //axesArray.add(relAxes[i]);
        }
    }

    /** Create the absolute axes for the device
     * @param numAbsAxes The number of absolute axes the device has
     */    
    private void createAbsAxes(int numAbsAxes) {
        int supportedAbsAxes[] = new int[numAbsAxes];
        getSupportedAbsAxes(supportedAbsAxes);
        absAxes = new LinuxAxis[numAbsAxes];
        for(int i=0;i<numAbsAxes;i++) {
            // Nasy code here
            // Hats underlinux are 2 axis, combine them
            if(supportedAbsAxes[i] == NativeDefinitions.ABS_HAT0X) {
                hatAxisIDs[0] = i;
            } else if(supportedAbsAxes[i] == NativeDefinitions.ABS_HAT0Y) {
                hatAxisIDs[1] = i;
            } else if(supportedAbsAxes[i] == NativeDefinitions.ABS_HAT1X) {
                hatAxisIDs[2] = i;
            } else if(supportedAbsAxes[i] == NativeDefinitions.ABS_HAT1Y) {
                hatAxisIDs[3] = i;
            } else if(supportedAbsAxes[i] == NativeDefinitions.ABS_HAT2X) {
                hatAxisIDs[4] = i;
            } else if(supportedAbsAxes[i] == NativeDefinitions.ABS_HAT2Y) {
                hatAxisIDs[5] = i;
            } else if(supportedAbsAxes[i] == NativeDefinitions.ABS_HAT3X) {
                hatAxisIDs[6] = i;
            } else if(supportedAbsAxes[i] == NativeDefinitions.ABS_HAT3Y) {
                hatAxisIDs[7] = i;
            } else {
                absAxes[i] = createAbsAxis(i, supportedAbsAxes[i]);
                //axesArray.add(absAxes[i]);
            }
        }
        
    }
    
    /** Create the coolie hats for the device
     */    
    private void createHats() {
        LinuxHat tempHats[] = new LinuxHat[4];
        for(int i=0;i<4;i++) {
            int x = i*2;
            int y= x+1;
            //Check we have at least one hat axis (can hats have just one axis?
            if((hatAxisIDs[x]!=-1) || (hatAxisIDs[y]!=-1)) {
                String hatName = "Hat " + i;
                tempHats[numHats] = createHat(hatName, hatAxisIDs[x], hatAxisIDs[y]);
                numHats++;
            }
        }
        hats = new LinuxHat[numHats];
        for(int i=0;i<numHats;i++) {
            hats[i] = tempHats[i];
            //axesArray.add(hats[i]);
        }
    }
    
    /** Take a guess at the device type.
     */    
    private void guessType() {
        int joystickCharacteristic=0;
        int digitiserCharacteristic=0;
        int gamepadCharacteristic=0;
        int miscCharacteristic=0;
        int mouseCharacteristic=0;

        int supportedButtons[] = new int[numButtons];
        getSupportedButtons(supportedButtons);

        for(int i=0;i<numButtons;i++) {
            switch (supportedButtons[i]) {
                case NativeDefinitions.BTN_TRIGGER : 
                case NativeDefinitions.BTN_THUMB : 
                case NativeDefinitions.BTN_THUMB2 : 
                case NativeDefinitions.BTN_TOP : 
                case NativeDefinitions.BTN_TOP2 : 
                case NativeDefinitions.BTN_PINKIE : 
                case NativeDefinitions.BTN_BASE : 
                case NativeDefinitions.BTN_BASE2 : 
                case NativeDefinitions.BTN_BASE3 : 
                case NativeDefinitions.BTN_BASE4 : 
                case NativeDefinitions.BTN_BASE5 : 
                case NativeDefinitions.BTN_BASE6 : 
                case NativeDefinitions.BTN_DEAD : 
                    joystickCharacteristic++;
                    break;
                case NativeDefinitions.BTN_A : 
                case NativeDefinitions.BTN_B : 
                case NativeDefinitions.BTN_C : 
                case NativeDefinitions.BTN_X : 
                case NativeDefinitions.BTN_Y : 
                case NativeDefinitions.BTN_Z : 
                case NativeDefinitions.BTN_TL : 
                case NativeDefinitions.BTN_TR : 
                case NativeDefinitions.BTN_TL2 : 
                case NativeDefinitions.BTN_TR2 : 
                case NativeDefinitions.BTN_SELECT : 
                case NativeDefinitions.BTN_MODE : 
                case NativeDefinitions.BTN_THUMBL : 
                case NativeDefinitions.BTN_THUMBR :
                    gamepadCharacteristic++;
                    break;
                case NativeDefinitions.BTN_0 : 
                case NativeDefinitions.BTN_1 : 
                case NativeDefinitions.BTN_2 : 
                case NativeDefinitions.BTN_3 : 
                case NativeDefinitions.BTN_4 : 
                case NativeDefinitions.BTN_5 : 
                case NativeDefinitions.BTN_6 : 
                case NativeDefinitions.BTN_7 : 
                case NativeDefinitions.BTN_8 : 
                case NativeDefinitions.BTN_9 : 
                    miscCharacteristic++;
                    break;
                case NativeDefinitions.BTN_LEFT : 
                case NativeDefinitions.BTN_RIGHT : 
                case NativeDefinitions.BTN_MIDDLE : 
                case NativeDefinitions.BTN_SIDE : 
                case NativeDefinitions.BTN_EXTRA : 
                case NativeDefinitions.BTN_FORWARD : 
                case NativeDefinitions.BTN_BACK : 
                    mouseCharacteristic++;
                    break;
                case NativeDefinitions.BTN_TOOL_PEN :
                case NativeDefinitions.BTN_TOOL_RUBBER : 
                case NativeDefinitions.BTN_TOOL_BRUSH : 
                case NativeDefinitions.BTN_TOOL_PENCIL : 
                case NativeDefinitions.BTN_TOOL_AIRBRUSH : 
                case NativeDefinitions.BTN_TOOL_FINGER : 
                case NativeDefinitions.BTN_TOOL_MOUSE : 
                case NativeDefinitions.BTN_TOOL_LENS : 
                case NativeDefinitions.BTN_TOUCH : 
                case NativeDefinitions.BTN_STYLUS : 
                case NativeDefinitions.BTN_STYLUS2 : 
                    digitiserCharacteristic++;
                    break;
                default:
                    // no sweat, it's non of the above, erg                    
            }
        }
        if((joystickCharacteristic > 0) &&
	   (joystickCharacteristic >= digitiserCharacteristic) && 
           (joystickCharacteristic >= gamepadCharacteristic) &&
           (joystickCharacteristic >= miscCharacteristic) &&
           (joystickCharacteristic >= mouseCharacteristic)) {
            typeGuess = Type.STICK;
        } else if((gamepadCharacteristic > 0) &&
	          (gamepadCharacteristic >= digitiserCharacteristic) && 
                  (gamepadCharacteristic >= joystickCharacteristic) &&
                  (gamepadCharacteristic >= miscCharacteristic) &&
                  (gamepadCharacteristic >= mouseCharacteristic)) {
            typeGuess = Type.GAMEPAD;
        } else if((digitiserCharacteristic > 0) &&
	          (digitiserCharacteristic >= gamepadCharacteristic) && 
                  (digitiserCharacteristic >= joystickCharacteristic) &&
                  (digitiserCharacteristic >= miscCharacteristic) &&
                  (digitiserCharacteristic >= mouseCharacteristic)) {
            typeGuess = Type.TRACKPAD;
        } else if((miscCharacteristic > 0) &&
	          (miscCharacteristic >= gamepadCharacteristic) && 
                  (miscCharacteristic >= joystickCharacteristic) &&
                  (miscCharacteristic >= miscCharacteristic) &&
                  (miscCharacteristic >= mouseCharacteristic)) {
            // I'm not sure what one of these would be, but it has axis other 
            // wise a LinuxKeyboard would have been constructed, so assume its
            // some kind of stick;
            typeGuess = Type.STICK;
        } else if((mouseCharacteristic > 0) &&
	          (mouseCharacteristic >= digitiserCharacteristic) && 
                  (mouseCharacteristic >= joystickCharacteristic) &&
                  (mouseCharacteristic >= miscCharacteristic) &&
                  (mouseCharacteristic >= gamepadCharacteristic)) {
            // We shouldn't ever get here, as a mouse should have constructed
            // a LinuxMouse object, but you never know
            typeGuess = Type.MOUSE;
        }
	if(typeGuess == Type.STICK) {
	    String tempName = getName().toLowerCase();
	    if((tempName.indexOf("gamepad") > -1) || (tempName.indexOf("game pad") > -1)) {
	        typeGuess = Type.GAMEPAD;
	    }
	}
    }
    
    /** Create an button for the device
     * @param buttonNumber The button number
     * @param nativeButtonType The type of button
     * @return The new button
     */    
    private LinuxAxis createButton(int buttonNumber, int nativeButtonType) {
        Component.Identifier id = LinuxNativeTypesMap.getButtonID(nativeButtonType);
        String name = LinuxNativeTypesMap.getButtonName(nativeButtonType);
        if(name == null) {
            name = "Uknown button";
            id = Component.Identifier.Button.UNKNOWN;
        }
        
        return new LinuxAxis(this, buttonNumber, name, id, 0, false, true, false);
    }
    
    /** Create a relative axis for the device
     * @param axisNumber The native axis id
     * @param nativeType The native type
     * @return The new axis
     */    
    private LinuxAxis createRelAxis(int axisNumber, int nativeType) {
        Component.Identifier id = LinuxNativeTypesMap.getRelAxisID(nativeType);
        String name = LinuxNativeTypesMap.getRelAxisName(nativeType);
        
        // This is done to be like the windows version
        // return new LinuxAxis(this, axisNumber, name, id, 0, true, true, 0, 0);
        
        // this is what should be done
        return new LinuxAxis(this, axisNumber, name, id, 0, true, false, true);
    }
    
    /** Create an absolute axis for the device
     * @param axisNumber The native axis number
     * @param nativeType The native tpye
     * @return The new axis
     */    
    private LinuxAxis createAbsAxis(int axisNumber, int nativeType) {
        Component.Identifier id = LinuxNativeTypesMap.getAbsAxisID(nativeType);
        String name = LinuxNativeTypesMap.getAbsAxisName(nativeType);
        
        // Work around for a kernel level (I think) bug that incorrectly reports
        // the third axis as a rudder not a throttle on analog (gameport) 3 axis
        // 4 button sticks
        if((getName().equals("Analog 3-axis 4-button joystick")) && (portType == Controller.PortType.GAME)) {
            if((id == Component.Identifier.Axis.RZ) && (name.equals("Rudder"))) {
                id = Component.Identifier.Axis.SLIDER;
                name = "Throttle";
            }
        }
        
        return new LinuxAxis(this, axisNumber, name, id, getAbsAxisFuzz(axisNumber), true, false, getAbsAxisMinimum(axisNumber), getAbsAxisMaximum(axisNumber));
        //return new LinuxAxis(this, axisNumber, name, id, getAbsAxisFuzz(axisNumber), true, false, false);
    }
    
    /** Create a hat for the device
     * @param name The name of the hat to create
     * @param xAxisID The axis that is the hats X axis
     * @param yAxisID The axis that is the hats Y axis
     * @return The new hat
     */    
    private LinuxHat createHat(String name, int xAxisID, int yAxisID) {
        return new LinuxHat(this, name, xAxisID, yAxisID);
    }
    
    /** Polls axes for data.  Returns false if the controller is no longer valid.
     * Polling reflects the current state of the device when polled.
     * @return false if the controller is no longer valid.
     */
    public boolean poll() {
        int retval = nativePoll(nativeID, buttonData, relAxesData, absAxesData);
        if(retval>=0) return true;
        return false;
    }
    
    /**
     * Retursn the value of a particular button or key
     * @param buttonID The button/key to check
     * @return The value fo the button/key
     */    
    public float getButtonValue(int buttonID) {
        if(buttonData[buttonID]>0) return 1.0f;
        return 0.0f;
    }
    
    /**
     * Returns the value of a particular absolute axis
     * @param axisID The axis id
     * @return The axis value
     */    
    public float getAbsAxisValue(int axisID) {
        return (float) absAxesData[axisID];
    }
    
    /**
     * Returns the value of the requested relative axis.
     * @param axisID The native axis ID.
     * @return The value of the axis
     */    
    public float getRelAxisValue(int axisID) {
        return (float) relAxesData[axisID];
    }
    
    /**
     * Gets the axis fuzz, used for nullzone information
     * @param axisID The axis to get the fuzz for
     * @return The axis fuzz.
     */    
    public float getAbsAxisFuzz(int axisID) {
        return (float) getNativeAbsAxisFuzz(nativeID, axisID);
    }
    
    /**
     * Returns the maximum value for the requested axis
     * @param axisID The native ID of the axis to check
     * @return The maximum value
     */    
    public float getAbsAxisMaximum(int axisID) {
        return (float) getNativeAbsAxisMaximum(nativeID, axisID);
    }

    /**
     * The minimum value the requested axis can have
     * @param axisID The native axis ID
     * @return The minimum axis value
     */    
    public float getAbsAxisMinimum(int axisID) {
        return (float) getNativeAbsAxisMinimum(nativeID, axisID);
    }

    /** Return the enumeration of supported button types for this device
     * @param supportedButtons Array to populate
     */    
    private void getSupportedButtons(int supportedButtons[]) {
    	if(supportedButtons.length==0) {
	    return;
	}
        getNativeSupportedButtons(nativeID, supportedButtons);
    }

    /** Return the enumeration of supported absolute axis types for this device
     * @param suportedAbsAxes The array to populate
     */    
    private void getSupportedAbsAxes(int suportedAbsAxes[]) {
        getNativeSupportedAbsAxes(nativeID, suportedAbsAxes);
    }

    /** Return the enumeration of supported relative axis types for this device
     * @param supportedRelAxes The array to populate
     */    
    private void getSupportedRelAxes(int supportedRelAxes[]) {
        getNativeSupportedRelAxes(nativeID, supportedRelAxes);
    }
    
    /** Native call to get the supported absolute axes for a device
     * @param deviceID The native device number
     * @param supportedAbsAxes aray to populate
     */    
    private native void getNativeSupportedAbsAxes(int deviceID, int supportedAbsAxes[]);
    /** Native call to get the supported relative axes for a device
     * @param deviceID The native device ID
     * @param supportedRelAxes the array to populate
     */    
    private native void getNativeSupportedRelAxes(int deviceID, int supportedRelAxes[]);
    /** Native call to get the supported buttons for a device
     * @param deviceID The native device ID
     * @param supportedButtons The array to populate
     */    
    private native void getNativeSupportedButtons(int deviceID, int supportedButtons[]);
    /** Call to poll the device at the native library
     * @param deviceID The native device ID
     * @param buttonData Array to populate with button values
     * @param relAxesData Array to populate with relative axes values
     * @param absAxesData Array to populate with absolute axes values
     * @return the number of events read
     */    
    private native int nativePoll(int deviceID, int buttonData[], int relAxesData[], int absAxesData[]);
    /** Returns the fuzz of an axis fro mthe native lib
     * @param deviceID The native device id
     * @param axisID The native axis ID
     * @return The fuzz
     */    
    private native int getNativeAbsAxisFuzz(int deviceID, int axisID);
    /** Gets the maximum value for an absloute axis fr omthe native library
     * @param deviceID The native device ID
     * @param axisID The native axis ID
     * @return The Max value
     */    
    private native int getNativeAbsAxisMaximum(int deviceID, int axisID);
    /** Gets the minimum value for an absloute axis from the native library
     * @param deviceID The native device ID
     * @param axisID The native axis number
     * @return The min value
     */    
    private native int getNativeAbsAxisMinimum(int deviceID, int axisID);
    /** Gets the port type from the native lib
     * @param deviceID The device to get the port type for
     * @return The port type
     */    
    private native int getNativePortType(int deviceID);
    
    /**
     * A device that represents a joystick coolie hat.
     * @author Jeremy Booth (jeremy@newdawnsoftware.com)
     */
    public static class LinuxHat extends AbstractComponent {
        
        /** The parent controller
         */        
        private LinuxDevice controller;
        
        /** The xAxis for this hat
         */        
        private int xAxisID;
        
        /** The y axis for this hat
         */        
        private int yAxisID;
        
        /** The last polled value of this hat
         */        
        private float value;
        
        /**
         * Creates a new instance of LinuxHat, coolie hats under linux are reported as
         * two independant axis, this class is responsible for combining the axis values
         * and returning a value appropriate for a coolie hat.
         * @param controller The parent controller
         * @param name The name of this hat
         * @param xAxisID The X axis native axis ID
         * @param yAxisID The Y axis native axis ID
         */
        public LinuxHat(LinuxDevice controller, String name, int xAxisID, int yAxisID) {
            super(name, Component.Identifier.Axis.POV);
            
            System.out.println("Creating a Hat for device " + controller.getName() + " named " + name + " from axis " + xAxisID + " and " + yAxisID);
            
            this.controller = controller;
            this.xAxisID = xAxisID;
            this.yAxisID = yAxisID;
            
            //System.err.println("New hat: " + name + " created");
            //System.err.flush();
        }
        
        /** Returns <code>true</code> if data returned from <code>poll</code>
         * is relative to the last call, or <code>false</code> if data
         * is absolute.
         * @return Returns <code>true</code> if data returned from <code>poll</code>
         * is relative to the last call, or <code>false</code> if data
         * is absolute.
         */
        public boolean isRelative() {
            return false;
        }
        
        /** Returns true if this axis is analog
         * @return Always retursn true as coolie hats are analog under linux
         */        
        public boolean isAnalog() {
            return false;
        }
        
        /**
         * Retursn true if this axis is normalised
         * @return Always returns true as linux hats are normalised
         */        
        public boolean isNormalised() {
            return true;
        }
        
        /**
         * Returns the current value of this axis
         * @return The current axis value
         */        
        public float getPollData() {
            //System.err.println("getPollData called, isPolling: " + isPolling());
            //System.err.flush();
            if(isPolling()) { updateData(); };
            return value;
        }
        
        /** Gets the data fro mthe native level and combines it to the hats value
         */        
        private void updateData() {
            //System.err.println("updateData called");
            //System.err.flush();
            int newXAxisValue = (int)controller.getAbsAxisValue(xAxisID);
            int newYAxisValue = (int)controller.getAbsAxisValue(yAxisID);
            
            //System.err.println("newXAxisValue: " + newXAxisValue + " newYAxisValue: " + newYAxisValue);
            
            if((newXAxisValue == 0) && (newYAxisValue == 0)) {
                value = POV.OFF;
            } else if((newXAxisValue > 0) && (newYAxisValue < 0)) {
                value = POV.UP_RIGHT;
            } else if((newXAxisValue > 0) && (newYAxisValue == 0)) {
                value = POV.RIGHT;
            } else if((newXAxisValue > 0) && (newYAxisValue > 0)) {
                value = POV.DOWN_RIGHT;
            } else if((newXAxisValue == 0) && (newYAxisValue > 0)) {
                value = POV.DOWN;
            } else if((newXAxisValue < 0) && (newYAxisValue > 0)) {
                value = POV.DOWN_LEFT;
            } else if((newXAxisValue < 0) && (newYAxisValue == 0)) {
                value = POV.LEFT;
            } else if((newXAxisValue < 0) && (newYAxisValue < 0)) {
                value = POV.UP_LEFT;
            } else if((newXAxisValue == 0) && (newYAxisValue < 0)) {
                value = POV.UP;
            }
            
            //System.err.println("new value: " + value);
            //System.err.flush();
        }
        
    }
    
}
