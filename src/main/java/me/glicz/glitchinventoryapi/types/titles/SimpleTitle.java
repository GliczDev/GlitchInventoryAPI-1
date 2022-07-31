package me.glicz.glitchinventoryapi.types.titles;

import me.glicz.glitchinventoryapi.types.Title;

public class SimpleTitle implements Title {

    private final String title;

    public SimpleTitle(String title) {
        this.title = title;
    }

    @Override
    public String getString() {
        return title;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Title clone() {
        return new SimpleTitle(title);
    }
}
