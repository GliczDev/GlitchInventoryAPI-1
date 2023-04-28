package me.glicz.inventoryapi.inventories;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.glicz.inventoryapi.GlitchInventoryAPI;
import me.glicz.inventoryapi.events.paginated.InventoryPageChangeEvent;
import me.glicz.inventoryapi.inventories.paginated.Margins;
import me.glicz.inventoryapi.inventories.paginated.PaginationMode;
import me.glicz.inventoryapi.itembuilders.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.function.Consumer;

public class PaginatedInventory extends GlitchInventory<PaginatedInventory> {

    private final Map<Player, List<GuiItem>> currentPageItemsMap = new HashMap<>();
    private final Map<Player, Integer> pageMap = new HashMap<>();
    private List<GuiItem> pageItems = new ArrayList<>();
    private Margins margins = Margins.zero();
    @Getter
    private PaginationMode paginationMode = PaginationMode.NORMAL;
    @Setter
    @Accessors(chain = true)
    private Consumer<InventoryPageChangeEvent> pageChangeAction;

    protected PaginatedInventory(InventoryType inventoryType) {
        super(inventoryType);
    }

    protected PaginatedInventory(int rows) {
        super(rows);
    }

    @Unmodifiable
    public List<GuiItem> getPageItems() {
        return Collections.unmodifiableList(pageItems);
    }

    public PaginatedInventory setPageItems(List<GuiItem> pageItems) {
        this.pageItems = pageItems;
        getViewers().forEach(this::updateCurrentPageItems);
        updateItems();
        return this;
    }

    @Unmodifiable
    public List<GuiItem> getCurrentPageItems(Player player) {
        return currentPageItemsMap.getOrDefault(player, List.of());
    }

    public PaginatedInventory updateCurrentPageItems(Player player) {
        GuiItem[] value = getViewerItems(player).toArray(GuiItem[]::new);
        GuiItem[] temp = paginationMode.parse(player, this);
        int slot = margins.getTop() * 9 + margins.getLeft();
        for (int i = 0; i < value.length - margins.getTop() * 9 && i < temp.length; i++) {
            GuiItem item = temp[i];
            if (item != null)
                value[slot] = item;
            if ((slot / 9 + 1) * 9 - margins.getRight() - 1 == slot)
                slot += margins.getLeft() + margins.getRight();
            slot++;
        }
        currentPageItemsMap.put(player, List.of(value));
        return this;
    }

    public Margins getMargins() {
        return Margins.copy(margins);
    }

    public PaginatedInventory setMargins(Margins margins) {
        this.margins = margins;
        getViewers().forEach(this::updateCurrentPageItems);
        updateItems();
        return this;
    }

    public PaginatedInventory setPaginationMode(PaginationMode paginationMode) {
        this.paginationMode = paginationMode;
        getViewers().forEach(this::updateCurrentPageItems);
        updateItems();
        return this;
    }

    public int getPage(Player player) {
        return pageMap.getOrDefault(player, 0);
    }

    public PaginatedInventory setPage(@Range(from = 0, to = Integer.MAX_VALUE) int page) {
        getViewers().forEach(player -> setPage(player, page));
        return this;
    }

    public PaginatedInventory setPage(Player player, @Range(from = 0, to = Integer.MAX_VALUE) int page) {
        if (!getViewers().contains(player))
            return this;
        int oldPage = getPage(player);
        pageMap.put(player, page);
        executePageChangeAction(new InventoryPageChangeEvent(player, this, page, oldPage));
        if (getViewers().contains(player)) {
            updateCurrentPageItems(player);
            updateItems(player);
        }
        return this;
    }

    @Override
    public GuiItem getItem(Player player, @Range(from = 0, to = Integer.MAX_VALUE) int slot) {
        try {
            return getCurrentPageItems(player).get(slot);
        } catch (IndexOutOfBoundsException ignored) {
            return ItemBuilder.of(Material.AIR).asGuiItem();
        }
    }

    @Override
    public PaginatedInventory open(Player player, boolean closeCurrent) {
        executePageChangeAction(new InventoryPageChangeEvent(player, this, 0, 0));
        pageMap.put(player, 0);
        updateCurrentPageItems(player);
        return super.open(player, closeCurrent);
    }

    @Override
    public PaginatedInventory updateItems(Player player) {
        GlitchInventoryAPI.getNms().setItems(getId(player), player, getCurrentPageItems(player).stream().map(GuiItem::getItemStack).toList());
        return this;
    }

    @Override
    public PaginatedInventory silentClose(Player player) {
        pageMap.remove(player);
        currentPageItemsMap.remove(player);
        return super.silentClose(player);
    }

    public void executePageChangeAction(InventoryPageChangeEvent event) {
        if (pageChangeAction == null)
            return;
        pageChangeAction.accept(event);
    }

    public int getPageCount() {
        return paginationMode.getPageCount(this);
    }

    public PaginatedInventory nextPage() {
        getViewers().forEach(this::nextPage);
        return this;
    }

    public PaginatedInventory nextPage(Player player) {
        if (hasNextPage(player))
            setPage(player, getPage(player) + 1);
        return this;
    }

    public PaginatedInventory previousPage() {
        getViewers().forEach(this::previousPage);
        return this;
    }

    public PaginatedInventory previousPage(Player player) {
        if (hasPreviousPage(player))
            setPage(player, getPage(player) - 1);
        return this;
    }

    public boolean hasNextPage(Player player) {
        return getPage(player) < getPageCount() - 1;
    }

    public boolean hasPreviousPage(Player player) {
        return getPage(player) > 0;
    }
}
