package me.glicz.inventoryapi.title;

import me.glicz.inventoryapi.inventory.GlitchInventory;
import net.kyori.adventure.text.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AnimatedTitle extends Title<AnimatedTitle> {

    private final Map<Integer, Component> titleMap;
    private final int maxFrame;
    private int tick;

    protected AnimatedTitle(int period, List<Component> titles) {
        this(titles.stream().collect(Collectors.toMap(title -> titles.indexOf(title) * period, Function.identity())));
        titleMap.put(maxFrame + period - 1, titleMap.get(maxFrame));
    }

    protected AnimatedTitle(Map<Integer, Component> titleMap) {
        this.titleMap = titleMap;
        maxFrame = titleMap.keySet().stream().max(Comparator.comparingInt(frame -> frame)).orElse(0);
        int minFrame = titleMap.keySet().stream().min(Comparator.comparingInt(frame -> frame)).orElse(0);
        titleMap.put(0, titleMap.get(minFrame));
    }

    private AnimatedTitle(Map<Integer, Component> titleMap, GlitchInventory<?> inventory) {
        this(titleMap);
        inventory.getAnimator()
                .addEveryTickAction("title", (tick, inv) -> {
                    if (titleMap.containsKey(tick % maxFrame))
                        inv.getViewers().forEach(inv::sendInventory);
                    this.tick++;
                });
    }

    @Override
    public Component getComponent() {
        return titleMap.entrySet().stream()
                .filter(entry -> entry.getKey() >= tick % maxFrame)
                .min(Comparator.comparingInt(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .orElse(titleMap.get(maxFrame));
    }

    @Override
    public AnimatedTitle accept(GlitchInventory<?> inventory) {
        return new AnimatedTitle(titleMap, inventory);
    }
}