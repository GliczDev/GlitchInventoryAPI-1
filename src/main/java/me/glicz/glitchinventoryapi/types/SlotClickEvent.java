package me.glicz.glitchinventoryapi.types;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SlotClickEvent {

    private final Player player;
    private final ItemStack itemStack;
    private final int slot;
    private final GlitchInventory glitchInventory;
    private final ClickType clickType;

    public SlotClickEvent(Player player, ItemStack itemStack, int slot, GlitchInventory glitchInventory, ClickType clickType) {
        this.player = player;
        this.itemStack = itemStack;
        this.slot = slot;
        this.glitchInventory = glitchInventory;
        this.clickType = clickType;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public int getSlot() {
        return slot;
    }

    public GlitchInventory getGlitchInventory() {
        return glitchInventory;
    }

    public ClickType getClickType() {
        return clickType;
    }
}
