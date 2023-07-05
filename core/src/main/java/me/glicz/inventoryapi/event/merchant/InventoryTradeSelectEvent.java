package me.glicz.inventoryapi.event.merchant;

import lombok.Getter;
import lombok.Setter;
import me.glicz.inventoryapi.event.InventoryEvent;
import me.glicz.inventoryapi.inventory.MerchantInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.MerchantRecipe;

@Getter
public class InventoryTradeSelectEvent extends InventoryEvent implements Cancellable {

    private final MerchantRecipe recipe;
    private final int recipeIndex;
    @Setter
    private boolean cancelled;

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