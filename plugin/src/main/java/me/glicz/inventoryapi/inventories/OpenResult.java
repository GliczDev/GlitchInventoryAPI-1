package me.glicz.inventoryapi.inventories;

import java.util.function.Consumer;

public record OpenResult<T extends GlitchInventory<T>>(boolean opened, GlitchInventory<T> inventory) {

    public GlitchInventory<T> ifOpened(Consumer<GlitchInventory<T>> ifOpenedAction) {
        if (opened)
            ifOpenedAction.accept(inventory);
        return inventory;
    }
}
