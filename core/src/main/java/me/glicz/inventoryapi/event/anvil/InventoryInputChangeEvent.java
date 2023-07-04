package me.glicz.inventoryapi.event.anvil;

import lombok.Getter;
import me.glicz.inventoryapi.event.InventoryEvent;
import me.glicz.inventoryapi.inventory.AnvilInventory;
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
