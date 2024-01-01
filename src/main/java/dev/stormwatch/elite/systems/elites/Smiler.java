package dev.stormwatch.elite.systems.elites;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;

public class Smiler {

    private static final int SLOW_RADIUS = 8;

    public static void onSpawn(Creeper smiler) {
        makeInvisible(smiler);
    }

    public static void tick(Creeper smiler) {
        slowNearbyPlayers(smiler);
    }

    public static void onHitByPlayer(Creeper smiler) {
        // TODO: teleport
    }

    public static void onHitPlayer(Player player, LivingHurtEvent event) {
        killPlayerOnDamage(player, event);
    }

    private static void makeInvisible(Creeper smiler) {
        smiler.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, -1));
    }

    private static void slowNearbyPlayers(Creeper smiler) {
        List<Player> nearbyPlayers = smiler.level().getEntitiesOfClass(Player.class, smiler.getBoundingBox().inflate(SLOW_RADIUS));
        for (Player player : nearbyPlayers) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 0, true, false, true));
        }
    }

    private static void killPlayerOnDamage(Player player, LivingHurtEvent event) {
        // FIXME: if this triggers in the event before possible damage reductions, it could ignore those
        if (!event.isCanceled() && event.getAmount() > 0) {
            player.kill();
        }
    }

}
