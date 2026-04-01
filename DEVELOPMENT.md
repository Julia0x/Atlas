# Atlas Mod - Development Guide

## Quick Start

### Setting Up Development Environment

1. **Prerequisites**
   - Java 21 or higher
   - Gradle 8+
   - Git

2. **Build the Mod**
   ```bash
   ./gradlew build
   ```

3. **Run in Development**
   ```bash
   ./gradlew runClient
   ```

4. **Generate IDE Configuration**
   ```bash
   ./gradlew genSources
   ```

## Adding New Features

### Step 1: Create API Interfaces

Define what your feature does:

```java
// features/myfeature/api/IMyFeature.java
public interface IMyFeature {
    String getId();
    boolean isEnabled();
    void setEnabled(boolean enabled);
}
```

### Step 2: Create Implementation

Implement the interface:

```java
// features/myfeature/impl/MyFeatureImpl.java
public class MyFeatureImpl implements IMyFeature {
    private boolean enabled = false;
    
    @Override
    public String getId() {
        return "myfeature";
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
```

### Step 3: Create Configuration Class

```java
// features/myfeature/config/MyFeatureConfig.java
public class MyFeatureConfig implements IConfigurable {
    private boolean enabled = true;
    private int updateFrequency = 20;
    
    @Override
    public String getConfigKey() {
        return "myfeature";
    }
    
    @Override
    public void onConfigReloaded() {
        // Reload settings
    }
}
```

### Step 4: Create Custom Events

```java
// features/myfeature/events/MyFeatureEvent.java
public class MyFeatureEvent extends Event {
    private final String data;
    
    public MyFeatureEvent(String data) {
        this.data = data;
    }
    
    public String getData() {
        return data;
    }
}
```

### Step 5: Register with Core Systems

```java
// In your mod initialization code
LifecycleManager lifecycleManager = new LifecycleManager();
SimpleEventBus eventBus = lifecycleManager.getEventBus();

// Subscribe to events
eventBus.subscribe(MyFeatureEvent.class, event -> {
    System.out.println("Event: " + event.getData());
});

// Initialize
lifecycleManager.initialize();
```

## Working with the Event System

### Subscribe to Events

```java
// High priority - runs before others
eventBus.subscribe(ClientTickEvent.class, new IEventListener() {
    @Override
    public void onEvent(Event event) {
        ClientTickEvent e = (ClientTickEvent) event;
        System.out.println("Tick: " + e.getTickCount());
    }
    
    @Override
    public int getPriority() {
        return 100; // High priority
    }
});

// Or as a lambda (default priority 0)
eventBus.subscribe(ClientTickEvent.class, event -> {
    // Handle event
});
```

### Post Custom Events

```java
// Create and post event
MyFeatureEvent event = new MyFeatureEvent("test data");
eventBus.post(event);

// Check if event was cancelled
if (event.isCancelled()) {
    System.out.println("Event was cancelled by a listener");
}
```

### Cancel Events

```java
eventBus.subscribe(ScreenOpenEvent.class, event -> {
    if (event.getScreenName().contains("Admin")) {
        event.setCancelled(true); // Prevent screen from opening
    }
});
```

## Working with Configuration

### Load Configuration

```java
JsonConfigManager configManager = new JsonConfigManager();
configManager.loadConfig();

// Get values with defaults
boolean enabled = configManager.get("ui.hudEnabled", true);
int scale = configManager.get("ui.hudScale", 1);
```

### Update Configuration

```java
// Set a value
configManager.set("ui.hudScale", 2);

// Save to disk
configManager.saveConfig();

// Reload from disk
configManager.reload();
```

### Listen for Configuration Changes

```java
configManager.registerListener(new IConfigManager.IConfigChangeListener() {
    @Override
    public void onConfigChanged(String key, Object oldValue, Object newValue) {
        System.out.println("Config changed: " + key);
        System.out.println("  Old: " + oldValue);
        System.out.println("  New: " + newValue);
    }
});
```

## Working with Registries

### Create a Registry

```java
RegistryManager registryManager = new RegistryManager();

// Get or create a registry for your objects
SimpleRegistry<MyObject> registry = registryManager.getOrCreate("myobjects");
```

### Register Objects

```java
MyObject object = new MyObject();
boolean success = registry.register("myobject1", object);

if (success) {
    System.out.println("Object registered");
} else {
    System.out.println("Object ID already exists");
}
```

### Look Up Objects

```java
// Get optional
Optional<MyObject> obj = registry.get("myobject1");
if (obj.isPresent()) {
    obj.get().doSomething();
}

// Check existence
if (registry.contains("myobject1")) {
    System.out.println("Object exists");
}

// Get all IDs
Set<String> ids = registry.getIds();
for (String id : ids) {
    System.out.println("Registered: " + id);
}
```

### Listen for Registry Changes

```java
registry.registerListener(new IRegistry.IRegistryListener<MyObject>() {
    @Override
    public void onRegistered(String id, MyObject object) {
        System.out.println("Registered: " + id);
    }
    
    @Override
    public void onUnregistered(String id, MyObject object) {
        System.out.println("Unregistered: " + id);
    }
});
```

## Working with Cache

### Store Values

```java
MemoryCacheManager cache = new MemoryCacheManager(1000); // Max 1000 entries

// Cache without expiration
cache.put("key1", "value1");

// Cache with 1 hour TTL
cache.put("key2", "value2", 3600000);
```

### Retrieve Values

```java
// Get optional
Optional<Object> value = cache.get("key1");
if (value.isPresent()) {
    System.out.println("Found: " + value.get());
}

// Get with type
Optional<String> strValue = cache.get("key2", String.class);
if (strValue.isPresent()) {
    System.out.println("Found string: " + strValue.get());
}
```

### Monitor Cache Performance

```java
// Periodic cleanup (remove expired)
cache.cleanup();

// Get statistics
ICacheManager.CacheStats stats = cache.getStats();
System.out.println("Hits: " + stats.getHits());
System.out.println("Hit Rate: " + stats.getHitRate() + "%");
```

## Creating Handlers

### Extend IHandler

```java
public class MyCustomHandler implements IHandler {
    private boolean enabled = false;
    
    @Override
    public boolean initialize() {
        System.out.println("Initializing MyCustomHandler");
        enabled = true;
        return true;
    }
    
    @Override
    public void shutdown() {
        System.out.println("Shutting down MyCustomHandler");
        enabled = false;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    @Override
    public String getName() {
        return "MyCustomHandler";
    }
    
    @Override
    public int getPriority() {
        return 50; // Higher priority initializes first
    }
}
```

### Register Handler

```java
LifecycleManager lifecycleManager = new LifecycleManager();
lifecycleManager.registerHandler(new MyCustomHandler());
lifecycleManager.initialize();
```

## Creating Mixins

Mixins modify Minecraft's bytecode at runtime. Example mixin structure:

```java
// src/main/java/io/julia0x/atlas/features/ui/mixins/ScreenMixin.java
@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Inject(method = "init", at = @At("HEAD"))
    private void onScreenInit(CallbackInfo ci) {
        // Called when screen initializes
        System.out.println("Screen opened: " + this.getClass().getSimpleName());
    }
}
```

Register in `src/main/resources/atlas.mixins.json`:

```json
{
  "mixins": [
    "features.ui.mixins.ScreenMixin"
  ]
}
```

## Debugging

### Enable Detailed Logging

```java
LifecycleManager lifecycleManager = new LifecycleManager();

// Enable event logging
EventLogger eventLogger = new EventLogger();
eventLogger.setEnabled(true);
lifecycleManager.getEventBus().subscribe(Event.class, eventLogger);

// Enable performance monitoring
PerformanceMonitor monitor = new PerformanceMonitor();
monitor.setEnabled(true);

// Log metrics
monitor.startTimer("myoperation");
// ... do work ...
monitor.stopTimer("myoperation");
monitor.logStatistics();
```

### Inspect System State

```java
// Check handler status
System.out.println("Handlers: " + lifecycleManager.getHandlerCount());
System.out.println("Initialized: " + lifecycleManager.isInitialized());

// Check registries
registryManager.logStatistics();

// Check cache
cache.logStatistics();

// Check event listeners
int count = eventBus.getListenerCount(MyEvent.class);
System.out.println("Listeners for MyEvent: " + count);
```

## Common Patterns

### Feature Enable/Disable

```java
// In config
private boolean featureEnabled = true;

// In manager
public void setEnabled(boolean enabled) {
    if (this.featureEnabled == enabled) return;
    this.featureEnabled = enabled;
    
    if (enabled) {
        // Initialize feature
    } else {
        // Cleanup feature
    }
}
```

### Tick-Based Updates

```java
public void onTick() {
    if (!enabled) return;
    
    tickCount++;
    
    // Update every 20 ticks (once per second)
    if (tickCount % 20 == 0) {
        update();
        tickCount = 0;
    }
}
```

### Safe Configuration Access

```java
// Always use try-catch for config
try {
    int value = configManager.get("key", 0);
    // Use value
} catch (Exception e) {
    LOGGER.warning("Failed to read config: " + e.getMessage());
    // Use default
}
```

## Performance Tips

1. **Use Cache** - For expensive computations
2. **Batch Operations** - Process multiple items at once
3. **Lazy Initialization** - Load features only when needed
4. **Event Priorities** - Put critical work first
5. **Profile Code** - Use PerformanceMonitor to find bottlenecks
6. **Avoid Reflection** - Use direct method calls when possible
7. **Minimize Event Creation** - Reuse event objects

## Testing Locally

### Test Installation

1. Build mod: `./gradlew build`
2. Navigate to: `run/mods/`
3. Copy JAR to mods folder
4. Run: `./gradlew runClient`

### Test Configuration

1. Create `run/config/atlas/` directory
2. Add JSON config files
3. Launch game and verify settings load

### Test Events

1. Subscribe to test event
2. Trigger event from mixin
3. Verify listener receives event
4. Check event cancellation works

## Code Style

Follow these conventions:

- **Naming**: `camelCase` for variables, `PascalCase` for classes
- **Interfaces**: Prefix with `I` (e.g., `IHudElement`)
- **Implementations**: Use descriptive names (e.g., `SimpleRegistry`)
- **Documentation**: JavaDoc all public methods
- **Logging**: Use LoggerFactory consistently
- **Error Handling**: Always log exceptions with context

## Next Steps

1. Review the architecture documentation
2. Look at existing feature implementations
3. Create a simple test feature
4. Subscribe to events and test
5. Add configuration and reload
6. Create custom event
7. Register a handler
8. Build and test locally

For more details, see ARCHITECTURE.md.
