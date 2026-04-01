package io.julia0x.atlas.mixins.network;

import io.julia0x.atlas.integration.events.NetworkEvent;
import io.julia0x.atlas.core.impl.lifecycle.LifecycleManager;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mixin for ClientConnection to monitor network packets.
 * Provides packet event dispatch for network monitoring.
 * 
 * @author Julia0x
 * @version 1.0
 */
@Mixin(ClientConnection.class)
public abstract class MixinClientConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger("Atlas/MixinClientConnection");

    /**
     * Inject into packet sending for network monitoring.
     */
    @Inject(
        method = "send",
        at = @At("HEAD")
    )
    private void onPacketSend(Packet<?> packet, CallbackInfo ci) {
        try {
            NetworkEvent event = new NetworkEvent(packet, NetworkEvent.Direction.OUTBOUND);
            LifecycleManager.getInstance().getEventBus().post(event);
        } catch (Exception e) {
            LOGGER.debug("[Atlas] Error dispatching outbound NetworkEvent", e);
        }
    }

    /**
     * Inject into packet receiving for network monitoring.
     */
    @Inject(
        method = "handlePacket",
        at = @At("HEAD")
    )
    private void onPacketReceive(Packet<?> packet, CallbackInfo ci) {
        try {
            NetworkEvent event = new NetworkEvent(packet, NetworkEvent.Direction.INBOUND);
            LifecycleManager.getInstance().getEventBus().post(event);
        } catch (Exception e) {
            LOGGER.debug("[Atlas] Error dispatching inbound NetworkEvent", e);
        }
    }
}
