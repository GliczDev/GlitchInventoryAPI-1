# GlitchInventoryAPI
**This API requires ProtocolLib!**

## Shading
If you want to shade this API into your plugin, you have to do something like this
```java
private static GlitchInventoryAPI glitchInventoryAPI;

@Override
public void onEnable() {
        glitchInventoryAPI = new GlitchInventoryAPI(this);
        glitchInventoryAPI.registerListeners();
}

@Override
public void onDisable() {
        glitchInventoryAPI.unregisterListeners();
}
```

## Usage
Usage is very simple, here is an example
```java
new GlitchInventory(3, "Title")
        .fill(FillPattern.Alternately, new ItemStack(Material.BLACK_STAINED_GLASS_PANE), new ItemStack(Material.GRAY_STAINED_GLASS_PANE))
        .setSlot(10, new ItemStack(Material.STONE), e -> e.getPlayer().sendMessage("Simple GUI created with GlitchInventoryAPI!"));
```
