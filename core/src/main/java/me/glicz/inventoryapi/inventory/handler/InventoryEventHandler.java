package me.glicz.inventoryapi.inventory.handler;

import me.glicz.inventoryapi.inventory.ClickType;
import org.bukkit.entity.Player;

public abstract class InventoryEventHandler {
    private static InventoryEventHandler instance;

    public static void set(InventoryEventHandler instance) {
        if (InventoryEventHandler.instance != null)
            throw new RuntimeException("PacketListener's instance is already set");
        InventoryEventHandler.instance = instance;
    }

    public static InventoryEventHandler get() {
        if (instance != null)
            return instance;
        throw new NullPointerException("PacketListener's instance is not set");
    }

    public abstract void handleClick(Player player, int inventoryId, ClickType clickType, int slot);

    public abstract void handleClose(Player player, int inventoryId);

    public abstract void handleItemRename(Player player, String name);

    public abstract void handleSelectTrade(Player player, int recipe);
}
