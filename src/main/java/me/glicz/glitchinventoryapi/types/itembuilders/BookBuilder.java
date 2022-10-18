package me.glicz.glitchinventoryapi.types.itembuilders;

import me.glicz.glitchinventoryapi.types.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class BookBuilder extends ItemBuilder<BookBuilder> {

    public BookBuilder(ItemStack itemStack) {
        super(itemStack);
        if (!(itemStack.getItemMeta() instanceof BookMeta)) {
            throw new UnsupportedOperationException("ItemStack have to be book");
        }
    }

    public BookBuilder author(String author) {
        ((BookMeta)itemMeta).setAuthor(author);
        return this;
    }

    public String author() {
        return ((BookMeta)itemMeta).getAuthor();
    }

    public BookBuilder generation(BookMeta.Generation generation) {
        ((BookMeta)itemMeta).setGeneration(generation);
        return this;
    }

    public BookMeta.Generation generation() {
        return ((BookMeta)itemMeta).getGeneration();
    }

    @SuppressWarnings("deprecation")
    public BookBuilder page(int index, String value) {
        ((BookMeta)itemMeta).setPage(index, value);
        return this;
    }

    @SuppressWarnings("deprecation")
    public BookBuilder page(int index, BaseComponent[] value) {
        ((BookMeta)itemMeta).spigot().setPage(index, value);
        return this;
    }

    public BookBuilder page(int index, Component value) {
        ((BookMeta)itemMeta).page(index, value);
        return this;
    }

    @SuppressWarnings("deprecation")
    public String page(int index) {
        return ((BookMeta)itemMeta).getPage(index);
    }
}
