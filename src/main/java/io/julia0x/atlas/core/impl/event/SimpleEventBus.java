package io.julia0x.atlas.core.impl.event;

import io.julia0x.atlas.core.api.event.Event;
import io.julia0x.atlas.core.api.event.IEventBus;
import io.julia0x.atlas.core.api.event.IEventListener;
import io.julia0x.atlas.core.utils.logging.LoggerFactory;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

/**
 * Simple in-memory event bus implementation with priority-based dispatch.
 * Thread-safe for concurrent listener registration and event posting.
 */
public class SimpleEventBus implements IEventBus {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleEventBus.class);

    private final Map<Class<? extends Event>, List<IEventListener>> listeners = new ConcurrentHashMap<>();
    private volatile boolean disposed = false;

    @Override
    public <T extends Event> void subscribe(Class<T> eventType, IEventListener listener) {
        if (listener == null) {
            LOGGER.warning("Attempted to subscribe null listener to event type: " + eventType.getSimpleName());
            return;
        }

        listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(listener);
        LOGGER.fine("Subscribed listener to " + eventType.getSimpleName() + " (priority: " + listener.getPriority() + ")");
    }

    @Override
    public <T extends Event> void unsubscribe(Class<T> eventType, IEventListener listener) {
        if (listener == null) {
            return;
        }

        List<IEventListener> list = listeners.get(eventType);
        if (list != null && list.remove(listener)) {
            LOGGER.fine("Unsubscribed listener from " + eventType.getSimpleName());
        }
    }

    @Override
    public void post(Event event) {
        if (disposed) {
            LOGGER.warning("Attempted to post event to disposed bus: " + event.getClass().getSimpleName());
            return;
        }

        if (event == null) {
            LOGGER.warning("Attempted to post null event");
            return;
        }

        List<IEventListener> list = listeners.get(event.getClass());
        if (list == null || list.isEmpty()) {
            return;
        }

        // Sort by priority (highest first) on each post
        List<IEventListener> sortedListeners = new ArrayList<>(list);
        sortedListeners.sort((a, b) -> Integer.compare(b.getPriority(), a.getPriority()));

        for (IEventListener listener : sortedListeners) {
            try {
                listener.onEvent(event);
            } catch (Exception e) {
                LOGGER.severe("Error dispatching " + event.getClass().getSimpleName() + " to listener: " + e.getMessage());
            }
        }
    }

    @Override
    public void clear() {
        listeners.clear();
        LOGGER.info("Event bus cleared");
    }

    @Override
    public int getListenerCount(Class<? extends Event> eventType) {
        List<IEventListener> list = listeners.get(eventType);
        return list != null ? list.size() : 0;
    }

    /**
     * Disposes the event bus, preventing further event posting.
     */
    public void dispose() {
        disposed = true;
        clear();
        LOGGER.info("Event bus disposed");
    }

    /**
     * Gets the total number of registered listeners across all event types.
     *
     * @return Total listener count
     */
    public int getTotalListenerCount() {
        return listeners.values().stream().mapToInt(List::size).sum();
    }
}
