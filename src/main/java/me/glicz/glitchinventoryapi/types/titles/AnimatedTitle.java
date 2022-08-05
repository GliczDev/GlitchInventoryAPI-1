package me.glicz.glitchinventoryapi.types.titles;

import me.glicz.glitchinventoryapi.types.GlitchInventory;
import me.glicz.glitchinventoryapi.types.Title;

import java.util.HashSet;
import java.util.Set;

public class AnimatedTitle implements Title {

    private static final Set<AnimatedTitle> animatedTitles = new HashSet<>();

    private GlitchInventory<?> glitchInventory;
    private final int refresh;
    private int left;
    private final String[] frames;
    private int frame = 0;

    @SuppressWarnings("unused")
    public AnimatedTitle(String... frames) { this(20, frames); }

    public AnimatedTitle(int refresh, String... frames) {
        this.refresh = refresh;
        this.left = refresh;
        this.frames = frames;
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

    @SuppressWarnings("MethodDoesntCallSuperMethod")
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

    public static Set<AnimatedTitle> getAnimatedTitles() {
        return animatedTitles;
    }
}
