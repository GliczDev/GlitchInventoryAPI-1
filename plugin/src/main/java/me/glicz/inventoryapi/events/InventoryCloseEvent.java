package me.glicz.inventoryapi.events;

import me.glicz.inventoryapi.inventories.GlitchInventory;
import org.bukkit.entity.Player;

public class InventoryCloseEvent extends InventoryEvent {

    public InventoryCloseEvent(Player player, GlitchInventory<?> inventory) {
        super(player, inventory);
    }
}
