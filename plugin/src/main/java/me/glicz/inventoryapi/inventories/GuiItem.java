package me.glicz.inventoryapi.inventories;

import lombok.Getter;
import me.glicz.inventoryapi.events.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class GuiItem {

    @Getter
    private final ItemStack itemStack;
    private final Consumer<InventoryClickEvent> clickAction;

    private GuiItem(ItemStack itemStack, Consumer<InventoryClickEvent> clickAction) {
        this.itemStack = itemStack;
        this.clickAction = clickAction;
    }

    public static GuiItem of(@NotNull ItemStack itemStack) {
        return new GuiItem(itemStack, null);
    }

    public static GuiItem of(@NotNull ItemStack itemStack, @Nullable Consumer<InventoryClickEvent> clickAction) {
        return new GuiItem(itemStack, clickAction);
    }

    public void executeClickAction(InventoryClickEvent event) {
        if (clickAction == null)
            return;
        clickAction.accept(event);
    }
}
