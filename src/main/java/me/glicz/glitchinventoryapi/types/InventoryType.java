package me.glicz.glitchinventoryapi.types;

import java.util.HashMap;

public enum InventoryType {
    Generic_9x1("a", 1),
    Generic_9x2("b", 2),
    Generic_9x3("c", 3),
    Generic_9x4("d", 4),
    Generic_9x5("e", 5),
    Generic_9x6("f", 6);

    private static final HashMap<Integer, InventoryType> BY_ROWS = new HashMap<>();
    static {
        for (InventoryType invType : values()) {
            BY_ROWS.put(invType.rows, invType);
        }
    }

    private final String fieldName;
    private final int rows;

    InventoryType(String fieldName, int rows) {
        this.rows = rows;
        this.fieldName = fieldName;
    }

    public static InventoryType getByRows(int rows) {
        return BY_ROWS.get(rows);
    }

    public String getFieldName() {
        return fieldName;
    }
}
