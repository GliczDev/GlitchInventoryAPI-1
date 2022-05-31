package me.glicz.glitchinventoryapi.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NMSUtil {

    private Method nextContainerCounter;
    private Method getEntityHandle;

    public NMSUtil() {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            this.getEntityHandle = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftEntity").getDeclaredMethod("getHandle");
            try {
                Class<?> entityClass = Class.forName("net.minecraft.server.level.EntityPlayer");
                this.nextContainerCounter = entityClass.getDeclaredMethod("nextContainerCounter");
            } catch (ClassNotFoundException e) {
                Class<?> entityClass = Class.forName("net.minecraft.server." + version + ".EntityPlayer");
                this.nextContainerCounter = entityClass.getDeclaredMethod("nextContainerCounter");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getNextContainerCounter(Player player) {
        try {
            Object handle = this.getEntityHandle.invoke(player);
            return (int) this.nextContainerCounter.invoke(handle);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
