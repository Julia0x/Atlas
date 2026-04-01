package io.julia0x.atlas.features.developer.config;

import io.julia0x.atlas.core.api.config.IConfigurable;

/**
 * Configuration for Developer Tools features.
 */
public class DeveloperConfig implements IConfigurable {
    private boolean devModeEnabled = false;
    private boolean logEventsEnabled = true;
    private boolean showPerformanceMetrics = false;
    private boolean enableDebugTools = true;
    private int debugVerbosity = 1; // 0=off, 1=normal, 2=verbose

    @Override
    public String getConfigKey() {
        return "developer";
    }

    @Override
    public void onConfigReloaded() {
        // Reload configuration from ConfigManager
    }

    public boolean isDevModeEnabled() {
        return devModeEnabled;
    }

    public void setDevModeEnabled(boolean enabled) {
        this.devModeEnabled = enabled;
    }

    public boolean isLogEventsEnabled() {
        return logEventsEnabled;
    }

    public void setLogEventsEnabled(boolean enabled) {
        this.logEventsEnabled = enabled;
    }

    public boolean isShowPerformanceMetrics() {
        return showPerformanceMetrics;
    }

    public void setShowPerformanceMetrics(boolean show) {
        this.showPerformanceMetrics = show;
    }

    public boolean isDebugToolsEnabled() {
        return enableDebugTools;
    }

    public void setDebugToolsEnabled(boolean enabled) {
        this.enableDebugTools = enabled;
    }

    public int getDebugVerbosity() {
        return debugVerbosity;
    }

    public void setDebugVerbosity(int verbosity) {
        this.debugVerbosity = Math.max(0, Math.min(2, verbosity));
    }
}
