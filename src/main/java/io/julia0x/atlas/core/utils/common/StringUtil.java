package io.julia0x.atlas.core.utils.common;

/**
 * Utility class for common string operations.
 */
public class StringUtil {
    private StringUtil() {
        // Utility class
    }

    /**
     * Converts a string to PascalCase.
     * Example: "my_variable" -> "MyVariable"
     *
     * @param string The string to convert
     * @return The PascalCase version
     */
    public static String toPascalCase(String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }

        String[] parts = string.split("[_\\-\\s]+");
        StringBuilder result = new StringBuilder();

        for (String part : parts) {
            if (!part.isEmpty()) {
                result.append(Character.toUpperCase(part.charAt(0)))
                      .append(part.substring(1).toLowerCase());
            }
        }

        return result.toString();
    }

    /**
     * Converts a string to camelCase.
     * Example: "my_variable" -> "myVariable"
     *
     * @param string The string to convert
     * @return The camelCase version
     */
    public static String toCamelCase(String string) {
        String pascalCase = toPascalCase(string);
        if (pascalCase.isEmpty()) {
            return pascalCase;
        }
        return Character.toLowerCase(pascalCase.charAt(0)) + pascalCase.substring(1);
    }

    /**
     * Converts a string to snake_case.
     * Example: "MyVariable" -> "my_variable"
     *
     * @param string The string to convert
     * @return The snake_case version
     */
    public static String toSnakeCase(String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }

        return string.replaceAll("([a-z])([A-Z]+)", "$1_$2")
                     .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
                     .toLowerCase();
    }

    /**
     * Repeats a string a given number of times.
     *
     * @param string The string to repeat
     * @param count The number of times to repeat
     * @return The repeated string
     */
    public static String repeat(String string, int count) {
        if (count <= 0) {
            return "";
        }
        return string.repeat(count);
    }

    /**
     * Pads a string to a certain length with a character on the left.
     *
     * @param string The string to pad
     * @param length The desired length
     * @param padChar The character to pad with
     * @return The padded string
     */
    public static String padLeft(String string, int length, char padChar) {
        if (string.length() >= length) {
            return string;
        }
        return repeat(String.valueOf(padChar), length - string.length()) + string;
    }

    /**
     * Pads a string to a certain length with a character on the right.
     *
     * @param string The string to pad
     * @param length The desired length
     * @param padChar The character to pad with
     * @return The padded string
     */
    public static String padRight(String string, int length, char padChar) {
        if (string.length() >= length) {
            return string;
        }
        return string + repeat(String.valueOf(padChar), length - string.length());
    }

    /**
     * Truncates a string to a maximum length.
     *
     * @param string The string to truncate
     * @param maxLength The maximum length
     * @return The truncated string
     */
    public static String truncate(String string, int maxLength) {
        if (string.length() <= maxLength) {
            return string;
        }
        return string.substring(0, maxLength);
    }

    /**
     * Truncates a string with an ellipsis.
     *
     * @param string The string to truncate
     * @param maxLength The maximum length (including ellipsis)
     * @return The truncated string with "..."
     */
    public static String truncateWithEllipsis(String string, int maxLength) {
        if (string.length() <= maxLength) {
            return string;
        }
        return truncate(string, maxLength - 3) + "...";
    }

    /**
     * Checks if a string contains any uppercase letters.
     *
     * @param string The string to check
     * @return true if contains uppercase, false otherwise
     */
    public static boolean hasUppercase(String string) {
        return string != null && string.matches(".*[A-Z].*");
    }

    /**
     * Checks if a string contains any lowercase letters.
     *
     * @param string The string to check
     * @return true if contains lowercase, false otherwise
     */
    public static boolean hasLowercase(String string) {
        return string != null && string.matches(".*[a-z].*");
    }

    /**
     * Checks if a string contains any digits.
     *
     * @param string The string to check
     * @return true if contains digits, false otherwise
     */
    public static boolean hasDigits(String string) {
        return string != null && string.matches(".*\\d.*");
    }

    /**
     * Removes all whitespace from a string.
     *
     * @param string The string to process
     * @return The string without whitespace
     */
    public static String removeWhitespace(String string) {
        return string.replaceAll("\\s+", "");
    }
}
