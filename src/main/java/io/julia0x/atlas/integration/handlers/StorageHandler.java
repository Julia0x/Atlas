package io.julia0x.atlas.integration.handlers;

import io.julia0x.atlas.core.api.handler.IHandler;
import io.julia0x.atlas.core.utils.logging.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Handles file storage and data persistence.
 * Manages saving and loading of mod data files.
 */
public class StorageHandler implements IHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageHandler.class);
    private static final String DATA_DIR = "mods/atlas";

    private boolean enabled = false;
    private Path storagePath;

    @Override
    public boolean initialize() {
        try {
            LOGGER.info("Initializing StorageHandler");
            storagePath = Paths.get(DATA_DIR);
            
            if (!Files.exists(storagePath)) {
                Files.createDirectories(storagePath);
                LOGGER.info("Created storage directory: " + storagePath);
            }

            enabled = true;
            return true;
        } catch (IOException e) {
            LOGGER.severe("Failed to initialize StorageHandler: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void shutdown() {
        LOGGER.info("Shutting down StorageHandler");
        enabled = false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return "StorageHandler";
    }

    @Override
    public int getPriority() {
        return 30;
    }

    /**
     * Saves data to a file.
     *
     * @param filename The filename (relative to storage directory)
     * @param data The data to save
     * @return true if saved successfully, false otherwise
     */
    public boolean saveData(String filename, byte[] data) {
        if (!enabled) {
            LOGGER.warning("Storage handler not enabled");
            return false;
        }

        try {
            Path filePath = storagePath.resolve(filename);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, data);
            LOGGER.fine("Saved data to: " + filename);
            return true;
        } catch (IOException e) {
            LOGGER.severe("Failed to save data to " + filename + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Loads data from a file.
     *
     * @param filename The filename (relative to storage directory)
     * @return The file data, or empty array if not found
     */
    public byte[] loadData(String filename) {
        if (!enabled) {
            LOGGER.warning("Storage handler not enabled");
            return new byte[0];
        }

        try {
            Path filePath = storagePath.resolve(filename);
            if (Files.exists(filePath)) {
                byte[] data = Files.readAllBytes(filePath);
                LOGGER.fine("Loaded data from: " + filename);
                return data;
            } else {
                LOGGER.fine("File not found: " + filename);
                return new byte[0];
            }
        } catch (IOException e) {
            LOGGER.severe("Failed to load data from " + filename + ": " + e.getMessage());
            return new byte[0];
        }
    }

    /**
     * Saves text data to a file.
     *
     * @param filename The filename
     * @param content The text content
     * @return true if saved successfully, false otherwise
     */
    public boolean saveText(String filename, String content) {
        return saveData(filename, content.getBytes());
    }

    /**
     * Loads text data from a file.
     *
     * @param filename The filename
     * @return The text content, or empty string if not found
     */
    public String loadText(String filename) {
        byte[] data = loadData(filename);
        return new String(data);
    }

    /**
     * Checks if a file exists in storage.
     *
     * @param filename The filename
     * @return true if file exists, false otherwise
     */
    public boolean fileExists(String filename) {
        return Files.exists(storagePath.resolve(filename));
    }

    /**
     * Deletes a file from storage.
     *
     * @param filename The filename
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteFile(String filename) {
        try {
            Path filePath = storagePath.resolve(filename);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                LOGGER.fine("Deleted file: " + filename);
                return true;
            }
            return false;
        } catch (IOException e) {
            LOGGER.severe("Failed to delete file " + filename + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets the storage directory path.
     *
     * @return The storage path
     */
    public Path getStoragePath() {
        return storagePath;
    }
}
