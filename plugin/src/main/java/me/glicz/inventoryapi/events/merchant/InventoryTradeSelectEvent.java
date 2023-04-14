package me.glicz.inventoryapi.events.merchant;

import lombok.Getter;
import me.glicz.inventoryapi.events.InventoryEvent;
import me.glicz.inventoryapi.inventories.MerchantInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MerchantRecipe;

@Getter
public class InventoryTradeSelectEvent extends InventoryEvent {

    private final MerchantRecipe recipe;
    private final int recipeIndex;

    public InventoryTradeSelectEvent(Player player, MerchantInventory inventory, int recipeIndex) {
        super(player, inventory);
        this.recipe = inventory.getRecipe(recipeIndex);
        this.recipeIndex = recipeIndex;
    }

    @Override
    public MerchantInventory getInventory() {
        return (MerchantInventory) super.getInventory();
    }
}
