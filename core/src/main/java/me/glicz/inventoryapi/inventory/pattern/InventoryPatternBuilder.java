package me.glicz.inventoryapi.inventory.pattern;

import lombok.Getter;
import me.glicz.inventoryapi.inventory.GlitchInventory;
import me.glicz.inventoryapi.inventory.GuiItem;
import me.glicz.inventoryapi.itembuilder.ItemBuilder;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryPatternBuilder<T extends GlitchInventory<T>> {

    private final Map<Character, GuiItem> replacements = new HashMap<>();
    private final T inventory;
    @Getter
    private List<String> pattern;

    private InventoryPatternBuilder(T inventory) {
        this.inventory = inventory;
    }

    public static <T extends GlitchInventory<T>> InventoryPatternBuilder<T> create(@NotNull T inventory) {
        return new InventoryPatternBuilder<>(inventory);
    }

    public InventoryPatternBuilder<T> setPattern(String... pattern) {
        if (String.join("", pattern).length() != inventory.getSize())
            throw new RuntimeException("Pattern length must be the same as inventory size!");
        this.pattern = List.of(pattern);
        return this;
    }

    public InventoryPatternBuilder<T> setReplacement(char character, GuiItem item) {
        replacements.put(character, item);
        return this;
    }

    public GuiItem getReplacement(char character) {
        return replacements.get(character);
    }

    public T build() {
        for (int y = 0; y < pattern.size(); y++) {
            String line = pattern.get(y);
            char[] chars = line.toCharArray();
            for (int x = 0; x < chars.length; x++) {
                char id = chars[x];
                if (!replacements.containsKey(id)) {
                    if (id == ' ')
                        continue;
                    throw new NullPointerException("No replacement found for key '%s'!".formatted(id));
                }
                inventory.setItem(y * line.length() + x, replacements.get(id));
            }
        }
        return inventory;
    }
}
