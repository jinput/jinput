/**
 * Copyright (C) 2004 Jeremy Booth (jeremy@newdawnsoftware.com)
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

import java.awt.AWTEvent;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * @author Jeremy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AWTMouse extends Mouse implements AWTEventListener {
    
    private AWTAxis xMove = new AWTAxis("X", Component.Identifier.Axis.X);
    private AWTAxis yMove = new AWTAxis("Y", Component.Identifier.Axis.Y);
    private AWTAxis zMove = new AWTAxis("Wheel", Component.Identifier.Axis.SLIDER);
    
    private AWTButton button1 = new AWTButton("Left", Component.Identifier.Button.LEFT);
    private AWTButton button2 = new AWTButton("Middle", Component.Identifier.Button.MIDDLE);
    private AWTButton button3 = new AWTButton("Right", Component.Identifier.Button.RIGHT);
    
    private Point oldMouseLocation = new Point(0,0);
    private Point newMouseLocation = new Point(0,0);
    private int scrollAmount = 0;
    private boolean button1Value = false;
    private boolean button2Value = false;
    private boolean button3Value = false;

    /**
     * @param name
     */
    protected AWTMouse(String name) {
        super(name);
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_WHEEL_EVENT_MASK);
        this.ball = new AWTMouseBall(xMove, yMove, zMove);
        this.buttons = new AWTMouseButtons(new AWTMouseButton(button1), new AWTMouseButton(button2), new AWTMouseButton(button3));
    }

    /* (non-Javadoc)
     * @see net.java.games.input.Controller#poll()
     */
    public boolean poll() {
        button1.setValue(button1Value);
        button2.setValue(button2Value);
        button3.setValue(button3Value);
        
        zMove.setValue(scrollAmount);
        scrollAmount = 0;
        
        //System.out.println("old mouse location " + oldMouseLocation);
        //System.out.println("new mouse location " + newMouseLocation);
        yMove.setValue((float)(newMouseLocation.getY() - oldMouseLocation.getY()));
        xMove.setValue((float)(newMouseLocation.getX() - oldMouseLocation.getX()));
        oldMouseLocation.setLocation(newMouseLocation.getLocation());
        //newMouseLocation.setLocation(0,0);
        
        return true;
    }

    /* (non-Javadoc)
     * @see java.awt.event.AWTEventListener#eventDispatched(java.awt.AWTEvent)
     */
    public void eventDispatched(AWTEvent event) {
        //System.out.println("AWTMouse: From: " + arg0.getSource() + " - " + arg0);
        if(event instanceof MouseWheelEvent) {
            MouseWheelEvent mwe = (MouseWheelEvent)event;
            scrollAmount += mwe.getWheelRotation();            
            //System.out.println("New scroll amount: " + scrollAmount);
        }
        if(event instanceof MouseEvent) {
            MouseEvent me = (MouseEvent)event;
            newMouseLocation.setLocation(me.getPoint());
            //System.out.println("Mouse moved to " + newMouseLocation);
            if(me.getID() == MouseEvent.MOUSE_PRESSED) {
                //System.out.println("Button was pressed");
                if(me.getButton() == MouseEvent.BUTTON1) {
                    //System.out.println("Button 1 was pressed");
                    button1Value = true;
                } else if(me.getButton() == MouseEvent.BUTTON2) {
                    //System.out.println("Button 2 was pressed");
                    button2Value = true;
                } else if(me.getButton() == MouseEvent.BUTTON3) {
                    //System.out.println("Button 3 was pressed");
                    button3Value = true;
                }
            } else if(me.getID() == MouseEvent.MOUSE_RELEASED) {
                //ystem.out.println("Button was released");
                if(me.getButton() == MouseEvent.BUTTON1) {
                    //System.out.println("Button 1 was released");
                    button1Value = false;
                } else if(me.getButton() == MouseEvent.BUTTON2) {
                    //System.out.println("Button 2 was released");
                    button2Value = false;
                } else if(me.getButton() == MouseEvent.BUTTON3) {
                    //System.out.println("Button 3 was released");
                    button3Value = false;
                }
            } else {
                //System.out.println("Mouse event ID " + me.getID() + " (" + me.getClass().getName() + ")");
            }
        } else {
            System.out.println("AWTMouse got an event of type " + event.getClass().getName());
        }
    }

    /** Mouse ball under AWT
     */    
    private class AWTMouseBall extends Ball {
        /** Constructs the new mouse ball
         * @param x The x axis
         * @param y The y axis
         * @param wheel The mouse wheel axis
         */        
        public AWTMouseBall(Component x, Component y, Component wheel) {
            super(AWTMouse.this.getName() + " ball");
            this.x = x;
            this.y = y;
            this.wheel = wheel;
        }
    }

    /** Mouse buttons under AWT
     */    
    private class AWTMouseButtons extends Buttons {
        /** Creates the new mouse's buttons
         * @param left Left mouse button
         * @param right Right mouse button
         * @param middle Middle mouse button
         */        
        public AWTMouseButtons(Button left, Button right, Button middle) {
            super(AWTMouse.this.getName() + " buttons");
            this.left = left;
            this.right = right;
            this.middle = middle;
        }
    }

    /** AWT specific mouse buttons
     */    
    private class AWTMouseButton extends Mouse.Button {
        /** The real Axis
         */        
        private Component realAxis;
        
        /** Construct an AWT mouse button from the given axis
         * @param axis The axis that holds the data
         */        
        public AWTMouseButton(Component axis) {
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
