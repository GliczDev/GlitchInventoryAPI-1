package me.glicz.inventoryapi.inventory.handler;

import me.glicz.inventoryapi.GlitchInventoryAPI;
import me.glicz.inventoryapi.event.InventoryClickEvent;
import me.glicz.inventoryapi.event.anvil.InventoryInputChangeEvent;
import me.glicz.inventoryapi.event.merchant.InventoryTradeSelectEvent;
import me.glicz.inventoryapi.inventory.AnvilInventory;
import me.glicz.inventoryapi.inventory.ClickType;
import me.glicz.inventoryapi.inventory.GlitchInventory;
import me.glicz.inventoryapi.inventory.MerchantInventory;
import me.glicz.inventoryapi.itembuilder.ItemBuilder;
import org.bukkit.entity.Player;

public class InventoryEventHandlerImpl extends InventoryEventHandler {

    @Override
    public void handleClick(Player player, int inventoryId, ClickType clickType, int slot) {
        if (GlitchInventory.has(player)) {
            GlitchInventory<?> inventory = GlitchInventory.get(player);
            inventory.updateItems(player);
            player.updateInventory();
            if (inventory.getId(player) != inventoryId) return;
            InventoryClickEvent event = new InventoryClickEvent(player, inventory, clickType, slot);
            inventory.runSlotClickListeners(slot, event);
            inventory.getItem(player, slot).runClickListener(event);
        }
    }

    @Override
    public void handleClose(Player player, int inventoryId) {
        if (GlitchInventory.has(player)) {
            GlitchInventory<?> inventory = GlitchInventory.get(player);
            if (GlitchInventoryAPI.getConfig().verifyInventoryIdOnClose() && inventory.getId(player) != inventoryId)
                return;
            inventory.silentClose(player);
        }
    }

    @Override
    public void handleItemRename(Player player, String name) {
        if (GlitchInventory.get(player) instanceof AnvilInventory inventory) {
            if (inventory.isShouldInsertItem())
                inventory.setItem(2, ItemBuilder.of(inventory.getItem(0).getItemStack()).setName(name).asGuiItem());
            inventory.runInputChangeListeners(new InventoryInputChangeEvent(player, inventory, name));
        }
    }

    @Override
    public void handleSelectTrade(Player player, int recipe) {
        if (GlitchInventory.get(player) instanceof MerchantInventory inventory) {
            InventoryTradeSelectEvent event = new InventoryTradeSelectEvent(player, inventory, recipe);
            inventory.runTradeSelectListeners(event);
            if (!event.isCancelled())
                inventory.setSelectedRecipe(player, recipe);
        }
    }
}