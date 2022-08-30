package me.glicz.glitchinventoryapi.types.itembuilders;

import me.glicz.glitchinventoryapi.types.ItemBuilder;
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
        ((LeatherArmorMeta)itemStack.getItemMeta()).setColor(color);
        return this;
    }

    public Color color() {
        return ((LeatherArmorMeta)itemStack.getItemMeta()).getColor();
    }
}
