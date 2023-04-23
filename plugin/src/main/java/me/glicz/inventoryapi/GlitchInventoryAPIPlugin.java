package me.glicz.inventoryapi;

import me.glicz.inventoryapi.nms.NMSInitializer;
import org.bukkit.plugin.java.JavaPlugin;

public class GlitchInventoryAPIPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        if (!GlitchInventoryAPI.load(this))
            return;
        getLogger().info("Successfully enabled for " + NMSInitializer.getVersion() + "!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Successfully disabled!");
    }
}
