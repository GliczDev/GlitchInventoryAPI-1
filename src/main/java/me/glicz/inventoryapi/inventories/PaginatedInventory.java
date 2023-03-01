package me.glicz.inventoryapi.inventories;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.glicz.inventoryapi.events.InventoryPageChangeEvent;
import me.glicz.inventoryapi.titles.Title;
import me.glicz.inventoryapi.types.GuiItem;
import me.glicz.inventoryapi.types.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class PaginatedInventory extends GlitchInventory<PaginatedInventory> {

    @Getter
    private List<GuiItem> pageItems = new ArrayList<>();
    @Getter
    private GuiItem[] currentPageItems;
    @Getter
    private int page = 0;
    @Getter
    @Setter
    @Accessors(chain = true)
    private int topMargin = 0;
    @Getter
    @Setter
    @Accessors(chain = true)
    private int bottomMargin = 0;

    @Getter
    @Setter
    @Accessors(chain = true)
    private Consumer<InventoryPageChangeEvent> pageChangeAction;

    @lombok.Builder(builderClassName = "Builder", buildMethodName = "create")
    private PaginatedInventory(InventoryType inventoryType, Title title) {
        super(inventoryType, title);
    }

    private PaginatedInventory(InventoryType inventoryType, Title title, GuiItem[] items, List<GuiItem> pageItems) {
        super(inventoryType, title, items);
        this.pageItems = pageItems;
    }

    public static PaginatedInventory fromSimple(SimpleInventory inventory) {
        return new PaginatedInventory(inventory.getInventoryType(), inventory.getTitle(),
                inventory.getItems(), List.of()).setId(inventory.getId());
    }

    @Override
    public PaginatedInventory resendInventory() {
        if (pageChangeAction != null)
            pageChangeAction.accept(new InventoryPageChangeEvent(player, this, hasNextPage(), hasPreviousPage(), page));
        return super.resendInventory();
    }

    @Override
    public GuiItem getItem(int slot) {
        try {
            return getCurrentPageItems()[(slot)];
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    @Override
    protected List<ItemStack> getItemStacks() {
        updateCurrentPageItems();
        return Arrays.stream(getCurrentPageItems()).map(GuiItem::getItemStack).toList();
    }

    public void updateCurrentPageItems() {
        GuiItem[] value = items.clone();
        GuiItem[] temp;
        int itemsPerPage = getInventoryType().getItems() - ((topMargin + bottomMargin) * 9);
        if ((page + 1) * itemsPerPage <= pageItems.size()) {
            temp = Arrays.copyOfRange(pageItems.toArray(GuiItem[]::new), page * itemsPerPage, (page + 1) * itemsPerPage);
        } else {
            temp = Arrays.copyOfRange(pageItems.toArray(GuiItem[]::new), page * itemsPerPage, pageItems.size());
        }
        for (int i = 0; i < value.length - topMargin * 9 && i < temp.length; i++)
            value[i + topMargin * 9] = temp[i];
        currentPageItems = value;
    }

    @Override
    public PaginatedInventory clone() {
        return new PaginatedInventory(getInventoryType(), getTitle().clone(), items, pageItems);
    }

    public PaginatedInventory nextPage() {
        if (!hasNextPage())
            return this;
        page++;
        if (pageChangeAction != null)
            pageChangeAction.accept(new InventoryPageChangeEvent(player, this, hasNextPage(), hasPreviousPage(), page));
        if (isOpen()) update();
        else updateCurrentPageItems();
        return this;
    }

    public PaginatedInventory previousPage() {
        if (!hasPreviousPage())
            return this;
        page--;
        if (pageChangeAction != null)
            pageChangeAction.accept(new InventoryPageChangeEvent(player, this, hasNextPage(), hasPreviousPage(), page));
        if (isOpen()) update();
        else updateCurrentPageItems();
        return this;
    }

    private boolean hasNextPage() {
        return !(pageItems.size() < (page + 1) * (getInventoryType().getItems() - ((topMargin + bottomMargin) * 9)));
    }

    private boolean hasPreviousPage() {
        return page != 0;
    }

    public PaginatedInventory setPageItems(List<GuiItem> pageItems) {
        this.pageItems = pageItems;
        if (isOpen()) update();
        else updateCurrentPageItems();
        return this;
    }
}
