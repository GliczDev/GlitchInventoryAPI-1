package me.glicz.glitchinventoryapi;

import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class GlitchInventoryAPIPlugin extends JavaPlugin {

    private static GlitchInventoryAPI glitchInventoryAPI;

    @Override
    public void onEnable() {
        glitchInventoryAPI = new GlitchInventoryAPI(this);
        glitchInventoryAPI.initialize();
        getLogger().info("Successfully enabled!");
    }

    @Override
    public void onDisable() {
        glitchInventoryAPI.unInitialize();
        getLogger().info("Successfully disabled!");
    }
}
