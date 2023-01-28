package me.glicz.glitchinventoryapi.titles;

import me.glicz.glitchinventoryapi.inventories.GlitchInventory;

@SuppressWarnings("unused")
public abstract class Title {

    public static SimpleTitle simple(String title) {
        return new SimpleTitle(title);
    }

    public static AnimatedTitle animated(String... title) {
        return animated(20, title);
    }

    public static AnimatedTitle animated(int refresh, String... title) {
        return new AnimatedTitle(refresh, title);
    }

    public static RandomTitle random(String... title) {
        return random(20, title);
    }

    public static RandomTitle random(int refresh, String... title) {
        return new RandomTitle(refresh, title);
    }

    public void setInventory(GlitchInventory<?> glitchInventory) {
    }

    public abstract String getString();

    @Override
    public abstract Title clone();
}
