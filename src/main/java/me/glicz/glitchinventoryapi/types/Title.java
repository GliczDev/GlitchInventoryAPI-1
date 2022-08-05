package me.glicz.glitchinventoryapi.types;

public interface Title {
    default void setInventory(GlitchInventory<?> glitchInventory) {}
    String getString();
    Title clone();
}
