package me.glicz.inventoryapi.itembuilder;

import me.glicz.inventoryapi.GlitchInventoryAPI;
import me.glicz.inventoryapi.event.InventoryClickEvent;
import me.glicz.inventoryapi.event.Listener;
import me.glicz.inventoryapi.inventory.GuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public class ItemBuilder<T extends ItemBuilder<T, I>, I extends ItemMeta> {

    protected final ItemStack itemStack;
    protected final I itemMeta;

    protected ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack.clone();
        try {
            this.itemMeta = (I) this.itemStack.getItemMeta();
        } catch (ClassCastException ignored) {
            throw new UnsupportedOperationException("ItemStack's ItemMeta has to be valid to selected ItemBuilder type");
        }
    }

    public static ItemBuilder<?, ItemMeta> of(Material material) {
        return of(new ItemStack(material));
    }

    public static ItemBuilder<?, ItemMeta> of(ItemStack itemStack) {
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

    public static PotionBuilder potion(Material material) {
        return potion(new ItemStack(material));
    }

    public static PotionBuilder potion(ItemStack itemStack) {
        return new PotionBuilder(itemStack);
    }

    public String getName() {
        return LegacyComponentSerializer.legacySection().serialize(getComponentName());
    }

    public T setName(String name) {
        itemMeta.displayName(Component.text(name));
        return (T) this;
    }

    public Component getComponentName() {
        return Objects.requireNonNullElse(itemMeta.displayName(), Component.translatable(itemStack.getType().translationKey()));
    }

    public T setComponentName(Component component) {
        itemMeta.displayName(component);
        return (T) this;
    }

    public List<String> getLore() {
        return getComponentLore().stream().map(LegacyComponentSerializer.legacySection()::serialize).toList();
    }

    public T setLore(List<String> lore) {
        itemMeta.lore(lore.stream().map(LegacyComponentSerializer.legacySection()::deserialize).map(TextComponent::asComponent).toList());
        return (T) this;
    }

    public List<Component> getComponentLore() {
        return Objects.requireNonNullElse(itemMeta.lore(), new ArrayList<>());
    }

    public T setComponentLore(List<Component> component) {
        itemMeta.lore(component);
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

    public <D, Z> T setPDCValue(String key, PersistentDataType<D, Z> valueType, Z value) {
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(GlitchInventoryAPI.getPlugin(), key), valueType, value);
        return (T) this;
    }

    public <D, Z> Z getPDCValue(String key, PersistentDataType<D, Z> valueType) {
        return itemMeta.getPersistentDataContainer().get(new NamespacedKey(GlitchInventoryAPI.getPlugin(), key), valueType);
    }

    public GuiItem asGuiItem() {
        return GuiItem.of(asItemStack());
    }

    public GuiItem asGuiItem(Consumer<InventoryClickEvent> clickAction) {
        return asGuiItem(clickAction, true);
    }

    public GuiItem asGuiItem(Consumer<InventoryClickEvent> clickAction, boolean sync) {
        return GuiItem.of(asItemStack(), new Listener<>(clickAction, sync));
    }

    public ItemStack asItemStack() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
