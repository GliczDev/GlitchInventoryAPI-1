package me.glicz.inventoryapi.nms.v1_20_R3;

import me.glicz.inventoryapi.nms.NativeVersion;
import me.glicz.inventoryapi.nms.v1_20_R2.v1_20_R2_NMS;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.trading.MerchantOffer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@NativeVersion(major = 1, minor = 20, patch = 3)
public class v1_20_R3_NMS extends v1_20_R2_NMS {
    public v1_20_R3_NMS(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    protected ServerPlayer getServerPlayer(Player player) {
        return MinecraftServer.getServer().getPlayerList().getPlayer(player.getUniqueId());
    }

    protected MerchantOffer getMerchantOffer(MerchantRecipe recipe) {
        return new MerchantOffer(
                !recipe.getIngredients().isEmpty()
                        ? net.minecraft.world.item.ItemStack.fromBukkitCopy(recipe.getIngredients().get(0))
                        : net.minecraft.world.item.ItemStack.EMPTY,
                recipe.getIngredients().size() > 1
                        ? net.minecraft.world.item.ItemStack.fromBukkitCopy(recipe.getIngredients().get(1))
                        : net.minecraft.world.item.ItemStack.EMPTY,
                net.minecraft.world.item.ItemStack.fromBukkitCopy(recipe.getResult()),
                recipe.getUses(),
                recipe.getMaxUses(),
                recipe.getVillagerExperience(),
                recipe.getPriceMultiplier(),
                0,
                recipe.shouldIgnoreDiscounts()
        );
    }

    @SuppressWarnings({"removal"})
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
            case SMITHING, SMITHING_NEW -> MenuType.SMITHING;
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
            default -> {
                if (!inventoryType.isCreatable())
                    throw new IllegalArgumentException("Can't create a %s inventory!".formatted(inventoryType));
                yield MenuType.GENERIC_9x3;
            }
        };
    }

    @Override
    public void setItems(int id, Player player, List<ItemStack> items) {
        net.minecraft.world.item.ItemStack[] itemStacks = items.stream()
                .map(net.minecraft.world.item.ItemStack::fromBukkitCopy)
                .toArray(net.minecraft.world.item.ItemStack[]::new);
        ClientboundContainerSetContentPacket packet = new ClientboundContainerSetContentPacket(
                id, 0,
                NonNullList.of(net.minecraft.world.item.ItemStack.EMPTY, itemStacks),
                net.minecraft.world.item.ItemStack.EMPTY
        );
        sendPacket(player, packet);
    }
}
