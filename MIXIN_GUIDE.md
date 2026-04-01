# Atlas Mixin Guide

## Overview

This guide provides comprehensive documentation for all mixins in the Atlas mod. Mixins are used to inject code into Minecraft's runtime classes without modifying them directly.

## Mixin Configuration

All mixins are configured in `src/main/resources/atlas.mixins.json`. The configuration is production-grade with:

- **Compatibility Level**: JAVA_21
- **Reference Map**: `atlas.refmap.json` (for obfuscation mapping)
- **Verbose Logging**: Disabled for performance
- **Injector Settings**: Default require 1 injection point with 5-shift tolerance

## Mixin Organization

Mixins are organized by target system:

```
io/julia0x/atlas/mixins/
├── client/           # Client-side mixins
├── server/           # Server-side mixins
├── world/            # World/Level mixins
├── entity/           # Entity-related mixins
├── network/          # Network packet mixins
├── ui/               # UI/HUD mixins
└── performance/      # Performance monitoring
```

## Client-Side Mixins

### MixinMinecraftClient
**Target**: `net.minecraft.client.MinecraftClient`

Hooks into client initialization and tick events:
- `tick()` - Dispatches `ClientTickEvent` at HEAD
- `<init>()` - Initializes client systems at TAIL

**Event**: `ClientTickEvent`
**Handlers**: `LifecycleManager`, `InputHandler`

```java
@Inject(method = "tick", at = @At(value = "HEAD"))
private void onClientTick(CallbackInfo ci) { ... }
```

**Usage**:
```java
eventBus.listen(ClientTickEvent.class, event -> {
    // Handle client tick
});
```

---

### MixinGameRenderer
**Target**: `net.minecraft.client.render.GameRenderer`

Provides HUD rendering hooks:
- `render()` - Monitors HUD rendering performance and dispatches `HudRenderEvent`
- Frame time tracking for performance monitoring

**Event**: `HudRenderEvent`
**Handlers**: `HudManager`, `PerformanceMonitor`

```java
@Inject(method = "render", at = @At(value = "INVOKE", 
    target = "Lnet/minecraft/client/gui/hud/InGameHud;render(...)"))
private void onHudRender(...) { ... }
```

**Usage**:
```java
eventBus.listen(HudRenderEvent.class, event -> {
    float tickDelta = event.getTickDelta();
    // Render custom HUD elements
});
```

---

### MixinScreen
**Target**: `net.minecraft.client.gui.screen.Screen`

Dispatches screen lifecycle events:
- `init()` - Fires `ScreenOpenEvent` when screen opens
- `close()` - Fires close event when screen closes

**Event**: `ScreenOpenEvent`

```java
@Inject(method = "init", at = @At("HEAD"))
private void onScreenOpen(CallbackInfo ci) { ... }
```

**Usage**:
```java
eventBus.listen(ScreenOpenEvent.class, event -> {
    Screen screen = event.getScreen();
    if (event.isClosing()) {
        // Handle screen close
    }
});
```

---

### MixinInputUtil
**Target**: `net.minecraft.client.input.Input`

Intercepts input handling:
- `tick()` - Allows cancellation of input processing

**Handler**: `InputHandler`

```java
@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
private void onInputTick(boolean slowDown, CallbackInfo ci) { ... }
```

---

## Server-Side Mixins

### MixinServer
**Target**: `net.minecraft.server.MinecraftServer`

Server lifecycle management:
- First tick - Initializes server systems
- `shutdown()` - Graceful shutdown with cleanup

**Handlers**: `LifecycleManager`

---

### MixinServerTickEvents
**Target**: `net.minecraft.server.MinecraftServer`

Dispatches server tick events:
- `tick()` - Posts `ServerTickEvent` at HEAD

**Event**: `ServerTickEvent`

```java
eventBus.listen(ServerTickEvent.class, event -> {
    // Handle server tick
});
```

---

## World Mixins

### MixinLevel
**Target**: `net.minecraft.world.World`

World state management:
- `close()` - Clears world-specific cache on unload

**Handler**: `MemoryCacheManager`

**Cache Categories**: `world`

---

### MixinBlockEntity
**Target**: `net.minecraft.block.entity.BlockEntity`

Block entity caching:
- `tick()` - Caches block entity state (5-minute TTL)

**Handler**: `MemoryCacheManager`

**Cache Key Pattern**: `blockentity_{blockPosAsLong}`

---

## Entity Mixins

### MixinEntity
**Target**: `net.minecraft.entity.Entity`

Entity state management:
- `tick()` - Caches entity state (1-minute TTL)

**Handler**: `MemoryCacheManager`

**Cache Key Pattern**: `entity_{entityId}`

---

### MixinLivingEntity
**Target**: `net.minecraft.entity.LivingEntity`

Living entity hooks:
- `setHealth()` - Detects health changes

**Usage**: Extend for damage tracking

---

### MixinPlayer
**Target**: `net.minecraft.entity.player.PlayerEntity`

Player-specific caching:
- `tick()` - Caches player data (5-minute TTL)

**Handler**: `MemoryCacheManager`

**Cache Key Pattern**: `player_{uuid}`

---

## Network Mixins

### MixinClientConnection
**Target**: `net.minecraft.network.ClientConnection`

Client-side network monitoring:
- `send()` - Monitors outbound packets
- `handlePacket()` - Monitors inbound packets

**Event**: `NetworkEvent` with `Direction.OUTBOUND/INBOUND`

```java
eventBus.listen(NetworkEvent.class, event -> {
    Packet<?> packet = event.getPacket();
    if (event.getDirection() == NetworkEvent.Direction.OUTBOUND) {
        // Handle outbound packet
    }
});
```

---

### MixinServerPlayNetworkHandler
**Target**: `net.minecraft.server.network.ServerPlayNetworkHandler`

Server-side packet handling:
- `onPacket()` - Monitors server-received packets

**Event**: `NetworkEvent` with `Direction.INBOUND`

---

## UI Mixins

### MixinHudElement
**Target**: `net.minecraft.client.gui.hud.InGameHud`

Custom HUD element rendering:
- `render()` - Renders all custom HUD elements

**Handler**: `HudManager`

```java
// Register custom HUD elements
HudManager.getInstance().register(new CustomHudElement());
```

---

### MixinScreenManager
**Target**: `net.minecraft.client.gui.screen.Screen`

Screen-level UI management:
- `render()` - Provides UI render hook

---

## Performance Monitoring

### MixinRenderSystem
**Target**: `net.minecraft.client.render.RenderSystem`

Frame time and render performance:
- `flipFrame()` - Tracks frame time in milliseconds

**Handler**: `PerformanceMonitor`

```java
PerformanceMonitor monitor = PerformanceMonitor.getInstance();
double avgFrameTime = monitor.getAverageFrameTime();
```

---

## Best Practices

### 1. Injection Points

Always be specific with injection points:

```java
// Good - specific target method
@Inject(method = "render", at = @At(value = "INVOKE", 
    target = "Lnet/minecraft/client/gui/hud/InGameHud;render(...)"))

// Avoid - too broad
@Inject(method = "render", at = @At("HEAD"))
```

### 2. Error Handling

Always wrap mixin code in try-catch:

```java
try {
    // Mixin code
} catch (Exception e) {
    LOGGER.error("[Atlas] Error in mixin", e);
}
```

### 3. Logging

Use context-aware logging:

```java
private static final Logger LOGGER = LoggerFactory.getLogger("Atlas/MixinName");
LOGGER.debug("[Atlas] Mixin operation completed");
```

### 4. Performance

Keep mixin operations lightweight:
- Avoid heavy computations in tick events
- Use caching for expensive operations
- Monitor with `PerformanceMonitor`

### 5. Cancellation

Only use `cancellable = true` when needed:

```java
@Inject(method = "process", at = @At("HEAD"), cancellable = true)
private void onProcess(CallbackInfo ci) {
    if (shouldCancel()) {
        ci.cancel();
    }
}
```

---

## Adding New Mixins

To add a new mixin:

1. **Create the mixin class** in the appropriate package under `mixins/`
2. **Add to configuration** in `atlas.mixins.json`
3. **Register handler** in `LifecycleManager`
4. **Document** in this guide
5. **Test** on both client and server

### Example Template

```java
package io.julia0x.atlas.mixins.{category};

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin for [TargetClass].
 * [Description of what this mixin does]
 */
@Mixin(TargetClass.class)
public abstract class Mixin[TargetClass] {

    private static final Logger LOGGER = LoggerFactory.getLogger("Atlas/Mixin[TargetClass]");

    @Inject(
        method = "[methodName]",
        at = @At("[injectionPoint]")
    )
    private void on[Event](CallbackInfo ci) {
        try {
            // Implementation
        } catch (Exception e) {
            LOGGER.error("[Atlas] Error in mixin", e);
        }
    }
}
```

---

## Debugging Mixins

### Enable Verbose Logging

Edit `atlas.mixins.json`:
```json
{
    "verbose": true
}
```

### Check Mixin Application

Look for logs at startup:
```
[Mixin] [Atlas] MixinMinecraftClient loaded
```

### Use Mixin Debugging

Enable MixinBootstrap debugging:
```properties
# In run configuration or environment
mixin.debug=true
```

---

## Troubleshooting

### Mixin Not Being Applied

1. Check JSON configuration syntax
2. Verify class and method names match Minecraft version
3. Ensure injection point exists at specified location
4. Check for obfuscation mismatches

### Injection Point Failures

- **"Could not find injection point"**: Method signature or location doesn't exist
- **"No compatible injection point found"**: Too restrictive injection point
- **"Injection already applied"**: Duplicate mixin configuration

### Performance Issues

- Profile with `PerformanceMonitor`
- Move expensive logic out of tick methods
- Use caching strategically
- Consider splitting mixins across methods

---

## Performance Metrics

### Expected Performance Impact

- **Client Tick**: < 1ms per tick
- **HUD Render**: < 2ms per frame
- **Network Monitoring**: < 0.5ms per packet
- **Cache Operations**: < 0.1ms per lookup

Monitor with:
```java
PerformanceMonitor monitor = PerformanceMonitor.getInstance();
monitor.printStats();
```

---

## References

- [Mixin Documentation](https://docs.spongepowered.org/stable/en/plugin/internals/mixin/index.html)
- [Injection Points](https://docs.spongepowered.org/stable/en/plugin/internals/mixin/injectors.html)
- [Fabric Wiki](https://fabricmc.net/)
- Atlas API Reference (API_REFERENCE.md)
