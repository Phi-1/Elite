package dev.stormwatch.elite.items.weapons;

import dev.stormwatch.elite.registry.EliteItems;
import dev.stormwatch.elite.util.AttributeUtil;
import dev.stormwatch.elite.util.InventoryUtil;
import dev.stormwatch.elite.util.TickTimers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class TheHeraldItem extends AxeItem {

    private static final int EFFECT_RADIUS = 16;
    public static final AttributeUtil.AttributeInfo SLOW_INFO = new AttributeUtil.AttributeInfo("theherald_slow", UUID.fromString("56bdd263-ec71-439e-bf81-d82c4837b2c2"));
    private static final double SLOW_AMOUNT = -0.2;

    public TheHeraldItem() {
        super(Tiers.DIAMOND, 6.0f, -3.5f, new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.EPIC)
                .fireResistant());
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        if (!attacker.level().isClientSide() && attacker instanceof Player player && player.getAttackStrengthScale(0) == 1) {
            List<LivingEntity> nearbyEnemies = player.level().getEntitiesOfClass(LivingEntity.class,
                    player.getBoundingBox().inflate(EFFECT_RADIUS),
                    (entity) -> entity instanceof Enemy);

            for (int i = 0; i < nearbyEnemies.size(); i++) {
                LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(player.level());
                if (lightningbolt != null) {
                    int index = ThreadLocalRandom.current().nextInt(nearbyEnemies.size());
                    Vec3 pos = nearbyEnemies.get(index).position();
                    lightningbolt.moveTo(pos);
                    lightningbolt.setVisualOnly(false);
                    TickTimers.schedule(() -> {
                        player.level().addFreshEntity(lightningbolt);
                    }, i * 3);
                }
            }
        }

        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected) {
        if (!(entity instanceof Player player)
                || player.level().isClientSide()) return;
        if (isSelected) {
            AttributeUtil.setTransientAttribute(player, Attributes.MOVEMENT_SPEED, SLOW_INFO.name(), SLOW_INFO.uuid(), SLOW_AMOUNT, AttributeModifier.Operation.MULTIPLY_BASE);
        } else {
            AttributeUtil.removeAttributeModifier(player, Attributes.MOVEMENT_SPEED, SLOW_INFO.uuid());
        }
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
