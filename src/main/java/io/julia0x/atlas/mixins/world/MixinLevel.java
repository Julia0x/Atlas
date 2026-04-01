package io.julia0x.atlas.mixins.world;

import io.julia0x.atlas.core.impl.cache.MemoryCacheManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mixin for Level/World to manage world-specific caching.
 * Provides world state management and cache clearing on world load/unload.
 * 
 * @author Julia0x
 * @version 1.0
 */
@Mixin(World.class)
public abstract class MixinLevel {

    private static final Logger LOGGER = LoggerFactory.getLogger("Atlas/MixinLevel");

    /**
     * Clear world-specific cache on world unload.
     */
    @Inject(
        method = "close",
        at = @At("HEAD")
    )
    private void onWorldClose(CallbackInfo ci) {
        try {
            MemoryCacheManager cacheManager = MemoryCacheManager.getInstance();
            cacheManager.clearCategory("world");
            LOGGER.debug("[Atlas] World cache cleared on unload");
        } catch (Exception e) {
            LOGGER.debug("[Atlas] Error clearing world cache", e);
        }
    }
}
