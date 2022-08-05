package me.glicz.glitchinventoryapi.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.glicz.glitchinventoryapi.GlitchInventoryAPI;
import me.glicz.glitchinventoryapi.events.ItemClickEvent;
import me.glicz.glitchinventoryapi.types.ClickType;
import me.glicz.glitchinventoryapi.types.GlitchInventory;
import me.glicz.glitchinventoryapi.types.GuiItem;
import org.bukkit.Bukkit;

import java.util.List;

public class WindowClickPacketListener extends PacketAdapter {

    public WindowClickPacketListener() {
        super(GlitchInventoryAPI.getPlugin(), ListenerPriority.HIGHEST, List.of(PacketType.Play.Client.WINDOW_CLICK), ListenerOptions.ASYNC);
    }

    @Override
    public void onPacketReceiving(PacketEvent e) {
        if (!GlitchInventory.getCurrentInventories().containsKey(e.getPlayer().getUniqueId())) return;
        GlitchInventory<?> glitchInventory = GlitchInventory.getCurrentInventories().get(e.getPlayer().getUniqueId());
        int slot = e.getPacket().getIntegers().read(2);
        GuiItem clickedItem = glitchInventory.getItem(slot);
        glitchInventory.update();
        e.getPlayer().setItemOnCursor(null);
        e.getPlayer().updateInventory();
        ItemClickEvent event = new ItemClickEvent(e.getPlayer(),
                clickedItem == null ? null : clickedItem.getItemStack(),
                slot,
                glitchInventory,
                ClickType.get(((Enum<?>) e.getPacket().getModifier().read(4)).ordinal(),
                        e.getPacket().getIntegers().read(3)));
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
                GlitchInventoryAPI.getPlugin(),
                () -> {
                    if (glitchInventory.getClickActions().containsKey(slot))
                        glitchInventory.getClickActions().get(slot).accept(event);
                    if (clickedItem == null) return;
                    if (clickedItem.getClickAction() != null)
                        clickedItem.getClickAction().accept(event);
                    if (glitchInventory.getDefaultClickAction() != null)
                        glitchInventory.getDefaultClickAction().accept(event);
                });
    }
}
