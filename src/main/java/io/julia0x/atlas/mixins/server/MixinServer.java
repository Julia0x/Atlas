package io.julia0x.atlas.mixins.server;

import io.julia0x.atlas.core.impl.lifecycle.LifecycleManager;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mixin for MinecraftServer to hook into server lifecycle.
 * Provides server initialization and shutdown event dispatch.
 * 
 * @author Julia0x
 * @version 1.0
 */
@Mixin(MinecraftServer.class)
public abstract class MixinServer {

    private static final Logger LOGGER = LoggerFactory.getLogger("Atlas/MixinServer");

    /**
     * Inject into server startup to initialize server-side systems.
     */
    @Inject(
        method = "runServer",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;tick(Ljava/util/function/BooleanSupplier;)V", ordinal = 0)
    )
    private void onServerStart(CallbackInfo ci) {
        try {
            LifecycleManager.getInstance().initializeServer();
            LOGGER.info("[Atlas] Server-side systems initialized");
        } catch (Exception e) {
            LOGGER.error("[Atlas] Error initializing server systems", e);
        }
    }

    /**
     * Inject into server shutdown for cleanup.
     */
    @Inject(
        method = "shutdown",
        at = @At("HEAD")
    )
    private void onServerShutdown(CallbackInfo ci) {
        try {
            LifecycleManager.getInstance().shutdown();
            LOGGER.info("[Atlas] Server shutdown complete");
        } catch (Exception e) {
            LOGGER.error("[Atlas] Error during server shutdown", e);
        }
    }
}
