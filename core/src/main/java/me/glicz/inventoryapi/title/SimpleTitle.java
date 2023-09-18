package me.glicz.inventoryapi.title;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import me.glicz.inventoryapi.inventory.GlitchInventory;
import net.kyori.adventure.text.Component;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SimpleTitle extends Title<SimpleTitle> {
    private final Component title;

    @Override
    public Component getComponent() {
        return title;
    }

    @Override
    public SimpleTitle accept(GlitchInventory<?> inventory) {
        return new SimpleTitle(title);
    }
}
