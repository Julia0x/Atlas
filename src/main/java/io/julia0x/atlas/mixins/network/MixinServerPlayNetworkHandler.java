package io.julia0x.atlas.mixins.network;

import io.julia0x.atlas.integration.events.NetworkEvent;
import io.julia0x.atlas.core.impl.lifecycle.LifecycleManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mixin for ServerPlayNetworkHandler to monitor server packets.
 * Provides server-side network event dispatch.
 * 
 * @author Julia0x
 * @version 1.0
 */
@Mixin(ServerPlayNetworkHandler.class)
public abstract class MixinServerPlayNetworkHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger("Atlas/MixinServerPlayNetworkHandler");

    /**
     * Inject into server packet handling for network monitoring.
     */
    @Inject(
        method = "onPacket",
        at = @At("HEAD")
    )
    private void onServerPacket(Packet<?> packet, CallbackInfo ci) {
        try {
            NetworkEvent event = new NetworkEvent(packet, NetworkEvent.Direction.INBOUND);
            LifecycleManager.getInstance().getEventBus().post(event);
        } catch (Exception e) {
            LOGGER.debug("[Atlas] Error dispatching server NetworkEvent", e);
        }
    }
}
