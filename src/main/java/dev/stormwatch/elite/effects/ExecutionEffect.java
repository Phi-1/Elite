package dev.stormwatch.elite.effects;

import dev.stormwatch.elite.doc.SoundEventIndices;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class ExecutionEffect extends InstantenousMobEffect {

    // When applying this effect, check whether entity already has it, otherwise use this duration. This allows the duration to function as an internal cooldown
    public static final int DURATION = 20;

    public ExecutionEffect() {
        super(MobEffectCategory.HARMFUL, 0xFFFFFF);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration >= DURATION;
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        applyEffect(livingEntity, amplifier);
    }

    @Override
    public void applyInstantenousEffect(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity livingEntity, int amplifier, double health) {
        applyEffect(livingEntity, amplifier);
    }

    private void applyEffect(LivingEntity livingEntity, int amplifier) {
        final float missingHealthDamagePercentage = 0.15f;
        float damage = (livingEntity.getMaxHealth() - livingEntity.getHealth()) * missingHealthDamagePercentage * (amplifier + 1);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                livingEntity.level().playSound(null, livingEntity.blockPosition(), SoundEventIndices.getSoundForIndex(SoundEventIndices.EXECUTION_TRIGGER), SoundSource.PLAYERS, 4.0f, 5.0f);
                livingEntity.hurt(livingEntity.damageSources().generic(), damage);
            }
        }, 1000);
    }

}
