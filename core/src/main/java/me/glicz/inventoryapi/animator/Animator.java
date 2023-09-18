package me.glicz.inventoryapi.animator;

import lombok.Getter;
import lombok.experimental.Accessors;
import me.glicz.inventoryapi.inventory.GlitchInventory;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.function.BiConsumer;

public class Animator<T extends GlitchInventory<T>> {
    private static final Set<Animator<?>> ACTIVE_ANIMATORS = new HashSet<>();
    private final Map<String, BiConsumer<Integer, T>> actionMap = new HashMap<>();
    @Getter
    @Accessors(fluent = true)
    private final T inventory;
    private int tick = -1;
    @Getter
    private boolean active;

    private Animator(T inventory) {
        if (inventory.getAnimator() != null)
            throw new IllegalArgumentException("This inventory already has an animator!");
        this.inventory = inventory;
    }

    public static <T extends GlitchInventory<T>> Animator<T> create(T inventory) {
        return new Animator<>(inventory);
    }

    @Unmodifiable
    public static Set<Animator<?>> getActiveAnimators() {
        return Collections.unmodifiableSet(ACTIVE_ANIMATORS);
    }

    public Animator<T> addEveryTickAction(String id, BiConsumer<Integer, T> action) {
        if (!actionMap.containsKey(id))
            actionMap.put(id, action);
        return this;
    }

    public Animator<T> removeEveryTickAction(String id) {
        actionMap.remove(id);
        return this;
    }

    public Animator<T> clear() {
        actionMap.clear();
        return this;
    }

    public Animator<T> setActive(boolean active) {
        if (this.active == active)
            return this;
        this.active = active;
        if (active) ACTIVE_ANIMATORS.add(this);
        else ACTIVE_ANIMATORS.remove(this);
        return this;
    }

    public void tick() {
        tick++;
        actionMap.values().forEach(action -> action.accept(tick, inventory));
    }
}
