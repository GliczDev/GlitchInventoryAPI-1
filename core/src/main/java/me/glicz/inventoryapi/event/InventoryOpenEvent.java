package me.glicz.inventoryapi.event;

import me.glicz.inventoryapi.inventory.GlitchInventory;
import org.bukkit.entity.Player;

public class InventoryOpenEvent extends InventoryEvent {

    public InventoryOpenEvent(Player player, GlitchInventory<?> inventory) {
        super(player, inventory);
    }
}