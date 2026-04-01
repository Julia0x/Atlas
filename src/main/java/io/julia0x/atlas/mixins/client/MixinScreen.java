package io.julia0x.atlas.mixins.client;

import io.julia0x.atlas.core.impl.lifecycle.LifecycleManager;
import io.julia0x.atlas.features.ui.events.ScreenOpenEvent;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mixin for Screen to hook into screen open/close lifecycle.
 * Provides screen event dispatch for UI features.
 * 
 * @author Julia0x
 * @version 1.0
 */
@Mixin(Screen.class)
public abstract class MixinScreen {

    private static final Logger LOGGER = LoggerFactory.getLogger("Atlas/MixinScreen");

    @Shadow
    protected String title;

    /**
     * Inject into screen initialization to dispatch ScreenOpenEvent.
     */
    @Inject(
        method = "init",
        at = @At("HEAD")
    )
    private void onScreenOpen(CallbackInfo ci) {
        try {
            Screen screen = (Screen) (Object) this;
            ScreenOpenEvent event = new ScreenOpenEvent(screen, title);
            LifecycleManager.getInstance().getEventBus().post(event);
        } catch (Exception e) {
            LOGGER.debug("[Atlas] Error dispatching ScreenOpenEvent", e);
        }
    }

    /**
     * Inject into screen close to dispatch screen close event.
     */
    @Inject(
        method = "close",
        at = @At("HEAD")
    )
    private void onScreenClose(CallbackInfo ci) {
        try {
            Screen screen = (Screen) (Object) this;
            ScreenOpenEvent closeEvent = new ScreenOpenEvent(screen, null);
            LifecycleManager.getInstance().getEventBus().post(closeEvent);
        } catch (Exception e) {
            LOGGER.debug("[Atlas] Error dispatching screen close event", e);
        }
    }
}
