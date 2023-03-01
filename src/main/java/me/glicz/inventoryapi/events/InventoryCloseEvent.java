package me.glicz.inventoryapi.events;

import lombok.Getter;
import me.glicz.inventoryapi.inventories.GlitchInventory;
import org.bukkit.entity.Player;

public class InventoryCloseEvent {

    @Getter
    private final Player player;
    @Getter
    private final GlitchInventory<?> glitchInventory;

    public InventoryCloseEvent(Player player, GlitchInventory<?> glitchInventory) {
        this.player = player;
        this.glitchInventory = glitchInventory;
    }
}
