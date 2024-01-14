package dev.stormwatch.elite.client;

import dev.stormwatch.elite.client.data.EliteTracker;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.TickEvent;

import java.util.List;

public class ClientEliteEnemyEvents {

    public static final int ELITE_TRACKING_RADIUS = 32;

    public static void trackNearestElite(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        List<LivingEntity> nearbyElites = event.player.level().getEntities(event.player,
                event.player.getBoundingBox().inflate(ELITE_TRACKING_RADIUS),
                (entity) -> EliteTracker.isTrackingElite(entity.getUUID()) && entity instanceof LivingEntity)
                .stream().map((entity) -> (LivingEntity) entity).toList();

    }

}
