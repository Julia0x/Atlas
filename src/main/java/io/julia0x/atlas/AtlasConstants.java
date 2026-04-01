package io.julia0x.atlas;

/**
 * Global constants for the Atlas mod.
 */
public class AtlasConstants {
    // Mod metadata
    public static final String MOD_ID = "atlas";
    public static final String MOD_NAME = "Atlas";
    public static final String MOD_VERSION = "1.0.0";
    public static final String AUTHOR = "Julia0x";

    // Configuration paths
    public static final String CONFIG_DIR = "config/atlas";
    public static final String CORE_CONFIG = "core.json";
    public static final String UI_CONFIG = "ui.json";
    public static final String DEVELOPER_CONFIG = "developer.json";
    public static final String GAMEPLAY_CONFIG = "gameplay.json";
    public static final String UTILITIES_CONFIG = "utilities.json";

    // Event priorities
    public static final int PRIORITY_HIGHEST = 100;
    public static final int PRIORITY_HIGH = 50;
    public static final int PRIORITY_NORMAL = 0;
    public static final int PRIORITY_LOW = -50;
    public static final int PRIORITY_LOWEST = -100;

    // Cache settings
    public static final long CACHE_DEFAULT_TTL = 3600000; // 1 hour in milliseconds
    public static final int CACHE_MAX_SIZE = 1000;

    // Handler priorities
    public static final int HANDLER_PRIORITY_CRITICAL = 100;
    public static final int HANDLER_PRIORITY_HIGH = 50;
    public static final int HANDLER_PRIORITY_NORMAL = 0;
    public static final int HANDLER_PRIORITY_LOW = -50;

    // Common error messages
    public static final String ERROR_NULL_ARGUMENT = "Argument cannot be null";
    public static final String ERROR_INVALID_STATE = "Invalid state";
    public static final String ERROR_INITIALIZATION_FAILED = "Initialization failed";

    // Feature keys
    public static final String FEATURE_UI = "ui";
    public static final String FEATURE_DEVELOPER = "developer";
    public static final String FEATURE_GAMEPLAY = "gameplay";
    public static final String FEATURE_UTILITIES = "utilities";

    // Environment flags
    public static final boolean DEBUG = System.getProperty("atlas.debug", "false").equals("true");
    public static final boolean DEV_MODE = System.getProperty("atlas.devmode", "false").equals("true");

    private AtlasConstants() {
        // Constants class
    }
}
