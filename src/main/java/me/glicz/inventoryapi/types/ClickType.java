package me.glicz.inventoryapi.types;

import java.util.Arrays;

public enum ClickType {
    LEFT(0, 0),
    RIGHT(0, 1),
    SHIFT_LEFT(1, 0),
    SHIFT_RIGHT(1, 1),
    NUM_KEY_1(2, 0),
    NUM_KEY_2(2, 1),
    NUM_KEY_3(2, 2),
    NUM_KEY_4(2, 3),
    NUM_KEY_5(2, 4),
    NUM_KEY_6(2, 5),
    NUM_KEY_7(2, 6),
    NUM_KEY_8(2, 7),
    NUM_KEY_9(2, 8),
    OFFHAND_SWAP(2, 40),
    MIDDLE(3, 2),
    DROP(4, 0),
    CTRL_DROP(4, 1),
    LEFT_DRAG(5, 0),
    RIGHT_DRAG(5, 4),
    MIDDLE_DRAG(5, 8),
    ADD_SLOT_LEFT_DRAG(5, 1),
    ADD_SLOT_RIGHT_DRAG(5, 5),
    ADD_SLOT_MIDDLE_DRAG(5, 9),
    END_LEFT_DRAG(5, 2),
    END_RIGHT_DRAG(5, 6),
    END_MIDDLE_DRAG(5, 10),
    DOUBLE(6, 0);

    private final int mode;
    private final int button;

    ClickType(int mode, int button) {
        this.mode = mode;
        this.button = button;
    }

    public static ClickType get(int mode, int button) {
        return Arrays.stream(values()).filter(clickType -> clickType.mode == mode && clickType.button == button).findFirst().orElseThrow();
    }
}
