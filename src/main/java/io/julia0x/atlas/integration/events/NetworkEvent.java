package io.julia0x.atlas.integration.events;

import io.julia0x.atlas.core.api.event.Event;

/**
 * Base class for network-related events.
 */
public abstract class NetworkEvent extends Event {
    public static class Connect extends NetworkEvent {
        private final String serverName;

        /**
         * Fired when connecting to a server.
         *
         * @param serverName The server name/address
         */
        public Connect(String serverName) {
            this.serverName = serverName;
        }

        /**
         * Gets the server name.
         *
         * @return The server name/address
         */
        public String getServerName() {
            return serverName;
        }
    }

    public static class Disconnect extends NetworkEvent {
        private final String reason;

        /**
         * Fired when disconnecting from a server.
         *
         * @param reason The disconnect reason
         */
        public Disconnect(String reason) {
            this.reason = reason;
        }

        /**
         * Gets the disconnect reason.
         *
         * @return The reason
         */
        public String getReason() {
            return reason;
        }
    }

    public static class PacketReceive extends NetworkEvent {
        private final String channel;
        private final byte[] data;

        /**
         * Fired when receiving a custom packet.
         *
         * @param channel The packet channel
         * @param data The packet data
         */
        public PacketReceive(String channel, byte[] data) {
            this.channel = channel;
            this.data = data;
        }

        /**
         * Gets the packet channel.
         *
         * @return The channel
         */
        public String getChannel() {
            return channel;
        }

        /**
         * Gets the packet data.
         *
         * @return The data bytes
         */
        public byte[] getData() {
            return data;
        }
    }

    public static class PacketSend extends NetworkEvent {
        private final String channel;
        private final byte[] data;

        /**
         * Fired when sending a custom packet.
         *
         * @param channel The packet channel
         * @param data The packet data
         */
        public PacketSend(String channel, byte[] data) {
            this.channel = channel;
            this.data = data;
        }

        /**
         * Gets the packet channel.
         *
         * @return The channel
         */
        public String getChannel() {
            return channel;
        }

        /**
         * Gets the packet data.
         *
         * @return The data bytes
         */
        public byte[] getData() {
            return data;
        }
    }
}
