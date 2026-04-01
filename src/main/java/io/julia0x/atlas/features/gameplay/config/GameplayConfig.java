package io.julia0x.atlas.features.gameplay.config;

import io.julia0x.atlas.core.api.config.IConfigurable;

/**
 * Configuration for Gameplay Mechanics features.
 */
public class GameplayConfig implements IConfigurable {
    private boolean customMechanicsEnabled = true;
    private float difficultyMultiplier = 1.0f;
    private boolean enableCustomStatusEffects = true;
    private boolean enableEntityInteractions = true;
    private int mechanicsUpdateRate = 20; // Ticks between updates

    @Override
    public String getConfigKey() {
        return "gameplay";
    }

    @Override
    public void onConfigReloaded() {
        // Reload configuration from ConfigManager
    }

    public boolean isCustomMechanicsEnabled() {
        return customMechanicsEnabled;
    }

    public void setCustomMechanicsEnabled(boolean enabled) {
        this.customMechanicsEnabled = enabled;
    }

    public float getDifficultyMultiplier() {
        return difficultyMultiplier;
    }

    public void setDifficultyMultiplier(float multiplier) {
        this.difficultyMultiplier = Math.max(0.1f, multiplier);
    }

    public boolean isCustomStatusEffectsEnabled() {
        return enableCustomStatusEffects;
    }

    public void setCustomStatusEffectsEnabled(boolean enabled) {
        this.enableCustomStatusEffects = enabled;
    }

    public boolean isEntityInteractionsEnabled() {
        return enableEntityInteractions;
    }

    public void setEntityInteractionsEnabled(boolean enabled) {
        this.enableEntityInteractions = enabled;
    }

    public int getMechanicsUpdateRate() {
        return mechanicsUpdateRate;
    }

    public void setMechanicsUpdateRate(int rate) {
        this.mechanicsUpdateRate = Math.max(1, rate);
    }
}
