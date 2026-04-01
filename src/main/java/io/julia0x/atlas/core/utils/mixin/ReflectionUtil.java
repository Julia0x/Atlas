package io.julia0x.atlas.core.utils.mixin;

import io.julia0x.atlas.core.utils.logging.LoggerFactory;

import java.lang.reflect.*;
import java.util.logging.Logger;

/**
 * Utility class for safe reflection operations.
 */
public class ReflectionUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);

    private ReflectionUtil() {
        // Utility class
    }

    /**
     * Gets a field from a class, handling access permissions.
     *
     * @param clazz The class to search
     * @param fieldName The field name
     * @return The field, or null if not found
     */
    public static Field getField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            LOGGER.warning("Field not found: " + clazz.getSimpleName() + "." + fieldName);
            return null;
        }
    }

    /**
     * Gets a field value from an object.
     *
     * @param object The object instance
     * @param fieldName The field name
     * @param <T> The field type
     * @return The field value, or null if not found or error occurs
     */
    public static <T> T getFieldValue(Object object, String fieldName) {
        if (object == null) {
            return null;
        }

        Field field = getField(object.getClass(), fieldName);
        if (field == null) {
            return null;
        }

        try {
            return (T) field.get(object);
        } catch (IllegalAccessException e) {
            LOGGER.severe("Cannot access field: " + fieldName);
            return null;
        }
    }

    /**
     * Sets a field value on an object.
     *
     * @param object The object instance
     * @param fieldName The field name
     * @param value The value to set
     * @return true if successful, false otherwise
     */
    public static boolean setFieldValue(Object object, String fieldName, Object value) {
        if (object == null) {
            return false;
        }

        Field field = getField(object.getClass(), fieldName);
        if (field == null) {
            return false;
        }

        try {
            field.set(object, value);
            return true;
        } catch (IllegalAccessException e) {
            LOGGER.severe("Cannot set field: " + fieldName);
            return false;
        }
    }

    /**
     * Gets a method from a class.
     *
     * @param clazz The class to search
     * @param methodName The method name
     * @param paramTypes The parameter types
     * @return The method, or null if not found
     */
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, paramTypes);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            LOGGER.warning("Method not found: " + clazz.getSimpleName() + "." + methodName);
            return null;
        }
    }

    /**
     * Invokes a method on an object.
     *
     * @param object The object instance (or null for static methods)
     * @param methodName The method name
     * @param args The method arguments
     * @param <T> The return type
     * @return The method result, or null if not found or error occurs
     */
    public static <T> T invokeMethod(Object object, String methodName, Object... args) {
        if (object == null) {
            return null;
        }

        Class<?>[] paramTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            paramTypes[i] = args[i] != null ? args[i].getClass() : Object.class;
        }

        Method method = getMethod(object.getClass(), methodName, paramTypes);
        if (method == null) {
            return null;
        }

        try {
            return (T) method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.severe("Error invoking method: " + methodName + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets a constructor from a class.
     *
     * @param clazz The class to search
     * @param paramTypes The parameter types
     * @return The constructor, or null if not found
     */
    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... paramTypes) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor(paramTypes);
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException e) {
            LOGGER.warning("Constructor not found for class: " + clazz.getSimpleName());
            return null;
        }
    }

    /**
     * Creates a new instance of a class using a constructor.
     *
     * @param clazz The class to instantiate
     * @param args The constructor arguments
     * @param <T> The class type
     * @return The new instance, or null if creation fails
     */
    public static <T> T newInstance(Class<T> clazz, Object... args) {
        Class<?>[] paramTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            paramTypes[i] = args[i] != null ? args[i].getClass() : Object.class;
        }

        Constructor<?> constructor = getConstructor(clazz, paramTypes);
        if (constructor == null) {
            return null;
        }

        try {
            return (T) constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.severe("Error instantiating class: " + clazz.getSimpleName() + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Checks if a class is assignable from another.
     *
     * @param parent The parent class
     * @param child The potential child class
     * @return true if child extends/implements parent, false otherwise
     */
    public static boolean isAssignableFrom(Class<?> parent, Class<?> child) {
        return parent != null && child != null && parent.isAssignableFrom(child);
    }

    /**
     * Gets all methods declared in a class (not inherited).
     *
     * @param clazz The class to search
     * @return Array of declared methods
     */
    public static Method[] getDeclaredMethods(Class<?> clazz) {
        return clazz.getDeclaredMethods();
    }

    /**
     * Finds the first method matching a predicate.
     *
     * @param clazz The class to search
     * @param predicate The matching condition
     * @return The first matching method, or null
     */
    public static Method findMethod(Class<?> clazz, java.util.function.Predicate<Method> predicate) {
        for (Method method : getDeclaredMethods(clazz)) {
            if (predicate.test(method)) {
                return method;
            }
        }
        return null;
    }
}
