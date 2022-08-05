package me.glicz.glitchinventoryapi;

import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class GlitchInventoryAPIPlugin extends JavaPlugin {

    private static GlitchInventoryAPI glitchInventoryAPI;

    @Override
    public void onEnable() {
        getLogger().info("Successfully enabled!");
        glitchInventoryAPI = new GlitchInventoryAPI(this);
        glitchInventoryAPI.initialize();
    }

    @Override
    public void onDisable() {
        getLogger().info("Successfully disabled!");
        glitchInventoryAPI.unInitialize();
    }
}
