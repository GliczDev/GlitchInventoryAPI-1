package me.glicz.glitchinventoryapi.inventories;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.glicz.glitchinventoryapi.events.InventoryPageChangeEvent;
import me.glicz.glitchinventoryapi.itembuilders.ItemBuilder;
import me.glicz.glitchinventoryapi.titles.Title;
import me.glicz.glitchinventoryapi.types.GuiItem;
import me.glicz.glitchinventoryapi.types.InventoryType;
import org.bukkit.Material;
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
        super(inventoryType, new GuiItem[inventoryType.getItems()]);
        Arrays.fill(items, ItemBuilder.from(Material.AIR).asGuiItem());
        setTitle(title);
    }

    private PaginatedInventory(InventoryType inventoryType, Title title, GuiItem[] items, List<GuiItem> pageItems) {
        super(inventoryType, items);
        setTitle(title);
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
            return getCurrentPageItems().get(slot);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    @Override
    protected ItemStack[] getItemStacks() {
        return getCurrentPageItems().stream().map(GuiItem::getItemStack).toArray(ItemStack[]::new);
    }

    public List<GuiItem> getCurrentPageItems() {
        List<GuiItem> value = new ArrayList<>(List.of(items));
        List<GuiItem> temp;
        int itemsPerPage = getInventoryType().getItems() - ((topMargin + bottomMargin) * 9);
        try {
            temp = pageItems.subList(page * itemsPerPage,
                    (page + 1) * itemsPerPage);
        } catch (IndexOutOfBoundsException ex) {
            temp = pageItems.subList(page * itemsPerPage, pageItems.size());
        }
        for (int i = 0; i < value.size() - topMargin * 9 && i < temp.size(); i++) {
            value.set(i + topMargin * 9, temp.get(i));
        }
        return value;
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
        return this;
    }

    public PaginatedInventory previousPage() {
        if (!hasPreviousPage())
            return this;
        page--;
        if (pageChangeAction != null)
            pageChangeAction.accept(new InventoryPageChangeEvent(player, this, hasNextPage(), hasPreviousPage(), page));
        if (isOpen()) update();
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
        return this;
    }
}