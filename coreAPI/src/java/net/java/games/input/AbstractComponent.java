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
 * Skeleton implementation of a named axis.
 */
public abstract class AbstractComponent implements Component {

    /**
     * Human-readable name for this Axis
     */
    protected String name;
    
    /**
     * Identifier for the axis
     */
    protected Identifier id;
    
    /**
     * Whether this axis is ready to receive polling data
     */
    private boolean polling;
    
    /**
     * Protected constructor
     * @param name A name for the axis
     */
    protected AbstractComponent(String name, Identifier id) {
        this.name = name;
        this.id = id;
        this.polling = true;
    }
        
    /**
     * Returns the type or identifier of the axis.
     */
    public Identifier getIdentifier() {
        return id;
    }
    
    /**
     * Returns whether or not the axis is analog, or false if it is digital.
     * @return false by default, can be overridden
     */
    public boolean isAnalog() {
        return false;
    }

    /**
     * Returns whether or not data polled from this axis is normalized
     * between the values of -1.0f and 1.0f.
     * @return true by default, can be overridden
     */
    public boolean isNormalized() {
        return true;
    }

    /**
     * Returns whether or not this axis is ready to receive polling data.
     * By default, an abstract axis is set to receive polling data.
     */
    public boolean isPolling() {
        return polling;
    }

    /**
     * Sets whether or not the axis should receive polling data.
     */
    public void setPolling(boolean polling) {
        this.polling = polling;
    }

    /**
     * Returns the suggested dead zone for this axis.  Dead zone is the
     * amount polled data can vary before considered a significant change
     * in value.  An application can safely ignore changes less than this
     * value in the positive or negative direction.
     * @return 0.0f by default, can be overridden
     */
    public float getDeadZone() {
        return 0.0f;
    }

    /**
     * Returns the data from the last time the control has been polled.
     * If this axis is a button, the value returned will be either 0.0f or 1.0f.
     * If this axis is normalized, the value returned will be between -1.0f and
     * 1.0f.
     * @return 0.0f by default, can be overridden
     */
    public float getPollData() {
        return 0.0f;
    }

    /**
     * Returns a human-readable name for this axis.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns a non-localized string description of this axis.
     */
    public String toString() {
        return name;
    }
    
    /**
     * Changes the name of this Axis.  This should be done only during
     * initialization of the axis so that its name remains immutable.
     */
    public void setName(String name) {
        this.name = name;
    }
} // AbstractAxis
