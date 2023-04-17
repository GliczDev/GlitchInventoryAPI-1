package me.glicz.inventoryapi;

import lombok.Getter;
import me.glicz.inventoryapi.inventories.handler.InventoryEventHandler;
import me.glicz.inventoryapi.inventories.handler.InventoryEventHandlerImpl;
import me.glicz.inventoryapi.listeners.JoinQuitListener;
import me.glicz.inventoryapi.nms.NMS;
import me.glicz.inventoryapi.nms.NMSInitializer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class GlitchInventoryAPI {

    @Getter
    private static NMS nms;
    @Getter
    private static JavaPlugin plugin;

    public GlitchInventoryAPI(@NotNull JavaPlugin plugin) {
        if (GlitchInventoryAPI.plugin != null)
            throw new RuntimeException("GlitchInventoryAPI instance is already created by " + GlitchInventoryAPI.plugin.getName());
        GlitchInventoryAPI.plugin = plugin;
    }

    public void load() {
        load(new InventoryEventHandlerImpl());
    }

    public void load(@NotNull InventoryEventHandler listener) {
        nms = NMSInitializer.initialize(plugin);
        InventoryEventHandler.set(listener);
        Bukkit.getPluginManager().registerEvents(new JoinQuitListener(), plugin);
    }
}
