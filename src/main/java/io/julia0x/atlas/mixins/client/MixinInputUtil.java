package io.julia0x.atlas.mixins.client;

import io.julia0x.atlas.integration.handlers.InputHandler;
import net.minecraft.client.input.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mixin for Input handling to intercept user input events.
 * Allows Atlas systems to react to keyboard and mouse input.
 * 
 * @author Julia0x
 * @version 1.0
 */
@Mixin(Input.class)
public abstract class MixinInputUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger("Atlas/MixinInputUtil");

    /**
     * Inject into input update to allow input interception.
     * This allows custom input handlers to modify or respond to input.
     */
    @Inject(
        method = "tick",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onInputTick(boolean slowDown, CallbackInfo ci) {
        try {
            InputHandler handler = InputHandler.getInstance();
            if (handler.shouldCancelInput()) {
                ci.cancel();
            }
        } catch (Exception e) {
            LOGGER.debug("[Atlas] Error in input tick handling", e);
        }
    }
}
