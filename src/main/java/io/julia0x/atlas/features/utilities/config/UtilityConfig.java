package io.julia0x.atlas.features.utilities.config;

import io.julia0x.atlas.core.api.config.IConfigurable;

/**
 * Configuration for Utility/Helper Tools features.
 */
public class UtilityConfig implements IConfigurable {
    private boolean utilitiesEnabled = true;
    private boolean searchToolEnabled = true;
    private boolean inventoryHelperEnabled = true;
    private int searchRange = 128; // Blocks
    private boolean enableShortcuts = true;

    @Override
    public String getConfigKey() {
        return "utilities";
    }

    @Override
    public void onConfigReloaded() {
        // Reload configuration from ConfigManager
    }

    public boolean isUtilitiesEnabled() {
        return utilitiesEnabled;
    }

    public void setUtilitiesEnabled(boolean enabled) {
        this.utilitiesEnabled = enabled;
    }

    public boolean isSearchToolEnabled() {
        return searchToolEnabled;
    }

    public void setSearchToolEnabled(boolean enabled) {
        this.searchToolEnabled = enabled;
    }

    public boolean isInventoryHelperEnabled() {
        return inventoryHelperEnabled;
    }

    public void setInventoryHelperEnabled(boolean enabled) {
        this.inventoryHelperEnabled = enabled;
    }

    public int getSearchRange() {
        return searchRange;
    }

    public void setSearchRange(int range) {
        this.searchRange = Math.max(1, range);
    }

    public boolean isShortcutsEnabled() {
        return enableShortcuts;
    }

    public void setShortcutsEnabled(boolean enabled) {
        this.enableShortcuts = enabled;
    }
}
