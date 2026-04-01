package io.julia0x.atlas.mixins.entity;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mixin for LivingEntity for living entity-specific operations.
 * Provides hooks for health, damage, and attribute changes.
 * 
 * @author Julia0x
 * @version 1.0
 */
@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

    private static final Logger LOGGER = LoggerFactory.getLogger("Atlas/MixinLivingEntity");

    /**
     * Hook into health changes for event dispatch.
     */
    @Inject(
        method = "setHealth",
        at = @At("HEAD")
    )
    private void onHealthChange(float health, CallbackInfo ci) {
        try {
            LivingEntity entity = (LivingEntity) (Object) this;
            float previousHealth = entity.getHealth();
            if (health != previousHealth) {
                LOGGER.debug("[Atlas] Entity health changed: {} -> {}", previousHealth, health);
            }
        } catch (Exception e) {
            LOGGER.debug("[Atlas] Error in health change hook", e);
        }
    }
}
