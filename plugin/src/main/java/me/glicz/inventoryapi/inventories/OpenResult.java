package me.glicz.inventoryapi.inventories;

import java.util.function.Consumer;

public record OpenResult<T extends GlitchInventory<T>>(boolean opened, T inventory) {

    public T ifOpened(Consumer<T> ifOpenedAction) {
        if (opened)
            ifOpenedAction.accept(inventory);
        return inventory;
    }
}
