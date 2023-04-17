package me.glicz.inventoryapi.inventories;

import lombok.Getter;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum ClickType {
    LEFT(0, 0, org.bukkit.event.inventory.ClickType.LEFT),
    RIGHT(0, 1, org.bukkit.event.inventory.ClickType.RIGHT),
    SHIFT_LEFT(1, 0, org.bukkit.event.inventory.ClickType.SHIFT_LEFT),
    SHIFT_RIGHT(1, 1, org.bukkit.event.inventory.ClickType.SHIFT_RIGHT),
    NUM_KEY_1(2, 0, org.bukkit.event.inventory.ClickType.NUMBER_KEY),
    NUM_KEY_2(2, 1, org.bukkit.event.inventory.ClickType.NUMBER_KEY),
    NUM_KEY_3(2, 2, org.bukkit.event.inventory.ClickType.NUMBER_KEY),
    NUM_KEY_4(2, 3, org.bukkit.event.inventory.ClickType.NUMBER_KEY),
    NUM_KEY_5(2, 4, org.bukkit.event.inventory.ClickType.NUMBER_KEY),
    NUM_KEY_6(2, 5, org.bukkit.event.inventory.ClickType.NUMBER_KEY),
    NUM_KEY_7(2, 6, org.bukkit.event.inventory.ClickType.NUMBER_KEY),
    NUM_KEY_8(2, 7, org.bukkit.event.inventory.ClickType.NUMBER_KEY),
    NUM_KEY_9(2, 8, org.bukkit.event.inventory.ClickType.NUMBER_KEY),
    OFFHAND_SWAP(2, 40, org.bukkit.event.inventory.ClickType.SWAP_OFFHAND),
    MIDDLE(3, 2, org.bukkit.event.inventory.ClickType.MIDDLE),
    DROP(4, 0, org.bukkit.event.inventory.ClickType.DROP),
    CTRL_DROP(4, 1, org.bukkit.event.inventory.ClickType.CONTROL_DROP),
    LEFT_DRAG(5, 0),
    RIGHT_DRAG(5, 4),
    MIDDLE_DRAG(5, 8),
    ADD_SLOT_LEFT_DRAG(5, 1),
    ADD_SLOT_RIGHT_DRAG(5, 5),
    ADD_SLOT_MIDDLE_DRAG(5, 9),
    END_LEFT_DRAG(5, 2),
    END_RIGHT_DRAG(5, 6),
    END_MIDDLE_DRAG(5, 10),
    DOUBLE(6, 0, org.bukkit.event.inventory.ClickType.DOUBLE_CLICK);

    private final int mode;
    private final int button;
    @Getter
    private final org.bukkit.event.inventory.ClickType asBukkit;

    ClickType(int mode, int button) {
        this.mode = mode;
        this.button = button;
        this.asBukkit = null;
    }

    ClickType(int mode, int button, org.bukkit.event.inventory.ClickType asBukkit) {
        this.mode = mode;
        this.button = button;
        this.asBukkit = asBukkit;
    }

    public static ClickType get(int mode, int button) {
        return Arrays.stream(values())
                .filter(clickType -> clickType.mode == mode && clickType.button == button)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No value present for (mode=" + mode + ";button=" + button + ")"));
    }
}
