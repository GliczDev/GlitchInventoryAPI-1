package me.glicz.glitchinventoryapi.types;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import lombok.*;
import me.glicz.glitchinventoryapi.GlitchInventoryAPI;
import me.glicz.glitchinventoryapi.events.InventoryCloseEvent;
import me.glicz.glitchinventoryapi.events.InventoryOpenEvent;
import me.glicz.glitchinventoryapi.events.ItemClickEvent;
import me.glicz.glitchinventoryapi.types.inventories.GlitchPagedInventory;
import me.glicz.glitchinventoryapi.types.inventories.GlitchSimpleInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Consumer;

@Data
@RequiredArgsConstructor
@SuppressWarnings({"unchecked", "unused"})
public abstract class GlitchInventory<T extends GlitchInventory<T>> {

    public static GlitchSimpleInventory.Builder simple() {
        return GlitchSimpleInventory.builder();
    }
    public static GlitchPagedInventory.Builder paged() {
        return GlitchPagedInventory.builder();
    }

    private final InventoryType inventoryType;
    @Getter
    private Title title;
    @Setter(AccessLevel.NONE)
    private boolean isOpen;
    @NonNull
    protected GuiItem[] items;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private int id;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected Player player;

    @Getter
    protected static final Map<UUID, GlitchInventory<?>> currentInventories = new HashMap<>();
    private final Map<Integer, Consumer<ItemClickEvent>> clickActions = new HashMap<>();

    @Setter(AccessLevel.NONE)
    private Consumer<ItemClickEvent> defaultClickAction;
    @Setter(AccessLevel.NONE)
    private Consumer<InventoryOpenEvent> openAction;
    @Setter(AccessLevel.NONE)
    private Consumer<InventoryCloseEvent> closeAction;

    public T setTitle(Title title) {
        title.setInventory(this);
        this.title = title;
        if (isOpen) sendInventory();
        return (T) this;
    }

    public T setItem(int slot, GuiItem guiItem) {
        if (guiItem == null) return removeItem(slot);
        items[slot] = guiItem;
        if (isOpen) {
            sendItem(slot, guiItem.getItemStack());
        }
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
        if (isOpen) {
            sendItem(slot, items[slot].getItemStack());
        }
        return (T) this;
    }

    public T setClickAction(int slot, Consumer<ItemClickEvent> clickAction) {
        clickActions.put(slot, clickAction);
        return (T) this;
    }

    public T drawColumn(int column, GuiItem guiItem) {
        for (int i = 0; i < inventoryType.getRows(); i++) {
            setItem(i * 9 + column, guiItem);
        }
        return (T) this;
    }

    public T drawRow(int row, GuiItem guiItem) {
        for (int i = 0; i < 9; i++) {
            setItem(row * 9 + i, guiItem);
        }
        return (T) this;
    }

    public T fill(FillPattern fillPattern, GuiItem... guiItems) {
        int item = 0;
        Random r = new Random();
        for (int i = 0; i < inventoryType.getItems(); i++) {
            if (fillPattern == FillPattern.Normal) {
                items[i] = guiItems[item];
                item = (item < guiItems.length - 1) ? item + 1 : 0;
            } else if (fillPattern == FillPattern.Random) {
                items[i] = guiItems[r.nextInt(guiItems.length)];
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
        if (this.player != null) {
            return clone().open(player);
        }
        if (currentInventories.containsKey(player.getUniqueId())) {
            currentInventories.get(player.getUniqueId()).close();
        }
        id = GlitchInventoryAPI.getNMSUtil().getNextContainerCounter(player);
        this.player = player;
        isOpen = true;
        currentInventories.put(player.getUniqueId(), this);
        if (openAction != null)
            openAction.accept(new InventoryOpenEvent(player, this));
        return sendInventory();
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
        List<ItemStack> itemStacks;
        try {
            itemStacks = List.of(getItemStacks());
        } catch (NullPointerException ex) {
            itemStacks = new ArrayList<>();
        }
        packet.getItemListModifier().write(0, itemStacks);
        packet.getItemModifier().write(0, new ItemStack(Material.AIR));
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
    }

    @SneakyThrows
    private T sendInventory() {
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

    protected ItemStack[] getItemStacks() {
        return Arrays.stream(items).map(GuiItem::getItemStack).toArray(ItemStack[]::new);
    }

    public abstract GuiItem getItem(int slot);

    public abstract T clone();

    public static GlitchInventory<?> getByPlayer(Player player) {
        return currentInventories.get(player.getUniqueId());
    }
}
