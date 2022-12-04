package me.glicz.glitchinventoryapi.types.inventories;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.glicz.glitchinventoryapi.events.InventoryPageChangeEvent;
import me.glicz.glitchinventoryapi.types.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
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
        super(inventoryType, new GuiItem[inventoryType.getItems()]);
        Arrays.fill(items, ItemBuilder.from(Material.AIR).asGuiItem());
        setTitle(title);
    }

    private GlitchPagedInventory(InventoryType inventoryType, Title title, GuiItem[] items, List<GuiItem> pageItems) {
        super(inventoryType, items);
        setTitle(title);
        this.pageItems = pageItems;
    }

    public static GlitchPagedInventory fromSimple(GlitchSimpleInventory inventory) {
        return (GlitchPagedInventory) new GlitchPagedInventory(inventory.getInventoryType(), inventory.getTitle(),
                inventory.getItems(), List.of()).setId(inventory.getId());
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
