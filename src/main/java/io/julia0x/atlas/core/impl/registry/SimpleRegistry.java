package io.julia0x.atlas.core.impl.registry;

import io.julia0x.atlas.core.api.registry.IRegistry;
import io.julia0x.atlas.core.utils.common.ValidationUtil;
import io.julia0x.atlas.core.utils.logging.LoggerFactory;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

/**
 * Generic registry implementation with listener support.
 * Thread-safe for concurrent access and registration.
 *
 * @param <T> The type of objects this registry manages
 */
public class SimpleRegistry<T> implements IRegistry<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleRegistry.class);

    private final String name;
    private final Map<String, T> registry = new ConcurrentHashMap<>();
    private final List<IRegistryListener<T>> listeners = new CopyOnWriteArrayList<>();

    /**
     * Creates a new registry with the given name.
     *
     * @param name The name of this registry (for logging)
     */
    public SimpleRegistry(String name) {
        this.name = ValidationUtil.requireNonEmpty(name, "Registry name cannot be empty");
    }

    @Override
    public boolean register(String id, T object) {
        ValidationUtil.requireNonEmpty(id, "Registration ID cannot be empty");
        ValidationUtil.requireNonNull(object, "Cannot register null object");

        if (registry.containsKey(id)) {
            LOGGER.warning("Cannot register duplicate ID '" + id + "' in registry '" + name + "'");
            return false;
        }

        registry.put(id, object);
        notifyRegistered(id, object);
        LOGGER.fine("Registered '" + id + "' in registry '" + name + "'");
        return true;
    }

    @Override
    public boolean unregister(String id) {
        T removed = registry.remove(id);
        if (removed != null) {
            notifyUnregistered(id, removed);
            LOGGER.fine("Unregistered '" + id + "' from registry '" + name + "'");
            return true;
        }
        return false;
    }

    @Override
    public Optional<T> get(String id) {
        return Optional.ofNullable(registry.get(id));
    }

    @Override
    public boolean contains(String id) {
        return registry.containsKey(id);
    }

    @Override
    public Set<String> getIds() {
        return Collections.unmodifiableSet(registry.keySet());
    }

    @Override
    public int size() {
        return registry.size();
    }

    @Override
    public void clear() {
        List<Map.Entry<String, T>> entries = new ArrayList<>(registry.entrySet());
        registry.clear();

        for (Map.Entry<String, T> entry : entries) {
            notifyUnregistered(entry.getKey(), entry.getValue());
        }

        LOGGER.info("Registry '" + name + "' cleared");
    }

    @Override
    public void registerListener(IRegistryListener<T> listener) {
        if (listener != null) {
            listeners.add(listener);
            LOGGER.fine("Registered listener for registry '" + name + "'");
        }
    }

    /**
     * Unregisters a listener from this registry.
     *
     * @param listener The listener to unregister
     */
    public void unregisterListener(IRegistryListener<T> listener) {
        if (listeners.remove(listener)) {
            LOGGER.fine("Unregistered listener from registry '" + name + "'");
        }
    }

    /**
     * Gets the name of this registry.
     *
     * @return The registry name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets all registered objects.
     *
     * @return A collection of all registered objects
     */
    public Collection<T> getAll() {
        return Collections.unmodifiableCollection(registry.values());
    }

    private void notifyRegistered(String id, T object) {
        for (IRegistryListener<T> listener : listeners) {
            try {
                listener.onRegistered(id, object);
            } catch (Exception e) {
                LOGGER.severe("Error notifying listener of registration: " + e.getMessage());
            }
        }
    }

    private void notifyUnregistered(String id, T object) {
        for (IRegistryListener<T> listener : listeners) {
            try {
                listener.onUnregistered(id, object);
            } catch (Exception e) {
                LOGGER.severe("Error notifying listener of unregistration: " + e.getMessage());
            }
        }
    }
}
