package me.glicz.inventoryapi.events.anvil;

import lombok.Getter;
import me.glicz.inventoryapi.events.InventoryEvent;
import me.glicz.inventoryapi.inventories.AnvilInventory;
import org.bukkit.entity.Player;

public class InventoryInputChangeEvent extends InventoryEvent {

    @Getter
    private final String text;

    public InventoryInputChangeEvent(Player player, AnvilInventory inventory, String text) {
        super(player, inventory);
        this.text = text;
    }

    @Override
    public AnvilInventory getInventory() {
        return (AnvilInventory) super.getInventory();
    }
}
