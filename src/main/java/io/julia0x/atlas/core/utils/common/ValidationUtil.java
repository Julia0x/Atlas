package io.julia0x.atlas.core.utils.common;

import java.util.Collection;
import java.util.Map;

/**
 * Utility class for common validation operations.
 */
public class ValidationUtil {
    private ValidationUtil() {
        // Utility class
    }

    /**
     * Checks if a string is null or empty.
     *
     * @param string The string to check
     * @return true if null or empty, false otherwise
     */
    public static boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    /**
     * Checks if a string is null or blank (whitespace only).
     *
     * @param string The string to check
     * @return true if null or blank, false otherwise
     */
    public static boolean isBlank(String string) {
        return string == null || string.isBlank();
    }

    /**
     * Requires that an object is not null.
     *
     * @param object The object to check
     * @param message The error message if null
     * @param <T> The object type
     * @return The object if not null
     * @throws IllegalArgumentException if the object is null
     */
    public static <T> T requireNonNull(T object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
        return object;
    }

    /**
     * Requires that a string is not empty.
     *
     * @param string The string to check
     * @param message The error message if empty
     * @return The string if not empty
     * @throws IllegalArgumentException if the string is empty
     */
    public static String requireNonEmpty(String string, String message) {
        if (isEmpty(string)) {
            throw new IllegalArgumentException(message);
        }
        return string;
    }

    /**
     * Requires that a collection is not empty.
     *
     * @param collection The collection to check
     * @param message The error message if empty
     * @param <T> The element type
     * @return The collection if not empty
     * @throws IllegalArgumentException if the collection is empty
     */
    public static <T extends Collection<?>> T requireNonEmpty(T collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return collection;
    }

    /**
     * Validates that a value is within a certain range.
     *
     * @param value The value to check
     * @param min The minimum (inclusive)
     * @param max The maximum (inclusive)
     * @param message The error message if out of range
     * @return The value if within range
     * @throws IllegalArgumentException if out of range
     */
    public static int requireInRange(int value, int min, int max, String message) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    /**
     * Validates that a value is between two numbers.
     *
     * @param value The value to check
     * @param min The minimum (inclusive)
     * @param max The maximum (inclusive)
     * @param message The error message if out of range
     * @return The value if within range
     * @throws IllegalArgumentException if out of range
     */
    public static long requireInRange(long value, long min, long max, String message) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    /**
     * Validates that a value matches a pattern.
     *
     * @param value The value to check
     * @param pattern The regex pattern
     * @param message The error message if not matching
     * @return The value if matching
     * @throws IllegalArgumentException if not matching
     */
    public static String requireMatches(String value, String pattern, String message) {
        if (!value.matches(pattern)) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    /**
     * Checks if all values in a map are non-null.
     *
     * @param map The map to check
     * @return true if all values are non-null, false otherwise
     */
    public static boolean allNonNull(Map<?, ?> map) {
        return map.values().stream().allMatch(v -> v != null);
    }

    /**
     * Checks if an object is an instance of a type.
     *
     * @param object The object to check
     * @param type The expected type
     * @param message The error message if not an instance
     * @param <T> The type
     * @return The object cast to the type
     * @throws IllegalArgumentException if not an instance
     */
    public static <T> T requireInstance(Object object, Class<T> type, String message) {
        if (!type.isInstance(object)) {
            throw new IllegalArgumentException(message);
        }
        return type.cast(object);
    }
}
