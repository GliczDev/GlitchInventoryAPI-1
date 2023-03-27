package me.glicz.inventoryapi.inventories;

public record OpenResult<T extends GlitchInventory<T>>(boolean opened, GlitchInventory<T> inventory) {
}
