package me.glicz.inventoryapi.inventories;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.glicz.inventoryapi.GlitchInventoryAPI;
import me.glicz.inventoryapi.events.merchant.InventoryTradeSelectEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MerchantInventory extends GlitchInventory<MerchantInventory> {

    @Getter
    private final List<MerchantRecipe> recipeList = new ArrayList<>();
    @Setter
    @Accessors(chain = true)
    private Consumer<InventoryTradeSelectEvent> tradeSelectAction;

    protected MerchantInventory() {
        super(InventoryType.MERCHANT);
    }

    public MerchantInventory addRecipe(MerchantRecipe recipe) {
        recipeList.add(recipe);
        return this;
    }

    public MerchantInventory removeRecipe(MerchantRecipe recipe) {
        recipeList.remove(recipe);
        return this;
    }

    public MerchantRecipe getRecipe(int index) {
        return recipeList.get(index);
    }

    public MerchantInventory sendRecipes(Player player) {
        GlitchInventoryAPI.getNms().setRecipes(getId(player), player, recipeList);
        return this;
    }

    @Override
    public MerchantInventory open(Player player, boolean closeCurrent) {
        super.open(player, closeCurrent);
        return sendRecipes(player);
    }

    public void executeTradeSelectAction(InventoryTradeSelectEvent event) {
        if (tradeSelectAction == null)
            return;
        tradeSelectAction.accept(event);
    }
}
