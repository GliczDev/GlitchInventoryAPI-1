package me.glicz.glitchinventoryapi.types.inventories;

import me.glicz.glitchinventoryapi.types.GlitchInventory;
import me.glicz.glitchinventoryapi.types.GuiItem;
import me.glicz.glitchinventoryapi.types.InventoryType;
import me.glicz.glitchinventoryapi.types.Title;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GlitchSimpleInventory extends GlitchInventory<GlitchSimpleInventory> {

    @lombok.Builder(builderClassName = "Builder", buildMethodName = "create")
    private GlitchSimpleInventory(InventoryType inventoryType, Title title) {
        super(inventoryType, new ArrayList<>(Collections.nCopies(inventoryType.getItems(), new GuiItem(Material.AIR))));
        setTitle(title);
    }

    private GlitchSimpleInventory(InventoryType inventoryType, Title title, List<GuiItem> items) {
        super(inventoryType, items);
        setTitle(title);
    }

    @Override
    public GuiItem getItem(int slot) {
        try {
            return items.get(slot);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    @Override
    public GlitchSimpleInventory clone() {
        return new GlitchSimpleInventory(getInventoryType(), getTitle().clone(), getItems());
    }
}
