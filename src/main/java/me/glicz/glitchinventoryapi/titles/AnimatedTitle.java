package me.glicz.glitchinventoryapi.titles;

import me.glicz.glitchinventoryapi.inventories.GlitchInventory;

import java.util.HashSet;
import java.util.Set;

public class AnimatedTitle extends Title {

    private static final Set<AnimatedTitle> animatedTitles = new HashSet<>();
    private final int refresh;
    private final String[] frames;
    private GlitchInventory<?> glitchInventory;
    private int left;
    private int frame = 0;

    @SuppressWarnings("unused")
    public AnimatedTitle(String... frames) {
        this(20, frames);
    }

    protected AnimatedTitle(int refresh, String... frames) {
        this.refresh = refresh;
        this.left = refresh;
        this.frames = frames;
    }

    public static Set<AnimatedTitle> getAnimatedTitles() {
        return animatedTitles;
    }

    @Override
    public void setInventory(GlitchInventory<?> glitchInventory) {
        this.glitchInventory = glitchInventory;
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

    public void tick() {
        if (!glitchInventory.isOpen()) animatedTitles.remove(this);
        if (left - 1 < 0) {
            frame = (frame < frames.length - 1) ? frame + 1 : 0;
            left = refresh;
            glitchInventory.setTitle(this);
            return;
        }
        left--;
    }
}
