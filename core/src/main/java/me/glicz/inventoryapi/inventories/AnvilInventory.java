package me.glicz.inventoryapi.inventories;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.glicz.inventoryapi.events.Listener;
import me.glicz.inventoryapi.events.anvil.InventoryInputChangeEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AnvilInventory extends GlitchInventory<AnvilInventory> {


    private final List<Listener<InventoryInputChangeEvent>> inputChangeListeners = new ArrayList<>();
    @Setter
    @Getter
    @Accessors(chain = true)
    private boolean shouldInsertItem = true;

    protected AnvilInventory() {
        super(InventoryType.ANVIL);
    }

    public AnvilInventory addInputChangeListener(Consumer<InventoryInputChangeEvent> action) {
        return addInputChangeListener(action, true);
    }

    public AnvilInventory addInputChangeListener(Consumer<InventoryInputChangeEvent> action, boolean sync) {
        inputChangeListeners.add(new Listener<>(action, sync));
        return this;
    }

    public AnvilInventory runInputChangeListeners(InventoryInputChangeEvent event) {
        inputChangeListeners.forEach(listener -> listener.run(event));
        return this;
    }
}
