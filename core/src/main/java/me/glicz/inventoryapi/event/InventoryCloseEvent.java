package me.glicz.inventoryapi.event;

import me.glicz.inventoryapi.inventory.GlitchInventory;
import org.bukkit.entity.Player;

public class InventoryCloseEvent extends InventoryEvent {
    public InventoryCloseEvent(Player player, GlitchInventory<?> inventory) {
        super(player, inventory);
    }
}