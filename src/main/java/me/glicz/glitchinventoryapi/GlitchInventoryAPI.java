package me.glicz.glitchinventoryapi;

import com.comphenix.protocol.ProtocolLibrary;
import me.glicz.glitchinventoryapi.listeners.CloseWindowPacketListener;
import me.glicz.glitchinventoryapi.listeners.WindowClickPacketListener;
import org.bukkit.plugin.java.JavaPlugin;

public class GlitchInventoryAPI extends JavaPlugin {

    private static GlitchInventoryAPI instance;

    public GlitchInventoryAPI() {
        instance = this;
    }

    @Override
    public void onEnable() {
        getLogger().info("Successfully enabled!");
        registerListeners();
    }

    @Override
    public void onDisable() {
        getLogger().info("Successfully disabled!");
    }

    public void registerListeners() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new WindowClickPacketListener());
        ProtocolLibrary.getProtocolManager().addPacketListener(new CloseWindowPacketListener());
    }

    public static GlitchInventoryAPI getInstance() {
        return instance;
    }
}
