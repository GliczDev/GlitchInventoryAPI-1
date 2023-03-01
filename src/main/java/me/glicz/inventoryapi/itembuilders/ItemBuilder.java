package me.glicz.inventoryapi.itembuilders;

import me.glicz.inventoryapi.GlitchInventoryAPI;
import me.glicz.inventoryapi.events.ItemClickEvent;
import me.glicz.inventoryapi.types.GuiItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Map;
import java.util.Set;
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
        return new LeatherArmorBuilder(itemStack);
    }

    public static BookBuilder book() {
        return new BookBuilder();
    }

    public static BannerBuilder banner(Material material) {
        return banner(new ItemStack(material));
    }

    public static BannerBuilder banner(ItemStack itemStack) {
        return new BannerBuilder(itemStack);
    }

    public String getName() {
        return itemMeta.getDisplayName();
    }

    public T setName(String name) {
        itemMeta.setDisplayName(name);
        return (T) this;
    }

    public List<String> getLore() {
        return itemMeta.getLore();
    }

    public T setLore(List<String> lore) {
        itemMeta.setLore(lore);
        return (T) this;
    }

    public int getAmount() {
        return itemStack.getAmount();
    }

    public T setAmount(int amount) {
        itemStack.setAmount(amount);
        return (T) this;
    }

    public int getCustomModelData() {
        return itemMeta.getCustomModelData();
    }

    public T setCustomModelData(int customModelData) {
        itemMeta.setCustomModelData(customModelData);
        return (T) this;
    }

    public boolean isUnbreakable() {
        return itemMeta.isUnbreakable();
    }

    public T setUnbreakable(boolean unbreakable) {
        itemMeta.setUnbreakable(unbreakable);
        return (T) this;
    }

    public T addEnchant(Enchantment enchant, int level) {
        return addEnchant(enchant, level, false);
    }

    public T addEnchant(Enchantment enchant, int level, boolean ignoreLevelRestriction) {
        itemMeta.addEnchant(enchant, level, ignoreLevelRestriction);
        return (T) this;
    }

    public T removeEnchant(Enchantment enchant) {
        itemMeta.removeEnchant(enchant);
        return (T) this;
    }

    public int getEnchantLevel(Enchantment enchant) {
        return itemMeta.getEnchantLevel(enchant);
    }

    public boolean hasEnchant(Enchantment enchant) {
        return itemMeta.hasEnchant(enchant);
    }

    public boolean hasConflictingEnchant(Enchantment enchant) {
        return itemMeta.hasConflictingEnchant(enchant);
    }

    public Map<Enchantment, Integer> getEnchants() {
        return itemMeta.getEnchants();
    }

    public boolean hasEnchants() {
        return itemMeta.hasEnchants();
    }

    public T addItemFlags(ItemFlag... itemFlags) {
        itemMeta.addItemFlags(itemFlags);
        return (T) this;
    }

    public T removeItemFlags(ItemFlag... itemFlags) {
        itemMeta.removeItemFlags(itemFlags);
        return (T) this;
    }

    public boolean hasItemFlag(ItemFlag itemFlag) {
        return itemMeta.hasItemFlag(itemFlag);
    }

    public Set<ItemFlag> getItemFlags() {
        return itemMeta.getItemFlags();
    }

    public <D, Z> T setNbt(String key, PersistentDataType<D, Z> valueType, Z value) {
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(GlitchInventoryAPI.getPlugin(), key), valueType, value);
        return (T) this;
    }

    public <D, Z> Z getNbt(String key, PersistentDataType<D, Z> valueType) {
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
