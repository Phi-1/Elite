package dev.stormwatch.elite.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;

public class SwiftSwimEffect extends MobEffect {
    public static final String ATTRIBUTE_UUID = "ec443e52-876a-4b79-87f9-3d5c8e3f83b3";

    private static final double EFFECT_STRENGTH = 1.0;

    public SwiftSwimEffect() {
        super(MobEffectCategory.BENEFICIAL, 41212);
        this.addAttributeModifier(ForgeMod.SWIM_SPEED.get(), ATTRIBUTE_UUID, EFFECT_STRENGTH, AttributeModifier.Operation.MULTIPLY_BASE);
    }
}
