package me.glicz.glitchinventoryapi.types;

import java.util.HashMap;

public enum ClickType {
    LeftClick(0, 0),
    RightClick(0, 1),
    ShiftLeftClick(1, 0),
    ShiftRightClick(1, 1),
    NumberKey1(2, 0),
    NumberKey2(2, 1),
    NumberKey3(2, 2),
    NumberKey4(2, 3),
    NumberKey5(2, 4),
    NumberKey6(2, 5),
    NumberKey7(2, 6),
    NumberKey8(2, 7),
    NumberKey9(2, 8),
    OffhandSwap(2, 40),
    MiddleClick(3, 2),
    Drop(4, 0),
    CtrlDrop(4, 1),
    LeftDrag(5, 0),
    RightDrag(5, 4),
    MiddleDrag(5, 8),
    AddSlotLeftDrag(5, 1),
    AddSlotRightDrag(5, 5),
    AddSlotMiddleDrag(5, 9),
    EndLeftDrag(5, 2),
    EndRightDrag(5, 6),
    EndMiddleDrag(5, 10),
    DoubleClick(6, 0);

    private final int mode;
    private final int button;

    private static final HashMap<Integer, HashMap<Integer, ClickType>> clickTypes = new HashMap<>();

    static {
        for (ClickType value : values()) {
            if (clickTypes.containsKey(value.mode)) {
                clickTypes.get(value.mode).put(value.button, value);
                continue;
            }
            HashMap<Integer, ClickType> tempValue = new HashMap<>();
            tempValue.put(value.button, value);
            clickTypes.put(value.mode, tempValue);
        }
    }

    ClickType(int mode, int button) {
        this.mode = mode;
        this.button = button;
    }

    public static ClickType get(int mode, int button) {
        return clickTypes.get(mode).get(button);
    }
}
