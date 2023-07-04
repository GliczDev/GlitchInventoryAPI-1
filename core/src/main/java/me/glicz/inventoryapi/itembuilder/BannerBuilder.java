package me.glicz.inventoryapi.itembuilder;

import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.List;

public class BannerBuilder extends ItemBuilder<BannerBuilder, BannerMeta> {

    protected BannerBuilder(ItemStack itemStack) {
        super(itemStack);
    }

    public List<Pattern> getPatterns() {
        return itemMeta.getPatterns();
    }

    public BannerBuilder setPatterns(List<Pattern> patterns) {
        itemMeta.setPatterns(patterns);
        return this;
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
