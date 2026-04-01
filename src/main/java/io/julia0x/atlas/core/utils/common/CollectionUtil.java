package io.julia0x.atlas.core.utils.common;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Utility class for common collection operations.
 */
public class CollectionUtil {
    private CollectionUtil() {
        // Utility class
    }

    /**
     * Checks if a collection is null or empty.
     *
     * @param collection The collection to check
     * @param <T> The element type
     * @return true if null or empty, false otherwise
     */
    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Checks if a map is null or empty.
     *
     * @param map The map to check
     * @param <K> The key type
     * @param <V> The value type
     * @return true if null or empty, false otherwise
     */
    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return map == null || map.isEmpty();
    }

    /**
     * Creates a list from varargs.
     *
     * @param elements The elements
     * @param <T> The element type
     * @return A list containing the elements
     */
    @SafeVarargs
    public static <T> List<T> listOf(T... elements) {
        return Arrays.asList(elements);
    }

    /**
     * Creates a set from varargs.
     *
     * @param elements The elements
     * @param <T> The element type
     * @return A set containing the elements
     */
    @SafeVarargs
    public static <T> Set<T> setOf(T... elements) {
        return new HashSet<>(Arrays.asList(elements));
    }

    /**
     * Creates a map with key-value pairs.
     *
     * @param pairs The key-value pairs (alternating key, value, key, value...)
     * @param <K> The key type
     * @param <V> The value type
     * @return A map containing the pairs
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> mapOf(Object... pairs) {
        Map<K, V> map = new HashMap<>();
        for (int i = 0; i < pairs.length; i += 2) {
            if (i + 1 < pairs.length) {
                map.put((K) pairs[i], (V) pairs[i + 1]);
            }
        }
        return map;
    }

    /**
     * Filters a collection based on a predicate.
     *
     * @param collection The collection to filter
     * @param predicate The filtering condition
     * @param <T> The element type
     * @return A new list with filtered elements
     */
    public static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        }
        return collection.stream()
            .filter(predicate)
            .collect(Collectors.toList());
    }

    /**
     * Finds the first element matching a predicate.
     *
     * @param collection The collection to search
     * @param predicate The matching condition
     * @param <T> The element type
     * @return An Optional containing the first match, or empty
     */
    public static <T> Optional<T> findFirst(Collection<T> collection, Predicate<T> predicate) {
        if (isEmpty(collection)) {
            return Optional.empty();
        }
        return collection.stream()
            .filter(predicate)
            .findFirst();
    }

    /**
     * Checks if any element in the collection matches a predicate.
     *
     * @param collection The collection to check
     * @param predicate The matching condition
     * @param <T> The element type
     * @return true if any element matches, false otherwise
     */
    public static <T> boolean anyMatch(Collection<T> collection, Predicate<T> predicate) {
        if (isEmpty(collection)) {
            return false;
        }
        return collection.stream().anyMatch(predicate);
    }

    /**
     * Checks if all elements in the collection match a predicate.
     *
     * @param collection The collection to check
     * @param predicate The matching condition
     * @param <T> The element type
     * @return true if all elements match, false otherwise
     */
    public static <T> boolean allMatch(Collection<T> collection, Predicate<T> predicate) {
        if (isEmpty(collection)) {
            return true; // Empty collection trivially matches all
        }
        return collection.stream().allMatch(predicate);
    }

    /**
     * Counts elements matching a predicate.
     *
     * @param collection The collection to count
     * @param predicate The matching condition
     * @param <T> The element type
     * @return The count of matching elements
     */
    public static <T> long count(Collection<T> collection, Predicate<T> predicate) {
        if (isEmpty(collection)) {
            return 0;
        }
        return collection.stream().filter(predicate).count();
    }

    /**
     * Gets a random element from a collection.
     *
     * @param collection The collection
     * @param <T> The element type
     * @return An Optional containing a random element, or empty if collection is empty
     */
    public static <T> Optional<T> randomElement(Collection<T> collection) {
        if (isEmpty(collection)) {
            return Optional.empty();
        }
        List<T> list = new ArrayList<>(collection);
        return Optional.of(list.get(new Random().nextInt(list.size())));
    }

    /**
     * Gets the first element of a collection.
     *
     * @param collection The collection
     * @param <T> The element type
     * @return An Optional containing the first element, or empty
     */
    public static <T> Optional<T> first(Collection<T> collection) {
        if (isEmpty(collection)) {
            return Optional.empty();
        }
        return Optional.of(collection.iterator().next());
    }

    /**
     * Gets the last element of a list.
     *
     * @param list The list
     * @param <T> The element type
     * @return An Optional containing the last element, or empty
     */
    public static <T> Optional<T> last(List<T> list) {
        if (isEmpty(list)) {
            return Optional.empty();
        }
        return Optional.of(list.get(list.size() - 1));
    }

    /**
     * Concatenates multiple collections into one.
     *
     * @param collections The collections to concatenate
     * @param <T> The element type
     * @return A new list with all elements
     */
    @SafeVarargs
    public static <T> List<T> concat(Collection<T>... collections) {
        List<T> result = new ArrayList<>();
        for (Collection<T> collection : collections) {
            if (!isEmpty(collection)) {
                result.addAll(collection);
            }
        }
        return result;
    }

    /**
     * Removes duplicates from a collection.
     *
     * @param collection The collection
     * @param <T> The element type
     * @return A new list with duplicates removed
     */
    public static <T> List<T> distinct(Collection<T> collection) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        }
        return collection.stream()
            .distinct()
            .collect(Collectors.toList());
    }
}
