package me.glicz.inventoryapi.plugin;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import me.glicz.inventoryapi.GlitchInventoryAPI;
import me.glicz.inventoryapi.GlitchInventoryAPIConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@NoArgsConstructor(access = AccessLevel.NONE)
public class ConfigLoader {
    @SneakyThrows
    public static void load(JavaPlugin plugin) {
        if (!plugin.getDataFolder().exists() && !plugin.getDataFolder().mkdirs())
            throw new RuntimeException("Could not create plugin data folder");
        File file = new File(plugin.getDataFolder(), "config.yml");
        YAMLFactory factory = new YAMLFactory()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES);
        ObjectMapper objectMapper = new ObjectMapper(factory)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE);
        if (file.exists())
            GlitchInventoryAPI.setConfig(objectMapper.readValue(file, GlitchInventoryAPIConfig.class));
        objectMapper.writeValue(file, GlitchInventoryAPI.getConfig());
    }
}
