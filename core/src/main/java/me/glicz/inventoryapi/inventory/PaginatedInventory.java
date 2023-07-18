package me.glicz.inventoryapi.inventory;

import lombok.Getter;
import me.glicz.inventoryapi.GlitchInventoryAPI;
import me.glicz.inventoryapi.event.Listener;
import me.glicz.inventoryapi.event.paginated.InventoryPageChangeEvent;
import me.glicz.inventoryapi.inventory.paginated.Margins;
import me.glicz.inventoryapi.inventory.paginated.PaginationDirection;
import me.glicz.inventoryapi.inventory.paginated.PaginationMode;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.function.Consumer;

public class PaginatedInventory extends GlitchInventory<PaginatedInventory> {

    private final Map<Player, List<GuiItem>> currentPageItemsMap = new HashMap<>();
    private final Map<Player, Integer> pageMap = new HashMap<>();
    private final List<Listener<InventoryPageChangeEvent>> pageChangeListeners = new ArrayList<>();
    private List<GuiItem> pageItems = new ArrayList<>();
    private Margins margins = Margins.zero();
    @Getter
    private PaginationMode paginationMode = PaginationMode.NORMAL;
    @Getter
    private PaginationDirection paginationDirection = PaginationDirection.DOWN;

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
        GuiItem[] temp = paginationDirection.parse(this, paginationMode.parse(player, this));
        for (int i = 0; i < value.length; i++) {
            if (temp[i] != null)
                value[i] = temp[i];
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

    public PaginatedInventory setPaginationDirection(PaginationDirection paginationDirection) {
        this.paginationDirection = paginationDirection;
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
        if (getPage(player) == page)
            return this;
        InventoryPageChangeEvent event = new InventoryPageChangeEvent(player, this, page, getPage(player));
        runPageChangeListeners(event);
        if (event.isCancelled())
            return this;
        pageMap.put(player, page);
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
            return GuiItem.EMPTY;
        }
    }

    @Override
    public PaginatedInventory open(Player player, boolean closeCurrent) {
        if (viewers.containsKey(player)) {
            sendInventory(player);
            updateCurrentPageItems(player);
            updateItems(player);
            return this;
        }
        runPageChangeListeners(new InventoryPageChangeEvent(player, this, 0, 0));
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

    public PaginatedInventory addPageChangeListener(Consumer<InventoryPageChangeEvent> action) {
        return addPageChangeListener(action, true);
    }

    public PaginatedInventory addPageChangeListener(Consumer<InventoryPageChangeEvent> action, boolean sync) {
        pageChangeListeners.add(new Listener<>(action, sync));
        return this;
    }

    public PaginatedInventory runPageChangeListeners(InventoryPageChangeEvent event) {
        pageChangeListeners.forEach(listener -> listener.run(event));
        return this;
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
