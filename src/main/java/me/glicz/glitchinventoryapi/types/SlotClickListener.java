package me.glicz.glitchinventoryapi.types;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public abstract class SlotClickListener {
    public abstract void onSlotClick(Player player, ItemStack item, int slot, GlitchInventory inventory, boolean isLeftClick, boolean isRightClick);
}
