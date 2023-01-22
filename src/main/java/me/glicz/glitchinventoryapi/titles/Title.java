package me.glicz.glitchinventoryapi.titles;

import me.glicz.glitchinventoryapi.inventories.GlitchInventory;

public abstract class Title {

    public static SimpleTitle simple(String title) {
        return new SimpleTitle(title);
    }

    public static AnimatedTitle animated(String title) {
        return new AnimatedTitle(title);
    }

    public static AnimatedTitle animated(int refresh, String title) {
        return new AnimatedTitle(refresh, title);
    }

    public void setInventory(GlitchInventory<?> glitchInventory) {
    }

    public abstract String getString();

    @Override
    public abstract Title clone();
}
