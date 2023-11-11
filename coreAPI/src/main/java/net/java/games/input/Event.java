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

import java.util.Objects;

public final class Event {
	/** {@link Component} that generated this {@code Event}. */
    private Component component;

    private float value;

	/**
	 * <p>This represents the time elapsed since the last {@code Event}, in nanoseconds.</p>
	 *
	 * <p>
	 *     e.g. If an {@code Event} happens, then one millisecond later another {@code Event} happens, the {@code nanos}
	 * 	   of the second {@code Event} will be 1,000,000 (1 millisecond).
	 * </p>
	 */
	private long nanos;

	@Override
	public String toString() {
		return "Event: component = " + component + " | value = " + value;
	}

	/**
	 * Sets the state of this {@code Event}.
	 *
	 * @param component {@link Component} that generated this {@code Event}.
	 * @param value todo Document
	 * @param nanos Time elapsed since the last {@code Event}, in nanoseconds.
	 */
    public void set(final Component component, final float value, final long nanos) {
		setComponent(component);
		setValue(value);
		setNanos(nanos);
	}

	/**
	 * Sets the state of this {@code Event} to the state of the specified {@code Event}.
	 *
	 * @param other {@code Event} to copy the state from.
	 */
	public void set(final Event other) {
		this.set(other.getComponent(), other.getValue(), other.getNanos());
	}

	/**
	 * Retrieves the {@link Component} that generated this {@code Event}.
	 *
	 * @return {@link Component} that generated this {@code Event}.
	 */
	public Component getComponent() {
		return component;
	}

	public float getValue() {
		return value;
	}

	/**
	 * Retrieves the time elapsed since the last {@code Event}, in nanoseconds.
	 *
	 * @return The time elapsed since the last {@code Event}, in nanoseconds.
	 */
	public long getNanos() {
		return nanos;
	}

	/**
	 * Sets the {@link Component} that generated this {@code Event}.
	 *
	 * @param component {@link Component} that generated this {@code Event}.
	 *
	 * @throws NullPointerException If {@code component} is {@code null}.
	 */
	public void setComponent(final Component component) {
		Objects.requireNonNull(component);
		this.component = component;
	}

	/**
	 * Sets the time elapsed since the last {@code Event}, in nanoseconds.
	 *
	 * @param nanos The time elapsed since the last {@code Event}, in nanoseconds.
	 */
	public void setNanos(final long nanos) {
		this.nanos = nanos;
	}

	// todo Document
	public void setValue(final float value) {
		this.value = value;
	}
}
