package me.glicz.glitchinventoryapi.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.glicz.glitchinventoryapi.GlitchInventoryAPI;
import me.glicz.glitchinventoryapi.types.GlitchInventory;
import me.glicz.glitchinventoryapi.types.InventoryType;
import me.glicz.glitchinventoryapi.types.ItemBuilder;

import java.util.List;

public class ItemRenamePacketListener extends PacketAdapter {

    public ItemRenamePacketListener() {
        super(GlitchInventoryAPI.getPlugin(), ListenerPriority.HIGHEST, List.of(PacketType.Play.Client.ITEM_NAME), ListenerOptions.ASYNC);
    }

    @Override
    public void onPacketReceiving(PacketEvent e) {
        if (!GlitchInventory.getCurrentInventories().containsKey(e.getPlayer().getUniqueId())) return;
        GlitchInventory<?> glitchInventory = GlitchInventory.getCurrentInventories().get(e.getPlayer().getUniqueId());
        if (glitchInventory.getInventoryType() != InventoryType.Anvil) return;
        glitchInventory.setItem(2, ItemBuilder.from(glitchInventory.getItem(0).getItemStack())
                .name(e.getPacket().getStrings().read(0)).asGuiItem());
    }
}
