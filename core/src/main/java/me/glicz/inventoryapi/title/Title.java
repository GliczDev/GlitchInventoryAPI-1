package me.glicz.inventoryapi.title;

import me.glicz.inventoryapi.inventory.GlitchInventory;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Map;

public abstract class Title<T extends Title<T>> {
    public static SimpleTitle simple(String title) {
        return simple(Component.text(title));
    }

    public static SimpleTitle simple(Component title) {
        return new SimpleTitle(title);
    }

    public static AnimatedTitle animated(int period, Component... titles) {
        return animated(period, List.of(titles));
    }

    public static AnimatedTitle animated(int period, List<Component> titles) {
        return new AnimatedTitle(period, titles);
    }

    public static AnimatedTitle animated(Map<Integer, Component> titleMap) {
        return new AnimatedTitle(titleMap);
    }

    public abstract Component getComponent();

    public abstract T accept(GlitchInventory<?> inventory);
}
