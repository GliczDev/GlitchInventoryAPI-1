package me.glicz.glitchinventoryapi.titles;

import lombok.Getter;
import lombok.Setter;
import me.glicz.glitchinventoryapi.inventories.GlitchInventory;

import java.util.HashSet;
import java.util.Set;

public class AnimatedTitle extends Title {

    @Getter
    private static final Set<AnimatedTitle> animatedTitles = new HashSet<>();
    protected final int refresh;
    protected final String[] frames;
    @Setter
    protected GlitchInventory<?> inventory;
    protected int left;
    protected int frame = 0;

    protected AnimatedTitle(int refresh, String... frames) {
        this.refresh = refresh;
        this.left = refresh;
        this.frames = frames;
    }

    @Override
    public String getString() {
        animatedTitles.add(this);
        return frames[frame];
    }

    @Override
    public Title clone() {
        return new AnimatedTitle(refresh, frames);
    }

    protected int getNextFrame() {
        return (frame < frames.length - 1) ? frame + 1 : 0;
    }

    public void tick() {
        if (!inventory.isOpen()) animatedTitles.remove(this);
        if (left - 1 < 0) {
            frame = getNextFrame();
            left = refresh;
            inventory.setTitle(this);
            return;
        }
        left--;
    }
}
