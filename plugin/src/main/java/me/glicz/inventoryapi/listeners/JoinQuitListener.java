package me.glicz.inventoryapi.listeners;

import me.glicz.inventoryapi.GlitchInventoryAPI;
import me.glicz.inventoryapi.inventories.GlitchInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        GlitchInventoryAPI.getNms().registerListener(GlitchInventoryAPI.getPlugin(), e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        GlitchInventoryAPI.getNms().unregisterListener(GlitchInventoryAPI.getPlugin(), e.getPlayer());
        e.getPlayer().getPersistentDataContainer().remove(GlitchInventory.OPENED_INVENTORY_KEY);
    }
}
