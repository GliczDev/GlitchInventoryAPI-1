package me.glicz.inventoryapi.plugin;

import me.glicz.inventoryapi.GlitchInventoryAPI;
import me.glicz.inventoryapi.nms.NMSInitializer;
import org.bukkit.plugin.java.JavaPlugin;

public class GlitchInventoryAPIPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        ConfigLoader.load(this);
        if (!GlitchInventoryAPI.load(this))
            return;
        getLogger().info("Successfully enabled for " + NMSInitializer.getVersion() + "!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Successfully disabled!");
    }
}
