package io.julia0x.atlas.mixins.performance;

import io.julia0x.atlas.integration.logging.PerformanceMonitor;
import net.minecraft.client.render.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mixin for RenderSystem to monitor rendering performance.
 * Provides performance tracking and optimization hooks.
 * 
 * @author Julia0x
 * @version 1.0
 */
@Mixin(RenderSystem.class)
public abstract class MixinRenderSystem {

    private static final Logger LOGGER = LoggerFactory.getLogger("Atlas/MixinRenderSystem");
    private static long lastFrameTime = 0;

    /**
     * Inject into frame flip for frame time tracking.
     */
    @Inject(
        method = "flipFrame",
        at = @At("HEAD")
    )
    private static void onFrameFlip(CallbackInfo ci) {
        try {
            long currentTime = System.nanoTime();
            if (lastFrameTime > 0) {
                long frameTime = (currentTime - lastFrameTime) / 1_000_000; // Convert to ms
                PerformanceMonitor.getInstance().recordFrameTime((float) frameTime);
            }
            lastFrameTime = currentTime;
        } catch (Exception e) {
            LOGGER.debug("[Atlas] Error tracking frame time", e);
        }
    }
}
