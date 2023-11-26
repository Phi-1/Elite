package dev.stormwatch.elite.items.weapons;

import dev.stormwatch.elite.registry.EliteItems;
import dev.stormwatch.elite.util.InventoryUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TheHeraldItem extends AxeItem {

    private static final int EFFECT_RADIUS = 16;

    public TheHeraldItem() {
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
        // TODO: in timertask check everything for null

        return super.hurtEnemy(stack, target, attacker);
    }

    @SubscribeEvent
    public static void negateLightningDamage(LivingHurtEvent event) {
        if (!event.getSource().is(DamageTypeTags.IS_LIGHTNING)) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (InventoryUtil.isHoldingItem(player, EliteItems.THE_HERALD.get())) {
            event.setAmount(0);
        }
    }

}
