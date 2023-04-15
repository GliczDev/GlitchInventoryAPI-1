package me.glicz.inventoryapi.inventories;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public class Margins {

    @Getter
    @Setter
    @Accessors(chain = true)
    private int top, bottom;

    private Margins(int top, int bottom) {
        this.top = top;
        this.bottom = bottom;
    }

    public static Margins zero() {
        return new Margins(0, 0);
    }

    public static Margins from(int top, int bottom) {
        return new Margins(top, bottom);
    }

    public static Margins top(int top) {
        return new Margins(top, 0);
    }

    public static Margins bottom(int bottom) {
        return new Margins(0, bottom);
    }

    public static Margins copy(Margins margins) {
        return from(margins.getTop(), margins.getBottom());
    }
}
