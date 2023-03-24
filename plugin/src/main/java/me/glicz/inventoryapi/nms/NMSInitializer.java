package me.glicz.inventoryapi.nms;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;

@NoArgsConstructor(access = AccessLevel.NONE)
public class NMSInitializer {

    public static String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    @SuppressWarnings("unchecked")
    public static NMS initialize(JavaPlugin plugin) {
        try {
            Class<? extends NMS> nmsServerClass = (Class<? extends NMS>) Class.forName(
                    NMSInitializer.class.getPackageName() + "." + getVersion() + "." + getVersion() + "_NMS");
            return (NMS) nmsServerClass.getConstructors()[0].newInstance();
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().severe("GlitchInventoryAPI DOES NOT support your server version!");
            plugin.getComponentLogger().error(Component.text("If you think this is a bug, please report it on ")
                    .append(Component.text("github").clickEvent(ClickEvent.openUrl("https://github.com/GliczDev/GlitchInventoryAPI/issues"))));
            Bukkit.getPluginManager().disablePlugin(plugin);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            ex.getCause().printStackTrace();
            plugin.getLogger().severe("Something went wrong while trying to create NMS object!");
            plugin.getComponentLogger().error(Component.text("If you think this is a bug, please report it on ")
                    .append(Component.text("github").clickEvent(ClickEvent.openUrl("https://github.com/GliczDev/GlitchInventoryAPI/issues"))));
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
        throw new NullPointerException("Cannot get NMS object");
    }
}
