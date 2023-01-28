package me.glicz.glitchinventoryapi.titles;

import java.security.SecureRandom;

public class RandomTitle extends AnimatedTitle {

    private final SecureRandom random;

    protected RandomTitle(int refresh, String... frames) {
        super(refresh, frames);
        this.random = new SecureRandom();
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Title clone() {
        return new RandomTitle(refresh, frames);
    }

    @Override
    protected int getNextFrame() {
        return random.nextInt(frames.length - 1);
    }
}
