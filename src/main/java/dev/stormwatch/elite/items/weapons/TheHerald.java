package dev.stormwatch.elite.items.weapons;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.Vec3;

public class TheHerald extends AxeItem {

    private static final int EFFECT_RADIUS = 16;

    public TheHerald() {
        super(Tiers.DIAMOND, 6.0f, -4f, new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.EPIC)
                .fireResistant());
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // TODO: find monsters in range -> spawn one lightning bolt for each + base amount, make lightning bolts strike random monsters in range one after the other with slight delay in between
        // TODO: bolts do not need to strike every enemy once, can be two on one and zero on another

        // Copied from ServerLevel#tickChunk
//        LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(this);
//        if (lightningbolt != null) {
//            lightningbolt.moveTo(Vec3.atBottomCenterOf(blockpos));
//            lightningbolt.setVisualOnly(flag1);
//            this.addFreshEntity(lightningbolt);
//        }

        return super.hurtEnemy(stack, target, attacker);
    }
}
