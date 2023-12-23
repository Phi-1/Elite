package dev.stormwatch.elite.effects;

import dev.stormwatch.elite.registry.EliteEffects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class InvincibilityEffect extends MobEffect {

    public InvincibilityEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xe8deae);
    }

    @SubscribeEvent
    public static void preventDamage(LivingHurtEvent event) {
        if (!event.getEntity().level().isClientSide()
                && event.getEntity().hasEffect(EliteEffects.INVINCIBILITY.get())) {
            event.setCanceled(true);
            event.getEntity().level().playSound(null, event.getEntity().blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.HOSTILE, 0.8f, 0.8f);
        }
    }

}
