package me.glicz.inventoryapi.events.merchant;

import lombok.Getter;
import me.glicz.inventoryapi.events.InventoryEvent;
import me.glicz.inventoryapi.inventories.GlitchInventory;
import me.glicz.inventoryapi.inventories.MerchantInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MerchantRecipe;

@Getter
public class InventoryTradeSelectEvent extends InventoryEvent {

    private final MerchantRecipe recipe;
    private final int recipeIndex;

    public InventoryTradeSelectEvent(Player player, GlitchInventory<?> inventory, int recipeIndex) {
        super(player, inventory);
        if (!(inventory instanceof MerchantInventory merchantInventory))
            throw new IllegalArgumentException("Inventory can be only a MerchantInventory");
        this.recipe = merchantInventory.getRecipe(recipeIndex);
        this.recipeIndex = recipeIndex;
    }

    @Override
    public MerchantInventory getInventory() {
        return (MerchantInventory) super.getInventory();
    }
}
