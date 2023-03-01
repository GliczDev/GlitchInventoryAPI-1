package me.glicz.inventoryapi.itembuilders;

import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

@SuppressWarnings({"unused"})
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

    protected SkullBuilder() {
        super(new ItemStack(Material.PLAYER_HEAD));
    }

    public OfflinePlayer getOwner() {
        return itemMeta.getOwningPlayer();
    }

    public SkullBuilder setOwner(OfflinePlayer player) {
        itemMeta.setOwningPlayer(player);
        return this;
    }

    @SneakyThrows
    public String getValue() {
        GameProfile gameProfile = null;
        try {
            gameProfile = (GameProfile) profileField.get(itemMeta);
        } catch (Exception ignored) {
        }
        if (gameProfile == null)
            return null;
        for (Property property : gameProfile.getProperties().get("textures"))
            if (property.getName().equals("textures")) return property.getValue();
        return null;
    }

    @SneakyThrows
    public SkullBuilder setValue(String value) {
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        gameProfile.getProperties().put("textures",
                new Property("textures", value));
        profileField.set(itemMeta, gameProfile);
        return this;
    }

    public String getUrl() {
        String value = getValue();
        if (value == null) return null;
        String texturesJson = new String(base64.decode(value));
        JsonObject jsonObject = new Gson().fromJson(texturesJson, JsonObject.class);
        return jsonObject.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
    }

    public SkullBuilder setUrl(String url) {
        return setValue(base64.encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes()));
    }
}
