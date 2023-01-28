package me.glicz.glitchinventoryapi.inventories;

import me.glicz.glitchinventoryapi.titles.Title;
import me.glicz.glitchinventoryapi.types.GuiItem;
import me.glicz.glitchinventoryapi.types.InventoryType;

@SuppressWarnings("unused")
public class SimpleInventory extends GlitchInventory<SimpleInventory> {

    @lombok.Builder(builderClassName = "Builder", buildMethodName = "create")
    private SimpleInventory(InventoryType inventoryType, Title title) {
        super(inventoryType, title);
    }

    private SimpleInventory(InventoryType inventoryType, Title title, GuiItem[] items) {
        super(inventoryType, title, items);
    }

    public static SimpleInventory fromPaginated(PaginatedInventory inventory) {
        return new SimpleInventory(inventory.getInventoryType(), inventory.getTitle(),
                inventory.getCurrentPageItems()).setId(inventory.getId());
    }

    @Override
    public SimpleInventory clone() {
        return new SimpleInventory(getInventoryType(), getTitle().clone(), getItems());
    }
}
