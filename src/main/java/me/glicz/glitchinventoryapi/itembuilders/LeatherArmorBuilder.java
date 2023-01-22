package me.glicz.glitchinventoryapi.itembuilders;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class LeatherArmorBuilder extends ItemBuilder<LeatherArmorBuilder> {

    public LeatherArmorBuilder(ItemStack itemStack) {
        super(itemStack);
        if (!(itemStack.getItemMeta() instanceof LeatherArmorMeta)) {
            throw new UnsupportedOperationException("ItemStack have to be any leather armor");
        }
    }

    public LeatherArmorBuilder color(Color color) {
        ((LeatherArmorMeta) itemMeta).setColor(color);
        return this;
    }

    public Color color() {
        return ((LeatherArmorMeta) itemMeta).getColor();
    }
}
