package me.glicz.inventoryapi.inventories.handler;

import me.glicz.inventoryapi.inventories.ClickType;
import org.bukkit.entity.Player;

public abstract class InventoryEventHandler {

    private static InventoryEventHandler instance;

    public static void set(InventoryEventHandler instance) {
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
}
