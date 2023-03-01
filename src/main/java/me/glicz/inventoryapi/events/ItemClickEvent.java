package me.glicz.inventoryapi.events;

import lombok.Getter;
import me.glicz.inventoryapi.inventories.GlitchInventory;
import me.glicz.inventoryapi.types.ClickType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemClickEvent {

    @Getter
    private final Player player;
    @Getter
    private final ItemStack itemStack;
    @Getter
    private final int slot;
    @Getter
    private final GlitchInventory<?> glitchInventory;
    @Getter
    private final ClickType clickType;

    public ItemClickEvent(Player player, ItemStack itemStack, int slot, GlitchInventory<?> glitchInventory, ClickType clickType) {
        this.player = player;
        this.itemStack = itemStack;
        this.slot = slot;
        this.glitchInventory = glitchInventory;
        this.clickType = clickType;
    }
}
