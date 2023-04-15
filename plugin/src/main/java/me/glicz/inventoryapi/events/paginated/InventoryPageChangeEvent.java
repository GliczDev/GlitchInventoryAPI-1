package me.glicz.inventoryapi.events.paginated;

import lombok.Getter;
import me.glicz.inventoryapi.events.InventoryEvent;
import me.glicz.inventoryapi.inventories.PaginatedInventory;
import org.bukkit.entity.Player;

public class InventoryPageChangeEvent extends InventoryEvent {

    @Getter
    private final int newPage, oldPage;

    public InventoryPageChangeEvent(Player player, PaginatedInventory inventory, int newPage, int oldPage) {
        super(player, inventory);
        this.newPage = newPage;
        this.oldPage = oldPage;
    }

    @Override
    public PaginatedInventory getInventory() {
        return (PaginatedInventory) super.getInventory();
    }
}
