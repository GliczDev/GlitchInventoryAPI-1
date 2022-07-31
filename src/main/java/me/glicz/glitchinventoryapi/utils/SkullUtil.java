package me.glicz.glitchinventoryapi.utils;

import com.google.common.io.BaseEncoding;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

@SuppressWarnings({"unused", "ConstantConditions"})
public class SkullUtil {

    private static final BaseEncoding base64 = BaseEncoding.base64();

    public static ItemStack getPlayerHeadByURL(String url, String name, String[] lore) {
        String value = base64.encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        return getPlayerHead(value, name, lore);
    }

    @SneakyThrows
    public static ItemStack getPlayerHead(String value, String name, String[] lore) {
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        gameProfile.getProperties().put("textures",
                new Property("textures", value));
        ItemStack skull = ItemUtil.getItem(1, Material.PLAYER_HEAD, name, lore, null);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        Field profileField;
        profileField = skullMeta.getClass().getDeclaredField("profile");
        profileField.setAccessible(true);
        profileField.set(skullMeta, gameProfile);
        skull.setItemMeta(skullMeta);
        return skull;
    }

    public static ItemStack getPlayerHeadByPlayer(OfflinePlayer player, String name, String[] lore) {
        ItemStack skull = ItemUtil.getItem(1, Material.PLAYER_HEAD, name, lore, null);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwnerProfile(player.getPlayerProfile());
        skull.setItemMeta(skullMeta);
        return skull;
    }
}
