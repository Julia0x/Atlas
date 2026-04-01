package io.julia0x.atlas.core.impl.config;

import io.julia0x.atlas.core.api.config.IConfigManager;
import io.julia0x.atlas.core.utils.common.ValidationUtil;
import io.julia0x.atlas.core.utils.logging.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

/**
 * JSON-based configuration manager.
 * Loads and manages configuration from JSON files in a config directory.
 */
public class JsonConfigManager implements IConfigManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonConfigManager.class);
    private static final String CONFIG_DIR = "config/atlas";

    private final Map<String, Object> config = new ConcurrentHashMap<>();
    private final List<IConfigChangeListener> listeners = new CopyOnWriteArrayList<>();
    private final Path configPath;

    public JsonConfigManager() {
        this.configPath = Paths.get(CONFIG_DIR);
    }

    @Override
    public boolean loadConfig() {
        try {
            if (!Files.exists(configPath)) {
                Files.createDirectories(configPath);
                LOGGER.info("Created config directory: " + configPath);
            }

            config.clear();
            boolean loaded = false;

            // Load all JSON files in the config directory
            File[] files = configPath.toFile().listFiles((dir, name) -> name.endsWith(".json"));
            if (files != null) {
                for (File file : files) {
                    if (loadJsonFile(file)) {
                        loaded = true;
                    }
                }
            }

            if (!loaded) {
                LOGGER.warning("No configuration files found in " + configPath);
            }

            return true;
        } catch (IOException e) {
            LOGGER.severe("Failed to load configuration: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean saveConfig() {
        try {
            if (!Files.exists(configPath)) {
                Files.createDirectories(configPath);
            }

            // Save core config
            saveJsonFile(configPath.resolve("core.json").toFile(), config);
            LOGGER.info("Configuration saved");
            return true;
        } catch (IOException e) {
            LOGGER.severe("Failed to save configuration: " + e.getMessage());
            return false;
        }
    }

    @Override
    public <T> T get(String key, T defaultValue) {
        Object value = getByKey(key, config);
        if (value != null) {
            try {
                return (T) value;
            } catch (ClassCastException e) {
                LOGGER.warning("Type mismatch for key '" + key + "': " + e.getMessage());
            }
        }
        return defaultValue;
    }

    @Override
    public void set(String key, Object value) {
        Object oldValue = getByKey(key, config);
        setByKey(key, value, config);
        notifyListeners(key, oldValue, value);
    }

    @Override
    public boolean has(String key) {
        return getByKey(key, config) != null;
    }

    @Override
    public boolean reload() {
        Map<String, Object> backup = new HashMap<>(config);
        if (loadConfig()) {
            LOGGER.info("Configuration reloaded");
            return true;
        } else {
            config.putAll(backup);
            LOGGER.warning("Failed to reload configuration, reverted to previous state");
            return false;
        }
    }

    @Override
    public void registerListener(IConfigChangeListener listener) {
        ValidationUtil.requireNonNull(listener, "Listener cannot be null");
        listeners.add(listener);
    }

    @Override
    public void unregisterListener(IConfigChangeListener listener) {
        listeners.remove(listener);
    }

    private boolean loadJsonFile(File file) {
        try {
            String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            Map<String, Object> data = parseJson(content);
            String fileName = file.getName().replace(".json", "");
            config.put(fileName, data);
            LOGGER.info("Loaded config file: " + file.getName());
            return true;
        } catch (IOException | RuntimeException e) {
            LOGGER.severe("Failed to load config file '" + file.getName() + "': " + e.getMessage());
            return false;
        }
    }

    private void saveJsonFile(File file, Map<String, Object> data) throws IOException {
        String json = toJson(data);
        Files.write(file.toPath(), json.getBytes(StandardCharsets.UTF_8));
    }

    @SuppressWarnings("unchecked")
    private Object getByKey(String key, Map<String, Object> map) {
        String[] parts = key.split("\\.");
        Object current = map;

        for (String part : parts) {
            if (current instanceof Map) {
                current = ((Map<String, Object>) current).get(part);
            } else {
                return null;
            }
        }

        return current;
    }

    @SuppressWarnings("unchecked")
    private void setByKey(String key, Object value, Map<String, Object> map) {
        String[] parts = key.split("\\.");
        Map<String, Object> current = map;

        for (int i = 0; i < parts.length - 1; i++) {
            current = (Map<String, Object>) current.computeIfAbsent(
                parts[i],
                k -> new HashMap<>()
            );
        }

        current.put(parts[parts.length - 1], value);
    }

    private void notifyListeners(String key, Object oldValue, Object newValue) {
        for (IConfigChangeListener listener : listeners) {
            try {
                listener.onConfigChanged(key, oldValue, newValue);
            } catch (Exception e) {
                LOGGER.severe("Error notifying listener: " + e.getMessage());
            }
        }
    }

    // Simple JSON parsing/serialization (can be replaced with Gson/Jackson for production)
    @SuppressWarnings("unchecked")
    private Map<String, Object> parseJson(String json) {
        // Simplified JSON parsing - in production, use Gson or Jackson
        Map<String, Object> result = new HashMap<>();
        // This is a placeholder - implement proper JSON parsing as needed
        return result;
    }

    private String toJson(Map<String, Object> map) {
        // Simplified JSON serialization - in production, use Gson or Jackson
        return "{}";
    }
}
