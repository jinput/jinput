/*
 * DirectInputRumbler.java
 *
 * Created on 01 December 2003, 21:39
 */

package net.java.games.input;

/**
 *
 * @author  Jeremy
 */
public class DirectInputRumbler implements net.java.games.input.Rumbler {
    
    private DirectInputDevice device;
    private long effect;
    private Axis.Identifier axisID;
    private String axisName;
    
    /** Creates a new instance of DirectInputRumbler */
    public DirectInputRumbler(DirectInputDevice device, long effect, Axis.Identifier axisID, String axisName) {
        this.device = device;
        this.effect = effect;
        this.axisID = axisID;
        this.axisName = axisName;
    }
    
    public Axis.Identifier getAxisIdentifier() {
        return axisID;
    }
    
    public String getAxisName() {
        return axisName;
    }
    
    public void rumble(float intensity) {
        setRumble(effect, intensity);
    }
    
    private native void setRumble(long effect, float intensity);
}
