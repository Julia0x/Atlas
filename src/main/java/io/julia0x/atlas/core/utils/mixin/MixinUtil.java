package io.julia0x.atlas.core.utils.mixin;

import io.julia0x.atlas.core.utils.logging.LoggerFactory;

import java.util.logging.Logger;

/**
 * Utility class for mixin-related operations and helpers.
 */
public class MixinUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(MixinUtil.class);

    private MixinUtil() {
        // Utility class
    }

    /**
     * Checks if the current thread is a game render thread.
     *
     * @return true if on render thread, false otherwise
     */
    public static boolean isRenderThread() {
        String threadName = Thread.currentThread().getName();
        return threadName.contains("Render") || threadName.contains("main");
    }

    /**
     * Checks if the current thread is a network thread.
     *
     * @return true if on network thread, false otherwise
     */
    public static boolean isNetworkThread() {
        String threadName = Thread.currentThread().getName();
        return threadName.contains("Netty") || threadName.contains("network");
    }

    /**
     * Gets a formatted name for a mixin class.
     *
     * @param className The class being mixed into
     * @return A formatted mixin name
     */
    public static String getMixinName(String className) {
        String simpleName = className.contains(".") 
            ? className.substring(className.lastIndexOf('.') + 1)
            : className;
        return simpleName + "Mixin";
    }

    /**
     * Validates that a class exists and is accessible.
     *
     * @param className The fully qualified class name
     * @return true if the class exists and is accessible, false otherwise
     */
    public static boolean validateClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            LOGGER.warning("Class not found for mixin: " + className);
            return false;
        }
    }

    /**
     * Gets a simple description of a method signature.
     *
     * @param methodName The method name
     * @param args The argument types
     * @return A formatted signature
     */
    public static String getMethodSignature(String methodName, Class<?>... args) {
        StringBuilder sb = new StringBuilder(methodName).append("(");
        for (int i = 0; i < args.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(args[i].getSimpleName());
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * Logs a mixin injection attempt.
     *
     * @param mixinName The mixin class name
     * @param targetClass The target class
     * @param method The target method
     */
    public static void logMixinInjection(String mixinName, String targetClass, String method) {
        LOGGER.info("Injecting mixin: " + mixinName + " into " + targetClass + "." + method);
    }

    /**
     * Logs a mixin injection failure.
     *
     * @param mixinName The mixin class name
     * @param targetClass The target class
     * @param method The target method
     * @param error The error message
     */
    public static void logMixinInjectionError(String mixinName, String targetClass, String method, String error) {
        LOGGER.severe("Mixin injection failed for " + mixinName + " into " + targetClass + "." + method + ": " + error);
    }
}
