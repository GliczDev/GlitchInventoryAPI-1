package me.glicz.glitchinventoryapi.types;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.glicz.glitchinventoryapi.GlitchInventoryAPI;
import me.glicz.glitchinventoryapi.utils.NMSUtil;
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
import net.minecraft.network.protocol.game.PacketPlayOutWindowItems;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;

public class GlitchInventory {

    private int id;
    private Player player;
    private final String title;
    private final ItemStack[] items;
    private boolean isOpen = false;

    private final HashMap<Integer, Consumer<? super SlotClickEvent>> slotClickListeners;
    private static final HashMap<UUID, GlitchInventory> currentInventories = new HashMap<>();

    public GlitchInventory(int rows, String title) {
        if (rows > 6) throw new IllegalArgumentException("Rows have to be less than 7");
        this.title = title;
        this.items = new ItemStack[9 * rows];
        Arrays.fill(items, new ItemStack(Material.AIR));
        this.slotClickListeners = new HashMap<>();
    }

    private GlitchInventory(ItemStack[] items, String title, HashMap<Integer, Consumer<? super SlotClickEvent>> slotListeners) {
        this.items = items;
        this.title = title;
        this.slotClickListeners = slotListeners;
    }

    public GlitchInventory fill(FillPattern fillPattern, ItemStack... itemStacks) {
        for (int i = 0; i <= items.length - 1; i++) {
            ItemStack itemStack = null;
            if (fillPattern == FillPattern.Normal) itemStack = itemStacks[0];
            if (fillPattern == FillPattern.Random) itemStack = itemStacks[new Random().nextInt(itemStacks.length)];
            if (fillPattern == FillPattern.Alternately) {
                int index = 0;
                if (i != 0) {
                    List<ItemStack> itemStacksList = List.of(itemStacks);
                    if (itemStacksList.indexOf(items[i - 1]) != itemStacks.length - 1) {
                        index = itemStacksList.indexOf(items[i - 1]) + 1;
                    }
                }
                itemStack = itemStacks[index];
            }
            setSlot(i, itemStack);
        }
        return this;
    }

    public GlitchInventory drawColumn(int column, ItemStack itemStack) {
        for (int i = 0; i <= (items.length / 3) - 1; i++) {
            setSlot(i * 9 + column, itemStack);
        }
        return this;
    }

    public GlitchInventory drawColumn(int column, ItemStack itemStack, Consumer<? super SlotClickEvent> action) {
        for (int i = 0; i <= (items.length / 3) - 1; i++) {
            setSlot(i * 9 + column, itemStack, action);
        }
        return this;
    }

    public GlitchInventory drawRow(int row, ItemStack itemStack) {
        for (int i = 0; i <= 8; i++) {
            setSlot(row * 9 + i, itemStack);
        }
        return this;
    }

    public GlitchInventory drawRow(int row, ItemStack itemStack, Consumer<? super SlotClickEvent> action) {
        for (int i = 0; i <= 8; i++) {
            setSlot(row * 9 + i, itemStack, action);
        }
        return this;
    }

    public ItemStack[] getItems() {
        return items;
    }

    public GlitchInventory setSlot(int slot, ItemStack itemStack) {
        items[slot] = itemStack;
        if (isOpen) {
            PacketContainer packet = new PacketContainer(PacketType.Play.Server.SET_SLOT);
            packet.getIntegers().write(0, id);
            packet.getIntegers().write(1, 0);
            packet.getIntegers().write(2, slot);
            packet.getItemModifier().write(0, itemStack);
            sendPacket(player, packet);
        }
        return this;
    }

    public GlitchInventory setSlot(int slot, ItemStack itemStack, Consumer<? super SlotClickEvent> action) {
        setSlot(slot, itemStack);
        slotClickListeners.put(slot, action);
        return this;
    }

    public GlitchInventory setSlot(int[] slots, ItemStack[] itemStacks) {
        if (slots.length != itemStacks.length)
            throw new IllegalArgumentException("Slots have to have the same amount of items as itemstacks");
        for (int i = 0; i <= slots.length - 1; i++) {
            setSlot(slots[i], itemStacks[i]);
        }
        return this;
    }

    public GlitchInventory setSlot(int[] slots, ItemStack[] itemStacks, Consumer<? super SlotClickEvent> action) {
        if (slots.length != itemStacks.length)
            throw new IllegalArgumentException("Slots have to have the same amount of items as itemstacks");
        for (int i = 0; i <= slots.length - 1; i++) {
            setSlot(slots[i], itemStacks[i], action);
        }
        return this;
    }

    public GlitchInventory setSlot(int[] slots, ItemStack itemStack) {
        for (int i : slots) {
            setSlot(i, itemStack);
        }
        return this;
    }

    public GlitchInventory setSlot(int[] slots, ItemStack itemStack, Consumer<? super SlotClickEvent> action) {
        for (int i : slots) {
            setSlot(i, itemStack, action);
        }
        return this;
    }

    public void unRegister() {
        currentInventories.remove(player.getUniqueId());
    }

    public GlitchInventory open(Player player) {
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

        PacketContainer packet = new PacketContainer(PacketType.Play.Server.OPEN_WINDOW);
        packet.getIntegers().write(0, id);
        packet.getModifier().write(1, GlitchInventoryAPI.getNMSUtil().getContainersClass(InventoryType.getByRows(items.length / 9).getFieldName()));
        packet.getChatComponents().write(0, WrappedChatComponent.fromText(title));
        sendPacket(player, packet);

        update();
        return this;
    }

    public GlitchInventory close() {
        unRegister();
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.CLOSE_WINDOW);
        packet.getIntegers().write(0, id);
        sendPacket(player, packet);
        return this;
    }

    public GlitchInventory update() {
        PacketContainer windowItemsPacket = new PacketContainer(PacketType.Play.Server.WINDOW_ITEMS);
        windowItemsPacket.getIntegers().write(0, id);
        windowItemsPacket.getIntegers().write(1, 0);
        windowItemsPacket.getItemListModifier().write(0, List.of(items));
        windowItemsPacket.getItemModifier().write(0, new ItemStack(Material.AIR));
        sendPacket(player, windowItemsPacket);
        return this;
    }

    public String getTitle() {
        return title;
    }

    public HashMap<Integer, Consumer<? super SlotClickEvent>> getSlotClickListeners() {
        return slotClickListeners;
    }

    public static GlitchInventory getByPlayer(Player player) {
        return currentInventories.get(player.getUniqueId());
    }

    public static HashMap<UUID, GlitchInventory> getCurrentInventories() {
        return currentInventories;
    }

    private static void sendPacket(Player player, PacketContainer packet) {
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GlitchInventory clone() {
        return new GlitchInventory(items, title, slotClickListeners);
    }
}
