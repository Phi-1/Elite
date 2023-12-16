package dev.stormwatch.elite.util;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class TickTimers {

    // TODO: separate client and server timers

    private static final List<TickTimer> TIMERS = new ArrayList<>();

    public static void schedule(Runnable task, int tickDelay) {
        TIMERS.add(new TickTimer(task, tickDelay));
    }

    public static void tickAll() {
        List<TickTimer> toRemove = new ArrayList<>();
        for (TickTimer timer : TIMERS) {
            int ticksRemaining = timer.tick();
            if (ticksRemaining <= 0) {
                timer.getTask().run();
                toRemove.add(timer);
            }
        }
        for (TickTimer timer : toRemove) {
            TIMERS.remove(timer);
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            tickAll();
        }
    }

    private static class TickTimer {

        private final Runnable task;
        private int ticksRemaining;

        public TickTimer(Runnable task, int tickDelay) {
            this.task = task;
            this.ticksRemaining = tickDelay;
        }

        public int tick() {
            this.ticksRemaining--;
            return this.ticksRemaining;
        }

        public Runnable getTask() {
            return this.task;
        }

    }

}
