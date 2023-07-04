package me.glicz.inventoryapi.itembuilder;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class SkullBuilder extends ItemBuilder<SkullBuilder, SkullMeta> {

    private static final BaseEncoding base64 = BaseEncoding.base64();

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

    public String getValue() {
        return getProfile().getProperties().stream()
                .filter(property -> property.getName().equals("textures"))
                .map(ProfileProperty::getValue)
                .findFirst()
                .orElse(null);
    }

    @SneakyThrows
    public SkullBuilder setValue(String value) {
        PlayerProfile profile = getProfile();
        profile.setProperty(new ProfileProperty("textures", value));
        itemMeta.setPlayerProfile(profile);
        return this;
    }

    public String getUrl() {
        String value = getValue();
        if (value == null) return null;
        String texturesJson = new String(base64.decode(value));
        JsonObject jsonObject = new Gson().fromJson(texturesJson, JsonObject.class);
        return jsonObject.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
    }

    @SneakyThrows
    public SkullBuilder setUrl(String url) {
        return setValue(base64.encode("{textures:{SKIN:{url:\"%s\"}}}".formatted(url).getBytes()));
    }

    private PlayerProfile getProfile() {
        PlayerProfile playerProfile = itemMeta.getPlayerProfile();
        if (playerProfile != null)
            return playerProfile;
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        itemMeta.setPlayerProfile(profile);
        return profile;
    }
}
