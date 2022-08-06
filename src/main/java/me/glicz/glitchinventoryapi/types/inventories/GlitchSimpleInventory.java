package me.glicz.glitchinventoryapi.types.inventories;

import me.glicz.glitchinventoryapi.types.*;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GlitchSimpleInventory extends GlitchInventory<GlitchSimpleInventory> {

    @lombok.Builder(builderClassName = "Builder", buildMethodName = "create")
    private GlitchSimpleInventory(InventoryType inventoryType, Title title) {
        super(inventoryType, new GuiItem[inventoryType.getItems()]);
        Arrays.fill(items, ItemBuilder.from(Material.AIR).asGuiItem());
        setTitle(title);
    }

    private GlitchSimpleInventory(InventoryType inventoryType, Title title, GuiItem[] items) {
        super(inventoryType, items);
        setTitle(title);
    }

    @Override
    public GuiItem getItem(int slot) {
        try {
            return items[slot];
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    @Override
    public GlitchSimpleInventory clone() {
        return new GlitchSimpleInventory(getInventoryType(), getTitle().clone(), getItems());
    }
}
