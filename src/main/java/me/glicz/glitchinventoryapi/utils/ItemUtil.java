package me.glicz.glitchinventoryapi.utils;

import me.glicz.glitchinventoryapi.GlitchInventoryAPI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

@SuppressWarnings({"ConstantConditions", "unused"})
public class ItemUtil {

    public static ItemStack getItem(int count, Material material, String name, String[] lore, ItemMeta itemMeta) {
        ItemStack itemStack = new ItemStack(material, count);
        if (itemMeta == null) itemMeta = itemStack.getItemMeta();
        if (name != null) itemMeta.setDisplayName(name);
        if (lore != null) itemMeta.setLore(List.of(lore));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static <T, Z> ItemStack setNBT(ItemStack itemStack, String key, PersistentDataType<T, Z> valueType, Z value) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(GlitchInventoryAPI.getPlugin(), key), valueType, value);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static <T, Z> Z getNBT(ItemStack itemStack, String key, PersistentDataType<T, Z> valueType) {
        return itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(GlitchInventoryAPI.getPlugin(), key), valueType);
    }
}
