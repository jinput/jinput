/*
 * %W% %E%
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
/*****************************************************************************
 * Copyright (c) 2003 Sun Microsystems, Inc.  All Rights Reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistribution of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materails provided with the distribution.
 *
 * Neither the name Sun Microsystems, Inc. or the names of the contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANT OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMEN, ARE HEREBY EXCLUDED.  SUN MICROSYSTEMS, INC. ("SUN") AND
 * ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS
 * A RESULT OF USING, MODIFYING OR DESTRIBUTING THIS SOFTWARE OR ITS 
 * DERIVATIVES.  IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES.  HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OUR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed or intended for us in
 * the design, construction, operation or maintenance of any nuclear facility
 *
 *****************************************************************************/
package net.java.games.input;

/**
 * A Mouse is a type of controller consisting of two child controllers,
 * a ball and a button pad.  This includes devices such as touch pads,
 * trackballs, and fingersticks.
 */
public abstract class Mouse extends AbstractController {
    
    /**
     * Mouse ball; should be initialized by subclasses
     */
    protected Ball ball;
    
    /**
     * Mouse buttons; should be initialized by subclasses
     */
    protected Buttons buttons;
    
    /**
     * Protected constructor;
     * Subclasses should initialize the ball and buttons
     */
    protected Mouse(String name) {
        super(name);
    }
    
    /**
     * Returns the controllers connected to make up this controller, or
     * an empty array if this controller contains no child controllers.
     * The objects in the array are returned in order of assignment priority
     * (primary stick, secondary buttons, etc.).
     */
    public Controller[] getControllers() {
        if (children.length == 0 && ball != null && buttons != null) {
            children = new Controller[] { ball, buttons };
        }
        return children;
    }
    
    /**
     * Returns the control for the ball of the mouse, never null.
     */
    public Ball getBall() {
        return ball;
    }

    /**
     * Returns the control for the buttons of the mouse, never null.
     */
    public Buttons getButtons() {
        return buttons;
    }

    /**
     * Returns the type of the Controller.
     */
    public Type getType() {
        return Type.MOUSE;
    }

    /**
     * Mouse ball controller
     */
    public abstract class Ball extends AbstractController {
        
        /**
         * X-axis; should be initialized by subclasses
         */
        protected Axis x;
        
        /**
         * Y-axis; should be initialized by subclasses
         */
        protected Axis y;
        
        /**
         * Mouse wheel; should be initialized by subclasses
         */
        protected Axis wheel;
        
        /**
         * Protected constructor
         */
        protected Ball(String name) {
            super(name);
        }
        
        /**
         * Returns the type of Controller.
         */
        public Type getType() {
            return Type.BALL;
        }
        
        /**
         * Returns the x-axis for the mouse ball, never null.
         */
        public Axis getX() {
            return x;
        }
        
        /**
         * Returns the y-axis for the mouse ball, never null.
         */
        public Axis getY() {
            return y;
        }
        
        /**
         * Returns the mouse wheel, or null if no mouse wheel is present.
         */
        public Axis getWheel() {
            return wheel;
        }
        
        /**
         * Returns the axes on this controller, in order of assignment priority.
         * Overridden to return the x-axis, followed by the y-axes, followed by
         * the wheel (if present).
         * The array returned is an empty array if this controller contains no
         * axes (such as a logical grouping of child controllers).
         */
        public Axis[] getAxes() {
            if (axes.length == 0 && x != null && y != null) {
                if (wheel == null) {
                    axes = new Axis[] { x, y };
                } else {
                    axes = new Axis[] { x, y, wheel };
                }
            }
            return axes;
        }

        /**
         * Polls axes for data.  Returns false if the controller is no longer
         * valid. Polling reflects the current state of the device when polled.
         * By default, polling a mouse ball or button polls the entire mouse
         * control.
         */
        public boolean poll() {
            return Mouse.this.poll();
        }
    } // class Mouse.Ball
    
    /**
     * Mouse buttons controller
     */
    public abstract class Buttons extends AbstractController {
        
        /**
         * Left button; should be initialized by subclasses
         */
        protected Button left;
        
        /**
         * Right button; should be initialized by subclasses
         */
        protected Button right;
        
        /**
         * Middle button; should be initialized by subclasses
         */
        protected Button middle;
        
        /**
         * Side button; should be initialized by subclasses
         */
        protected Button side;
        
        /**
         * Extra button; should be initialized by subclasses
         */
        protected Button extra;
        
        /**
         * Forward button; should be initialized by subclasses
         */
        protected Button forward;
        
        /**
         * Back button; should be initialized by subclasses
         */
        protected Button back;
        
        /**
         * Protected constructor
         */
        protected Buttons(String name) {
            super(name);
        }
        
        /**
         * Returns the type or identifier of the Controller.
         */
        public Type getType() {
            return Type.BUTTONS;
        }
        
        /**
         * Returns the left or primary mouse button, never null.
         */
        public Button getLeft() {
            return left;
        }
        
        /**
         * Returns the right or secondary mouse button, null if the mouse is
         * a single-button mouse.
         */
        public Button getRight() {
            return right;
        }
        
        /**
         * Returns the middle or tertiary mouse button, null if the mouse has
         * fewer than three buttons.
         */
        public Button getMiddle() {
            return middle;
        }
        
        /**
         * Returns the side or 4th mouse button, null if the mouse has
         * fewer than 4 buttons.
         */
        public Button getSide() {
            return side;
        }
        
        /**
         * Returns the extra or 5th mouse button, null if the mouse has
         * fewer than 5 buttons.
         */
        public Button getExtra() {
            return extra;
        }
        
        /**
         * Returns the forward mouse button, null if the mouse hasn't
         * got one.
         */
        public Button getForward() {
            return forward;
        }
        
        /**
         * Returns the back mouse button, null if the mouse hasn't
         * got one.
         */
        public Button getBack() {
            return back;
        }
        
        /**
         * Returns the axes on this controller, in order of assignment priority.
         * Overridden to return the the primary or leftmost mouse button,
         * followed by the secondary or rightmost mouse button (if present),
         * followed by the middle mouse button (if present).
         * The array returned is an empty array if this controller contains no
         * axes (such as a logical grouping of child controllers).
         */
        public Axis[] getAxes() {
            if (axes.length == 0 && left != null) {
                if (right == null) {
                    axes = new Axis[] { left };
                } else if (middle == null) {
                    axes = new Axis[] { left, right };
                } else if (side == null) {
                    axes = new Axis[] { left, right, middle };
                } else if (extra == null) {
                    axes = new Axis[] { left, right, middle, side };
                } else if (forward == null) {
                    axes = new Axis[] { left, right, middle, side, extra };
                } else if (back == null) {
                    axes = new Axis[] { left, right, middle, side, extra, forward };
                } else {
                    axes = new Axis[] { left, right, middle, side, extra, forward, back };
                }
            }
            return axes;
        }

        /**
         * Polls axes for data.  Returns false if the controller is no longer
         * valid. Polling reflects the current state of the device when polled.
         * By default, polling a mouse ball or button polls the entire mouse
         * control.
         */
        public boolean poll() {
            return Mouse.this.poll();
        }
    } // class Mouse.Buttons
    
    /**
     * Mouse button axis
     */
    public abstract class Button extends AbstractAxis {
        
        /**
         * Protected constructor
         */
        protected Button(String name, ButtonID id) {
            super(name, id);
        }
    } // class Mouse.Button

    /**
     * Identifier for types of mouse buttons
     */
    public static class ButtonID extends Axis.Identifier {

        /**
         * Protected constructor
         */
        protected ButtonID(String name) {
            super(name);
        }

        /**
         * The primary or leftmost mouse button.
         */
        public static final ButtonID LEFT = new ButtonID("left");

        /**
         * The secondary or rightmost mouse button, not present if
         * the mouse is a single-button mouse.
         */
        public static final ButtonID RIGHT = new ButtonID("right");

        /**
         * Returns the middle mouse button, not present if the
         * mouse has fewer than three buttons.
         */
        public static final ButtonID MIDDLE = new ButtonID("middle");
        
        /**
         * Returns the side mouse button.
         */
        public static final ButtonID SIDE = new ButtonID("side");
        
        /**
         * Returns the extra mouse button.
         */
        public static final ButtonID EXTRA = new ButtonID("extra");
        
        /**
         * Returns the forward mouse button.
         */
        public static final ButtonID FORWARD = new ButtonID("forward");
        
        /**
         * Returns the back mouse button.
         */
        public static final ButtonID BACK = new ButtonID("back");
        
    } // class Mouse.ButtonID
} // class Mouse
