package io.julia0x.atlas.mixins.client;

import io.julia0x.atlas.core.impl.lifecycle.LifecycleManager;
import io.julia0x.atlas.integration.events.ClientTickEvent;
import io.julia0x.atlas.integration.handlers.InputHandler;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mixin for MinecraftClient to hook into client tick events and initialization.
 * Provides client-side event dispatch for Atlas mod features.
 * 
 * @author Julia0x
 * @version 1.0
 */
@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {

    private static final Logger LOGGER = LoggerFactory.getLogger("Atlas/MixinMinecraftClient");

    /**
     * Inject into client tick to dispatch ClientTickEvent.
     * This allows all systems to react to client ticks in a centralized manner.
     */
    @Inject(
        method = "tick",
        at = @At(value = "HEAD"),
        cancellable = false
    )
    private void onClientTick(CallbackInfo ci) {
        try {
            ClientTickEvent event = new ClientTickEvent();
            LifecycleManager.getInstance().getEventBus().post(event);
        } catch (Exception e) {
            LOGGER.error("[Atlas] Error dispatching ClientTickEvent", e);
        }
    }

    /**
     * Inject into client initialization to set up Atlas systems.
     */
    @Inject(
        method = "<init>",
        at = @At(value = "TAIL")
    )
    private void onClientInit(CallbackInfo ci) {
        try {
            LifecycleManager.getInstance().initializeClient();
            InputHandler.getInstance().registerInputListeners();
            LOGGER.info("[Atlas] Client initialization completed successfully");
        } catch (Exception e) {
            LOGGER.error("[Atlas] Error during client initialization", e);
        }
    }
}
