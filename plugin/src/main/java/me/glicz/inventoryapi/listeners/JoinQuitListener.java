package me.glicz.inventoryapi.listeners;

import me.glicz.inventoryapi.GlitchInventoryAPI;
import me.glicz.inventoryapi.inventories.GlitchInventory;
import me.glicz.inventoryapi.itembuilders.ItemBuilder;
import me.glicz.inventoryapi.titles.Title;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.MerchantRecipe;

public class JoinQuitListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        GlitchInventoryAPI.getNms().registerListener(e.getPlayer());
        MerchantRecipe recipe = new MerchantRecipe(ItemBuilder.of(Material.STICK).asItemStack(), Integer.MAX_VALUE);
        recipe.addIngredient(ItemBuilder.of(Material.STONE).setName("testowo").asItemStack());
        GlitchInventory.merchant()
                .addRecipe(recipe)
                .addRecipe(recipe)
                .setTradeSelectAction(event -> {
                    if (event.getRecipeIndex() == 0)
                        event.getInventory().setTitle(event.getPlayer(), Title.simple(Component.empty()));
                    else
                        event.getInventory().setTitle(event.getPlayer(), Title.simple(
                                Component.text("<shift:-98><glyph:test_1><shift:-1><glyph:test_2>")));
                })
                .addSlotClickAction(2, event -> event.getPlayer().sendMessage("test123"))
                .open(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        GlitchInventoryAPI.getNms().unregisterListener(e.getPlayer());
    }
}
