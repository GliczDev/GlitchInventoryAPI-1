package me.glicz.glitchinventoryapi.types;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public record SlotClickEvent(@Getter Player player, @Getter ItemStack itemStack, @Getter int slot,
                             @Getter GlitchInventory glitchInventory, @Getter ClickType clickType) {}
