package me.glicz.inventoryapi.inventories;

import lombok.Getter;
import me.glicz.inventoryapi.GlitchInventoryAPI;
import me.glicz.inventoryapi.events.InventoryClickEvent;
import me.glicz.inventoryapi.events.InventoryCloseEvent;
import me.glicz.inventoryapi.events.InventoryOpenEvent;
import me.glicz.inventoryapi.events.Listener;
import me.glicz.inventoryapi.itembuilders.ItemBuilder;
import me.glicz.inventoryapi.nms.NMS;
import me.glicz.inventoryapi.titles.Title;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public abstract class GlitchInventory<T extends GlitchInventory<T>> {

    private static final Map<Player, GlitchInventory<?>> PLAYER_INVENTORY_MAP = new HashMap<>();
    protected final Map<Player, Integer> viewers = new HashMap<>();
    private final Map<Player, Map<Integer, GuiItem>> viewerItems = new HashMap<>();
    @Getter
    private final InventoryType inventoryType;
    @Getter
    private final int size;
    private final List<GuiItem> items;
    private final Map<Integer, List<Listener<InventoryClickEvent>>> slotClickListeners = new HashMap<>();
    private final List<Listener<InventoryOpenEvent>> openListeners = new ArrayList<>();
    private final List<Listener<InventoryCloseEvent>> closeListeners = new ArrayList<>();
    @Getter
    private Title title;

    protected GlitchInventory(InventoryType inventoryType) {
        this.inventoryType = inventoryType;
        this.size = inventoryType.getDefaultSize();
        this.items = new ArrayList<>(Collections.nCopies(size, ItemBuilder.of(Material.AIR).asGuiItem()));
        this.title = Title.simple(inventoryType.defaultTitle());
    }

    protected GlitchInventory(int rows) {
        this.inventoryType = InventoryType.CHEST;
        this.size = rows * 9;
        this.items = new ArrayList<>(Collections.nCopies(size, ItemBuilder.of(Material.AIR).asGuiItem()));
        this.title = Title.simple(inventoryType.defaultTitle());
    }

    public static SimpleInventory simple(InventoryType inventoryType) {
        return new SimpleInventory(inventoryType);
    }

    public static SimpleInventory simple(@Range(from = 1, to = 6) int rows) {
        return new SimpleInventory(rows);
    }

    public static PaginatedInventory paginated(InventoryType inventoryType) {
        return new PaginatedInventory(inventoryType);
    }

    public static PaginatedInventory paginated(@Range(from = 1, to = 6) int rows) {
        return new PaginatedInventory(rows);
    }

    public static MerchantInventory merchant() {
        return new MerchantInventory();
    }

    public static AnvilInventory anvil() {
        return new AnvilInventory();
    }

    public static GlitchInventory<?> get(Player player) {
        return PLAYER_INVENTORY_MAP.get(player);
    }

    public static boolean has(Player player) {
        return get(player) != null;
    }

    public T setItem(@Range(from = 0, to = Integer.MAX_VALUE) int slot, GuiItem item) {
        try {
            if (item == null)
                item = ItemBuilder.of(Material.AIR).asGuiItem();
            items.set(slot, item);
            if (GlitchInventoryAPI.getConfig().removeViewerItemOnItemSet())
                viewerItems.values().forEach(map -> map.remove(slot));
            final GuiItem finalItem = item;
            viewers.forEach((viewer, id) -> GlitchInventoryAPI.getNms().setItem(id, slot, viewer, finalItem.getItemStack()));
        } catch (IndexOutOfBoundsException ignored) {
        }
        return (T) this;
    }

    public T setItem(Player player, @Range(from = 0, to = Integer.MAX_VALUE) int slot, GuiItem item) {
        try {
            if (!viewerItems.containsKey(player))
                viewerItems.put(player, new HashMap<>());
            ItemStack itemStack;
            if (item != null) {
                viewerItems.get(player).put(slot, item);
                itemStack = item.getItemStack();
            } else {
                viewerItems.get(player).remove(slot);
                itemStack = items.get(slot).getItemStack();
            }
            if (viewers.containsKey(player))
                GlitchInventoryAPI.getNms().setItem(getId(player), slot, player, itemStack);
        } catch (IndexOutOfBoundsException ignored) {
        }
        return (T) this;
    }

    public GuiItem getItem(@Range(from = 0, to = Integer.MAX_VALUE) int slot) {
        try {
            return items.get(slot);
        } catch (IndexOutOfBoundsException ignored) {
            return ItemBuilder.of(Material.AIR).asGuiItem();
        }
    }

    /**
     * Allows you to get item from player's view.
     *
     * @param player Selected viewer
     * @param slot   Selected slot
     * @return Item from selected viewer's view
     */
    public GuiItem getItem(Player player, @Range(from = 0, to = Integer.MAX_VALUE) int slot) {
        try {
            return getViewerItems(player).get(slot);
        } catch (IndexOutOfBoundsException ignored) {
            return ItemBuilder.of(Material.AIR).asGuiItem();
        }
    }

    /**
     * @apiNote draft API
     */
    @ApiStatus.Experimental
    public T drawColumn(@Range(from = 0, to = Integer.MAX_VALUE) int column, GuiItem item) {
        for (int i = 0; i < getSize() / 9; i++)
            setItem(i * 9 + column, item);
        return (T) this;
    }

    /**
     * @apiNote draft API
     */
    @ApiStatus.Experimental
    public T drawColumn(Player player, @Range(from = 0, to = Integer.MAX_VALUE) int column, GuiItem item) {
        for (int i = 0; i < getSize() / 9; i++)
            setItem(player, i * 9 + column, item);
        return (T) this;
    }

    /**
     * @apiNote draft API
     */
    @ApiStatus.Experimental
    public T drawRow(@Range(from = 0, to = Integer.MAX_VALUE) int row, GuiItem item) {
        for (int i = 0; i < 9; i++)
            setItem(row * 9 + i, item);
        return (T) this;
    }

    /**
     * @apiNote draft API
     */
    @ApiStatus.Experimental
    public T drawRow(Player player, @Range(from = 0, to = Integer.MAX_VALUE) int row, GuiItem item) {
        for (int i = 0; i < 9; i++)
            setItem(player, row * 9 + i, item);
        return (T) this;
    }

    public T clearItems() {
        Collections.fill(items, ItemBuilder.of(Material.AIR).asGuiItem());
        updateItems();
        return (T) this;
    }

    public T setProperty(InventoryView.Property property, int value) {
        if (property.getType() == inventoryType)
            viewers.keySet().forEach(player -> setProperty(player, property, value));
        return (T) this;
    }

    public T setProperty(Player player, InventoryView.Property property, int value) {
        if (property.getType() == inventoryType)
            GlitchInventoryAPI.getNms().setProperty(getId(player), player, property, value);
        return (T) this;
    }

    @Unmodifiable
    public List<GuiItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    @Unmodifiable
    public List<ItemStack> getItemStacks() {
        return getItems().stream().map(GuiItem::getItemStack).toList();
    }

    @Unmodifiable
    public List<GuiItem> getViewerItems(Player player) {
        List<GuiItem> result = new ArrayList<>(items);
        viewerItems.getOrDefault(player, new HashMap<>()).forEach(result::set);
        return Collections.unmodifiableList(result);
    }

    @Unmodifiable
    public List<ItemStack> getViewerItemStacks(Player player) {
        return getViewerItems(player).stream().map(GuiItem::getItemStack).toList();
    }

    @Unmodifiable
    public Set<Player> getViewers() {
        return Collections.unmodifiableSet(viewers.keySet());
    }

    public int getId(Player player) {
        return viewers.get(player);
    }

    public T setTitle(Title title) {
        this.title = title;
        viewers.keySet().forEach(this::sendInventory);
        return (T) this;
    }

    public T setTitle(Player player, Title title) {
        sendInventory(player, title);
        return (T) this;
    }

    public T addSlotClickListener(int slot, Consumer<InventoryClickEvent> action) {
        return addSlotClickListener(slot, action, true);
    }

    public T addSlotClickListener(int slot, Consumer<InventoryClickEvent> action, boolean sync) {
        slotClickListeners.putIfAbsent(slot, new ArrayList<>());
        slotClickListeners.get(slot).add(new Listener<>(action, sync));
        return (T) this;
    }

    public T runSlotClickListeners(int slot, InventoryClickEvent event) {
        slotClickListeners.getOrDefault(slot, new ArrayList<>()).forEach(listener -> listener.run(event));
        return (T) this;
    }

    public T addOpenListener(Consumer<InventoryOpenEvent> action) {
        return addOpenListener(action, true);
    }

    public T addOpenListener(Consumer<InventoryOpenEvent> action, boolean sync) {
        openListeners.add(new Listener<>(action, sync));
        return (T) this;
    }

    public T runOpenListeners(InventoryOpenEvent event) {
        openListeners.forEach(listener -> listener.run(event));
        return (T) this;
    }

    public T addCloseListener(Consumer<InventoryCloseEvent> action) {
        return addCloseListener(action, true);
    }

    public T addCloseListener(Consumer<InventoryCloseEvent> action, boolean sync) {
        closeListeners.add(new Listener<>(action, sync));
        return (T) this;
    }

    public T runCloseListeners(InventoryCloseEvent event) {
        closeListeners.forEach(listener -> listener.run(event));
        return (T) this;
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
        player.closeInventory();
        if (id == null)
            id = GlitchInventoryAPI.getNms().getNextInventoryID(player);
        final int finalId = id;
        Bukkit.getScheduler().runTaskLater(GlitchInventoryAPI.getPlugin(), () -> {
            if (!viewers.containsKey(player))
                viewers.put(player, finalId);
            PLAYER_INVENTORY_MAP.put(player, this);
            sendInventory(player);
            updateItems(player);
            runOpenListeners(new InventoryOpenEvent(player, this));
        }, 1);
        return (T) this;
    }

    public T sendInventory(Player player) {
        return sendInventory(player, title);
    }

    public T sendInventory(Player player, Title title) {
        NMS nms = GlitchInventoryAPI.getNms();
        if (inventoryType == InventoryType.CHEST)
            nms.openInventory(getId(player), player, size / 9, title.getComponent());
        else
            nms.openInventory(getId(player), player, inventoryType, title.getComponent());
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
        viewerItems.remove(player);
        runCloseListeners(new InventoryCloseEvent(player, this));
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
        return (T) this;
    }

    public T updateItems() {
        viewers.keySet().forEach(this::updateItems);
        return (T) this;
    }

    public T updateItems(Player player) {
        GlitchInventoryAPI.getNms().setItems(getId(player), player, getViewerItemStacks(player));
        return (T) this;
    }
}
