package me.glicz.inventoryapi.inventories;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.glicz.inventoryapi.GlitchInventoryAPI;
import me.glicz.inventoryapi.events.InventoryClickEvent;
import me.glicz.inventoryapi.events.InventoryCloseEvent;
import me.glicz.inventoryapi.events.InventoryOpenEvent;
import me.glicz.inventoryapi.itembuilders.ItemBuilder;
import me.glicz.inventoryapi.nms.NMS;
import me.glicz.inventoryapi.titles.Title;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public abstract class GlitchInventory<T extends GlitchInventory<T>> {

    @SuppressWarnings("deprecation")
    public static final NamespacedKey OPENED_INVENTORY_KEY = new NamespacedKey("glitchinventoryapi", "inventory-opened");

    private static final Map<Player, GlitchInventory<?>> PLAYER_INVENTORY_MAP = new HashMap<>();
    protected final Map<Player, Integer> viewers = new HashMap<>();
    @Getter
    private final InventoryType inventoryType;
    @Getter
    private final int size;
    private final List<GuiItem> items;
    private final Map<Integer, Consumer<InventoryClickEvent>> slotClickActions = new HashMap<>();
    @Setter
    @Accessors(chain = true)
    private Consumer<InventoryOpenEvent> openAction;
    @Setter
    @Accessors(chain = true)
    private Consumer<InventoryCloseEvent> closeAction;
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

    public static GlitchInventory<?> get(Player player) {
        return PLAYER_INVENTORY_MAP.get(player);
    }

    public static boolean has(Player player) {
        return has(player, true);
    }

    public static boolean has(Player player, boolean deep) {
        if (deep)
            return player.getPersistentDataContainer().has(OPENED_INVENTORY_KEY, PersistentDataType.BYTE);
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

    public GuiItem getItem(@Range(from = 0, to = Integer.MAX_VALUE) int slot) {
        try {
            return items.get(slot);
        } catch (IndexOutOfBoundsException ignored) {
            return ItemBuilder.of(Material.AIR).asGuiItem();
        }
    }

    /**
     * Allows you to get item from player's view.
     * Better implemented in paginated-based inventories.
     *
     * @param player Selected viewer
     * @param slot   Selected slot
     * @return Item from selected viewer's view
     */
    public GuiItem getItem(Player player, @Range(from = 0, to = Integer.MAX_VALUE) int slot) {
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
        return getItems().stream().map(GuiItem::getItemStack).toList();
    }

    public Set<Player> getViewers() {
        return viewers.keySet();
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

    public void executeOpenAction(InventoryOpenEvent event) {
        if (openAction == null)
            return;
        openAction.accept(event);
    }

    public void executeCloseAction(InventoryCloseEvent event) {
        if (closeAction == null)
            return;
        closeAction.accept(event);
    }

    public OpenResult<T> open(Player player) {
        return open(player, true);
    }

    public OpenResult<T> open(Player player, boolean closeCurrent) {
        Integer id = null;
        if (has(player)) {
            if (!has(player, false))
                return new OpenResult<>(false, (T) this);
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
        player.getPersistentDataContainer().set(OPENED_INVENTORY_KEY, PersistentDataType.BYTE, (byte) 1);
        sendInventory(player);
        updateItems(player);
        executeOpenAction(new InventoryOpenEvent(player, this));
        return new OpenResult<>(true, (T) this);
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
        player.getPersistentDataContainer().remove(OPENED_INVENTORY_KEY);
        viewers.remove(player);
        executeCloseAction(new InventoryCloseEvent(player, this));
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
