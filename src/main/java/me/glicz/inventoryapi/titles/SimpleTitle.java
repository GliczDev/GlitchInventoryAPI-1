package me.glicz.inventoryapi.titles;

public class SimpleTitle extends Title {

    private final String title;

    protected SimpleTitle(String title) {
        this.title = title;
    }

    @Override
    public String getString() {
        return title;
    }

    @Override
    public Title clone() {
        return new SimpleTitle(title);
    }
}
