package me.glicz.glitchinventoryapi.types;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class SlotClickListener {
    public abstract void onSlotClick(Player player, ItemStack item, GlitchInventory inventory);
}
