package me.glicz.inventoryapi.nms;

import com.google.common.reflect.ClassPath;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import me.glicz.inventoryapi.exceptions.UnsupportedVersionException;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.NONE)
public class NMSInitializer {

    public static String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    @SneakyThrows
    @SuppressWarnings({"unchecked", "UnstableApiUsage"})
    public static NMS initialize(JavaPlugin plugin) {
        try {
            int[] serverVersion = Arrays.stream(Bukkit.getMinecraftVersion().split("\\.")).mapToInt(Integer::parseInt).toArray();
            int serverMajor = serverVersion[0];
            int serverMinor = serverVersion[1];
            int serverPatch = (serverVersion.length == 3) ? serverVersion[2] : 0;
            var nearestVersion = ClassPath.from(plugin.getClass().getClassLoader())
                    .getTopLevelClassesRecursive(NMSInitializer.class.getPackageName())
                    .stream()
                    .filter(classInfo -> {
                        try {
                            Class<?> clazz = classInfo.load();
                            return NMS.class.isAssignableFrom(clazz) && clazz.getDeclaredAnnotation(NativeVersion.class) != null;
                        } catch (UnsupportedClassVersionError ex) {
                            return false;
                        }
                    })
                    .map(classInfo -> (Class<? extends NMS>) classInfo.load())
                    .map(clazz -> Map.entry(clazz, clazz.getDeclaredAnnotation(NativeVersion.class)))
                    .filter(entry -> {
                        NativeVersion version = entry.getValue();
                        return version.major() < serverMajor ||
                                (version.major() == serverMajor && version.minor() < serverMinor) ||
                                (version.major() == serverMajor && version.minor() == serverMinor && version.patch() <= serverPatch);
                    })
                    .max(Comparator
                            .<Map.Entry<? extends Class<? extends NMS>, NativeVersion>>comparingInt(entry -> entry.getValue().major())
                            .thenComparingInt(entry -> entry.getValue().minor())
                            .thenComparingInt(entry -> entry.getValue().patch()));
            if (nearestVersion.isPresent())
                return nearestVersion.get().getKey().getConstructor(JavaPlugin.class).newInstance(plugin);
            throw new ClassNotFoundException();
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            throw new UnsupportedVersionException();
        }
    }
}
