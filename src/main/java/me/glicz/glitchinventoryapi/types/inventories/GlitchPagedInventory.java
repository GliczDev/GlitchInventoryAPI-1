package me.glicz.glitchinventoryapi.types.inventories;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.glicz.glitchinventoryapi.events.InventoryPageChangeEvent;
import me.glicz.glitchinventoryapi.types.GlitchInventory;
import me.glicz.glitchinventoryapi.types.GuiItem;
import me.glicz.glitchinventoryapi.types.InventoryType;
import me.glicz.glitchinventoryapi.types.Title;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class GlitchPagedInventory extends GlitchInventory<GlitchPagedInventory> {

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
    private GlitchPagedInventory(InventoryType inventoryType, Title title) {
        super(inventoryType, new ArrayList<>(Collections.nCopies(inventoryType.getItems(), new GuiItem(Material.AIR))));
        setTitle(title);
    }

    private GlitchPagedInventory(InventoryType inventoryType, Title title, List<GuiItem> items, List<GuiItem> pageItems) {
        super(inventoryType, items);
        setTitle(title);
        this.pageItems = pageItems;
    }

    @Override
    public GlitchPagedInventory open(Player player) {
        if (pageChangeAction != null)
            pageChangeAction.accept(new InventoryPageChangeEvent(player, this, hasNextPage(), hasPreviousPage(), page));
        return super.open(player);
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
    public List<ItemStack> getItemStacks() {
        return getCurrentPageItems().stream().map(GuiItem::getItemStack).toList();
    }

    private List<GuiItem> getCurrentPageItems() {
        List<GuiItem> value = new ArrayList<>(items);
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
    public GlitchPagedInventory clone() {
        return new GlitchPagedInventory(getInventoryType(), getTitle().clone(), items, pageItems);
    }

    public GlitchPagedInventory nextPage() {
        if (!hasNextPage()) {
            return this;
        }
        page++;
        if (pageChangeAction != null)
            pageChangeAction.accept(new InventoryPageChangeEvent(player, this, hasNextPage(), hasPreviousPage(), page));
        if (isOpen()) update();
        return this;
    }

    public GlitchPagedInventory previousPage() {
        if (!hasPreviousPage()) {
            return this;
        }
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

    public GlitchPagedInventory setPageItems(List<GuiItem> pageItems) {
        this.pageItems = pageItems;
        if (isOpen()) update();
        return this;
    }
}
