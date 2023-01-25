package me.glicz.glitchinventoryapi.itembuilders;

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

@SuppressWarnings({"deprecation", "unused"})
public class SkullBuilder extends ItemBuilder<SkullBuilder, SkullMeta> {

    private static final BaseEncoding base64 = BaseEncoding.base64();
    private static final Field profileField;

    static {
        Field field;
        try {
            SkullMeta skullMeta = ItemBuilder.skull().itemMeta;
            field = skullMeta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        profileField = field;
    }

    public SkullBuilder() {
        super(new ItemStack(Material.PLAYER_HEAD));
    }

    public SkullBuilder owner(OfflinePlayer player) {
        itemMeta.setOwnerProfile(player.getPlayerProfile());
        return this;
    }

    @SneakyThrows
    public SkullBuilder value(String value) {
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        gameProfile.getProperties().put("textures",
                new Property("textures", value));
        profileField.set(itemMeta, gameProfile);
        return this;
    }

    public SkullBuilder url(String url) {
        return value(base64.encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes()));
    }
}
