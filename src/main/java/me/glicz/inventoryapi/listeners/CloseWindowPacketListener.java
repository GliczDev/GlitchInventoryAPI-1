package me.glicz.inventoryapi.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.glicz.inventoryapi.GlitchInventoryAPI;
import me.glicz.inventoryapi.inventories.GlitchInventory;

import java.util.List;

public class CloseWindowPacketListener extends PacketAdapter {

    public CloseWindowPacketListener() {
        super(GlitchInventoryAPI.getPlugin(), ListenerPriority.HIGHEST, List.of(PacketType.Play.Client.CLOSE_WINDOW), ListenerOptions.ASYNC);
    }

    @Override
    public void onPacketReceiving(PacketEvent e) {
        if (!GlitchInventory.getCurrentInventories().containsKey(e.getPlayer().getUniqueId())) return;
        GlitchInventory.getCurrentInventories().get(e.getPlayer().getUniqueId()).unRegister();
    }
}
