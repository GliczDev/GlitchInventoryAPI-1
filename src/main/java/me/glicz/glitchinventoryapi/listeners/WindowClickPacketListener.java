package me.glicz.glitchinventoryapi.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.glicz.glitchinventoryapi.GlitchInventoryAPI;
import me.glicz.glitchinventoryapi.types.GlitchInventory;
import me.glicz.glitchinventoryapi.types.SlotClickListener;
import net.minecraft.network.protocol.game.PacketPlayInWindowClick;
import org.bukkit.craftbukkit.v1_18_R2.inventory.util.CraftCustomInventoryConverter;
import org.bukkit.event.inventory.ClickType;

import java.util.HashMap;
import java.util.List;

public class WindowClickPacketListener extends PacketAdapter {

    public WindowClickPacketListener() {
        super(GlitchInventoryAPI.getInstance(), ListenerPriority.HIGHEST, List.of(PacketType.Play.Client.WINDOW_CLICK));
    }

    @Override
    public void onPacketReceiving(PacketEvent e) {
        if (!GlitchInventory.getCurrentInventories().containsKey(e.getPlayer().getUniqueId().toString() + e.getPacket().getIntegers().read(0))) return;
        GlitchInventory glitchInventory = GlitchInventory.getCurrentInventories().get(e.getPlayer().getUniqueId().toString() + e.getPacket().getIntegers().read(0));
        if (GlitchInventory.getSlotListeners().containsKey(e.getPlayer().getUniqueId().toString() + e.getPacket().getIntegers().read(0))) {
            HashMap<Integer, SlotClickListener> listenerMap = GlitchInventory.getSlotListeners().get(e.getPlayer().getUniqueId().toString() + e.getPacket().getIntegers().read(0));
            if (listenerMap.containsKey(e.getPacket().getIntegers().read(2))) {
                listenerMap.get(e.getPacket().getIntegers().read(2))
                        .onSlotClick(e.getPlayer(),
                                glitchInventory.getItems()[e.getPacket().getIntegers().read(2)],
                                e.getPacket().getIntegers().read(2),
                                glitchInventory,
                                (e.getPacket().getIntegers().read(3) == 0),
                                (e.getPacket().getIntegers().read(3) == 1));
            }
        }
        glitchInventory.setSlot(e.getPacket().getIntegers().read(2), glitchInventory.getItems()[e.getPacket().getIntegers().read(2)]);
        e.getPlayer().setItemOnCursor(null);
        e.getPlayer().updateInventory();
    }
}
