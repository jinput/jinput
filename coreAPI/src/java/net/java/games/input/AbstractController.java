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
 * An AbstractController is a skeleton implementation of a controller that
 * contains a fixed number of axes, controllers, and rumblers.
 */
public abstract class AbstractController implements Controller {

    /**
     * Null array representing no axes
     */
    protected static final Component[] NO_COMPONENTS = {};
    
    /**
     * Null array representing no child controllers
     */
    protected static final Controller[] NO_CONTROLLERS = {};
    
    /**
     * Null array representing no rumblers
     */
    protected static final Rumbler[] NO_RUMBLERS = {};
    
    /**
     * Human-readable name for this Controller
     */
    private final String name;
    
    /**
     * Array of components
     */
    protected Component[] components;
    
    /**
     * Array of child controllers
     */
    protected Controller[] children;
    
    /**
     * Array of rumblers
     */
    protected Rumbler[] rumblers;
    
    /**
     * Protected constructor for a controller; initially contains no axes,
     * child controllers, or rumblers.
     * @param name The name for the controller
     */
    protected AbstractController(String name) {
        this(name, NO_COMPONENTS, NO_CONTROLLERS, NO_RUMBLERS);
    }
    
    /**
     * Protected constructor for a controller containing the specified
     * axes, child controllers, and rumblers
     * @param name name for the controller
     * @param components components for the controller
     * @param children child controllers for the controller
     * @param rumblers rumblers for the controller
     */
    protected AbstractController(String name, Component[] components,
        Controller[] children, Rumbler[] rumblers) {
        this.name = name;
        this.components = components;
        this.children = children;
        this.rumblers = rumblers;
    }
    
    /**
     * Returns the controllers connected to make up this controller, or
     * an empty array if this controller contains no child controllers.
     * The objects in the array are returned in order of assignment priority
     * (primary stick, secondary buttons, etc.).
     */
    public Controller[] getControllers() {
        return children;
    }

    /**
     * Returns the components on this controller, in order of assignment priority.
     * For example, the button controller on a mouse returns an array containing
     * the primary or leftmost mouse button, followed by the secondary or
     * rightmost mouse button (if present), followed by the middle mouse button
     * (if present).
     * The array returned is an empty array if this controller contains no components
     * (such as a logical grouping of child controllers).
     */
    public Component[] getComponents() {
        return components;
    }

    /**
     * Returns a single component based on its identifier, or null
     * if no component with the specified type could be found.
     * By default, AbstractController calls getComponents in this method so that
     * subclasses may lazily initialize the array of components, if necessary.
     */
    public Component getComponent(Component.Identifier id) {
        // Calls getAxes() so that subclasses may lazily set the array of axes.
        Component[] components = getComponents();
        if (components.length == 0) {
            return null;
        }
        for (int i = 0; i < components.length; i++) {
            if (components[i].getIdentifier() == id) {
                return components[i];
            }
        }
        return null;
    }

    /**
     * Returns the rumblers for sending feedback to this controller, or an
     * empty array if there are no rumblers on this controller.
     */
    public Rumbler[] getRumblers() {
        return rumblers;
    }

    /**
     * Returns the port type for this Controller.
     * @return PortType.UNKNOWN by default, can be overridden
     */
    public PortType getPortType() {
        return PortType.UNKNOWN;
    }

    /**
     * Returns the zero-based port number for this Controller.
     * @return 0 by default, can be overridden
     */
    public int getPortNumber() {
        return 0;
    }

    /**
     * Returns a human-readable name for this Controller.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns a non-localized string description of this controller.
     */
    public String toString() {
        return name;
    }
    
    /** Returns the type of the Controller.
     */
    public Type getType() {
        return Type.UNKNOWN;
    }
    
} // class AbstractController
