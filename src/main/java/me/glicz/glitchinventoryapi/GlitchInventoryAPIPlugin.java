package me.glicz.glitchinventoryapi;

import org.bukkit.plugin.java.JavaPlugin;

public class GlitchInventoryAPIPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Successfully enabled!");
        new GlitchInventoryAPI(this).registerListeners();
    }

    @Override
    public void onDisable() {
        getLogger().info("Successfully disabled!");
    }
}
