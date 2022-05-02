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
        super(GlitchInventoryAPI.getPlugin(), ListenerPriority.HIGHEST, List.of(PacketType.Play.Client.CLOSE_WINDOW));
    }

    @Override
    public void onPacketReceiving(PacketEvent e) {
        if (!GlitchInventory.getCurrentInventories().containsKey(e.getPlayer().getUniqueId().toString() + e.getPacket().getIntegers().read(0))) return;
        GlitchInventory.getCurrentInventories().get(e.getPlayer().getUniqueId().toString() + e.getPacket().getIntegers().read(0)).unRegister();
    }
}
