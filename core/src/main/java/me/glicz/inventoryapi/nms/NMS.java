package me.glicz.inventoryapi.nms;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public interface NMS {

    void registerListener(JavaPlugin plugin, Player player);

    void unregisterListener(JavaPlugin plugin, Player player);

    int getNextInventoryID(Player player);

    void openInventory(int id, Player player, InventoryType inventoryType, Component title);

    void openInventory(int id, Player player, int rows, Component title);

    void setProperty(int id, Player player, InventoryView.Property property, int value);

    void setItems(int id, Player player, List<ItemStack> items);

    void setItem(int id, int slot, Player player, ItemStack item);

    void setRecipes(int id, Player player, List<MerchantRecipe> recipeList);

    void closeInventory(int id, Player player);
}
