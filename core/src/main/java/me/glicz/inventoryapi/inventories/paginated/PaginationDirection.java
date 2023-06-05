package me.glicz.inventoryapi.inventories.paginated;

import lombok.AllArgsConstructor;
import me.glicz.inventoryapi.inventories.GuiItem;
import me.glicz.inventoryapi.inventories.PaginatedInventory;

import java.util.function.BiFunction;

@AllArgsConstructor
public enum PaginationDirection {
    UP((inventory, items) -> {
        GuiItem[] value = new GuiItem[inventory.getSize()];
        Margins margins = inventory.getMargins();
        int slot = margins.getBottom() * 9 + margins.getLeft();
        for (int i = 0; i < value.length - margins.getBottom() * 9 && i < items.length; i++) {
            GuiItem item = items[i];
            if (item != null)
                value[value.length - 9 * ((slot + 9) / 9) + slot % 9] = item;
            if ((slot / 9 + 1) * 9 - margins.getRight() - 1 == slot)
                slot += margins.getLeft() + margins.getRight();
            slot++;
        }
        return value;
    }),
    DOWN((inventory, items) -> {
        GuiItem[] value = new GuiItem[inventory.getSize()];
        Margins margins = inventory.getMargins();
        int slot = margins.getTop() * 9 + margins.getLeft();
        for (int i = 0; i < value.length - margins.getTop() * 9 && i < items.length; i++) {
            GuiItem item = items[i];
            if (item != null)
                value[slot] = item;
            if ((slot / 9 + 1) * 9 - margins.getRight() - 1 == slot)
                slot += margins.getLeft() + margins.getRight();
            slot++;
        }
        return value;
    });

    private final BiFunction<PaginatedInventory, GuiItem[], GuiItem[]> parser;

    public GuiItem[] parse(PaginatedInventory inventory, GuiItem[] items) {
        return parser.apply(inventory, items);
    }
}
