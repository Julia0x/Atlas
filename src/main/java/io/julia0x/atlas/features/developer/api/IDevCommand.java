package io.julia0x.atlas.features.developer.api;

/**
 * Interface for developer commands.
 */
public interface IDevCommand {
    /**
     * Gets the command name.
     *
     * @return The command name
     */
    String getName();

    /**
     * Gets the command syntax/usage.
     *
     * @return Usage help text
     */
    String getSyntax();

    /**
     * Gets the command description.
     *
     * @return What the command does
     */
    String getDescription();

    /**
     * Executes the command.
     *
     * @param args The command arguments
     * @return Result/status message
     */
    String execute(String[] args);

    /**
     * Gets command aliases.
     *
     * @return Array of alternative command names
     */
    String[] getAliases();
}
