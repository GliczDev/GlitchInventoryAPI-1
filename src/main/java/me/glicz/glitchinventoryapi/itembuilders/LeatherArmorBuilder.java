package me.glicz.glitchinventoryapi.itembuilders;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

@SuppressWarnings("unused")
public class LeatherArmorBuilder extends ItemBuilder<LeatherArmorBuilder, LeatherArmorMeta> {

    protected LeatherArmorBuilder(ItemStack itemStack) {
        super(itemStack);
    }

    public LeatherArmorBuilder setColor(Color color) {
        itemMeta.setColor(color);
        return this;
    }

    public Color getColor() {
        return itemMeta.getColor();
    }
}
