package me.glicz.inventoryapi.titles;

import net.kyori.adventure.text.Component;

public class SimpleTitle extends Title {

    private final Component title;

    protected SimpleTitle(Component title) {
        this.title = title;
    }

    @Override
    public Component getComponent() {
        return title;
    }
}
