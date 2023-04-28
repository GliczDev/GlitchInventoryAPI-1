package me.glicz.inventoryapi;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.glicz.inventoryapi.inventories.handler.InventoryEventHandler;
import me.glicz.inventoryapi.inventories.handler.InventoryEventHandlerImpl;
import me.glicz.inventoryapi.listeners.JoinQuitListener;
import me.glicz.inventoryapi.nms.NMS;
import me.glicz.inventoryapi.nms.NMSInitializer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.NONE)
public class GlitchInventoryAPI {

    @Getter
    @Setter
    private static GlitchInventoryAPIConfig config = new GlitchInventoryAPIConfig();
    @Getter
    private static NMS nms;
    @Getter
    private static JavaPlugin plugin;

    public static boolean load(@NotNull JavaPlugin plugin) {
        if (GlitchInventoryAPI.plugin != null)
            throw new RuntimeException("GlitchInventoryAPI instance is already created by " + GlitchInventoryAPI.plugin.getName());
        return load(plugin, new InventoryEventHandlerImpl());
    }

    public static boolean load(@NotNull JavaPlugin plugin, @NotNull InventoryEventHandler listener) {
        GlitchInventoryAPI.plugin = plugin;
        nms = NMSInitializer.initialize(plugin);
        if (!plugin.isEnabled())
            return false;
        InventoryEventHandler.set(listener);
        Bukkit.getPluginManager().registerEvents(new JoinQuitListener(), plugin);
        return true;
    }
}
