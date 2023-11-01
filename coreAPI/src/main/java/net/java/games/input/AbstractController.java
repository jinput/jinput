/*
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
 */
package net.java.games.input;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * An AbstractController is a skeleton implementation of a controller that
 * contains a fixed number of axes, controllers, and rumblers.
 */
public abstract class AbstractController implements Controller {
	/** The default maximum number of {@link Event}s that can be stored in the {@link EventQueue}. */
	final static int EVENT_QUEUE_DEPTH = 32;
	
    /** Human-readable name for this {@code Controller}. */
    private final String name;

	/**
	 * <p>The set of {@link Component}s that make up this {@link Controller}, in order of assignment priority.</p>
	 *
	 * <p>
	 *     For example, the button {@link Controller} on a mouse may return the following {@link Component}s:
	 * </p>
	 *
	 * <ol>
	 *     <li>The primary or left-most button.</li>
	 *     <li>The secondary or right-most button, if present.</li>
	 *     <li>The middle mouse button, if present.</li>
	 * </ol>
	 */
    private final Component[] components;

	/** The set of connected {@link Controller}s which comprise this {@link Controller}, in order of assignment priority. */
    private final Controller[] controllers;

    /** The set of {@link Rumbler}s for sending feedback to this {@link Controller}. */
    private final Rumbler[] rumblers;

	/** A mapping of {@link Component.Identifier}s to {@link Component}s. */
	private final Map<Component.Identifier, Component> idToComponents = new HashMap<>();

	/**
	 * <p>The set of {@link Event}s polled from this controller.</p>
	 *
	 * <p>See {@link EventQueue} for more information.</p>
	 */
	private EventQueue eventQueue = new EventQueue(EVENT_QUEUE_DEPTH);
    
    /**
	 * Constructs a new {@link AbstractController} with the specified name, {@link Component}s, {@link Controller}s, and
	 * {@link Rumbler}s.
	 *
	 * @param name Human-readable name for the {@link AbstractController}.
	 * @param components The set of {@link Component}s which comprise this {@link AbstractController}.
	 * @param controllers The set of {@link Controller}s which comprise this {@link AbstractController}.
	 * @param rumblers The set of {@link Rumbler}s for sending feedback to this {@link AbstractController}.
     */
    protected AbstractController(final String name, final Component[] components, final Controller[] controllers, final Rumbler[] rumblers) {
        this.name = name;
        this.components = components;
        this.controllers = controllers;
        this.rumblers = rumblers;

		// process from last to first to let earlier listed Components get higher priority
		for (int i = components.length - 1; i >= 0; i--) {
			idToComponents.put(components[i].getIdentifier(), components[i]);
		}
	}

	@Override
    public String toString() {
        return name;
    }

	/**
	 * Polls the device for new data and queues any new {@link Event}s.
	 *
	 * @return Whether the device was successfully polled.
	 */
	public boolean poll() {
		try {
			pollDevice();
		} catch (final IOException e) {
			ControllerEnvironment.log("Failed to poll device: " + e.getMessage());
			return false;
		}

		for (final AbstractComponent component : (AbstractComponent[]) getComponents()) {
			if (component.isRelative()) {
				component.setPollData(0);
			} else {
				component.resetHasPolled(); // Let the component poll itself lazily
			}
		}

		final Event event = new Event();
		while (true) {
			try {
				if (!getNextDeviceEvent(event)) {
					break;
				}
			} catch (final IOException e) {
				ControllerEnvironment.log("Failed to poll device: " + e.getMessage());
				return false;
			}

			final AbstractComponent component = (AbstractComponent) event.getComponent();
			final float value = event.getValue();

			if (component.isRelative()) {
				if (value == 0) {
					continue;
				}

				component.setPollData(component.getPollData() + value);
			} else {
				if (value == component.getEventValue()) {
					continue;
				}

				component.setEventValue(value);
			}

			if (!eventQueue.isFull()) {
				eventQueue.add(event);
			}
		}

		return true;
	}

	// todo Document
	protected void pollDevice() throws IOException {
	}

	/**
	 * Retrieves the {@link Component} associated with the specified {@link Component.Identifier}.
	 *
	 * @param id {@link Component.Identifier} of the {@link Component} to retrieve.
	 * @return {@link Component} associated with the specified {@link Component.Identifier}, or null if no such {@link Component} exists.
	 */
	public final Component getComponent(final Component.Identifier id) {
		return idToComponents.get(id);
	}

	/**
	 * Retrieves the set of {@link Component}s which comprise this controller.
	 *
	 * @return The set of {@link Component}s.
	 */
	public final Component[] getComponents() {
		return components;
	}

	/**
	 * Retrieves the set of {@link Controller}s which comprise this controller.
	 *
	 * @return The set of {@link Controller}s.
	 */
	public final Controller[] getControllers() {
		return controllers;
	}

	/**
	 * Retrieves the {@link EventQueue} for this {@link Controller}.
	 *
	 * @return {@link EventQueue} for this {@link Controller}.
	 */
	public final EventQueue getEventQueue() {
		return eventQueue;
	}

	/**
	 * Retrieves the human-readable name of this {@link Controller}.
	 *
	 * @return Human-readable name of this {@link Controller}.
	 */
	public final String getName() {
		return name;
	}

	// todo Document
	protected abstract boolean getNextDeviceEvent(final Event event) throws IOException;

	/**
	 * Returns the zero-based port number for this {@link Controller}.
	 *
	 * @return 0 by default, can be overridden
	 */
	public int getPortNumber() {
		return 0;
	}

	/**
	 * Retrieves the {@link PortType} of this {@link Controller}.
	 *
	 * @return {@link PortType} of this {@link Controller}.
	 */
	public PortType getPortType() {
		return PortType.UNKNOWN;
	}

	/**
	 * Retrieves the set of {@link Rumbler}s for sending feedback to this controller.
	 *
	 * @return The set of {@link Rumbler}s.
	 */
	public final Rumbler[] getRumblers() {
		return rumblers;
	}

	/**
	 * Retrieves the {@link Type} of this {@link Controller}.
	 *
	 * @return {@link Type} of this {@link Controller}.
	 */
	public Type getType() {
		return Type.UNKNOWN;
	}

	/**
	 * <p>Sets the size of the device's internal event queue.</p>
	 *
	 * <p>Plugins must implement this method to adjust their internal {@link EventQueue} size.</p>
	 *
	 * @param size Maximum number of {@link Event}s that can be stored in the queue.
	 *
	 * @throws IOException If an I/O error occurs.
	 */
	protected void setDeviceEventQueueSize(final int size) throws IOException {
	}

	/**
	 * <p>Creates a new {@link EventQueue}.</p>
	 *
	 * <p>Events in the old queue are discarded.</p>
	 *
	 * @param size Maximum number of {@link Event}s that can be stored in the new queue.
	 */
	public final void setEventQueueSize(final int size) {
		try {
			setDeviceEventQueueSize(size);
			eventQueue = new EventQueue(size);
		} catch (IOException e) {
			ControllerEnvironment.log("Failed to create new event queue of size " + size + ": " + e);
		}
	}
}
