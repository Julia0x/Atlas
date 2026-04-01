package io.julia0x.atlas.mixins.world;

import io.julia0x.atlas.core.impl.cache.MemoryCacheManager;
import net.minecraft.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mixin for BlockEntity to manage block-specific caching.
 * Provides cache management for block entities.
 * 
 * @author Julia0x
 * @version 1.0
 */
@Mixin(BlockEntity.class)
public abstract class MixinBlockEntity {

    private static final Logger LOGGER = LoggerFactory.getLogger("Atlas/MixinBlockEntity");

    @Shadow
    protected net.minecraft.util.math.BlockPos pos;

    /**
     * Inject into block entity tick for cache updates.
     */
    @Inject(
        method = "tick",
        at = @At("TAIL")
    )
    private static void onBlockEntityTick(BlockEntity blockEntity, CallbackInfo ci) {
        try {
            String cacheKey = "blockentity_" + blockEntity.getPos().asLong();
            MemoryCacheManager.getInstance().put("world", cacheKey, blockEntity, 300); // 5 minutes
        } catch (Exception e) {
            LOGGER.debug("[Atlas] Error caching block entity", e);
        }
    }
}
