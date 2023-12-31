package dev.stormwatch.elite.systems.elites;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;

public class Monstrosity {

    private static final int REGENERATION_INTERVAL = 50;
    private static final int SLOWNESS_DURATION = 60;
    private static final int SLOWNESS_AMPLIFIER = 1;

    public static void tick(Zombie monstrosity) {
        healMonstrosity(monstrosity);
    }

    public static void onHitPlayer(Player player) {
        inflictSlowness(player);
    }

    public static void onHitByPlayer(Player player) {
        inflictSlowness(player);
    }

    private static void healMonstrosity(Zombie monstrosity) {
        if (monstrosity.level().getGameTime() % REGENERATION_INTERVAL == 0) {
            monstrosity.heal(1.0f);
        }
    }

    private static void inflictSlowness(Player player) {
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, SLOWNESS_DURATION, SLOWNESS_AMPLIFIER));
    }

}
