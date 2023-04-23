package me.glicz.inventoryapi.nms.v1_19_R3;

import me.glicz.inventoryapi.inventories.ClickType;
import me.glicz.inventoryapi.inventories.handler.InventoryEventHandler;
import me.glicz.inventoryapi.nms.NativeVersion;
import me.glicz.inventoryapi.nms.v1_18_R1.v1_18_R1_NMS;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.inventory.MenuType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

@NativeVersion(major = 1, minor = 19, patch = 4)
public class v1_19_R3_NMS extends v1_18_R1_NMS {

    public v1_19_R3_NMS() throws ClassNotFoundException, NoSuchMethodException {
    }

    @Override
    protected Connection getConnection(Player player) {
        return getNmsPlayer(player).connection.connection;
    }

    @Override
    protected void handleS2CPacket(Player player, Object rawPacket) {
        if (rawPacket instanceof ClientboundContainerClosePacket packet)
            InventoryEventHandler.get().handleClose(player, packet.getContainerId());
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

    @SuppressWarnings("UnstableApiUsage")
    @Override
    protected MenuType<?> getMenuType(InventoryType inventoryType) {
        return switch (inventoryType) {
            case PLAYER -> MenuType.GENERIC_9x4;
            case WORKBENCH -> MenuType.CRAFTING;
            case FURNACE -> MenuType.FURNACE;
            case DISPENSER, DROPPER -> MenuType.GENERIC_3x3;
            case ENCHANTING -> MenuType.ENCHANTMENT;
            case BREWING -> MenuType.BREWING_STAND;
            case BEACON -> MenuType.BEACON;
            case ANVIL -> MenuType.ANVIL;
            case SMITHING -> MenuType.LEGACY_SMITHING;
            case HOPPER -> MenuType.HOPPER;
            case SHULKER_BOX -> MenuType.SHULKER_BOX;
            case BLAST_FURNACE -> MenuType.BLAST_FURNACE;
            case LECTERN -> MenuType.LECTERN;
            case SMOKER -> MenuType.SMOKER;
            case LOOM -> MenuType.LOOM;
            case CARTOGRAPHY -> MenuType.CARTOGRAPHY_TABLE;
            case GRINDSTONE -> MenuType.GRINDSTONE;
            case STONECUTTER -> MenuType.STONECUTTER;
            case MERCHANT -> MenuType.MERCHANT;
            case SMITHING_NEW -> MenuType.SMITHING;
            case CREATIVE, CRAFTING ->
                    throw new IllegalArgumentException("Can't open a " + inventoryType + " inventory!");
            default -> MenuType.GENERIC_9x3;
        };
    }
}
