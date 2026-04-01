package io.julia0x.atlas.core.utils.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Factory for creating loggers with consistent naming and configuration.
 * Simplifies logger creation and ensures consistent formatting.
 */
public class LoggerFactory {
    private static final String MOD_PREFIX = "[Atlas] ";

    /**
     * Creates a logger for the given class.
     *
     * @param clazz The class to create a logger for
     * @return A configured logger
     */
    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getSimpleName());
    }

    /**
     * Creates a logger with the given name.
     *
     * @param name The name for the logger
     * @return A configured logger
     */
    public static Logger getLogger(String name) {
        Logger logger = Logger.getLogger(MOD_PREFIX + name);
        if (!logger.getHandlers().length > 0) {
            // Use the default handlers from the parent
            logger.setUseParentHandlers(true);
        }
        return logger;
    }

    /**
     * Logs an informational message.
     *
     * @param logger The logger to use
     * @param message The message to log
     */
    public static void info(Logger logger, String message) {
        logger.log(Level.INFO, message);
    }

    /**
     * Logs a warning message.
     *
     * @param logger The logger to use
     * @param message The message to log
     */
    public static void warn(Logger logger, String message) {
        logger.log(Level.WARNING, message);
    }

    /**
     * Logs an error message with exception.
     *
     * @param logger The logger to use
     * @param message The message to log
     * @param throwable The exception
     */
    public static void error(Logger logger, String message, Throwable throwable) {
        logger.log(Level.SEVERE, message, throwable);
    }

    /**
     * Logs a debug message.
     *
     * @param logger The logger to use
     * @param message The message to log
     */
    public static void debug(Logger logger, String message) {
        logger.log(Level.FINE, message);
    }
}
