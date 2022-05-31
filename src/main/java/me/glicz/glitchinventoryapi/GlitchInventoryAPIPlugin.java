package me.glicz.glitchinventoryapi;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class GlitchInventoryAPIPlugin extends JavaPlugin implements Listener {

    private static GlitchInventoryAPI glitchInventoryAPI;

    @Override
    public void onEnable() {
        getLogger().info("Successfully enabled!");
        glitchInventoryAPI = new GlitchInventoryAPI(this);
        glitchInventoryAPI.registerListeners();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Successfully disabled!");
        glitchInventoryAPI.unregisterListeners();
    }
}
