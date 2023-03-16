package me.glicz.inventoryapi.events;

import lombok.Getter;
import me.glicz.inventoryapi.inventories.ClickType;
import me.glicz.inventoryapi.inventories.GlitchInventory;
import me.glicz.inventoryapi.inventories.GuiItem;
import org.bukkit.entity.Player;

@Getter
public class InventoryClickEvent {

    private final Player player;
    private final GlitchInventory<?> inventory;
    private final ClickType clickType;
    private final int slot;
    private final GuiItem item;

    public InventoryClickEvent(Player player, GlitchInventory<?> inventory, ClickType clickType, int slot) {
        this.player = player;
        this.inventory = inventory;
        this.clickType = clickType;
        this.slot = slot;
        this.item = inventory.getItem(slot);
    }
}
