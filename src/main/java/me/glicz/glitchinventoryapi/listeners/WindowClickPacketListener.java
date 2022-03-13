package me.glicz.glitchinventoryapi.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.glicz.glitchinventoryapi.GlitchInventoryAPI;
import me.glicz.glitchinventoryapi.types.GlitchInventory;
import me.glicz.glitchinventoryapi.types.SlotClickListener;

import java.util.HashMap;
import java.util.List;

public class WindowClickPacketListener extends PacketAdapter {

    public WindowClickPacketListener() {
        super(GlitchInventoryAPI.getInstance(), ListenerPriority.HIGHEST, List.of(PacketType.Play.Client.WINDOW_CLICK), ListenerOptions.ASYNC);
    }

    @Override
    public void onPacketReceiving(PacketEvent e) {
        if (!GlitchInventory.getCurrentInventories().containsKey(e.getPacket().getIntegers().read(0))) return;
        GlitchInventory glitchInventory = GlitchInventory.getCurrentInventories().get(e.getPacket().getIntegers().read(0));
        if (GlitchInventory.getSlotListeners().containsKey(e.getPacket().getIntegers().read(0))) {
            HashMap<Integer, SlotClickListener> listenerMap = GlitchInventory.getSlotListeners().get(e.getPacket().getIntegers().read(0));
            if (listenerMap.containsKey(e.getPacket().getIntegers().read(2))) {
                listenerMap.get(e.getPacket().getIntegers().read(2))
                        .onSlotClick(e.getPlayer(), e.getPacket().getItemModifier().read(0), glitchInventory);
            }
        }
        glitchInventory.setSlot(e.getPacket().getIntegers().read(2), glitchInventory.getItems()[e.getPacket().getIntegers().read(2)]);
        e.getPlayer().setItemOnCursor(null);
    }
}
