package me.glicz.glitchinventoryapi.events;

import lombok.Getter;
import lombok.experimental.Accessors;
import me.glicz.glitchinventoryapi.types.GlitchInventory;
import org.bukkit.entity.Player;

public class InventoryPageChangeEvent {

    @Getter
    private final Player player;
    @Getter
    private final GlitchInventory<?> glitchInventory;
    @Getter
    @Accessors(fluent = true)
    private final boolean hasNext, hasPrevious;
    @Getter
    private final int page;

    public InventoryPageChangeEvent(Player player, GlitchInventory<?> glitchInventory, boolean hasNext, boolean hasPrevious, int page) {
        this.player = player;
        this.glitchInventory = glitchInventory;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
        this.page = page;
    }
}
