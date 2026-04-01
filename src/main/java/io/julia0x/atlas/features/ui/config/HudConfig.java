package io.julia0x.atlas.features.ui.config;

import io.julia0x.atlas.core.api.config.IConfigurable;

/**
 * Configuration for UI/HUD features.
 */
public class HudConfig implements IConfigurable {
    private boolean hudEnabled = true;
    private boolean useCustomFonts = true;
    private int hudScale = 1;
    private boolean transparentBackground = false;
    private int backgroundOpacity = 200;
    private boolean animationsEnabled = true;

    @Override
    public String getConfigKey() {
        return "ui";
    }

    @Override
    public void onConfigReloaded() {
        // Reload configuration from ConfigManager
    }

    public boolean isHudEnabled() {
        return hudEnabled;
    }

    public void setHudEnabled(boolean enabled) {
        this.hudEnabled = enabled;
    }

    public boolean isUsingCustomFonts() {
        return useCustomFonts;
    }

    public void setUseCustomFonts(boolean custom) {
        this.useCustomFonts = custom;
    }

    public int getHudScale() {
        return hudScale;
    }

    public void setHudScale(int scale) {
        this.hudScale = Math.max(1, scale);
    }

    public boolean isTransparentBackground() {
        return transparentBackground;
    }

    public void setTransparentBackground(boolean transparent) {
        this.transparentBackground = transparent;
    }

    public int getBackgroundOpacity() {
        return backgroundOpacity;
    }

    public void setBackgroundOpacity(int opacity) {
        this.backgroundOpacity = Math.max(0, Math.min(255, opacity));
    }

    public boolean isAnimationsEnabled() {
        return animationsEnabled;
    }

    public void setAnimationsEnabled(boolean enabled) {
        this.animationsEnabled = enabled;
    }
}
