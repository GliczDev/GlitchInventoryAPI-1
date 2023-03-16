package me.glicz.inventoryapi.itembuilders;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;

import java.net.URL;
import java.util.UUID;

public class SkullBuilder extends ItemBuilder<SkullBuilder, SkullMeta> {

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
        PlayerTextures textures = getProfile().getTextures();
        if (textures.getSkin() != null)
            return textures.getSkin().toString();
        return null;
    }

    @SneakyThrows
    public SkullBuilder setUrl(String url) {
        PlayerProfile profile = getProfile();
        PlayerTextures textures = profile.getTextures();
        textures.setSkin(new URL(url));
        profile.setTextures(textures);
        itemMeta.setPlayerProfile(profile);
        return this;
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
