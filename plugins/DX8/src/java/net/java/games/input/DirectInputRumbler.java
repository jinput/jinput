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

/**
 * Implementation of the Rumbler interface for direct x
 *
 * @author Endolf
 */
public class DirectInputRumbler implements Rumbler {
    
    /** The parent device */
    private DirectInputDevice device;
    /** The native effect */
    private long effect;
    /** The identifier of the axis we are attached too */
    private Component.Identifier axisID;
    /** The name of the axis this rumbler is attached too */
    private String axisName;
    
    /** 
     * Creates a new instance of DirectInputRumbler 
     *
     * @param device The parent device
     * @param effect The native effect
     * @param axisID The id of the axis this rumbler is attached too
     * @param axisName The name of the axis this rumbler is attached too
     */
    public DirectInputRumbler(DirectInputDevice device, long effect, Component.Identifier axisID, String axisName) {
        this.device = device;
        this.effect = effect;
        this.axisID = axisID;
        this.axisName = axisName;
    }
    
    /**
     * Gets the identifier of the axis this rumbler is attached too
     *
     * @return The axis id
     */
    public Component.Identifier getAxisIdentifier() {
        return axisID;
    }
    
    /**
     * Gets the name of the axis this rumbler is attached too
     *
     * @return The axis name
     */
    public String getAxisName() {
        return axisName;
    }
    
    /**
     * Rumbles this rumbler at the given intensity. 
     * This will start or stop the effect if necesary. 
     * The intensity is in the range of -1 to 1 and will be clipped if values
     * outside that range are provided.
     *
     * @param intensity The intensity
     */
    public void rumble(float intensity) {
        if(intensity>1f) {
            intensity = 1.0f;
        } else if(intensity<-1f) {
            intensity = -1.0f;
        }
        setRumble(effect, intensity);
    }
    
    /**
     * Performs the native call(s) to run the effect
     *
     * @param effect The native effect
     * @param intensity The intensity of the rumble
     */
    private native void setRumble(long effect, float intensity);
}
