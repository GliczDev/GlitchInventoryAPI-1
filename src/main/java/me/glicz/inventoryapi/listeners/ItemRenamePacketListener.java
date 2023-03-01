package me.glicz.inventoryapi.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.glicz.inventoryapi.GlitchInventoryAPI;
import me.glicz.inventoryapi.inventories.GlitchInventory;
import me.glicz.inventoryapi.itembuilders.ItemBuilder;
import me.glicz.inventoryapi.types.InventoryType;

import java.util.List;

public class ItemRenamePacketListener extends PacketAdapter {

    public ItemRenamePacketListener() {
        super(GlitchInventoryAPI.getPlugin(), ListenerPriority.HIGHEST, List.of(PacketType.Play.Client.ITEM_NAME), ListenerOptions.ASYNC);
    }

    @Override
    public void onPacketReceiving(PacketEvent e) {
        if (!GlitchInventory.getCurrentInventories().containsKey(e.getPlayer().getUniqueId())) return;
        GlitchInventory<?> glitchInventory = GlitchInventory.getCurrentInventories().get(e.getPlayer().getUniqueId());
        if (glitchInventory.getInventoryType() != InventoryType.ANVIL) return;
        glitchInventory.setItem(2, ItemBuilder.from(glitchInventory.getItem(0).getItemStack())
                .setName(e.getPacket().getStrings().read(0)).asGuiItem());
    }
}
