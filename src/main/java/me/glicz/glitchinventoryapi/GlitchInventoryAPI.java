package me.glicz.glitchinventoryapi;

import com.comphenix.protocol.ProtocolLibrary;
import me.glicz.glitchinventoryapi.listeners.CloseWindowPacketListener;
import me.glicz.glitchinventoryapi.listeners.WindowClickPacketListener;
import org.bukkit.plugin.java.JavaPlugin;

public class GlitchInventoryAPI{

    private static JavaPlugin plugin;

    public GlitchInventoryAPI(JavaPlugin plugin) {
        GlitchInventoryAPI.plugin = plugin;
    }

    public void registerListeners() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new WindowClickPacketListener());
        ProtocolLibrary.getProtocolManager().addPacketListener(new CloseWindowPacketListener());
    }

    public void unregisterListeners() {
        ProtocolLibrary.getProtocolManager().removePacketListeners(plugin);
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }
}
