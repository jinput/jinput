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

import net.java.games.input.AbstractAxis;
import net.java.games.input.LinuxDevice;

/** Represents an Axis absolute or relative
 *
 * @author Jeremy Booth (jeremy@newdawnsoftware.com)
 */
public class LinuxAxis extends AbstractAxis {
    
    /** The controller this axis is part of */
    private LinuxDevice controller;
    /** The native ID of this axis */
    private int axisID;
    /** The null zone, defaulted to 0 */
    private float nullZone = 0.0f;
    /** Is this axis analog */
    private boolean isAnalog = true;
    /** Are the values returned by getPollData normalised */
    private boolean isNormalized = false;
    /** Is this a relative axis */
    private boolean isRelative = false;
    /** Should we normalise the value before returning it in getPollData */
    private boolean needsNormalising = false;
    /** The minimum possible axis value (not always used) */    
    private float minAxisValue;
    /** The maximum possibe axis value (not always used */
    private float maxAxisValue;
    /** The current value of the axis */
    private float value = 0.0f;

    /**
     * Creates a new instance of LinuxAxis
     * @param controller The parent Controller
     * @param axisID The native ID of this axis
     * @param name The name of this axis
     * @param id The Axis.Identifier of this axis
     * @param deadzone The deadzone (null zone) of this axis
     * @param isAnalog Is this axis analog
     * @param isNormalized Is this axis normalised
     * @param isRelative Is this axis relative
     */
    public LinuxAxis(LinuxDevice controller, int axisID, String name, Identifier id, float deadzone, boolean isAnalog, boolean isNormalized, boolean isRelative) {
        super(name, id);
        this.controller = controller;
        this.axisID = axisID;
        this.nullZone = deadzone;
        this.isAnalog = isAnalog;
        this.isNormalized = isNormalized;
        this.isRelative = isRelative;
        
    }

    /** Creates a new instance of LinuxAxis, it will auto normalise the data based on
     * the minimum and maximum values provided
     * @param controller The parent Controller
     * @param axisID The native ID of this axis
     * @param name The name of this axis
     * @param id The Axis.Identifier of this axis
     * @param deadzone The deadzone (null zone) of this axis
     * @param isAnalog Is this axis analog
     * @param isRelative Is this axis relative
     * @param minAxisValue Minimum value that the native library will return for this axis
     * @param maxAxisValue Maximum value that the native library will return for this axis
     */
    public LinuxAxis(LinuxDevice controller, int axisID, String name, Identifier id, float deadzone, boolean isAnalog, boolean isRelative, float minAxisValue, float maxAxisValue) {
        super(name, id);
        
        this.controller = controller;
        this.axisID = axisID;
        this.nullZone = deadzone;
        this.isAnalog = isAnalog;
        this.isNormalized = false;
        this.isRelative = isRelative;
        this.needsNormalising = true;
        this.minAxisValue = minAxisValue;
        this.maxAxisValue = maxAxisValue;
        
    }
    
     /** Returns <code>true</code> if data returned from <code>poll</code>
      * is relative to the last call, or <code>false</code> if data
      * is absolute.
      * @return Returns <code>true</code> if data returned from <code>poll</code>
      * is relative to the last call, or <code>false</code> if data
      * is absolute.
      */
    public boolean isRelative() {
        return isRelative;
    }
    
    /** Returns the suggested dead zone for this axis.  Dead zone is the
     * amount polled data can vary before considered a significant change
     * in value.  An application can safely ignore changes less than this
     * value in the positive or negative direction.
     * @return 0.0f by default, can be overridden
     */
    public float getDeadZone() {
        return nullZone;
    }
    
    /** Returns whether or not the axis is analog, or false if it is digital.
     * @return false by default, can be overridden
     */
    public boolean isAnalog() {
        return isAnalog;
    }
    
    /** Returns the data from the last time the control has been polled.
     * If this axis is a button, the value returned will be either 0.0f or 1.0f.
     * If this axis is normalized, the value returned will be between -1.0f and
     * 1.0f.
     * @return 0.0f by default, can be overridden
     */
    public float getPollData() {  
        if(isPolling()) {
            updateValue();
        }
        return value;
    }
    
    /** Update this axis data from the latest native poll value
     */    
    private void updateValue() {
        if(isAnalog) {
            float tempVal;
            
            if(isRelative) {
                tempVal = (float)controller.getRelAxisValue(axisID);
            } else {
                tempVal = (float)controller.getAbsAxisValue(axisID);
            }
            if(needsNormalising) {
                if(isRelative) {
                    if(tempVal>1) {
                        tempVal = 1;
                    } else if(tempVal<-1) {
                        tempVal = -1;
                    }
                    value = tempVal;
                } else {
                    //float center = (minAxisValue + maxAxisValue) / 2;
                    //value = (tempVal - center) / center;
                    float center = ((maxAxisValue - minAxisValue)/2);
                    value = (((tempVal - minAxisValue) - center) / center);
                    //System.out.println("tempVal: " + tempVal + " minAxisValue: " + minAxisValue + " maxAxisValue: " + maxAxisValue + " center: " + center + " value: " + value);
                    //System.out.flush();
                }
            } else {
                value = tempVal;
            }
        } else {
            value = (float)controller.getButtonValue(axisID);
        }
    }
    
    /** Returns whether or not data polled from this axis is normalized
     * between the values of -1.0f and 1.0f.
     * @return true by default, can be overridden
     */
    public boolean isNormalized() {
        return (isNormalized || needsNormalising);
    }
    
}
