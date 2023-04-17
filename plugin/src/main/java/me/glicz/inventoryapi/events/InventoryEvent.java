package me.glicz.inventoryapi.events;

import lombok.Getter;
import me.glicz.inventoryapi.inventories.GlitchInventory;
import org.bukkit.entity.Player;

@Getter
public class InventoryEvent {

    private final Player player;
    private final GlitchInventory<?> inventory;

    public InventoryEvent(Player player, GlitchInventory<?> inventory) {
        this.player = player;
        this.inventory = inventory;
    }
}
