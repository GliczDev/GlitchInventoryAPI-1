package me.glicz.inventoryapi.tasks;

import me.glicz.inventoryapi.titles.AnimatedTitle;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;

public class AnimatedTitleTask extends BukkitRunnable {

    @SuppressWarnings("ForLoopReplaceableByForEach")
    @Override
    public void run() {
        for (Iterator<AnimatedTitle> iterator = AnimatedTitle.getAnimatedTitles().iterator(); iterator.hasNext(); ) {
            iterator.next().tick();
        }
    }
}
