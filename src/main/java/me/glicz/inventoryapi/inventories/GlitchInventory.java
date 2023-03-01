package me.glicz.inventoryapi.inventories;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import lombok.*;
import me.glicz.inventoryapi.GlitchInventoryAPI;
import me.glicz.inventoryapi.events.InventoryCloseEvent;
import me.glicz.inventoryapi.events.InventoryOpenEvent;
import me.glicz.inventoryapi.events.ItemClickEvent;
import me.glicz.inventoryapi.itembuilders.ItemBuilder;
import me.glicz.inventoryapi.titles.Title;
import me.glicz.inventoryapi.types.FillPattern;
import me.glicz.inventoryapi.types.GuiItem;
import me.glicz.inventoryapi.types.InventoryType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Consumer;

@Data
@SuppressWarnings({"unchecked", "unused", "UnusedReturnValue"})
public abstract class GlitchInventory<T extends GlitchInventory<T>> {

    @Getter
    protected static final Map<UUID, GlitchInventory<?>> currentInventories = new HashMap<>();
    private final Map<Integer, Consumer<ItemClickEvent>> clickActions = new HashMap<>();
    private final InventoryType inventoryType;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final SecureRandom random = new SecureRandom();
    protected GuiItem[] items;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected Player player;
    private Title title;
    @Setter(AccessLevel.NONE)
    private boolean isOpen;
    @Setter(AccessLevel.NONE)
    private int id;
    @Setter(AccessLevel.NONE)
    private Consumer<ItemClickEvent> defaultClickAction;
    @Setter(AccessLevel.NONE)
    private Consumer<InventoryOpenEvent> openAction;
    @Setter(AccessLevel.NONE)
    private Consumer<InventoryCloseEvent> closeAction;

    protected GlitchInventory(InventoryType inventoryType, Title title) {
        this(inventoryType, title, new GuiItem[inventoryType.getItems()]);
        Arrays.fill(items, ItemBuilder.from(Material.AIR).asGuiItem());
    }

    protected GlitchInventory(InventoryType inventoryType, Title title, GuiItem[] items) {
        this.inventoryType = inventoryType;
        this.title = title;
        this.items = items;
    }

    public static SimpleInventory.Builder simple() {
        return SimpleInventory.builder();
    }

    public static PaginatedInventory.Builder paginated() {
        return PaginatedInventory.builder();
    }

    public static GlitchInventory<?> getByPlayer(Player player) {
        return currentInventories.get(player.getUniqueId());
    }

    public T setTitle(Title title) {
        title.setInventory(this);
        this.title = title;
        if (isOpen) resendInventory();
        return (T) this;
    }

    public GuiItem getItem(int slot) {
        try {
            return items[slot];
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public T setItem(int slot, GuiItem guiItem) {
        if (guiItem == null) return removeItem(slot);
        items[slot] = guiItem;
        if (isOpen) sendItem(slot, guiItem.getItemStack());
        return (T) this;
    }

    public T setItems(int[] slots, GuiItem... guiItems) {
        for (int i = 0; i < slots.length && i < guiItems.length; i++) {
            setItem(slots[i], guiItems[i]);
        }
        return (T) this;
    }

    public T removeItem(int slot) {
        items[slot] = ItemBuilder.from(Material.AIR).asGuiItem();
        if (isOpen) sendItem(slot, items[slot].getItemStack());
        return (T) this;
    }

    public T setClickAction(int slot, Consumer<ItemClickEvent> clickAction) {
        if (clickAction == null) clickActions.remove(slot);
        else clickActions.put(slot, clickAction);
        return (T) this;
    }

    public T drawColumn(int column, GuiItem guiItem) {
        for (int i = 0; i < inventoryType.getRows(); i++)
            setItem(i * 9 + column, guiItem);
        return (T) this;
    }

    public T drawRow(int row, GuiItem guiItem) {
        for (int i = 0; i < 9; i++)
            setItem(row * 9 + i, guiItem);
        return (T) this;
    }

    public T fill(FillPattern fillPattern, GuiItem... guiItems) {
        int item = 0;
        for (int i = 0; i < inventoryType.getItems(); i++) {
            if (fillPattern == FillPattern.NORMAL) {
                items[i] = guiItems[item];
                item = (item < guiItems.length - 1) ? item + 1 : 0;
            } else if (fillPattern == FillPattern.RANDOM) {
                items[i] = guiItems[random.nextInt(guiItems.length)];
            }
        }
        if (isOpen) update();
        return (T) this;
    }

    public void unRegister() {
        isOpen = false;
        currentInventories.remove(player.getUniqueId());
        if (closeAction != null)
            closeAction.accept(new InventoryCloseEvent(player, this));
    }

    public T open(Player player) {
        return open(player, true);
    }

    public T open(Player player, boolean closeCurrent) {
        if (this.player != null)
            return clone().open(player);
        if (currentInventories.containsKey(player.getUniqueId())) {
            if (closeCurrent) currentInventories.get(player.getUniqueId()).close();
            else currentInventories.get(player.getUniqueId()).unRegister();
        }
        if (id == 0) id = GlitchInventoryAPI.getNMSUtil().getNextContainerCounter(player);
        this.player = player;
        isOpen = true;
        currentInventories.put(player.getUniqueId(), this);
        if (openAction != null)
            openAction.accept(new InventoryOpenEvent(player, this));
        return resendInventory();
    }

    public T setDefaultClickAction(Consumer<ItemClickEvent> defaultClickAction) {
        this.defaultClickAction = defaultClickAction;
        return (T) this;
    }

    public T setOpenAction(Consumer<InventoryOpenEvent> openAction) {
        this.openAction = openAction;
        return (T) this;
    }

    public T setCloseAction(Consumer<InventoryCloseEvent> closeAction) {
        this.closeAction = closeAction;
        return (T) this;
    }

    @SneakyThrows
    private void sendItem(int slot, ItemStack itemStack) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SET_SLOT);
        packet.getIntegers().write(0, id);
        packet.getIntegers().write(1, 0);
        packet.getIntegers().write(2, slot);
        packet.getItemModifier().write(0, itemStack);
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
    }

    @SneakyThrows
    public void update() {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.WINDOW_ITEMS);
        packet.getIntegers().write(0, id);
        packet.getIntegers().write(1, 0);
        packet.getItemListModifier().write(0, getItemStacks());
        packet.getItemModifier().write(0, new ItemStack(Material.AIR));
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
    }

    @SneakyThrows
    public T resendInventory() {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.OPEN_WINDOW);
        packet.getIntegers().write(0, id);
        packet.getModifier().write(1, GlitchInventoryAPI.getNMSUtil().getContainersClass(inventoryType.getFieldName()));
        packet.getChatComponents().write(0, WrappedChatComponent.fromText(title.getString()));
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        update();
        return (T) this;
    }

    @SneakyThrows
    public T close() {
        unRegister();
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.CLOSE_WINDOW);
        packet.getIntegers().write(0, id);
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        return (T) this;
    }

    protected List<ItemStack> getItemStacks() {
        return Arrays.stream(items).map(GuiItem::getItemStack).toList();
    }

    public T clear() {
        Arrays.fill(items, ItemBuilder.from(Material.AIR).asGuiItem());
        update();
        return (T) this;
    }

    /**
     * @param id The new ID of inventory
     * @return GlitchInventory
     * @apiNote <b>WARNING!</b> Be careful when changing inventory id. If you don't know what you're doing <b>do not change it!</b>
     */
    public T setId(int id) {
        this.id = id;
        return (T) this;
    }

    public abstract T clone();
}
