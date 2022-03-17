package me.glicz.glitchinventoryapi.types;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GlitchInventory {

    private final int id;
    private final String equalID;
    private final String title;
    private final Player player;
    private final ItemStack[] items;

    private static final HashMap<String, HashMap<Integer, SlotClickListener>> slotListeners = new HashMap<>();
    private static final HashMap<String, GlitchInventory> currentInventories = new HashMap<>();

    public GlitchInventory(int rows, String title, Player player) {
        if (rows > 6) throw new IllegalArgumentException("Rows have to be less than 7");
        EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
        id = entityPlayer.nextContainerCounter();
        equalID = player.getUniqueId().toString() + id;
        this.title = title;
        this.player = player;
        items = new ItemStack[9 * rows];
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.OPEN_WINDOW);
        packet.getIntegers().write(0, id);
        packet.getIntegers().write(1, rows - 1);
        packet.getChatComponents().write(0, WrappedChatComponent.fromText(title));
        sendPacket(player, packet);
        currentInventories.put(equalID, this);
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

    public ItemStack[] getItems() {
        return items;
    }

    public GlitchInventory setSlot(int slot, ItemStack itemStack) {
        items[slot] = itemStack;
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SET_SLOT);
        packet.getIntegers().write(0, id);
        packet.getIntegers().write(1, 0);
        packet.getIntegers().write(2, slot);
        packet.getItemModifier().write(0, itemStack);
        sendPacket(player, packet);
        return this;
    }

    public GlitchInventory setSlot(int slot, ItemStack itemStack, SlotClickListener listener) {
        setSlot(slot, itemStack);
        if (slotListeners.containsKey(equalID)) {
            slotListeners.get(equalID).put(slot, listener);
        } else {
            HashMap<Integer, SlotClickListener> listenerMap = new HashMap<>();
            listenerMap.put(slot, listener);
            slotListeners.put(equalID, listenerMap);
        }
        return this;
    }

    public GlitchInventory setSlot(int[] slots, ItemStack[] itemStacks) {
        if (slots.length != itemStacks.length) throw new IllegalArgumentException("Slots have to have the same amount of items as itemstacks");
        for (int i = 0; i <= slots.length - 1; i++) {
            setSlot(slots[i], itemStacks[i]);
        }
        return this;
    }

    public GlitchInventory setSlot(int[] slots, ItemStack[] itemStacks, SlotClickListener listener) {
        if (slots.length != itemStacks.length) throw new IllegalArgumentException("Slots have to have the same amount of items as itemstacks");
        for (int i = 0; i <= slots.length - 1; i++) {
            setSlot(slots[i], itemStacks[i], listener);
        }
        return this;
    }

    public void unRegister() {
        slotListeners.remove(equalID);
        currentInventories.remove(equalID);
    }

    public String getTitle() {
        return title;
    }

    public static HashMap<String, HashMap<Integer, SlotClickListener>> getSlotListeners() {
        return slotListeners;
    }

    public static HashMap<String, GlitchInventory> getCurrentInventories() {
        return currentInventories;
    }

    private static void sendPacket(Player player, PacketContainer packet) {
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
