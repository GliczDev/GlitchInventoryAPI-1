package me.glicz.inventoryapi.event;

import lombok.Getter;
import me.glicz.inventoryapi.inventory.GlitchInventory;
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
