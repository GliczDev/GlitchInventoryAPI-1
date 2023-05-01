package me.glicz.inventoryapi.inventories;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.glicz.inventoryapi.events.anvil.InventoryInputChangeEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.function.Consumer;

@Setter
public class AnvilInventory extends GlitchInventory<AnvilInventory> {

    @Accessors(chain = true)
    private Consumer<InventoryInputChangeEvent> inputChangeAction;
    @Getter
    @Accessors(chain = true)
    private boolean shouldInsertItem = true;

    protected AnvilInventory() {
        super(InventoryType.ANVIL);
    }

    public void executeInputChangeAction(InventoryInputChangeEvent event) {
        if (inputChangeAction == null)
            return;
        inputChangeAction.accept(event);
    }
}
