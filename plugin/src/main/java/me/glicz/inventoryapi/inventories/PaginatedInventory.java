package me.glicz.inventoryapi.inventories;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.glicz.inventoryapi.GlitchInventoryAPI;
import me.glicz.inventoryapi.events.paginated.InventoryPageChangeEvent;
import me.glicz.inventoryapi.itembuilders.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.Range;

import java.util.*;
import java.util.function.Consumer;

public class PaginatedInventory extends GlitchInventory<PaginatedInventory> {

    private final Map<Player, List<GuiItem>> currentPageItemsMap = new HashMap<>();
    private final Map<Player, Integer> pageMap = new HashMap<>();
    @Getter
    private List<GuiItem> pageItems;
    private Margins margins = Margins.zero();
    @Setter
    @Accessors(chain = true)
    private Consumer<InventoryPageChangeEvent> pageChangeAction;

    protected PaginatedInventory(InventoryType inventoryType) {
        super(inventoryType);
        pageItems = new ArrayList<>(Collections.nCopies(getSize(), ItemBuilder.of(Material.AIR).asGuiItem()));
    }

    protected PaginatedInventory(int rows) {
        super(rows);
        pageItems = new ArrayList<>(Collections.nCopies(getSize(), ItemBuilder.of(Material.AIR).asGuiItem()));
    }

    public PaginatedInventory setPageItems(List<GuiItem> pageItems) {
        this.pageItems = pageItems;
        updateItems();
        return this;
    }

    public List<GuiItem> getCurrentPageItems(Player player) {
        return currentPageItemsMap.getOrDefault(player, List.of());
    }

    public PaginatedInventory updateCurrentPageItems(Player player) {
        GuiItem[] value = super.getItems().toArray(GuiItem[]::new);
        GuiItem[] temp;
        int page = getPage(player);
        int itemsPerPage = getSize() - margins.sumSlots(getSize() / 9);
        if ((page + 1) * itemsPerPage <= pageItems.size())
            temp = Arrays.copyOfRange(pageItems.toArray(GuiItem[]::new), page * itemsPerPage, (page + 1) * itemsPerPage);
        else
            temp = Arrays.copyOfRange(pageItems.toArray(GuiItem[]::new), page * itemsPerPage, pageItems.size());
        int slot = margins.getTop() * 9 + margins.getLeft();
        for (int i = 0; i < value.length - margins.getTop() * 9 && i < temp.length; i++) {
            if ((slot / 9 + 1) * 9 - margins.getRight() == slot)
                slot += margins.getRight() + margins.getLeft();
            value[slot] = temp[i];
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
        updateItems();
        return this;
    }

    public int getPage(Player player) {
        return pageMap.getOrDefault(player, 0);
    }

    public PaginatedInventory setPage(Player player, @Range(from = 0, to = Integer.MAX_VALUE) int page) {
        if (!getViewers().contains(player))
            return this;
        int oldPage = getPage(player);
        pageMap.put(player, page);
        if (pageChangeAction != null)
            pageChangeAction.accept(new InventoryPageChangeEvent(player, this, page, oldPage));
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
    public OpenResult<PaginatedInventory> open(Player player, boolean closeCurrent) {
        if (has(player) && !has(player, false))
            return new OpenResult<>(false, this);
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
        return super.silentClose(player);
    }

    public void executePageChangeAction(InventoryPageChangeEvent event) {
        if (pageChangeAction == null)
            return;
        pageChangeAction.accept(event);
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
        return !(pageItems.size() < (getPage(player) + 1) * (getSize() - margins.sumSlots(getSize() / 9)));
    }

    public boolean hasPreviousPage(Player player) {
        return getPage(player) > 0;
    }
}
