package me.glicz.inventoryapi;

import me.glicz.inventoryapi.nms.NMSInitializer;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class GlitchInventoryAPIPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        new GlitchInventoryAPI(this).load();
        getLogger().info("Successfully enabled for " + NMSInitializer.getVersion() + "!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Successfully disabled!");
    }
}
