# Atlas Mod - Quick Start Guide

## 5-Minute Overview

### What You Have

A professional-grade Minecraft Fabric mod framework with:
- Event-driven architecture
- Configuration system
- Registry management
- In-memory caching
- Four feature modules
- Cross-cutting handlers
- Comprehensive documentation

### Core Concepts (2 min)

1. **Events** - Things that happen (tick, input, network, etc.)
2. **Handlers** - Process specific events
3. **Features** - Groupings of related functionality
4. **Config** - Settings stored in JSON files
5. **Registry** - Manages collections of objects

### Getting Started (3 min)

```java
// Create the mod's core
LifecycleManager mod = new LifecycleManager();

// Get the systems
SimpleEventBus eventBus = mod.getEventBus();
JsonConfigManager config = mod.getConfigManager();

// Listen to events
eventBus.subscribe(ClientTickEvent.class, event -> {
    System.out.println("Tick: " + event.getTickCount());
});

// Initialize
mod.initialize();
```

## File Organization

```
src/main/java/io/julia0x/atlas/
├── core/          <- Framework systems
├── features/      <- Feature modules
├── integration/   <- Event handlers
└── AtlasConstants <- Global settings
```

## Core Systems at a Glance

### SimpleEventBus
- **What**: Event dispatcher
- **Use**: `eventBus.subscribe(Event.class, listener)`
- **Key Feature**: Priority-based dispatch

### JsonConfigManager
- **What**: Configuration loader
- **Use**: `config.get("key", defaultValue)`
- **Key Feature**: Hot-reload support

### SimpleRegistry<T>
- **What**: Object storage
- **Use**: `registry.register("id", object)`
- **Key Feature**: Type-safe generics

### MemoryCacheManager
- **What**: Data cache
- **Use**: `cache.put("key", value, ttl)`
- **Key Feature**: Automatic expiration

## Adding a New Feature

### 1. Create Interface (api/)
```java
public interface IMyFeature {
    boolean isEnabled();
    void setEnabled(boolean enabled);
}
```

### 2. Implement It (impl/)
```java
public class MyFeatureImpl implements IMyFeature {
    private boolean enabled = false;
    // ... implementation
}
```

### 3. Create Config
```java
public class MyFeatureConfig implements IConfigurable {
    @Override
    public String getConfigKey() { return "myfeature"; }
}
```

### 4. Create Event (optional)
```java
public class MyFeatureEvent extends Event {
    // ... event details
}
```

### 5. Register with Manager
```java
MyFeatureImpl feature = new MyFeatureImpl();
eventBus.subscribe(MyFeatureEvent.class, event -> {
    // Handle event
});
```

## Common Tasks

### Listen to Game Tick
```java
eventBus.subscribe(ClientTickEvent.class, event -> {
    if (event.getTickCount() % 20 == 0) {
        // Once per second
    }
});
```

### Get Configuration
```java
boolean enabled = config.get("ui.hudEnabled", true);
int scale = config.get("ui.scale", 1);
```

### Store Data
```java
cache.put("mykey", data, 3600000); // 1 hour
Optional<MyData> data = cache.get("mykey", MyData.class);
```

### Register Objects
```java
SimpleRegistry<MyObject> registry = new SimpleRegistry<>("objects");
registry.register("id", new MyObject());
MyObject obj = registry.get("id").orElse(null);
```

### Validate Input
```java
ValidationUtil.requireNonEmpty(name, "Name required");
ValidationUtil.requireInRange(value, 0, 100, "Value 0-100");
```

### Log Messages
```java
Logger logger = LoggerFactory.getLogger(MyClass.class);
logger.info("Something happened");
logger.warning("Warning: " + problem);
```

## File Locations

### Configuration
- **Path**: `config/atlas/`
- **Format**: JSON files
- **Reload**: Call `config.reload()`

### Mod Data
- **Path**: `mods/atlas/`
- **Use**: StorageHandler for save/load

### Logs
- **Location**: Console and Minecraft log
- **Prefix**: `[Atlas]` on all messages

## Priority Levels

Use these for event dispatch order:
```java
PRIORITY_HIGHEST  = 100   // First to process
PRIORITY_HIGH     = 50    // Early processing
PRIORITY_NORMAL   = 0     // Default
PRIORITY_LOW      = -50   // Late processing
PRIORITY_LOWEST   = -100  // Last to process
```

## Feature Modules Available

1. **UI/HUD** - Screen rendering and HUD elements
2. **Developer** - Debug tools and commands
3. **Gameplay** - Game mechanics and interactions
4. **Utilities** - Helper tools and utilities

Each has:
- API interface
- Implementation
- Events
- Configuration
- Mixin hooks

## Key Interfaces

| Interface | Purpose |
|-----------|---------|
| IEventListener | Handle events |
| IHandler | Lifecycle management |
| IHudElement | Render on screen |
| IDebugTool | Debug utility |
| IDevCommand | Chat command |
| IMechanics | Game mechanic |
| IUtility | Helper tool |
| IConfigurable | Config support |
| IRegistry | Object storage |

## Handlers

Four built-in handlers:
1. **InputHandler** - Mouse/keyboard
2. **NetworkHandler** - Packets
3. **StorageHandler** - File I/O
4. **UpdateHandler** - Ticks

All implement `IHandler` interface.

## Events

### Lifecycle
- `LoadEvent.Init` - Initializing
- `LoadEvent.PostInit` - Post-init
- `LoadEvent.Complete` - Ready
- `LoadEvent.Unload` - Shutting down

### Tick
- `ClientTickEvent` - Client tick
- `ServerTickEvent` - Server tick

### Network
- `NetworkEvent.Connect` - Connected
- `NetworkEvent.Disconnect` - Disconnected
- `NetworkEvent.PacketReceive` - Packet in
- `NetworkEvent.PacketSend` - Packet out

### UI
- `HudRenderEvent` - Before HUD render
- `ScreenOpenEvent` - Screen opened

## Utilities Cheat Sheet

### String Operations
```java
StringUtil.toCamelCase("my_var");      // myVar
StringUtil.toSnakeCase("MyVar");       // my_var
StringUtil.truncate("hello", 3);       // hel
```

### Collections
```java
CollectionUtil.filter(list, x -> x > 5);
CollectionUtil.findFirst(list, x -> x > 5);
CollectionUtil.distinct(list);
```

### Validation
```java
ValidationUtil.requireNonNull(obj, "Required");
ValidationUtil.requireInRange(val, 0, 100, "0-100");
ValidationUtil.requireNonEmpty(str, "Required");
```

### Reflection (Safe)
```java
Field field = ReflectionUtil.getField(clazz, "fieldName");
Object value = ReflectionUtil.getFieldValue(obj, "fieldName");
```

## Configuration Example

```json
{
  "ui": {
    "hudEnabled": true,
    "hudScale": 1,
    "animationsEnabled": true
  },
  "developer": {
    "devMode": false,
    "logEvents": true
  }
}
```

Access with: `config.get("ui.hudEnabled", true)`

## Documentation

**Full Details**:
- `ARCHITECTURE.md` - System design
- `DEVELOPMENT.md` - How to build
- `API_REFERENCE.md` - Method reference

**Quick Links**:
- This file - 5-minute overview
- IMPLEMENTATION_SUMMARY.md - What was built
- JavaDoc in code - Implementation details

## Troubleshooting

### Event not firing?
1. Check you subscribed to correct event class
2. Verify listener priority
3. Check if event is being posted

### Config not loading?
1. Verify JSON syntax
2. Check file location: `config/atlas/`
3. Call `config.loadConfig()` after creating file

### Handler not initializing?
1. Verify you called `lifecycle.registerHandler()`
2. Check handler.initialize() returned true
3. Check priority order

### Memory leak?
1. Unsubscribe listeners properly
2. Check cache TTL settings
3. Monitor with PerformanceMonitor

## Next Steps

1. **Read** ARCHITECTURE.md (15 min)
2. **Study** Code examples in DEVELOPMENT.md (30 min)
3. **Create** a simple test feature (1 hour)
4. **Review** API_REFERENCE.md for details (30 min)
5. **Start building** your mod features

## Resources

- **API Docs**: API_REFERENCE.md
- **Code Guide**: DEVELOPMENT.md
- **Architecture**: ARCHITECTURE.md
- **Summary**: IMPLEMENTATION_SUMMARY.md
- **Code**: Look at existing feature modules
- **Examples**: DEVELOPMENT.md has many code samples

## Key Takeaways

- **Event-Driven**: Use events for all communications
- **Configuration**: All settings in JSON, hot-reloadable
- **Type-Safe**: Use generics, no casting needed
- **Error-Friendly**: Always log errors, continue on failure
- **Extensible**: Add features without modifying core
- **Documented**: Everything has JavaDoc and examples
- **Tested**: Architecture supports unit testing

## Getting Help

1. Check JavaDoc on the class/method
2. Search DEVELOPMENT.md for examples
3. Look at existing feature implementations
4. Check API_REFERENCE.md for detailed signatures
5. Review ARCHITECTURE.md for design decisions

---

**Ready to build?** Start with Step 1 in "Adding a New Feature" section above!
