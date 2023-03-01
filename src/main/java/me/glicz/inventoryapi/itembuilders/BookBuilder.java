package me.glicz.inventoryapi.itembuilders;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

@SuppressWarnings({"deprecation", "unused"})
public class BookBuilder extends ItemBuilder<BookBuilder, BookMeta> {

    protected BookBuilder() {
        super(new ItemStack(Material.WRITTEN_BOOK));
    }

    public String getAuthor() {
        return itemMeta.getAuthor();
    }

    public BookBuilder setAuthor(String author) {
        itemMeta.setAuthor(author);
        return this;
    }

    public BookMeta.Generation getGeneration() {
        return itemMeta.getGeneration();
    }

    public BookBuilder setGeneration(BookMeta.Generation generation) {
        itemMeta.setGeneration(generation);
        return this;
    }

    public BookBuilder addPages(String... value) {
        itemMeta.addPage(value);
        return this;
    }

    public BookBuilder addPages(BaseComponent[]... value) {
        itemMeta.spigot().addPage(value);
        return this;
    }

    public BookBuilder addPages(Component... value) {
        itemMeta.addPages(value);
        return this;
    }

    public String getPage(int index) {
        return itemMeta.getPage(index);
    }

    public String getTitle() {
        return itemMeta.getTitle();
    }

    public BookBuilder setTitle(String title) {
        itemMeta.setTitle(title);
        return this;
    }
}
