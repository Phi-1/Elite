package dev.stormwatch.elite.effects;

import dev.stormwatch.elite.doc.SoundEventIndices;
import dev.stormwatch.elite.networking.EliteNetworking;
import dev.stormwatch.elite.networking.packets.AddShimmeringFallParticlesS2CPacket;
import dev.stormwatch.elite.networking.packets.PlaySoundS2CPacket;
import dev.stormwatch.elite.registry.EliteEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.concurrent.ThreadLocalRandom;

public class ShimmeringCrownPassivesEffect extends MobEffect {

    private static final int FIRE_RESISTANCE_AMP = 0;
    private static final int NEGATE_FALL_DAMAGE_AMP = 1;
    private static final int INFLICT_ALCHEMIZED_AMP = 2;

    public ShimmeringCrownPassivesEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x3734eb);
    }

    @SubscribeEvent
    public static void negateFireDamage(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player player)
                || player.level().isClientSide()
                || !event.getSource().is(DamageTypeTags.IS_FIRE)) return;

        MobEffectInstance instance = player.getEffect(EliteEffects.SHIMMERING_CROWN_PASSIVES.get());
        if (instance == null) return;
        int amp = instance.getAmplifier();
        if (amp >= FIRE_RESISTANCE_AMP) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void negateFallDamage(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player player)
                || player.level().isClientSide()
                || !event.getSource().is(DamageTypeTags.IS_FALL)) return;

        MobEffectInstance instance = player.getEffect(EliteEffects.SHIMMERING_CROWN_PASSIVES.get());
        if (instance == null) return;
        int amp = instance.getAmplifier();
        if (amp >= NEGATE_FALL_DAMAGE_AMP) {
            event.setCanceled(true);
            EliteNetworking.sendToPlayer(new PlaySoundS2CPacket(SoundEventIndices.SHIMMERING_CROWN_NEGATE_FALL_DAMAGE, 0.6f, 0.6f), (ServerPlayer) player);
            EliteNetworking.sendToClientsTrackingChunk(new AddShimmeringFallParticlesS2CPacket(player.position()), player.level().getChunkAt(player.blockPosition()));
        }
    }

    public static void addFallNegationParticles(Vec3 pos, Level level) {
        final int nParticles = 19;
        for (int i = 0; i < nParticles; i++) {
            double dX = ThreadLocalRandom.current().nextDouble(-1, 1);
            double dY = ThreadLocalRandom.current().nextDouble(0, 1);
            double dZ = ThreadLocalRandom.current().nextDouble(-1, 1);
            level.addParticle(ParticleTypes.ENCHANTED_HIT, pos.x, pos.y, pos.z, dX, dY, dZ);
        }
    }

    @SubscribeEvent
    public static void inflictAlchemized(LivingHurtEvent event) {
        if (!(event.getSource().getEntity() instanceof Player player)
                || player.level().isClientSide()) return;

        MobEffectInstance instance = player.getEffect(EliteEffects.SHIMMERING_CROWN_PASSIVES.get());
        if (instance == null) return;
        int amp = instance.getAmplifier();
        if (amp >= INFLICT_ALCHEMIZED_AMP) {
            event.getEntity().addEffect(new MobEffectInstance(EliteEffects.ALCHEMIZED.get(), 140, 1));
        }
    }

}
