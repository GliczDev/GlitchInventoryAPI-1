package me.glicz.inventoryapi.events;

import me.glicz.inventoryapi.inventories.GlitchInventory;
import org.bukkit.entity.Player;

public class InventoryOpenEvent extends InventoryEvent {

    public InventoryOpenEvent(Player player, GlitchInventory<?> inventory) {
        super(player, inventory);
    }
}
