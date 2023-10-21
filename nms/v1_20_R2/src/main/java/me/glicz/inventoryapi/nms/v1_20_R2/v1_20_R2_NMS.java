package me.glicz.inventoryapi.nms.v1_20_R2;

import me.glicz.inventoryapi.inventory.ClickType;
import me.glicz.inventoryapi.inventory.handler.InventoryEventHandler;
import me.glicz.inventoryapi.nms.NativeVersion;
import me.glicz.inventoryapi.nms.v1_20_R1.v1_20_R1_NMS;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
import net.minecraft.network.protocol.game.ServerboundSelectTradePacket;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@NativeVersion(major = 1, minor = 20, patch = 2)
public class v1_20_R2_NMS extends v1_20_R1_NMS {
    public v1_20_R2_NMS(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    protected Connection getConnection(Player player) {
        return getNmsPlayer(player).connection.connection;
    }

    @Override
    protected void handleC2SPacket(Player player, Object rawPacket) {
        if (rawPacket instanceof ServerboundContainerClickPacket packet)
            InventoryEventHandler.get().handleClick(
                    player,
                    packet.getContainerId(),
                    ClickType.get(packet.getClickType().ordinal(), packet.getButtonNum()),
                    packet.getSlotNum()
            );
        else if (rawPacket instanceof ServerboundContainerClosePacket packet)
            InventoryEventHandler.get().handleClose(player, packet.getContainerId());
        else if (rawPacket instanceof ServerboundRenameItemPacket packet)
            InventoryEventHandler.get().handleItemRename(player, packet.getName());
        else if (rawPacket instanceof ServerboundSelectTradePacket packet)
            InventoryEventHandler.get().handleSelectTrade(player, packet.getItem());
    }
}
