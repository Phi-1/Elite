package dev.stormwatch.elite.effects;

import dev.stormwatch.elite.networking.EliteNetworking;
import dev.stormwatch.elite.networking.packets.AddEmptinessParticlesS2CPacket;
import dev.stormwatch.elite.registry.EliteEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EmptinessEffect extends MobEffect {

    private static final int EXPLOSION_RADIUS = 5;
    private static final float DAMAGE_MODIFIER = 2.0f;

    public EmptinessEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x6a9e9c);
    }

    @SubscribeEvent
    public static void reflectDamage(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player player)
                || player.level().isClientSide()
                || !player.hasEffect(EliteEffects.EMPTINESS.get())) return;

        performExplosion(player, event.getAmount() * DAMAGE_MODIFIER);
        event.setCanceled(true);
        player.removeEffect(EliteEffects.EMPTINESS.get());
    }

    private static void performExplosion(Player player, float damage) {
        List<LivingEntity> inRange = player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(EXPLOSION_RADIUS));
        for (LivingEntity living : inRange) {
            if (living == player) continue;
            living.hurt(living.damageSources().playerAttack(player), damage);
            living.knockback(1, Mth.sin((float) (player.getYRot() * (Math.PI / 180))), -Mth.cos((float) (player.getYRot() * (Math.PI / 180))));
        }
        player.level().playSound(null, player.blockPosition(), SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 0.8f, 1.0f);
        EliteNetworking.sendToClientsTrackingChunk(new AddEmptinessParticlesS2CPacket(player.blockPosition().above()), player.level().getChunkAt(player.blockPosition()));
    }

    public static void createExplosionParticles(Level level, BlockPos center) {
        final int nRings = 7;
        final int nBubbles = 15;
        final float radiusPerRing = 1;

        for (int ring = 0; ring < nRings; ring++) {
            // divide along unit circle, multiply by radius, add player position
            double offset = 2 * Math.PI / nBubbles;
            for (int bubble = 0; bubble < nBubbles; bubble++) {
                double z = Math.cos(bubble * offset) + ThreadLocalRandom.current().nextFloat(-1, 1);
                double x = Math.sin(bubble * offset) + ThreadLocalRandom.current().nextFloat(-1, 1);
                level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, center.getX() + x * radiusPerRing * (ring + 1), center.getY(), center.getZ() + z * radiusPerRing * (ring + 1), z, 0.2, z);
            }
        }
    }

}
