package me.glicz.glitchinventoryapi.inventories;

import me.glicz.glitchinventoryapi.itembuilders.ItemBuilder;
import me.glicz.glitchinventoryapi.titles.Title;
import me.glicz.glitchinventoryapi.types.GuiItem;
import me.glicz.glitchinventoryapi.types.InventoryType;
import org.bukkit.Material;

import java.util.Arrays;

@SuppressWarnings("unused")
public class SimpleInventory extends GlitchInventory<SimpleInventory> {

    @lombok.Builder(builderClassName = "Builder", buildMethodName = "create")
    private SimpleInventory(InventoryType inventoryType, Title title) {
        super(inventoryType, new GuiItem[inventoryType.getItems()]);
        Arrays.fill(items, ItemBuilder.from(Material.AIR).asGuiItem());
        setTitle(title);
    }

    private SimpleInventory(InventoryType inventoryType, Title title, GuiItem[] items) {
        super(inventoryType, items);
        setTitle(title);
    }

    public static SimpleInventory fromPaged(PaginatedInventory inventory) {
        return new SimpleInventory(inventory.getInventoryType(), inventory.getTitle(),
                inventory.getCurrentPageItems().toArray(GuiItem[]::new)).setId(inventory.getId());
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
    public SimpleInventory clone() {
        return new SimpleInventory(getInventoryType(), getTitle().clone(), getItems());
    }
}
