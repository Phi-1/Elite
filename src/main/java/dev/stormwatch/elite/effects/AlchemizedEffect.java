package dev.stormwatch.elite.effects;

import dev.stormwatch.elite.registry.EliteEffects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AlchemizedEffect extends MobEffect {

    private static final int BONUS_LOOTING = 1;
    private static final float BONUS_DAMAGE = 2;

    public AlchemizedEffect() {
        super(MobEffectCategory.HARMFUL, 0xFCE803);
    }

    @SubscribeEvent
    public static void addLootingLevel(LootingLevelEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        MobEffectInstance instance = event.getEntity().getEffect(EliteEffects.ALCHEMIZED.get());
        if (instance != null) {
            int amp = instance.getAmplifier();
            event.setLootingLevel(event.getLootingLevel() + BONUS_LOOTING * amp);
        }
    }

    @SubscribeEvent
    public static void dealBonusDamage(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        MobEffectInstance instance = event.getEntity().getEffect(EliteEffects.ALCHEMIZED.get());
        if (instance != null) {
            int amp = instance.getAmplifier();
            event.setAmount(event.getAmount() + BONUS_DAMAGE * amp);
            event.getEntity().level().playSound(null, event.getEntity().blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.HOSTILE, 0.2f, 5.0f);
        }
    }

}
