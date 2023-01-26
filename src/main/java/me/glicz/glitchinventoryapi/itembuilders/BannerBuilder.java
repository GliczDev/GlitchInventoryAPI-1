package me.glicz.glitchinventoryapi.itembuilders;

import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings({"deprecation", "unused"})
public class BannerBuilder extends ItemBuilder<BannerBuilder, BannerMeta> {

    protected BannerBuilder(ItemStack itemStack) {
        super(itemStack);
    }

    public BannerBuilder setBaseColor(DyeColor color) {
        itemMeta.setBaseColor(color);
        return this;
    }

    @Nullable
    public DyeColor getBaseColor() {
        return itemMeta.getBaseColor();
    }

    public BannerBuilder setPatterns(List<Pattern> patterns) {
        itemMeta.setPatterns(patterns);
        return this;
    }

    public List<Pattern> getPatterns() {
        return itemMeta.getPatterns();
    }

    public BannerBuilder addPattern(Pattern pattern) {
        itemMeta.addPattern(pattern);
        return this;
    }

    public BannerBuilder setPattern(int index, Pattern pattern) {
        itemMeta.setPattern(index, pattern);
        return this;
    }

    public BannerBuilder removePattern(int index) {
        itemMeta.removePattern(index);
        return this;
    }

    public Pattern getPattern(int index) {
        return itemMeta.getPattern(index);
    }

    public int numberOfPatterns() {
        return itemMeta.numberOfPatterns();
    }
}
