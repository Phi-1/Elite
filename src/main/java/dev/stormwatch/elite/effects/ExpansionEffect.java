package dev.stormwatch.elite.effects;

import dev.stormwatch.elite.registry.EliteEffects;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

public class ExpansionEffect extends MobEffect {
    public static final String GRAVITY_ATTRIBUTE_UUID = "f072b6e2-fd84-43d1-9e7d-1d538a183711";
    public static final String JUMP_ATTRIBUTE_UUID = "f072b6e2-fd84-43d1-9e7d-1d538a183711";

    private static final double EFFECT_STRENGTH = -0.5;
    private static final double STRONG_EFFECT_MULTIPLIER = 1.6;

    public ExpansionEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x26133b);
        this.addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), GRAVITY_ATTRIBUTE_UUID, EFFECT_STRENGTH, AttributeModifier.Operation.MULTIPLY_BASE);
        this.addAttributeModifier(Attributes.JUMP_STRENGTH, JUMP_ATTRIBUTE_UUID, Math.abs(EFFECT_STRENGTH / 2), AttributeModifier.Operation.MULTIPLY_BASE);
    }

    @Override
    public double getAttributeModifierValue(int amplifier, @NotNull AttributeModifier modifier) {
        // NOTE: strong effect shouldn't be 100% reduced gravity, greater amps are unhandled and wacky
        return amplifier == 1 ? modifier.getAmount() * STRONG_EFFECT_MULTIPLIER : super.getAttributeModifierValue(amplifier, modifier);
    }

    @SubscribeEvent
    public static void reduceFallDamage(LivingHurtEvent event) {
        LivingEntity living = event.getEntity();
        if (living.level().isClientSide()) return;
        if (event.getSource().is(DamageTypes.FALL) && living.hasEffect(EliteEffects.EXPANSION.get())) {
            int amplifier = living.getEffect(EliteEffects.EXPANSION.get()).getAmplifier();
            double damageReduction = amplifier == 1 ? Math.abs(EFFECT_STRENGTH * STRONG_EFFECT_MULTIPLIER)
                    : Math.abs(EFFECT_STRENGTH * (amplifier + 1));
            event.setAmount(Math.max((float) (event.getAmount() * (1 - damageReduction)), 0));
        }
    }
}
