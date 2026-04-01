package io.julia0x.atlas.integration.handlers;

import io.julia0x.atlas.core.api.handler.IHandler;
import io.julia0x.atlas.core.utils.logging.LoggerFactory;

import java.util.logging.Logger;

/**
 * Handles network-related events and data transmission.
 * Manages custom network packets and server communication.
 */
public class NetworkHandler implements IHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkHandler.class);

    private boolean enabled = false;
    private boolean connected = false;

    @Override
    public boolean initialize() {
        try {
            LOGGER.info("Initializing NetworkHandler");
            // Register network listeners
            enabled = true;
            return true;
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize NetworkHandler: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void shutdown() {
        LOGGER.info("Shutting down NetworkHandler");
        if (connected) {
            disconnect();
        }
        enabled = false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return "NetworkHandler";
    }

    @Override
    public int getPriority() {
        return 40;
    }

    /**
     * Called when the player connects to a server.
     */
    public void onServerConnect() {
        if (!enabled) {
            return;
        }
        connected = true;
        LOGGER.info("Connected to server");
    }

    /**
     * Called when the player disconnects from a server.
     */
    public void onServerDisconnect() {
        if (!enabled) {
            return;
        }
        connected = false;
        LOGGER.info("Disconnected from server");
    }

    /**
     * Checks if the client is connected to a server.
     *
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Sends a custom network packet.
     *
     * @param channel The packet channel
     * @param data The packet data
     * @return true if sent successfully, false otherwise
     */
    public boolean sendPacket(String channel, byte[] data) {
        if (!enabled || !connected) {
            LOGGER.warning("Cannot send packet: handler not enabled or not connected");
            return false;
        }

        LOGGER.fine("Sending packet on channel: " + channel);
        // Implementation would send the actual packet
        return true;
    }

    /**
     * Receives a custom network packet.
     *
     * @param channel The packet channel
     * @param data The packet data
     */
    public void onPacketReceived(String channel, byte[] data) {
        if (!enabled) {
            return;
        }
        LOGGER.fine("Received packet on channel: " + channel + ", size: " + data.length + " bytes");
    }
}
