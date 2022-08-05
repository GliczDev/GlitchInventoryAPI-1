package me.glicz.glitchinventoryapi.types;

import lombok.Getter;

public enum InventoryType {
    Chest_9x1("a", 1, 9),
    Chest_9x2("b", 2, 2 * 9),
    Chest_9x3("c", 3, 3 * 9),
    Chest_9x4("d", 4, 4 * 9),
    Chest_9x5("e", 5, 5 * 9),
    Chest_9x6("f", 6, 6 * 9),
    Anvil("h", 3)
    ;

    @Getter
    private final String fieldName;
    @Getter
    private final int rows;
    @Getter
    private final int items;

    InventoryType(String fieldName, int items) {
        this.rows = -1;
        this.items = items;
        this.fieldName = fieldName;
    }

    InventoryType(String fieldName, int rows, int items) {
        this.items = items;
        this.rows = rows;
        this.fieldName = fieldName;
    }
}
