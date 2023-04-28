package me.glicz.inventoryapi.plugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import me.glicz.inventoryapi.GlitchInventoryAPI;
import me.glicz.inventoryapi.GlitchInventoryAPIConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;

@NoArgsConstructor(access = AccessLevel.NONE)
public class ConfigLoader {

    @SneakyThrows
    public static void load(JavaPlugin plugin) {
        if (!plugin.getDataFolder().exists() && !plugin.getDataFolder().mkdirs())
            throw new RuntimeException("Could not create plugin data folder");
        File file = new File(plugin.getDataFolder(), "config.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(GlitchInventoryAPI.getConfig(), writer);
            writer.flush();
        }
        GlitchInventoryAPI.setConfig(gson.fromJson(new FileReader(file), GlitchInventoryAPIConfig.class));
    }
}
