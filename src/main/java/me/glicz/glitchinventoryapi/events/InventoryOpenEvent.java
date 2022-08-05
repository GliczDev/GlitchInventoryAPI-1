package me.glicz.glitchinventoryapi.events;

import lombok.Getter;
import me.glicz.glitchinventoryapi.types.GlitchInventory;
import org.bukkit.entity.Player;

public class InventoryOpenEvent {

    @Getter
    private final Player player;
    @Getter
    private final GlitchInventory<?> glitchInventory;

    public InventoryOpenEvent(Player player, GlitchInventory<?> glitchInventory) {
        this.player = player;
        this.glitchInventory = glitchInventory;
    }
}
