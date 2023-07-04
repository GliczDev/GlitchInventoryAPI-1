package me.glicz.inventoryapi.inventory;

import me.glicz.inventoryapi.GlitchInventoryAPI;
import me.glicz.inventoryapi.event.Listener;
import me.glicz.inventoryapi.event.merchant.InventoryTradeSelectEvent;
import me.glicz.inventoryapi.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.function.Consumer;

public class MerchantInventory extends GlitchInventory<MerchantInventory> {

    private final List<MerchantRecipe> recipeList = new ArrayList<>();
    private final Map<Player, MerchantRecipe> selectedRecipes = new HashMap<>();
    private final List<Listener<InventoryTradeSelectEvent>> tradeSelectListeners = new ArrayList<>();

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

    @Unmodifiable
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
    public MerchantInventory open(Player player, boolean closeCurrent) {
        super.open(player, closeCurrent);
        setSelectedRecipe(player, 0);
        if (getSelectedRecipe(player) != null)
            runTradeSelectListeners(new InventoryTradeSelectEvent(player, this, 0));
        return this;
    }

    public MerchantInventory addTradeSelectListener(Consumer<InventoryTradeSelectEvent> action) {
        return addTradeSelectListener(action, true);
    }

    public MerchantInventory addTradeSelectListener(Consumer<InventoryTradeSelectEvent> action, boolean sync) {
        tradeSelectListeners.add(new Listener<>(action, sync));
        return this;
    }

    public MerchantInventory runTradeSelectListeners(InventoryTradeSelectEvent event) {
        tradeSelectListeners.forEach(listener -> listener.run(event));
        return this;
    }
}
