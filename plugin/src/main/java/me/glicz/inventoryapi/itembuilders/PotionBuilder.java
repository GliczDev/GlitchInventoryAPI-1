package me.glicz.inventoryapi.itembuilders;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class PotionBuilder extends ItemBuilder<PotionBuilder, PotionMeta> {

    protected PotionBuilder(ItemStack itemStack) {
        super(itemStack);
    }

    public Color getColor() {
        return itemMeta.getColor();
    }

    public PotionBuilder setColor(Color color) {
        itemMeta.setColor(color);
        return this;
    }

    public PotionData getBasePotionData() {
        return itemMeta.getBasePotionData();
    }

    public PotionBuilder setBasePotionData(PotionData data) {
        itemMeta.setBasePotionData(data);
        return this;
    }

    public List<PotionEffect> getCustomEffects() {
        return itemMeta.getCustomEffects();
    }

    public PotionBuilder addCustomEffect(PotionEffect effect) {
        return addCustomEffect(effect, false);
    }

    public PotionBuilder addCustomEffect(PotionEffect effect, boolean overwrite) {
        itemMeta.addCustomEffect(effect, overwrite);
        return this;
    }

    public PotionBuilder removeCustomEffect(PotionEffectType type) {
        itemMeta.removeCustomEffect(type);
        return this;
    }

    public boolean hasCustomEffect(PotionEffectType type) {
        return itemMeta.hasCustomEffect(type);
    }
}
