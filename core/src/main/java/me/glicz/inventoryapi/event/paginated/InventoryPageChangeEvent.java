package me.glicz.inventoryapi.event.paginated;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.glicz.inventoryapi.event.InventoryEvent;
import me.glicz.inventoryapi.inventory.PaginatedInventory;
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
        this.hasNext = inventory.getPageCount() > newPage - 1;
        this.hasPrevious = newPage > 0;
    }

    @Override
    public PaginatedInventory getInventory() {
        return (PaginatedInventory) super.getInventory();
    }
}
