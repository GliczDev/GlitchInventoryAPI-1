package me.glicz.glitchinventoryapi.itembuilders;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

@SuppressWarnings({"deprecation", "unused"})
public class BookBuilder extends ItemBuilder<BookBuilder, BookMeta> {

    public BookBuilder() {
        super(new ItemStack(Material.WRITTEN_BOOK));
    }

    public BookBuilder author(String author) {
        itemMeta.setAuthor(author);
        return this;
    }

    public String author() {
        return itemMeta.getAuthor();
    }

    public BookBuilder generation(BookMeta.Generation generation) {
        itemMeta.setGeneration(generation);
        return this;
    }

    public BookMeta.Generation generation() {
        return itemMeta.getGeneration();
    }

    public BookBuilder page(String... value) {
        itemMeta.addPage(value);
        return this;
    }

    public BookBuilder page(BaseComponent[]... value) {
        itemMeta.spigot().addPage(value);
        return this;
    }

    public BookBuilder page(Component... value) {
        itemMeta.addPages(value);
        return this;
    }

    public String page(int index) {
        return itemMeta.getPage(index);
    }

    public BookBuilder title(String title) {
        itemMeta.setTitle(title);
        return this;
    }

    public String title() {
        return itemMeta.getTitle();
    }
}
