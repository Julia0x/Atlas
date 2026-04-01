package io.julia0x.atlas.mixins.client;

import io.julia0x.atlas.core.impl.lifecycle.LifecycleManager;
import io.julia0x.atlas.features.ui.events.HudRenderEvent;
import io.julia0x.atlas.integration.logging.PerformanceMonitor;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mixin for GameRenderer to hook into render pipeline.
 * Provides HUD rendering and performance monitoring capabilities.
 * 
 * @author Julia0x
 * @version 1.0
 */
@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    private static final Logger LOGGER = LoggerFactory.getLogger("Atlas/MixinGameRenderer");

    /**
     * Inject into HUD render phase to allow custom HUD elements to render.
     * Monitors performance of HUD rendering.
     */
    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/hud/InGameHud;render(Lnet/minecraft/client/gui/DrawContext;)V"
        )
    )
    private void onHudRender(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        try {
            long renderStart = System.nanoTime();
            
            HudRenderEvent event = new HudRenderEvent(tickDelta);
            LifecycleManager.getInstance().getEventBus().post(event);
            
            long renderTime = (System.nanoTime() - renderStart) / 1_000_000; // Convert to ms
            PerformanceMonitor.getInstance().recordRenderTime("hud", renderTime);
        } catch (Exception e) {
            LOGGER.error("[Atlas] Error during HUD render", e);
        }
    }

    /**
     * Inject for render cleanup and monitoring.
     */
    @Inject(
        method = "render",
        at = @At("TAIL")
    )
    private void onRenderComplete(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        PerformanceMonitor.getInstance().recordFrameTime(tickDelta);
    }
}
