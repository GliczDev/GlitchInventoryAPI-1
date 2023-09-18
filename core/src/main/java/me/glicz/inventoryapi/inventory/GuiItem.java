package me.glicz.inventoryapi.inventory;

import lombok.Getter;
import me.glicz.inventoryapi.event.InventoryClickEvent;
import me.glicz.inventoryapi.event.Listener;
import me.glicz.inventoryapi.itembuilder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GuiItem {
    public static final GuiItem EMPTY = ItemBuilder.of(Material.AIR).asGuiItem();
    @Getter
    private final ItemStack itemStack;
    private final Listener<InventoryClickEvent> clickListener;

    private GuiItem(ItemStack itemStack, Listener<InventoryClickEvent> clickListener) {
        this.itemStack = itemStack;
        this.clickListener = clickListener;
    }

    public static GuiItem of(@NotNull ItemStack itemStack) {
        return new GuiItem(itemStack, null);
    }

    public static GuiItem of(@NotNull ItemStack itemStack, @Nullable Listener<InventoryClickEvent> clickListener) {
        return new GuiItem(itemStack, clickListener);
    }

    public void runClickListener(InventoryClickEvent event) {
        if (clickListener == null)
            return;
        clickListener.run(event);
    }
}
