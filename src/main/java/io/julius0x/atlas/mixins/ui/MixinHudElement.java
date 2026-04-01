package io.julia0x.atlas.mixins.ui;

import io.julia0x.atlas.features.ui.impl.HudManager;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mixin for InGameHud to manage custom HUD elements.
 * Provides hooks for HUD element rendering.
 * 
 * @author Julia0x
 * @version 1.0
 */
@Mixin(InGameHud.class)
public abstract class MixinHudElement {

    private static final Logger LOGGER = LoggerFactory.getLogger("Atlas/MixinHudElement");

    /**
     * Inject into HUD render to render custom HUD elements.
     */
    @Inject(
        method = "render",
        at = @At("TAIL")
    )
    private void onHudRender(CallbackInfo ci) {
        try {
            HudManager.getInstance().renderAllElements();
        } catch (Exception e) {
            LOGGER.debug("[Atlas] Error rendering custom HUD elements", e);
        }
    }
}
