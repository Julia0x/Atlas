package io.julia0x.atlas.mixins.entity;

import io.julia0x.atlas.core.impl.cache.MemoryCacheManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mixin for Entity base class for entity-related operations.
 * Provides caching and state management for entities.
 * 
 * @author Julia0x
 * @version 1.0
 */
@Mixin(Entity.class)
public abstract class MixinEntity {

    private static final Logger LOGGER = LoggerFactory.getLogger("Atlas/MixinEntity");

    @Shadow
    public int id;

    /**
     * Cache entity state on tick.
     */
    @Inject(
        method = "tick",
        at = @At("TAIL")
    )
    private void onEntityTick(CallbackInfo ci) {
        try {
            Entity entity = (Entity) (Object) this;
            String cacheKey = "entity_" + id;
            MemoryCacheManager.getInstance().put("entities", cacheKey, entity, 60); // 1 minute
        } catch (Exception e) {
            LOGGER.debug("[Atlas] Error caching entity", e);
        }
    }
}
