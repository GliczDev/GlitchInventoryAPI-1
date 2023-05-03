package me.glicz.inventoryapi.events;

import lombok.AllArgsConstructor;
import me.glicz.inventoryapi.GlitchInventoryAPI;
import org.bukkit.Bukkit;

import java.util.function.Consumer;

@AllArgsConstructor
public class Listener<T extends InventoryEvent> {
    private final Consumer<T> action;
    private final boolean sync;

    public void run(T event) {
        if (sync && !Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(GlitchInventoryAPI.getPlugin(), () -> action.accept(event));
            return;
        }
        action.accept(event);
    }
}
