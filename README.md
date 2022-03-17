# GlitchInventoryAPI
**This API requires ProtocolLib!**

## Shading
If you want to shade this API into your plugin, you have to add this line in your `onEnable`
```java
new GlitchInventoryAPI(this).registerListeners();
```

## Usage
Usage is very simple, here is an example
```java
new GlitchInventory(3, player, "Title")
        .fill(FillPattern.Alternately, new ItemStack(Material.BLACK_STAINED_GLASS_PANE), new ItemStack(Material.GRAY_STAINED_GLASS_PANE))
        .setSlot(10, new ItemStack(Material.STONE), new SlotClickListener() {
            @Override
            public void onSlotClick(Player player, ItemStack item, GlitchInventory inventory) {
                player.sendMessage("Simple GUI created with GlitchInventoryAPI!");
            }
        });
```
