package dev.stormwatch.elite.effects;

import dev.stormwatch.elite.registry.EliteEffects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AlchemizedEffect extends MobEffect {

    private static final int BONUS_LOOTING = 1;
    private static final float BONUS_DAMAGE = 4;

    public AlchemizedEffect() {
        super(MobEffectCategory.HARMFUL, 0xFCE803);
    }

    @SubscribeEvent
    public static void addLootingLevel(LootingLevelEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (event.getEntity().hasEffect(EliteEffects.ALCHEMIZED.get())) {
            event.setLootingLevel(event.getLootingLevel() + BONUS_LOOTING);
        }
    }

    @SubscribeEvent
    public static void dealBonusDamage(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (event.getEntity().hasEffect(EliteEffects.ALCHEMIZED.get())) {
            event.setAmount(event.getAmount() + BONUS_DAMAGE);
            event.getEntity().level().playSound(null, event.getEntity().blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.HOSTILE, 0.3f, 5.0f);
        }
    }

}
