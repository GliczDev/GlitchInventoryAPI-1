package me.glicz.glitchinventoryapi;

import com.comphenix.protocol.ProtocolLibrary;
import me.glicz.glitchinventoryapi.listeners.CloseWindowPacketListener;
import me.glicz.glitchinventoryapi.listeners.WindowClickPacketListener;
import me.glicz.glitchinventoryapi.utils.NMSUtil;
import org.bukkit.plugin.java.JavaPlugin;

public class GlitchInventoryAPI {

    private static JavaPlugin plugin;
    private static NMSUtil nmsUtil;

    public GlitchInventoryAPI(JavaPlugin plugin) {
        GlitchInventoryAPI.plugin = plugin;
        GlitchInventoryAPI.nmsUtil = new NMSUtil();
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

    public static NMSUtil getNMSUtil() {
        return nmsUtil;
    }
}
