package me.glicz.inventoryapi.inventories;

import lombok.Getter;
import me.glicz.inventoryapi.GlitchInventoryAPI;
import me.glicz.inventoryapi.events.InventoryClickEvent;
import me.glicz.inventoryapi.itembuilders.ItemBuilder;
import me.glicz.inventoryapi.nms.NMS;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public abstract class GlitchInventory<T extends GlitchInventory<T>> {

    private static final Map<Player, GlitchInventory<?>> PLAYER_INVENTORY_MAP = new HashMap<>();
    @Getter
    private final InventoryType inventoryType;
    @Getter
    private final int size;
    private final List<GuiItem> items;
    private final Map<Player, Integer> viewers = new HashMap<>();
    private final Map<Integer, Consumer<InventoryClickEvent>> slotClickActions = new HashMap<>();

    protected GlitchInventory(InventoryType inventoryType) {
        this.inventoryType = inventoryType;
        this.size = inventoryType.getDefaultSize();
        this.items = new ArrayList<>(Collections.nCopies(size, ItemBuilder.of(Material.AIR).asGuiItem()));
    }

    protected GlitchInventory(int rows) {
        this.inventoryType = InventoryType.CHEST;
        this.size = rows * 9;
        this.items = new ArrayList<>(Collections.nCopies(size, ItemBuilder.of(Material.AIR).asGuiItem()));
    }

    public static SimpleInventory simple(InventoryType inventoryType) {
        return new SimpleInventory(inventoryType);
    }

    public static SimpleInventory simple(@Range(from = 1, to = 6) int rows) {
        return new SimpleInventory(rows);
    }

    public static MerchantInventory merchant() {
        return new MerchantInventory();
    }

    public static GlitchInventory<?> get(Player player) {
        return PLAYER_INVENTORY_MAP.get(player);
    }

    public static boolean has(Player player) {
        return get(player) != null;
    }

    public T setItem(@Range(from = 0, to = Integer.MAX_VALUE) int slot, @NotNull GuiItem item) {
        try {
            items.set(slot, item);
            viewers.forEach((viewer, id) -> GlitchInventoryAPI.getNms().setItem(id, slot, viewer, item.getItemStack()));
        } catch (IndexOutOfBoundsException ignored) {
        }
        return (T) this;
    }

    public @NotNull GuiItem getItem(@Range(from = 0, to = Integer.MAX_VALUE) int slot) {
        try {
            return items.get(slot);
        } catch (IndexOutOfBoundsException ignored) {
            return ItemBuilder.of(Material.AIR).asGuiItem();
        }
    }

    public T setProperty(InventoryView.Property property, int value) {
        if (property.getType() == inventoryType)
            viewers.keySet().forEach(player -> setProperty(player, property, value));
        return (T) this;
    }

    public T setProperty(Player player, InventoryView.Property property, int value) {
        GlitchInventoryAPI.getNms().setProperty(getId(player), player, property, value);
        return (T) this;
    }

    public List<GuiItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public List<ItemStack> getItemStacks() {
        return items.stream().map(GuiItem::getItemStack).toList();
    }

    public Set<Player> getViewers() {
        return viewers.keySet();
    }

    public int getId(Player player) {
        return viewers.get(player);
    }

    public T addSlotClickAction(int slot, Consumer<InventoryClickEvent> action) {
        slotClickActions.put(slot, action);
        return (T) this;
    }

    public T removeSlotClickAction(int slot) {
        slotClickActions.remove(slot);
        return (T) this;
    }

    public void executeSlotClickAction(int slot, InventoryClickEvent event) {
        if (!slotClickActions.containsKey(slot))
            return;
        slotClickActions.get(slot).accept(event);
    }

    public T open(Player player) {
        return open(player, true);
    }

    public T open(Player player, boolean closeCurrent) {
        Integer id = null;
        if (has(player)) {
            GlitchInventory<?> current = get(player);
            if (closeCurrent)
                current.close(player);
            else {
                id = current.getId(player);
                current.silentClose(player);
            }
        }
        NMS nms = GlitchInventoryAPI.getNms();
        if (id == null)
            id = nms.getNextInventoryID(player);
        if (!viewers.containsKey(player))
            viewers.put(player, id);
        PLAYER_INVENTORY_MAP.put(player, this);
        if (inventoryType == InventoryType.CHEST)
            nms.openInventory(id, player, size / 9, inventoryType.defaultTitle());
        else
            nms.openInventory(id, player, inventoryType, inventoryType.defaultTitle());
        nms.setItems(id, player, getItemStacks());
        return (T) this;
    }

    public T silentClose() {
        viewers.keySet().forEach(this::silentClose);
        return (T) this;
    }

    public T silentClose(Player player) {
        if (!viewers.containsKey(player))
            return (T) this;
        PLAYER_INVENTORY_MAP.remove(player);
        viewers.remove(player);
        return (T) this;
    }

    public T close() {
        viewers.keySet().forEach(this::close);
        return (T) this;
    }

    public T close(Player player) {
        if (!viewers.containsKey(player))
            return (T) this;
        GlitchInventoryAPI.getNms().closeInventory(viewers.get(player), player);
        return silentClose(player);
    }

    public T updateItems() {
        viewers.keySet().forEach(this::updateItems);
        return (T) this;
    }

    public T updateItems(Player player) {
        GlitchInventoryAPI.getNms().setItems(getId(player), player, getItemStacks());
        return (T) this;
    }
}
