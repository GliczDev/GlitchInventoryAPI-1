package me.glicz.inventoryapi.nms.v1_19_R2;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.papermc.paper.adventure.PaperAdventure;
import me.glicz.inventoryapi.inventories.ClickType;
import me.glicz.inventoryapi.inventories.handler.InventoryEventHandler;
import me.glicz.inventoryapi.nms.NMS;
import net.kyori.adventure.text.Component;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.inventory.MenuType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R2.inventory.CraftContainer;
import org.bukkit.craftbukkit.v1_19_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class v1_19_R2_NMS implements NMS {

    @Override
    public void registerListener(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        Channel channel = craftPlayer.getHandle().connection.connection.channel;
        channel.pipeline().addBefore("packet_handler", "GlitchInventoryAPI_Handler", new ChannelDuplexHandler() {
            @Override
            public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object rawPacket) throws Exception {
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
                super.channelRead(ctx, rawPacket);
            }
        });
    }

    @Override
    public void unregisterListener(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        Channel channel = craftPlayer.getHandle().connection.connection.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove("GlitchInventoryAPI_Handler");
            return null;
        });
    }

    @Override
    public int getNextInventoryID(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        return craftPlayer.getHandle().nextContainerCounter();
    }

    @Override
    public void openInventory(int id, Player player, InventoryType inventoryType, Component title) {
        openInventory0(id, player, CraftContainer.getNotchInventoryType(Bukkit.createInventory(null, inventoryType)), title);
    }

    @Override
    public void openInventory(int id, Player player, int rows, Component title) {
        openInventory0(id, player, CraftContainer.getNotchInventoryType(Bukkit.createInventory(null, rows * 9)), title);
    }

    private void openInventory0(int id, Player player, MenuType<?> menuType, Component title) {
        ClientboundOpenScreenPacket packet = new ClientboundOpenScreenPacket(id, menuType, PaperAdventure.asVanilla(title));
        CraftPlayer craftPlayer = (CraftPlayer) player;
        craftPlayer.getHandle().connection.send(packet);
    }

    @SuppressWarnings("deprecation")
    public void setProperty(int id, Player player, InventoryView.Property property, int value) {
        ClientboundContainerSetDataPacket packet = new ClientboundContainerSetDataPacket(id, property.getId(), value);
        CraftPlayer craftPlayer = (CraftPlayer) player;
        craftPlayer.getHandle().connection.send(packet);
    }

    @Override
    public void setItems(int id, Player player, List<ItemStack> items) {
        net.minecraft.world.item.ItemStack[] itemStacks = items.stream()
                .map(CraftItemStack::asNMSCopy)
                .toArray(net.minecraft.world.item.ItemStack[]::new);
        var airItem = CraftItemStack.asNMSCopy(new ItemStack(Material.AIR));
        ClientboundContainerSetContentPacket packet = new ClientboundContainerSetContentPacket(id, 0,
                NonNullList.of(airItem, itemStacks), airItem);
        CraftPlayer craftPlayer = (CraftPlayer) player;
        craftPlayer.getHandle().connection.send(packet);
    }

    @Override
    public void setItem(int id, int slot, Player player, ItemStack item) {
        ClientboundContainerSetSlotPacket packet = new ClientboundContainerSetSlotPacket(id, 0, slot, CraftItemStack.asNMSCopy(item));
        CraftPlayer craftPlayer = (CraftPlayer) player;
        craftPlayer.getHandle().connection.send(packet);
    }

    @Override
    public void closeInventory(int id, Player player) {
        ClientboundContainerClosePacket packet = new ClientboundContainerClosePacket(id);
        CraftPlayer craftPlayer = (CraftPlayer) player;
        craftPlayer.getHandle().connection.send(packet);
    }
}
