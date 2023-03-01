package me.glicz.inventoryapi.types;

import lombok.Getter;
import lombok.Setter;
import me.glicz.inventoryapi.events.ItemClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class GuiItem {

    @Getter
    private final ItemStack itemStack;
    @Getter
    @Setter
    private Consumer<ItemClickEvent> clickAction;

    public GuiItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public GuiItem(ItemStack itemStack, Consumer<ItemClickEvent> clickAction) {
        this.itemStack = itemStack;
        this.clickAction = clickAction;
    }
}
