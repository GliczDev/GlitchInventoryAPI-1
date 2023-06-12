package me.glicz.inventoryapi.nms.v1_20_R1;

import me.glicz.inventoryapi.nms.NativeVersion;
import me.glicz.inventoryapi.nms.v1_19_R3.v1_19_R3_NMS;
import net.minecraft.network.Connection;
import net.minecraft.world.inventory.MenuType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.java.JavaPlugin;

@NativeVersion(major = 1, minor = 20)
public class v1_20_R1_NMS extends v1_19_R3_NMS {

    public v1_20_R1_NMS(JavaPlugin plugin) throws ClassNotFoundException, NoSuchMethodException {
        super(plugin);
    }

    @Override
    protected Connection getConnection(Player player) {
        return getNmsPlayer(player).connection.connection;
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
            case CREATIVE, CRAFTING ->
                    throw new IllegalArgumentException("Can't open a " + inventoryType + " inventory!");
            default -> MenuType.GENERIC_9x3;
        };
    }
}
