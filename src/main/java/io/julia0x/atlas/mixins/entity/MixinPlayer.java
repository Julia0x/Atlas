package io.julia0x.atlas.mixins.entity;

import io.julia0x.atlas.core.impl.cache.MemoryCacheManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mixin for PlayerEntity for player-specific operations.
 * Provides caching and hooks for player state changes.
 * 
 * @author Julia0x
 * @version 1.0
 */
@Mixin(PlayerEntity.class)
public abstract class MixinPlayer {

    private static final Logger LOGGER = LoggerFactory.getLogger("Atlas/MixinPlayer");

    @Shadow
    public abstract String getGameProfile();

    /**
     * Cache player data on tick.
     */
    @Inject(
        method = "tick",
        at = @At("TAIL")
    )
    private void onPlayerTick(CallbackInfo ci) {
        try {
            PlayerEntity player = (PlayerEntity) (Object) this;
            String cacheKey = "player_" + player.getUuid();
            MemoryCacheManager.getInstance().put("players", cacheKey, player, 300); // 5 minutes
        } catch (Exception e) {
            LOGGER.debug("[Atlas] Error caching player", e);
        }
    }
}
