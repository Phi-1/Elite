package dev.stormwatch.elite.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class BleedEffect extends MobEffect {

    public static final int DAMAGE_INTERVAL = 60;

    public BleedEffect() {
        super(MobEffectCategory.HARMFUL, 0x662211);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.level().getGameTime() % DAMAGE_INTERVAL != 0) return;
        final float baseMaxHealthDamagePercentage = 0.015f;
        float damage = (amplifier + 1) * baseMaxHealthDamagePercentage * livingEntity.getMaxHealth();
        livingEntity.hurt(livingEntity.damageSources().generic(), damage);
    }
}
