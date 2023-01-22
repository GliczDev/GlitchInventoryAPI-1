package me.glicz.glitchinventoryapi.itembuilders;

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
        ((BookMeta) itemMeta).setAuthor(author);
        return this;
    }

    public String author() {
        return ((BookMeta) itemMeta).getAuthor();
    }

    public BookBuilder generation(BookMeta.Generation generation) {
        ((BookMeta) itemMeta).setGeneration(generation);
        return this;
    }

    public BookMeta.Generation generation() {
        return ((BookMeta) itemMeta).getGeneration();
    }

    @SuppressWarnings("deprecation")
    public BookBuilder page(String... value) {
        ((BookMeta) itemMeta).addPage(value);
        return this;
    }

    @SuppressWarnings("deprecation")
    public BookBuilder page(BaseComponent[]... value) {
        ((BookMeta) itemMeta).spigot().addPage(value);
        return this;
    }

    public BookBuilder page(Component... value) {
        ((BookMeta) itemMeta).addPages(value);
        return this;
    }

    @SuppressWarnings("deprecation")
    public String page(int index) {
        return ((BookMeta) itemMeta).getPage(index);
    }

    public BookBuilder title(String title) {
        ((BookMeta) itemMeta).setTitle(title);
        return this;
    }

    public String title() {
        return ((BookMeta) itemMeta).getTitle();
    }
}
