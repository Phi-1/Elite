package dev.stormwatch.elite.effects;

import dev.stormwatch.elite.registry.EliteEffects;
import dev.stormwatch.elite.util.AttributeUtil;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.UUID;

public class OverloadedEffect extends MobEffect {

    private static final AttributeUtil.AttributeInfo SLOW_INFO = new AttributeUtil.AttributeInfo("overloaded_slow", UUID.fromString("a55132e8-8919-464a-9d01-cdc05d13ba9d"));
    private static final float PRIMARY_TARGET_DAMAGE_INCREASE = 1.2f;
    private static final int LIGHTNING_AREA_DIAMETER = 12;

    public OverloadedEffect() {
        super(MobEffectCategory.HARMFUL, 0xC889F5);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, SLOW_INFO.uuid().toString(), -0.8, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @SubscribeEvent
    public static void applyEffect(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!event.getEntity().hasEffect(EliteEffects.OVERLOADED.get())) return;
        if (event.getSource().is(DamageTypeTags.IS_LIGHTNING)) return;

        hitSurroundingOverloadedEntitiesWithLightning(event.getEntity().level(), event.getEntity());

        event.setAmount(event.getAmount() * PRIMARY_TARGET_DAMAGE_INCREASE);
    }

    private static void hitSurroundingOverloadedEntitiesWithLightning(Level level, LivingEntity primaryTarget) {
        List<Entity> nearbyEntities = level.getEntities(primaryTarget,
                AABB.ofSize(primaryTarget.position(), LIGHTNING_AREA_DIAMETER, LIGHTNING_AREA_DIAMETER, LIGHTNING_AREA_DIAMETER));

        for (Entity entity : nearbyEntities) {
            if (!(entity instanceof LivingEntity living)
                    || !living.hasEffect(EliteEffects.OVERLOADED.get())) continue;

            LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(level);
            if (lightningbolt != null) {
                lightningbolt.moveTo(Vec3.atBottomCenterOf(new Vec3i(living.getBlockX(), living.getBlockY(), living.getBlockZ())));
                lightningbolt.setVisualOnly(false);
                level.addFreshEntity(lightningbolt);
            }
        }
    }

}
