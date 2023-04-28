package me.glicz.inventoryapi.titles;

import net.kyori.adventure.text.Component;

public abstract class Title {

    public static SimpleTitle simple(String title) {
        return simple(Component.text(title));
    }

    public static SimpleTitle simple(Component title) {
        return new SimpleTitle(title);
    }

    public abstract Component getComponent();
}
