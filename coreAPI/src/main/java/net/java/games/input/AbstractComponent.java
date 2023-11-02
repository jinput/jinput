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

import java.io.IOException;

/**
 * Skeleton implementation of a named axis.
 */
public abstract class AbstractComponent implements Component {
    /** Human-readable name for this {@code Component}. */
    private final String name;

    /** The type or identifier of this {@code Component}. */
    private final Identifier id;

    /** Whether this {@link Component} has been polled since the last time it was reset. */
	private boolean hasPolled;

    /** The value of this {@link Component}, from when it was last polled. */
	private float value;

    // todo Document this.
	private float eventValue;
    
    /**
     * Protected constructor
     * @param name A name for the axis
     */
    protected AbstractComponent(String name, Identifier id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }

    protected abstract float poll() throws IOException;

    final void resetHasPolled() {
        hasPolled = false;
    }

    /**
     * Determines whether this {@code Component} is analog or digital.
     *
     * @return {@code true} if this {@code Component} is analog; {@code false} otherwise.
     */
    public boolean isAnalog() {
        return false;
    }

    /**
     * <p>Retrieves the <em>suggested</em> dead zone for this {@code Component}.</p>
     *
     * <p>
     *     A dead zone is the amount polled data can vary before considered a significant change in value. Any changes
     *     less than this value, in the positive or negative direction, can be safely ignored.
     * </p>
     *
     * @return The suggested dead zone for this {@code Component}.
     */
    public float getDeadZone() {
        return 0.0f;
    }

    // todo Document this, after eventValue has been defined.
    final float getEventValue() {
        return eventValue;
    }

    /**
     * Retrieves the type or identifier of this {@link Component}.
     *
     * @return Type or identifier of this {@link Component}.
     */
    public Identifier getIdentifier() {
        return id;
    }

    /**
     * Retrieves the human-readable name for this {@link Component}.
     *
     * @return Human-readable name for this {@link Component}.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Retrieves the value of this {@link Component}, from the last time it was polled.</p>
     *
     * <p>If this {@link Component} is a button, then the value will be either {@code 0.0f} or {@code 1.0f}.</p>
     *
     * <p>
     *     If this {@link Component} is normalized, then the value will be between {@code -1.0f} and {@code 1.0f}
     *     (inclusive).
     * </p>
     *
     * @return The value of this {@link Component}, from the last time it was polled.
     */
    public final float getPollData() {
		if (!hasPolled && !isRelative()) {
			hasPolled = true;
			try {
				setPollData(poll());
			} catch (IOException e) {
				ControllerEnvironment.log("Failed to poll component: " + e);
			}
		}
        return value;
    }

    // Document this after, eventValue has been defined.
    final void setEventValue(float event_value) {
        this.eventValue = event_value;
    }

    final void setPollData(float value) {
        this.value = value;
    }
}
