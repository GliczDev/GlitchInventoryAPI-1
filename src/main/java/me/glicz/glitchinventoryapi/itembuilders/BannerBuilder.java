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

    public BannerBuilder baseColor(DyeColor color) {
        itemMeta.setBaseColor(color);
        return this;
    }

    @Nullable
    public DyeColor baseColor() {
        return itemMeta.getBaseColor();
    }

    public BannerBuilder patterns(List<Pattern> patterns) {
        itemMeta.setPatterns(patterns);
        return this;
    }

    public List<Pattern> patterns() {
        return itemMeta.getPatterns();
    }

    public BannerBuilder pattern(Pattern pattern) {
        itemMeta.addPattern(pattern);
        return this;
    }

    public BannerBuilder pattern(int index, @Nullable Pattern pattern) {
        if (pattern == null)
            itemMeta.removePattern(index);
        else
            itemMeta.setPattern(index, pattern);
        return this;
    }

    public Pattern pattern(int index) {
        return itemMeta.getPattern(index);
    }

    public int numberOfPatterns() {
        return itemMeta.numberOfPatterns();
    }
}
