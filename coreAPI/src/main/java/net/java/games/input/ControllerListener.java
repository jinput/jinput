package net.java.games.input;

import java.util.EventListener;

/**
 * The listener interface for receiving {@link Controller} {@link Event}s.
 *
 * @author Valkryst
 */
public interface ControllerListener extends EventListener {
    /**
     * Invoked when an {@link Event} occurs.
     *
     * @param event {@code Event} to be processed.
     */
    void eventOccurred(final Event event);
}
