# Atlas Mod - API Reference

## Core Framework APIs

### Event System

#### SimpleEventBus
Central event dispatcher with priority-based listener management.

```java
// Create bus
SimpleEventBus eventBus = new SimpleEventBus();

// Subscribe to events
eventBus.subscribe(MyEvent.class, listener);
eventBus.subscribe(MyEvent.class, listener, priority);

// Unsubscribe
eventBus.unsubscribe(MyEvent.class, listener);

// Post events
eventBus.post(new MyEvent());

// Get listener count
int count = eventBus.getListenerCount(MyEvent.class);

// Clear all
eventBus.clear();

// Dispose (cleanup)
eventBus.dispose();
```

#### IEventListener
Interface for event handlers.

```java
public class MyListener implements IEventListener {
    @Override
    public void onEvent(Event event) {
        // Handle event
    }
    
    @Override
    public int getPriority() {
        return 50; // 0 = normal, >0 = higher priority
    }
}
```

#### Event Base Class
All events extend this.

```java
public class MyEvent extends Event {
    // Events automatically track creation time
    
    // Check if cancelled
    event.isCancelled();
    
    // Cancel event
    event.setCancelled(true);
    
    // Get creation time
    event.getTimestamp();
}
```

### Configuration System

#### JsonConfigManager
Manages JSON-based configuration files.

```java
// Create manager
JsonConfigManager configManager = new JsonConfigManager();

// Load from disk
configManager.loadConfig();

// Save to disk
configManager.saveConfig();

// Get values
String value = configManager.get("key", "default");
int value = configManager.get("ui.scale", 1);

// Set values
configManager.set("key", newValue);

// Check existence
boolean exists = configManager.has("key");

// Reload from disk
configManager.reload();

// Register change listener
configManager.registerListener(changeListener);
configManager.unregisterListener(changeListener);
```

#### IConfigurable
Interface for configuration-aware objects.

```java
public class MyConfig implements IConfigurable {
    @Override
    public String getConfigKey() {
        return "myfeature";
    }
    
    @Override
    public void onConfigReloaded() {
        // Re-load config when it changes
    }
}
```

### Registry System

#### SimpleRegistry<T>
Generic type-safe registry implementation.

```java
// Create registry
SimpleRegistry<MyObject> registry = new SimpleRegistry<>("objects");

// Register object
boolean success = registry.register("id", object);

// Unregister object
boolean removed = registry.unregister("id");

// Get object
Optional<MyObject> obj = registry.get("id");

// Check existence
boolean exists = registry.contains("id");

// Get all IDs
Set<String> ids = registry.getIds();

// Get all objects
Collection<MyObject> all = registry.getAll();

// Get count
int size = registry.size();

// Clear all
registry.clear();

// Listen for changes
registry.registerListener(listener);
registry.unregisterListener(listener);

// Get registry name
String name = registry.getName();
```

#### RegistryManager
Manages multiple registries.

```java
// Create manager
RegistryManager manager = new RegistryManager();

// Get or create registry
SimpleRegistry<MyObject> registry = manager.getOrCreate("objects");

// Get existing registry
Optional<SimpleRegistry<MyObject>> registry = manager.get("objects");

// Check existence
boolean exists = manager.contains("objects");

// Remove registry
boolean removed = manager.remove("objects");

// Clear all registries
manager.clear();

// Get statistics
manager.logStatistics();
```

### Cache System

#### MemoryCacheManager
In-memory cache with TTL and LRU eviction.

```java
// Create cache (max 1000 entries)
MemoryCacheManager cache = new MemoryCacheManager(1000);

// Cache without expiration
cache.put("key", value);

// Cache with 1 hour TTL
cache.put("key", value, 3600000);

// Get value
Optional<Object> value = cache.get("key");

// Get with type safety
Optional<String> value = cache.get("key", String.class);

// Check existence
boolean exists = cache.contains("key");

// Remove from cache
boolean removed = cache.remove("key");

// Clear all
cache.clear();

// Get size
int size = cache.size();

// Cleanup expired entries
cache.cleanup();

// Get statistics
CacheStats stats = cache.getStats();
stats.getHits();
stats.getMisses();
stats.getEvictions();
stats.getHitRate();

// Log statistics
cache.logStatistics();
```

### Lifecycle Management

#### LifecycleManager
Manages mod initialization and shutdown.

```java
// Create manager
LifecycleManager lifecycle = new LifecycleManager();

// Register handlers
lifecycle.registerHandler(handler);

// Initialize mod
boolean success = lifecycle.initialize();

// Shutdown mod
lifecycle.shutdown();

// Get event bus
SimpleEventBus eventBus = lifecycle.getEventBus();

// Get config manager
JsonConfigManager configManager = lifecycle.getConfigManager();

// Check initialization status
boolean initialized = lifecycle.isInitialized();

// Get handler count
int count = lifecycle.getHandlerCount();
```

### Utilities

#### LoggerFactory
Creates configured loggers.

```java
// Get logger for class
Logger logger = LoggerFactory.getLogger(MyClass.class);

// Get logger with name
Logger logger = LoggerFactory.getLogger("MyLogger");

// Log methods
LoggerFactory.info(logger, "message");
LoggerFactory.warn(logger, "message");
LoggerFactory.debug(logger, "message");
LoggerFactory.error(logger, "message", exception);
```

#### ValidationUtil
Input validation utilities.

```java
// Check if empty
ValidationUtil.isEmpty(string);
ValidationUtil.isBlank(string);

// Require non-null
ValidationUtil.requireNonNull(obj, "message");

// Require non-empty
ValidationUtil.requireNonEmpty(string, "message");
ValidationUtil.requireNonEmpty(collection, "message");

// Require in range
ValidationUtil.requireInRange(value, min, max, "message");

// Require pattern match
ValidationUtil.requireMatches(string, pattern, "message");

// Require instance
ValidationUtil.requireInstance(obj, Type.class, "message");

// Check map values
ValidationUtil.allNonNull(map);
```

#### StringUtil
String manipulation utilities.

```java
// Case conversion
StringUtil.toPascalCase("my_variable");    // MyVariable
StringUtil.toCamelCase("my_variable");     // myVariable
StringUtil.toSnakeCase("MyVariable");      // my_variable

// String operations
StringUtil.repeat("x", 5);                 // xxxxx
StringUtil.padLeft("10", 5, '0');          // 00010
StringUtil.padRight("x", 5, '-');          // x----
StringUtil.truncate("hello", 3);           // hel
StringUtil.truncateWithEllipsis("hello", 5); // he...

// Character checks
StringUtil.hasUppercase("Hello");          // true
StringUtil.hasLowercase("HELLO");          // false
StringUtil.hasDigits("test123");           // true

// Processing
StringUtil.removeWhitespace("a b c");      // abc
```

#### CollectionUtil
Collection operations.

```java
// Checks
CollectionUtil.isEmpty(collection);
CollectionUtil.isEmpty(map);

// Creation
CollectionUtil.listOf(e1, e2, e3);
CollectionUtil.setOf(e1, e2, e3);
CollectionUtil.mapOf("key1", val1, "key2", val2);

// Operations
CollectionUtil.filter(list, predicate);
CollectionUtil.findFirst(list, predicate);
CollectionUtil.anyMatch(list, predicate);
CollectionUtil.allMatch(list, predicate);
CollectionUtil.count(list, predicate);
CollectionUtil.randomElement(list);

// Navigation
CollectionUtil.first(collection);
CollectionUtil.last(list);

// Utilities
CollectionUtil.concat(list1, list2, list3);
CollectionUtil.distinct(list);
```

#### MixinUtil
Mixin-related utilities.

```java
// Thread checks
MixinUtil.isRenderThread();
MixinUtil.isNetworkThread();

// Mixin helpers
MixinUtil.getMixinName(className);
MixinUtil.validateClass(className);
MixinUtil.getMethodSignature(methodName, argTypes);

// Logging
MixinUtil.logMixinInjection(mixinName, targetClass, method);
MixinUtil.logMixinInjectionError(mixinName, targetClass, method, error);
```

#### ReflectionUtil
Safe reflection operations.

```java
// Field operations
Field field = ReflectionUtil.getField(clazz, fieldName);
Object value = ReflectionUtil.getFieldValue(obj, fieldName);
ReflectionUtil.setFieldValue(obj, fieldName, newValue);

// Method operations
Method method = ReflectionUtil.getMethod(clazz, methodName, paramTypes);
Object result = ReflectionUtil.invokeMethod(obj, methodName, args);

// Constructor operations
Constructor<?> ctor = ReflectionUtil.getConstructor(clazz, paramTypes);
MyClass instance = ReflectionUtil.newInstance(MyClass.class, args);

// Type checks
ReflectionUtil.isAssignableFrom(parent, child);

// Introspection
Method[] methods = ReflectionUtil.getDeclaredMethods(clazz);
Method method = ReflectionUtil.findMethod(clazz, predicate);
```

## Feature Module APIs

### UI/HUD Module

#### IHudElement
Interface for HUD elements.

```java
// Lifecycle
element.isVisible();
element.setVisible(boolean);
element.isEnabled();
element.setEnabled(boolean);

// Positioning
element.getX();
element.getY();
element.setPosition(x, y);

// Sizing
element.getWidth();
element.getHeight();

// Rendering
element.render(partialTicks);
element.update();

// Input
element.handleMouseInput(x, y, button);
element.handleKeyInput(key, scanCode, mods);

// Properties
element.getId();
element.getName();
element.getZOrder();
```

#### HudManager
Manages HUD elements.

```java
// Create manager
HudManager manager = new HudManager();

// Initialize
manager.initialize();

// Register element
manager.registerElement(element);
manager.unregisterElement(elementId);

// Get element
IHudElement element = manager.getElement(elementId);

// Get sorted elements
List<IHudElement> sorted = manager.getElementsSorted();

// Rendering
manager.renderAll(partialTicks);
manager.updateAll();

// Input handling
manager.handleMouseInput(x, y, button);
manager.handleKeyInput(key, scanCode, mods);

// Control
manager.setEnabled(true);
manager.isEnabled();
manager.getElementCount();
manager.clear();
```

### Developer Tools Module

#### IDebugTool
Interface for debug tools.

```java
tool.getId();
tool.getName();
tool.getDescription();
tool.isEnabled();
tool.toggle();
tool.execute();  // Returns result message
```

#### IDevCommand
Interface for developer commands.

```java
command.getName();
command.getSyntax();
command.getDescription();
command.getAliases();
command.execute(args);  // Returns result message
```

### Gameplay Module

#### IMechanics
Interface for gameplay mechanics.

```java
mechanic.getId();
mechanic.getName();
mechanic.getDescription();
mechanic.isEnabled();
mechanic.setEnabled(boolean);
mechanic.initialize();
mechanic.cleanup();
```

### Utilities Module

#### IUtility
Interface for utility tools.

```java
util.getId();
util.getName();
util.getDescription();
util.isAvailable();
util.isEnabled();
util.setEnabled(boolean);
util.getConfigKey();
util.initialize();
util.cleanup();
```

## Integration APIs

### Handlers

#### InputHandler
Handles keyboard and mouse input.

```java
handler.initialize();
handler.shutdown();
handler.isEnabled();
handler.getName();     // "InputHandler"
handler.getPriority(); // 50

handler.onKeyPress(key, mods);
handler.onKeyRelease(key, mods);
handler.onMousePress(button, x, y);
handler.onMouseRelease(button, x, y);
handler.onMouseMove(x, y);
handler.onMouseScroll(x, y, deltaY);
```

#### NetworkHandler
Handles network communication.

```java
handler.initialize();
handler.shutdown();
handler.isEnabled();
handler.onServerConnect();
handler.onServerDisconnect();
handler.isConnected();
handler.sendPacket(channel, data);
handler.onPacketReceived(channel, data);
```

#### StorageHandler
Handles file I/O.

```java
handler.initialize();
handler.shutdown();
handler.isEnabled();

handler.saveData(filename, data);
handler.loadData(filename);
handler.saveText(filename, content);
handler.loadText(filename);
handler.fileExists(filename);
handler.deleteFile(filename);
handler.getStoragePath();
```

#### UpdateHandler
Handles periodic updates.

```java
handler.initialize();
handler.shutdown();
handler.isEnabled();

handler.onTick();
handler.onUpdate();
handler.getTickCount();
handler.setUpdateFrequency(ticks);
handler.getUpdateFrequency();
handler.getTimeSinceLastUpdate();
handler.forceUpdate();
```

### Events

#### Core Events
- `ClientTickEvent` - Every client tick
- `ServerTickEvent` - Every server tick
- `LoadEvent.Init` - Mod initializing
- `LoadEvent.PostInit` - Mod post-init
- `LoadEvent.Complete` - Mod ready
- `LoadEvent.Unload` - Mod unloading

#### Network Events
- `NetworkEvent.Connect(serverName)` - Server connected
- `NetworkEvent.Disconnect(reason)` - Server disconnected
- `NetworkEvent.PacketReceive(channel, data)` - Packet received
- `NetworkEvent.PacketSend(channel, data)` - Packet sent

#### UI Events
- `HudRenderEvent(partialTicks, width, height)` - Before HUD render
- `ScreenOpenEvent(screenName, width, height)` - Screen opened

### Logging & Monitoring

#### EventLogger
Logs event dispatches.

```java
logger.setEnabled(true);
logger.setLogCancelled(true);
logger.onEvent(event);
logger.getPriority();  // Integer.MIN_VALUE
```

#### PerformanceMonitor
Monitors performance.

```java
monitor.setEnabled(true);

monitor.startTimer("operation");
// ... do work ...
monitor.stopTimer("operation");

monitor.record("metric", nanoSeconds);

monitor.getAverageTimeMs("operation");
monitor.getCount("operation");

monitor.clear();
monitor.logStatistics();
monitor.getMetricsReport();
```

## Constants

### AtlasConstants
Global constants and defaults.

```java
// Metadata
AtlasConstants.MOD_ID          // "atlas"
AtlasConstants.MOD_NAME        // "Atlas"
AtlasConstants.MOD_VERSION     // "1.0.0"

// Configuration
AtlasConstants.CONFIG_DIR              // "config/atlas"
AtlasConstants.CORE_CONFIG             // "core.json"
AtlasConstants.UI_CONFIG               // "ui.json"
AtlasConstants.DEVELOPER_CONFIG        // "developer.json"

// Priorities
AtlasConstants.PRIORITY_HIGHEST        // 100
AtlasConstants.PRIORITY_HIGH           // 50
AtlasConstants.PRIORITY_NORMAL         // 0
AtlasConstants.PRIORITY_LOW            // -50
AtlasConstants.PRIORITY_LOWEST         // -100

// Cache
AtlasConstants.CACHE_DEFAULT_TTL       // 3600000 (1 hour)
AtlasConstants.CACHE_MAX_SIZE          // 1000

// Features
AtlasConstants.FEATURE_UI              // "ui"
AtlasConstants.FEATURE_DEVELOPER       // "developer"
AtlasConstants.FEATURE_GAMEPLAY        // "gameplay"
AtlasConstants.FEATURE_UTILITIES       // "utilities"

// Environment
AtlasConstants.DEBUG                   // From system property
AtlasConstants.DEV_MODE                // From system property
```

## Quick Reference Table

| Component | Package | Purpose |
|-----------|---------|---------|
| SimpleEventBus | core.impl.event | Event dispatch |
| JsonConfigManager | core.impl.config | Configuration |
| SimpleRegistry | core.impl.registry | Object registry |
| MemoryCacheManager | core.impl.cache | Data caching |
| LifecycleManager | core.impl.lifecycle | Mod lifecycle |
| LoggerFactory | core.utils.logging | Logging |
| ValidationUtil | core.utils.common | Input validation |
| StringUtil | core.utils.common | String ops |
| CollectionUtil | core.utils.common | Collection ops |
| MixinUtil | core.utils.mixin | Mixin helpers |
| ReflectionUtil | core.utils.mixin | Safe reflection |
| HudManager | features.ui.impl | HUD management |
| InputHandler | integration.handlers | Input events |
| NetworkHandler | integration.handlers | Network events |
| StorageHandler | integration.handlers | File I/O |
| UpdateHandler | integration.handlers | Tick updates |
| EventLogger | integration.logging | Event logging |
| PerformanceMonitor | integration.logging | Performance |

## Error Handling

Common exceptions and recovery:

```java
try {
    configManager.loadConfig();
} catch (IOException e) {
    LOGGER.severe("Config load failed: " + e.getMessage());
    // Use defaults and continue
}

// Safe reflection
Object value = ReflectionUtil.getFieldValue(obj, "field");
if (value == null) {
    LOGGER.warning("Field access failed, using default");
}

// Safe registry lookup
Optional<MyObject> obj = registry.get("id");
if (obj.isEmpty()) {
    LOGGER.fine("Object not found, creating new");
}

// Safe cache access
Optional<String> cached = cache.get("key", String.class);
String value = cached.orElseGet(() -> {
    LOGGER.fine("Cache miss, computing value");
    return computeExpensiveValue();
});
```

## Threading Notes

- Most operations are thread-safe via ConcurrentHashMap
- Event posting must happen from appropriate thread
- Minecraft code (rendering) must run on render thread
- Use thread checks: `MixinUtil.isRenderThread()`

For more details, see DEVELOPMENT.md.
