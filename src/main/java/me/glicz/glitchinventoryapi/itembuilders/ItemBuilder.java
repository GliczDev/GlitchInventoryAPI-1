package me.glicz.glitchinventoryapi.itembuilders;

import me.glicz.glitchinventoryapi.GlitchInventoryAPI;
import me.glicz.glitchinventoryapi.events.ItemClickEvent;
import me.glicz.glitchinventoryapi.types.GuiItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings({"unchecked", "deprecation", "unused"})
public class ItemBuilder<T extends ItemBuilder<T, I>, I extends ItemMeta> {

    protected final ItemStack itemStack;
    protected final I itemMeta;

    protected ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        try {
            this.itemMeta = (I) itemStack.getItemMeta();
        } catch (ClassCastException ignored) {
            throw new UnsupportedOperationException("ItemStack's ItemMeta has to be valid to selected ItemBuilder type");
        }
    }

    public static ItemBuilder<?, ItemMeta> from(Material material) {
        return from(new ItemStack(material));
    }

    public static ItemBuilder<?, ItemMeta> from(ItemStack itemStack) {
        return new ItemBuilder<>(itemStack);
    }

    public static SkullBuilder skull() {
        return new SkullBuilder();
    }

    public static LeatherArmorBuilder leatherArmor(Material material) {
        return leatherArmor(new ItemStack(material));
    }

    public static LeatherArmorBuilder leatherArmor(ItemStack itemStack) {
        if (!(itemStack.getItemMeta() instanceof LeatherArmorMeta))
            throw new UnsupportedOperationException("ItemStack have to be any leather armor");
        return new LeatherArmorBuilder(itemStack);
    }

    public static BookBuilder book() {
        return new BookBuilder();
    }

    public T name(String name) {
        itemMeta.setDisplayName(name);
        return (T) this;
    }

    public String name() {
        return itemMeta.getDisplayName();
    }

    public T lore(List<String> lore) {
        itemMeta.setLore(lore);
        return (T) this;
    }

    public List<String> lore() {
        return itemMeta.getLore();
    }

    public T amount(int amount) {
        itemStack.setAmount(amount);
        return (T) this;
    }

    public int amount() {
        return itemStack.getAmount();
    }

    public <T2, Z> T nbt(String key, PersistentDataType<T2, Z> valueType, Z value) {
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(GlitchInventoryAPI.getPlugin(), key), valueType, value);
        return (T) this;
    }

    public <T2, Z> Z nbt(String key, PersistentDataType<T2, Z> valueType) {
        return itemMeta.getPersistentDataContainer().get(new NamespacedKey(GlitchInventoryAPI.getPlugin(), key), valueType);
    }

    public GuiItem asGuiItem() {
        return new GuiItem(asItemStack());
    }

    public GuiItem asGuiItem(Consumer<ItemClickEvent> clickAction) {
        return new GuiItem(asItemStack(), clickAction);
    }

    public ItemStack asItemStack() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
