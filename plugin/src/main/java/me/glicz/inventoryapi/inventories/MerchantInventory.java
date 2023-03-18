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
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class MerchantInventory extends GlitchInventory<MerchantInventory> {

    private final List<MerchantRecipe> recipeList = new ArrayList<>();
    @Getter
    @Setter
    private MerchantRecipe selectedRecipe;
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
        try {
            return recipeList.get(index);
        } catch (IndexOutOfBoundsException ignored) {
            return null;
        }
    }

    public List<MerchantRecipe> getRecipeList() {
        return Collections.unmodifiableList(recipeList);
    }

    public MerchantInventory sendRecipes(Player player) {
        GlitchInventoryAPI.getNms().setRecipes(getId(player), player, recipeList);
        return this;
    }

    @Override
    public MerchantInventory open(Player player, boolean closeCurrent) {
        super.open(player, closeCurrent);
        selectedRecipe = getRecipe(0);
        if (selectedRecipe != null)
            executeTradeSelectAction(new InventoryTradeSelectEvent(player, this, 0));
        return sendRecipes(player);
    }

    public void executeTradeSelectAction(InventoryTradeSelectEvent event) {
        if (tradeSelectAction == null)
            return;
        tradeSelectAction.accept(event);
    }
}
