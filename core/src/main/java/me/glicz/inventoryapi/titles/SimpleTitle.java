package me.glicz.inventoryapi.titles;

import me.glicz.inventoryapi.inventories.GlitchInventory;
import net.kyori.adventure.text.Component;

public class SimpleTitle extends Title<SimpleTitle> {

    private final Component title;

    protected SimpleTitle(Component title) {
        this.title = title;
    }

    @Override
    public Component getComponent() {
        return title;
    }

    @Override
    public SimpleTitle accept(GlitchInventory<?> inventory) {
        return new SimpleTitle(title);
    }
}
