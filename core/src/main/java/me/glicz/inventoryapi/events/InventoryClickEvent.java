package me.glicz.inventoryapi.events;

import lombok.Getter;
import me.glicz.inventoryapi.inventories.ClickType;
import me.glicz.inventoryapi.inventories.GlitchInventory;
import me.glicz.inventoryapi.inventories.GuiItem;
import org.bukkit.entity.Player;

@Getter
public class InventoryClickEvent extends InventoryEvent {
    private final ClickType clickType;
    private final int slot;
    private final GuiItem item;

    public InventoryClickEvent(Player player, GlitchInventory<?> inventory, ClickType clickType, int slot) {
        super(player, inventory);
        this.clickType = clickType;
        this.slot = slot;
        this.item = inventory.getItem(slot);
    }
}
