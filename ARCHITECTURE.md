# Atlas Mod - Architecture Guide

## Overview

This document describes the architectural design of the Atlas mod, a professional-grade Minecraft Fabric mod with support for UI/HUD improvements, developer tools, gameplay mechanics, and utilities.

## Directory Structure

The mod is organized into clear, logical layers:

```
src/main/java/io/julia0x/atlas/
├── AtlasMod.java              # Main mod entry point
├── AtlasConstants.java         # Global constants and defaults
│
├── core/                       # Core framework (reusable systems)
│   ├── api/                    # Public API interfaces
│   │   ├── event/             # Event system interfaces
│   │   ├── config/            # Configuration interfaces
│   │   ├── registry/          # Registry interfaces
│   │   ├── cache/             # Caching interfaces
│   │   └── handler/           # Handler interfaces
│   │
│   ├── impl/                   # Core implementations
│   │   ├── event/             # Event bus implementation
│   │   ├── config/            # JSON config manager
│   │   ├── registry/          # Registry implementation
│   │   ├── cache/             # Cache implementation
│   │   └── lifecycle/         # Lifecycle manager
│   │
│   └── utils/                  # Shared utilities
│       ├── logging/           # Logging infrastructure
│       ├── mixin/             # Mixin utilities
│       └── common/            # Common utilities
│
├── features/                   # Feature modules
│   ├── ui/                     # HUD and screen rendering
│   ├── developer/              # Developer tools and debugging
│   ├── gameplay/               # Gameplay mechanics
│   └── utilities/              # Utility tools
│
└── integration/                # Cross-cutting concerns
    ├── handlers/              # Event handlers (Input, Network, Storage, Update)
    ├── events/                # Integration events
    └── logging/               # Logging and monitoring
```

## Core Systems

### 1. Event System
**Location**: `core/api/event/`, `core/impl/event/`

- **SimpleEventBus**: Central event dispatcher with priority support
- **IEventListener**: Interface for event handlers
- **Event**: Base class for all events

**Key Features**:
- Priority-based dispatch (highest priority first)
- Thread-safe listener registration
- Error recovery and graceful degradation
- Cancellable events

**Usage Example**:
```java
eventBus.subscribe(MyEvent.class, (event) -> {
    // Handle event
}, 50); // Priority 50
```

### 2. Configuration System
**Location**: `core/impl/config/`

- **JsonConfigManager**: JSON-based configuration with hot-reload support
- **ConfigValidator**: Schema validation and type checking
- **IConfigurable**: Interface for objects that need configuration

**Key Features**:
- JSON file loading from `config/atlas/`
- Nested configuration support (e.g., `ui.hud.enabled`)
- Live reload capabilities
- Validation before applying changes

### 3. Registry System
**Location**: `core/impl/registry/`

- **SimpleRegistry<T>**: Generic type-safe registry
- **RegistryManager**: Manages multiple registries
- **IRegistry**: Interface for registry implementations

**Key Features**:
- Type-safe registration and lookup
- Listener notifications on changes
- Immutability options
- Thread-safe operations

### 4. Cache Management
**Location**: `core/impl/cache/`

- **MemoryCacheManager**: In-memory cache with TTL support
- **CacheEntry**: Individual cache entries with metadata
- **CacheEvictionPolicy**: LRU and TTL-based eviction

**Key Features**:
- Automatic expiration (TTL)
- LRU eviction when full
- Hit/miss statistics
- Size-configurable

### 5. Handler System
**Location**: `integration/handlers/`

- **InputHandler**: Keyboard and mouse input
- **NetworkHandler**: Network packet handling
- **StorageHandler**: File I/O and persistence
- **UpdateHandler**: Tick-based updates

**Key Features**:
- Lifecycle management (init, shutdown)
- Priority-based initialization order
- State tracking and error handling
- Consistent interface for all handlers

### 6. Utilities
**Location**: `core/utils/`

- **MixinUtil**: Mixin-specific helpers
- **ReflectionUtil**: Safe reflection operations
- **StringUtil**: String manipulation and formatting
- **ValidationUtil**: Input validation
- **CollectionUtil**: Collection operations
- **LoggerFactory**: Consistent logger creation

## Feature Modules

Each feature module follows the same consistent structure:

```
features/<feature>/
├── api/              # Public interfaces
├── impl/             # Implementations
├── events/           # Custom events
├── config/           # Configuration
└── mixins/           # Minecraft bytecode modifications
```

### Feature Modules Included

1. **UI/HUD Module** (`features/ui/`)
   - HUD element rendering and management
   - Screen handling
   - Custom UI components

2. **Developer Tools** (`features/developer/`)
   - Debug utilities
   - Command system
   - Performance monitoring

3. **Gameplay Mechanics** (`features/gameplay/`)
   - Custom game mechanics
   - Entity interactions
   - Status effects

4. **Utilities** (`features/utilities/`)
   - Helper tools
   - Search functionality
   - Inventory management

## Dependency Injection

The mod uses constructor-based dependency injection without a framework:

```java
public class MyManager {
    private final SimpleEventBus eventBus;
    private final JsonConfigManager configManager;
    
    public MyManager(SimpleEventBus eventBus, JsonConfigManager configManager) {
        this.eventBus = eventBus;
        this.configManager = configManager;
    }
}
```

## Lifecycle

### Initialization Flow

1. **AtlasMod.onInitializeClient()** - Mod entry point
2. **LifecycleManager.initialize()** - Core initialization
3. **JsonConfigManager.loadConfig()** - Load configuration files
4. **Handler initialization** - Sorted by priority
5. **Feature initialization** - Each feature initializes independently
6. **Event bus ready** - System ready to handle events

### Shutdown Flow

1. **Handler shutdown** - Reverse order of initialization
2. **Resource cleanup** - Close files, release memory
3. **Event bus disposal** - Stop accepting new events

## Event Flow

```
1. Mixin intercepts Minecraft event
2. Mixin creates Atlas event object (e.g., HudRenderEvent)
3. Event posted to SimpleEventBus
4. EventBus finds all subscribers
5. Subscribers called in priority order (highest first)
6. Listeners can modify state, cancel event, or log data
```

## Design Patterns Used

| Pattern | Usage | Example |
|---------|-------|---------|
| **Singleton** | One instance per system | EventBus, ConfigManager |
| **Observer** | Event listeners, config changes | Event subscribers |
| **Factory** | Create managers | RegistryManager.getOrCreate() |
| **Strategy** | Cache eviction policies | LRU, TTL-based |
| **Adapter** | Minecraft API abstraction | IHudElement wraps rendering |
| **Dependency Injection** | Loose coupling | Constructor parameters |
| **Builder** | Complex object creation | Event creation |

## Configuration Structure

Configuration files in `config/atlas/`:

```json
{
  "core": {
    "debug": false,
    "logLevel": "INFO"
  },
  "ui": {
    "hudEnabled": true,
    "hudScale": 1,
    "animationsEnabled": true
  },
  "developer": {
    "devMode": false,
    "logEvents": true
  },
  "gameplay": {
    "customMechanicsEnabled": true,
    "difficultyMultiplier": 1.0
  },
  "utilities": {
    "searchRange": 128
  }
}
```

## Best Practices

### When Adding New Features

1. **Create API Interface** - Define public contract in `api/` package
2. **Implement Feature** - Put implementation in `impl/` package
3. **Create Events** - Custom events in `events/` package
4. **Add Configuration** - Config class for feature settings
5. **Register with Managers** - Register in appropriate registry/manager
6. **Add Mixins** - If Minecraft modification needed in `mixins/` package
7. **Document** - JavaDoc all public APIs

### Threading Considerations

- **Main Thread Only**: Rendering, UI updates
- **Network Thread**: Packet handling (use handlers)
- **Async Safe**: Registry operations, config reads
- **Synchronization**: Use ConcurrentHashMap for shared data

### Error Handling

- **Never silent failures** - Always log errors
- **Graceful degradation** - Feature fails, mod continues
- **User-friendly messages** - Clear error descriptions
- **Recovery attempts** - Retry failed operations when safe

## Testing Architecture

While comprehensive tests aren't included, the architecture supports testing:

```java
// Example unit test structure
class MyManagerTest {
    private SimpleEventBus eventBus = new SimpleEventBus();
    private MyManager manager = new MyManager(eventBus);
    
    @Test
    void testInitialization() {
        assertTrue(manager.initialize());
    }
}
```

## Performance Considerations

- **Caching** - Expensive computations cached with TTL
- **Event Priorities** - Critical listeners run first
- **Lazy Loading** - Features load only when needed
- **Memory Pooling** - Reuse objects where possible
- **Profiling** - PerformanceMonitor for metrics

## Security

- **No Reflection** - Except in ReflectionUtil with safety checks
- **Input Validation** - All external input validated
- **File Access** - Restricted to mod directories
- **Network Safety** - Custom protocol for network comms

## Future Extensions

The architecture supports:
- Adding new feature modules
- Custom event types
- New handler types
- Configuration hot-reload
- Plugin system (with IRegistry)
- Advanced caching strategies
- Distributed event handling
- Persistent data stores

## Troubleshooting

### Common Issues

1. **Event not firing** - Check subscription and event class
2. **Config not loading** - Verify JSON format and file location
3. **Mixin not injecting** - Check class names and mixins.json
4. **Handler not initialized** - Check priority and initialization order
5. **Memory leak** - Check cache TTL settings and listener unregistration

## Support

For questions about the architecture, refer to specific component documentation or code comments. All public APIs are fully JavaDoc'd.
