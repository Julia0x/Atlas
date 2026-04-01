package io.julia0x.atlas.features.developer.api;

/**
 * Interface for developer debugging tools.
 */
public interface IDebugTool {
    /**
     * Gets the tool identifier.
     *
     * @return The tool ID
     */
    String getId();

    /**
     * Gets the tool display name.
     *
     * @return The tool name
     */
    String getName();

    /**
     * Checks if the tool is enabled.
     *
     * @return true if enabled, false otherwise
     */
    boolean isEnabled();

    /**
     * Toggles the tool on/off.
     */
    void toggle();

    /**
     * Executes the tool action.
     *
     * @return Result message
     */
    String execute();

    /**
     * Gets tool information.
     *
     * @return Description of what the tool does
     */
    String getDescription();
}
