package io.julia0x.atlas.integration.events;

import io.julia0x.atlas.core.api.event.Event;

/**
 * Base class for mod load events.
 */
public abstract class LoadEvent extends Event {
    public static class Init extends LoadEvent {
        /**
         * Fired when the mod is being initialized.
         */
    }

    public static class PostInit extends LoadEvent {
        /**
         * Fired after the mod has been fully initialized.
         */
    }

    public static class Complete extends LoadEvent {
        /**
         * Fired when mod loading is complete and the game is ready.
         */
    }

    public static class Unload extends LoadEvent {
        /**
         * Fired when the mod is being unloaded.
         */
    }
}
