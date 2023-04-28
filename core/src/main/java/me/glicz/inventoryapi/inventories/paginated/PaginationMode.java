package me.glicz.inventoryapi.inventories.paginated;

import lombok.AllArgsConstructor;
import me.glicz.inventoryapi.inventories.GuiItem;
import me.glicz.inventoryapi.inventories.PaginatedInventory;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

@AllArgsConstructor
public enum PaginationMode {
    NORMAL((player, inventory) -> {
        Margins margins = inventory.getMargins();
        int page = inventory.getPage(player);
        int itemsPerPage = inventory.getSize() - margins.sumSlots(inventory.getSize() / 9);
        GuiItem[] pageItems = inventory.getPageItems().toArray(GuiItem[]::new);
        int to = (page + 1) * itemsPerPage;
        if (to < pageItems.length)
            return Arrays.copyOfRange(pageItems, page * itemsPerPage, to);
        else
            return Arrays.copyOfRange(pageItems, page * itemsPerPage, pageItems.length);
    }, inventory -> {
        double itemsPerPage = inventory.getSize() - inventory.getMargins().sumSlots(inventory.getSize() / 9);
        return (int) Math.ceil(inventory.getPageItems().size() / itemsPerPage);
    }),
    SCROLLING((player, inventory) -> {
        Margins margins = inventory.getMargins();
        int page = inventory.getPage(player);
        int marginsSlotsSum = margins.sumSlots(inventory.getSize() / 9);
        int rowSize = 9 - margins.getLeft() - margins.getRight();
        GuiItem[] pageItems = inventory.getPageItems().toArray(GuiItem[]::new);
        int to = (int) ((inventory.getSize() / (double) rowSize + page) * rowSize - marginsSlotsSum);
        if (to < pageItems.length)
            return Arrays.copyOfRange(pageItems, page * rowSize, to);
        else
            return Arrays.copyOfRange(pageItems, page * rowSize, pageItems.length);
    }, inventory -> {
        Margins margins = inventory.getMargins();
        int rowCount = inventory.getSize() / 9 - margins.getTop() - margins.getBottom();
        double rowSize = 9 - margins.getLeft() - margins.getRight();
        return ((int) Math.ceil(inventory.getPageItems().size() / rowSize)) - rowCount + 1;
    });

    private final BiFunction<Player, PaginatedInventory, GuiItem[]> parser;
    private final Function<PaginatedInventory, Integer> pageCountParser;

    public GuiItem[] parse(Player player, PaginatedInventory inventory) {
        return parser.apply(player, inventory);
    }

    public int getPageCount(PaginatedInventory inventory) {
        return pageCountParser.apply(inventory);
    }
}
