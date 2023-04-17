package me.glicz.inventoryapi.inventories;

import lombok.Setter;
import lombok.experimental.Accessors;
import me.glicz.inventoryapi.GlitchInventoryAPI;
import me.glicz.inventoryapi.events.merchant.InventoryTradeSelectEvent;
import me.glicz.inventoryapi.titles.Title;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.MerchantRecipe;

import java.util.*;
import java.util.function.Consumer;

public class MerchantInventory extends GlitchInventory<MerchantInventory> {

    private final List<MerchantRecipe> recipeList = new ArrayList<>();
    private final Map<Player, MerchantRecipe> selectedRecipes = new HashMap<>();
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

    public MerchantInventory setSelectedRecipe(Player player, int recipeIndex) {
        if (getRecipe(recipeIndex) == null)
            selectedRecipes.remove(player);
        else
            selectedRecipes.put(player, getRecipe(recipeIndex));
        return this;
    }

    public MerchantRecipe getSelectedRecipe(Player player) {
        return selectedRecipes.get(player);
    }

    public MerchantInventory sendRecipes(Player player) {
        GlitchInventoryAPI.getNms().setRecipes(getId(player), player, recipeList);
        return this;
    }

    @Override
    public MerchantInventory sendInventory(Player player, Title title) {
        super.sendInventory(player, title);
        return sendRecipes(player);
    }

    @Override
    public OpenResult<MerchantInventory> open(Player player, boolean closeCurrent) {
        OpenResult<MerchantInventory> result = super.open(player, closeCurrent);
        if (result.opened()) {
            setSelectedRecipe(player, 0);
            if (getSelectedRecipe(player) != null)
                executeTradeSelectAction(new InventoryTradeSelectEvent(player, this, 0));
        }
        return result;
    }

    public void executeTradeSelectAction(InventoryTradeSelectEvent event) {
        if (tradeSelectAction == null)
            return;
        tradeSelectAction.accept(event);
    }
}
