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

/**  Class that represents a mouse under linux.
 *
 * @author Jeremy Booth (jeremy@newdawnsoftware.com)
 */
public class LinuxMouse extends Mouse {
    
    /** The parent linux device
     */    
    private LinuxDevice device;
    
    /** Creates a new instance of LinuxMouse
     * @param device The parent device
     */
    public LinuxMouse(LinuxDevice device) {
        super(device.getName());
        
        this.device = device;
        Component[] components = device.getComponents();
        Component x = null;
        Component y = null;
        Component wheel = null;
        Button left = null;
        Button right = null;
        Button middle = null;
        Button side = null;
        Button extra = null;
        Button forward = null;
        Button back = null;
        
        // start from the back, that way the first one is it
        for(int i = (components.length -1);i>=0;i--) {
            Component tempAxis = components[i];
            if(tempAxis.isRelative()) {
                if(tempAxis.getIdentifier() == Component.Identifier.Axis.X) {
                    x = tempAxis;
                } else if(tempAxis.getIdentifier() == Component.Identifier.Axis.Y) {
                    y = tempAxis;
                } else if(tempAxis.getIdentifier() == Component.Identifier.Axis.SLIDER) {
                    wheel = tempAxis;
                }
            } else if(!(tempAxis.isAnalog())) {
                if(tempAxis.getIdentifier() == Component.Identifier.Button.LEFT) {
                    left = new LinuxMouseButton(tempAxis);
                } else if(tempAxis.getIdentifier() == Component.Identifier.Button.RIGHT) {
                    right = new LinuxMouseButton(tempAxis);
                } else if(tempAxis.getIdentifier() == Component.Identifier.Button.MIDDLE) {
                    middle = new LinuxMouseButton(tempAxis);
                } else if(tempAxis.getIdentifier() == Component.Identifier.Button.SIDE) {
                    side = new LinuxMouseButton(tempAxis);
                } else if(tempAxis.getIdentifier() == Component.Identifier.Button.EXTRA) {
                    extra = new LinuxMouseButton(tempAxis);
                } else if(tempAxis.getIdentifier() == Component.Identifier.Button.FORWARD) {
                    forward = new LinuxMouseButton(tempAxis);
                } else if(tempAxis.getIdentifier() == Component.Identifier.Button.BACK) {
                    back = new LinuxMouseButton(tempAxis);
                }
            }
        }
        ball = new LinuxMouseBall(x,y,wheel);
        buttons = new LinuxMouseButtons(left, right, middle, side, extra, forward, back);
        
    }
    
    /** Polls axes for data.  Returns false if the controller is no longer valid.
     * Polling reflects the current state of the device when polled.
     * @return Returns false if the controller is no longer valid.
     */
    public boolean poll() {
        return device.poll();
    }
    
    /** Mouse ball under linux
     */    
    private class LinuxMouseBall extends Ball {
        /** Constructs the new mouse ball
         * @param x The x axis
         * @param y The y axis
         * @param wheel The mouse wheel axis
         */        
        public LinuxMouseBall(Component x, Component y, Component wheel) {
            super(LinuxMouse.this.getName() + " ball");
            this.x = x;
            this.y = y;
            this.wheel = wheel;
        }
    }
    
    /** Mouse buttons under linux
     */    
    private class LinuxMouseButtons extends Buttons {
        /** Creates the new mouse's buttons
         * @param left Left mouse button
         * @param right Right mouse button
         * @param middle Middle mouse button
         * @param side Side mouse button
         * @param extra Extra mouse button
         * @param forward Forward mouse button
         * @param back Back mouse button
         */        
        public LinuxMouseButtons(Button left, Button right, Button middle, Button side, Button extra, Button forward, Button back) {
            super(LinuxMouse.this.getName() + " buttons");
            this.left = left;
            this.right = right;
            this.middle = middle;
            this.side = side;
            this.extra = extra;
            this.forward = forward;
            this.back = back;
        }
    }
    
    /** Linux specific mouse buttons
     */    
    private class LinuxMouseButton extends Mouse.Button {
        /** The real Axis
         */        
        private Component realAxis;
        
        /** Construct a linux mouse button fro mthe given axis
         * @param axis The axis that holds the data
         */        
        public LinuxMouseButton(Component axis) {
            super(axis.getName(), (Component.Identifier.Button)axis.getIdentifier());
            this.realAxis = axis;
        }
        
        /** Returns true f this axis is relative
         * @return Always returns false for a mouse button
         */        
        public boolean isRelative() {
            return false;
        }
        
        /** Returns the data for this mouse button
         * @return Retursn this mouse buttons value
         */        
        public float getPollData(){
            return realAxis.getPollData();
        }
    }
}
