package io.julia0x.atlas.mixins.server;

import io.julia0x.atlas.core.impl.lifecycle.LifecycleManager;
import io.julia0x.atlas.integration.events.ServerTickEvent;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mixin for server tick event dispatch.
 * Provides per-tick event posting for server-side systems.
 * 
 * @author Julia0x
 * @version 1.0
 */
@Mixin(MinecraftServer.class)
public abstract class MixinServerTickEvents {

    private static final Logger LOGGER = LoggerFactory.getLogger("Atlas/MixinServerTickEvents");

    /**
     * Inject into server tick method to dispatch ServerTickEvent.
     */
    @Inject(
        method = "tick",
        at = @At("HEAD")
    )
    private void onServerTick(CallbackInfo ci) {
        try {
            ServerTickEvent event = new ServerTickEvent();
            LifecycleManager.getInstance().getEventBus().post(event);
        } catch (Exception e) {
            LOGGER.error("[Atlas] Error dispatching ServerTickEvent", e);
        }
    }
}
