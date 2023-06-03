package me.glicz.inventoryapi.plugin;

import me.glicz.inventoryapi.GlitchInventoryAPI;
import me.glicz.inventoryapi.exceptions.UnsupportedVersionException;
import me.glicz.inventoryapi.nms.NMSInitializer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class GlitchInventoryAPIPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        ConfigLoader.load(this);
        try {
            GlitchInventoryAPI.load(this);
        } catch (UnsupportedVersionException ex) {
            getSLF4JLogger().error("Error occurred while initializing GlitchInventoryAPI NMS object in %s"
                    .formatted(getDescription().getFullName()), ex);
            getLogger().severe("GlitchInventoryAPI DOES NOT support your server version (%s)!".formatted(Bukkit.getMinecraftVersion()));
            getLogger().severe("If you think this is a bug, please report it on github (https://github.com/GliczDev/GlitchInventoryAPI/issues)");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("Successfully enabled for " + NMSInitializer.getVersion() + "!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Successfully disabled!");
    }
}
