package me.glicz.inventoryapi.nms.v1_19_R3;

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
import net.minecraft.world.item.trading.MerchantOffers;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftMerchantRecipe;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class v1_19_R3_NMS implements NMS {

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
                else if (rawPacket instanceof ServerboundSelectTradePacket packet)
                    InventoryEventHandler.get().handleSelectTrade(player, packet.getItem());
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

    private MenuType<?> getMenuType(int rows) {
        return switch (rows) {
            case 1 -> MenuType.GENERIC_9x1;
            case 2 -> MenuType.GENERIC_9x2;
            case 3 -> MenuType.GENERIC_9x3;
            case 4 -> MenuType.GENERIC_9x4;
            case 5 -> MenuType.GENERIC_9x5;
            case 6 -> MenuType.GENERIC_9x6;
            default -> throw new IllegalArgumentException("Can't open a " + rows + " rows inventory!");
        };
    }

    @SuppressWarnings({"deprecation", "UnstableApiUsage"})
    private MenuType<?> getMenuType(InventoryType inventoryType) {
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

    @Override
    public void openInventory(int id, Player player, InventoryType inventoryType, Component title) {
        openInventory0(id, player, getMenuType(inventoryType), title);
    }

    @Override
    public void openInventory(int id, Player player, int rows, Component title) {
        openInventory0(id, player, getMenuType(rows), title);
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
    public void setRecipes(int id, Player player, List<MerchantRecipe> recipeList) {
        MerchantOffers offers = new MerchantOffers();
        recipeList.forEach(recipe -> offers.add(CraftMerchantRecipe.fromBukkit(recipe).toMinecraft()));
        ClientboundMerchantOffersPacket packet = new ClientboundMerchantOffersPacket(
                id, offers, 0, 0, false, false);
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
