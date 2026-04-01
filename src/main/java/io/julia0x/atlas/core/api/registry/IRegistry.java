package io.julia0x.atlas.core.api.registry;

import java.util.Optional;
import java.util.Set;

/**
 * Generic registry interface for managing collections of unique objects.
 *
 * @param <T> The type of objects this registry manages
 */
public interface IRegistry<T> {
    /**
     * Registers an object with the given identifier.
     *
     * @param id The unique identifier for the object
     * @param object The object to register
     * @return true if registration was successful, false if ID already exists
     */
    boolean register(String id, T object);

    /**
     * Unregisters an object by its identifier.
     *
     * @param id The identifier of the object to remove
     * @return true if unregistration was successful, false if ID not found
     */
    boolean unregister(String id);

    /**
     * Gets an object by its identifier.
     *
     * @param id The identifier to look up
     * @return An Optional containing the object, or empty if not found
     */
    Optional<T> get(String id);

    /**
     * Checks if an identifier is registered.
     *
     * @param id The identifier to check
     * @return true if the identifier is registered, false otherwise
     */
    boolean contains(String id);

    /**
     * Gets all registered identifiers.
     *
     * @return A set of all registered identifiers
     */
    Set<String> getIds();

    /**
     * Gets the number of registered objects.
     *
     * @return The count of registered objects
     */
    int size();

    /**
     * Clears all registered objects.
     */
    void clear();

    /**
     * Registers a listener to be notified of registry changes.
     *
     * @param listener The listener to register
     */
    void registerListener(IRegistryListener<T> listener);

    /**
     * Listener interface for registry changes.
     *
     * @param <T> The registry element type
     */
    interface IRegistryListener<T> {
        /**
         * Called when an object is registered.
         *
         * @param id The identifier used
         * @param object The registered object
         */
        void onRegistered(String id, T object);

        /**
         * Called when an object is unregistered.
         *
         * @param id The identifier that was removed
         * @param object The unregistered object
         */
        void onUnregistered(String id, T object);
    }
}
