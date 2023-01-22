package me.glicz.glitchinventoryapi.events;

import lombok.Getter;
import me.glicz.glitchinventoryapi.inventories.GlitchInventory;
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
