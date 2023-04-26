package me.glicz.inventoryapi.nms.v1_17_R1;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.papermc.paper.adventure.PaperAdventure;
import lombok.SneakyThrows;
import me.glicz.inventoryapi.inventories.ClickType;
import me.glicz.inventoryapi.inventories.handler.InventoryEventHandler;
import me.glicz.inventoryapi.nms.NMS;
import me.glicz.inventoryapi.nms.NMSInitializer;
import me.glicz.inventoryapi.nms.NativeVersion;
import net.kyori.adventure.text.Component;
import net.minecraft.core.NonNullList;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@NativeVersion(major = 1, minor = 17, patch = 1)
public class v1_17_R1_NMS implements NMS {

    protected final JavaPlugin plugin;
    protected Method playerGetHandle, itemStackAsNMS, merchantRecipeFromBukkit, merchantRecipeToMinecraft;
    protected Field connectionChannel;

    public v1_17_R1_NMS(JavaPlugin plugin) throws ClassNotFoundException, NoSuchMethodException {
        this.plugin = plugin;

        Class<?> craftPlayerClass = Class.forName(
                "org.bukkit.craftbukkit.%s.entity.CraftPlayer".formatted(NMSInitializer.getVersion()));
        playerGetHandle = craftPlayerClass.getMethod("getHandle");

        Class<?> craftItemStackClass = Class.forName(
                "org.bukkit.craftbukkit.%s.inventory.CraftItemStack".formatted(NMSInitializer.getVersion()));
        itemStackAsNMS = craftItemStackClass.getMethod("asNMSCopy", org.bukkit.inventory.ItemStack.class);

        Class<?> craftMerchantRecipeClass = Class.forName(
                "org.bukkit.craftbukkit.%s.inventory.CraftMerchantRecipe".formatted(NMSInitializer.getVersion()));
        merchantRecipeFromBukkit = craftMerchantRecipeClass.getMethod("fromBukkit", MerchantRecipe.class);
        merchantRecipeToMinecraft = craftMerchantRecipeClass.getMethod("toMinecraft");

        connectionChannel = Arrays.stream(Connection.class.getFields())
                .filter(field -> field.getType().isAssignableFrom(Channel.class))
                .findFirst()
                .orElseThrow();
    }

    @SneakyThrows
    protected ServerPlayer getNmsPlayer(Player player) {
        return (ServerPlayer) playerGetHandle.invoke(player);
    }

    protected Connection getConnection(Player player) {
        return getNmsPlayer(player).connection.getConnection();
    }

    @SneakyThrows
    protected Channel getChannel(Player player) {
        return (Channel) connectionChannel.get(getConnection(player));
    }

    @SneakyThrows
    protected void sendPacket(Player player, Object packetObj) {
        if (!(packetObj instanceof Packet<?> packet))
            throw new IllegalArgumentException("Provided packetObj should be an instance of Packet class!");
        getConnection(player).send(packet);
    }

    @SneakyThrows
    protected ItemStack getNmsItemStack(org.bukkit.inventory.ItemStack itemStack) {
        return (ItemStack) itemStackAsNMS.invoke(null, itemStack);
    }

    @SneakyThrows
    protected MerchantOffer getNmsMerchantOffer(MerchantRecipe merchantRecipe) {
        Object craftMerchantRecipe = merchantRecipeFromBukkit.invoke(null, merchantRecipe);
        return (MerchantOffer) merchantRecipeToMinecraft.invoke(craftMerchantRecipe);
    }

    @Override
    public void registerListener(Player player) {
        getChannel(player).pipeline().addBefore("packet_handler", plugin.getName() + "_GlitchInventoryAPI_Handler",
                new ChannelDuplexHandler() {
                    @Override
                    public void write(ChannelHandlerContext ctx, Object rawPacket, ChannelPromise promise) throws Exception {
                        try {
                            handleS2CPacket(player, rawPacket);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        super.write(ctx, rawPacket, promise);
                    }

                    @Override
                    public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object rawPacket) throws Exception {
                        try {
                            handleC2SPacket(player, rawPacket);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        super.channelRead(ctx, rawPacket);
                    }
                });
    }

    @Override
    public void unregisterListener(Player player) {
        Channel channel = getChannel(player);
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(plugin.getName() + "_GlitchInventoryAPI_Handler");
            return null;
        });
    }

    protected void handleS2CPacket(Player player, Object rawPacket) {
        if (rawPacket instanceof ClientboundContainerClosePacket packet)
            InventoryEventHandler.get().handleClose(player, packet.getContainerId());
    }

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

    @SneakyThrows
    @Override
    public int getNextInventoryID(Player player) {
        return getNmsPlayer(player).nextContainerCounter();
    }

    protected MenuType<?> getMenuType(int rows) {
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
            case SMITHING -> MenuType.SMITHING;
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
        sendPacket(player, packet);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setProperty(int id, Player player, InventoryView.Property property, int value) {
        ClientboundContainerSetDataPacket packet = new ClientboundContainerSetDataPacket(id, property.getId(), value);
        sendPacket(player, packet);
    }

    @Override
    public void setItems(int id, Player player, List<org.bukkit.inventory.ItemStack> items) {
        ItemStack[] itemStacks = items.stream()
                .map(this::getNmsItemStack)
                .toArray(ItemStack[]::new);
        ClientboundContainerSetContentPacket packet = new ClientboundContainerSetContentPacket(id, 0,
                NonNullList.of(ItemStack.EMPTY, itemStacks), ItemStack.EMPTY);
        sendPacket(player, packet);
    }

    @Override
    public void setItem(int id, int slot, Player player, org.bukkit.inventory.ItemStack item) {
        ClientboundContainerSetSlotPacket packet = new ClientboundContainerSetSlotPacket(id, 0, slot, getNmsItemStack(item));
        sendPacket(player, packet);
    }

    @Override
    public void setRecipes(int id, Player player, List<MerchantRecipe> recipeList) {
        MerchantOffers offers = new MerchantOffers();
        recipeList.forEach(recipe -> offers.add(getNmsMerchantOffer(recipe)));
        ClientboundMerchantOffersPacket packet = new ClientboundMerchantOffersPacket(
                id, offers, 0, 0, false, false);
        sendPacket(player, packet);
    }

    @Override
    public void closeInventory(int id, Player player) {
        ClientboundContainerClosePacket packet = new ClientboundContainerClosePacket(id);
        sendPacket(player, packet);
    }
}

