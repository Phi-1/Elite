package dev.stormwatch.elite.util;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class TickTasks {

    private static final List<TickTask> TASKS = new ArrayList<>();

    public static void run(Runnable task, Runnable callback) {
        Thread thread = new Thread(task);
        thread.start();
        TASKS.add(new TickTask(thread, callback));
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            checkTasks();
        }
    }

    private static void checkTasks() {
        List<TickTask> toRemove = new ArrayList<>();
        for (TickTask task : TASKS) {
            if (!task.thread.isAlive()) {
                task.callback.run();
                toRemove.add(task);
            }
        }
        for (TickTask task : toRemove) {
            TASKS.remove(task);
        }
    }

    private record TickTask(Thread thread, Runnable callback) {}

}
