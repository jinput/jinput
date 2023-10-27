package net.java.games.input;

import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class used to poll a {@link Controller} and notify {@link ControllerListener}s when {@link Event}s occur.
 *
 * @author Valkryst
 */
public class ControllerPoller {
    /** {@link Controller} to poll. */
    private final Controller controller;

    /** Name of the {@link Controller}. */
    private final String controllerName;

    /** {@link ControllerListener}s to notify when {@link Event}s occur. */
    private final CopyOnWriteArrayList<ControllerListener> listeners = new CopyOnWriteArrayList<>();

    /** {@link ScheduledExecutorService} used to poll the {@link Controller} and notify {@code listeners}. */
    private ScheduledExecutorService executorService;

    /**
     * Constructs a new {@code ControllerPoller}.
     *
     * @param controller The {@link Controller} to poll.
     */
    public ControllerPoller(final Controller controller) {
        Objects.requireNonNull(controller);
        this.controller = controller;

        controllerName = controller.getName();
    }

    /**
     * <p>Starts polling the {@link Controller}.</p>
     *
     * <p>See {@link ScheduledExecutorService#scheduleAtFixedRate(Runnable, long, long, TimeUnit)} for caveats.</p>
     *
     * @param pollingRate The polling rate, in milliseconds.
     * @throws IllegalStateException If the {@code ControllerPoller} is already running or if the polling rate is less than 16 millisecond.
     */
    public void start(final int pollingRate) {
        if (pollingRate < 1) {
            throw new IllegalArgumentException("The polling rate cannot be less than 1 millisecond.");
        }

        // todo Verify thread safety of this block and everything within.
        synchronized (this) {
            if (executorService != null) {
                throw new IllegalStateException("ControllerPoller is already running for '" + controllerName + "'.");
            }

            executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.scheduleAtFixedRate(() -> {
                if (!controller.poll()) {
                    System.err.println("The controller is no-longer valid. Stopping ControllerPoller for '" + controllerName + "'.");
                    this.stop();
                }

                final EventQueue eventQueue = controller.getEventQueue();
                while (true) {
                    final Event event = new Event();
                    eventQueue.getNextEvent(event);

                    if (event.getComponent() == null) {
                        break;
                    } else {
                        for (final ControllerListener listener : listeners) {
                            listener.eventOccurred(event);
                        }
                    }
                }
            }, 0, pollingRate, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Stops polling the {@link Controller}.
     *
     * @throws IllegalStateException If the {@code ControllerPoller} is not running.
     */
    public void stop() {
        synchronized (this) {
            if (executorService == null) {
                throw new IllegalStateException("ControllerPoller is not running for '" + controllerName + "'.");
            }

            executorService.shutdown();
            executorService = null;
        }
    }

    /**
     * Adds a {@link ControllerListener} to {@code listeners}.
     *
     * @param listener Listener to add.
     * @throws NullPointerException If {@code listener} is {@code null}.
     */
    public void addListener(final ControllerListener listener) {
        Objects.requireNonNull(listener);
        listeners.add(listener);
    }

    /**
     * Removes a {@link ControllerListener} from {@code listeners}.
     *
     * @param listener Listener to remove.
     * @throws NullPointerException If {@code listener} is {@code null}.
     */
    public void removeListener(final ControllerListener listener) {
        Objects.requireNonNull(listener);
        listeners.remove(listener);
    }
}
