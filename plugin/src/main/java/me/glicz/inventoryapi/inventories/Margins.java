package me.glicz.inventoryapi.inventories;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public class Margins {

    @Getter
    @Setter
    @Accessors(chain = true)
    private int top, left, bottom, right;

    private Margins(int top, int left, int bottom, int right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

    public static Margins zero() {
        return new Margins(0, 0, 0, 0);
    }

    public static Margins from(int top, int left, int bottom, int right) {
        return new Margins(top, left, bottom, right);
    }

    public static Margins top(int top) {
        return new Margins(top, 0, 0, 0);
    }

    public static Margins left(int left) {
        return new Margins(0, left, 0, 0);
    }

    public static Margins bottom(int bottom) {
        return new Margins(0, 0, bottom, 0);
    }

    public static Margins right(int right) {
        return new Margins(0, 0, 0, right);
    }

    public static Margins copy(Margins margins) {
        return from(margins.getTop(), margins.getLeft(), margins.getBottom(), margins.getRight());
    }

    public int sumSlots(int rows) {
        return (left + right) * rows + (9 - (left + right)) * (top + bottom);
    }
}
