package me.glicz.glitchinventoryapi.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.glicz.glitchinventoryapi.GlitchInventoryAPI;
import me.glicz.glitchinventoryapi.types.GlitchInventory;

import java.util.List;

public class CloseWindowPacketListener extends PacketAdapter {

    public CloseWindowPacketListener() {
        super(GlitchInventoryAPI.getInstance(), ListenerPriority.HIGHEST, List.of(PacketType.Play.Client.CLOSE_WINDOW), ListenerOptions.ASYNC);
    }

    @Override
    public void onPacketReceiving(PacketEvent e) {
        if (!GlitchInventory.getCurrentInventories().containsKey(e.getPacket().getIntegers().read(0))) return;
        GlitchInventory.getCurrentInventories().get(e.getPacket().getIntegers().read(0)).unRegister();
    }
}
