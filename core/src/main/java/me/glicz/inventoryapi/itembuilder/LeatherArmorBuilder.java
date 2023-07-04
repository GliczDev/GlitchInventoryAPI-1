package me.glicz.inventoryapi.itembuilder;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class LeatherArmorBuilder extends ItemBuilder<LeatherArmorBuilder, LeatherArmorMeta> {

    protected LeatherArmorBuilder(ItemStack itemStack) {
        super(itemStack);
    }

    public Color getColor() {
        return itemMeta.getColor();
    }

    public LeatherArmorBuilder setColor(Color color) {
        itemMeta.setColor(color);
        return this;
    }
}
