package me.glicz.inventoryapi.events.paginated;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.glicz.inventoryapi.events.InventoryEvent;
import me.glicz.inventoryapi.inventories.PaginatedInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

@Getter
public class InventoryPageChangeEvent extends InventoryEvent implements Cancellable {

    private final int newPage, oldPage;
    @Accessors(fluent = true)
    private final boolean hasNext, hasPrevious;
    @Setter
    private boolean cancelled;

    public InventoryPageChangeEvent(Player player, PaginatedInventory inventory, int newPage, int oldPage) {
        super(player, inventory);
        this.newPage = newPage;
        this.oldPage = oldPage;
        this.hasNext = inventory.hasNextPage(player);
        this.hasPrevious = inventory.hasPreviousPage(player);
    }

    @Override
    public PaginatedInventory getInventory() {
        return (PaginatedInventory) super.getInventory();
    }
}
