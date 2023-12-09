package dev.stormwatch.elite.items.weapons;

import dev.stormwatch.elite.Elite;
import dev.stormwatch.elite.doc.TickRates;
import dev.stormwatch.elite.registry.EliteItems;
import dev.stormwatch.elite.util.AttributeUtil;
import dev.stormwatch.elite.util.InventoryUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class HungeringBladeItem extends SwordItem {

    private static final int SELF_DAMAGE_INTERVAL = 3 * 20;
    private static final float SELF_DAMAGE_AMOUNT = 1.0f;
    private static final float MAX_MISSING_HEALTH_DAMAGE = 4.0f;
    private static final float LIFESTEAL_FRACTION = 0.2f;
    private static final double SPEED_AMOUNT = 0.4;
    public static final AttributeUtil.AttributeInfo SPEED_MODIFIER_INFO = new AttributeUtil.AttributeInfo("hungering_blade_speed", UUID.fromString("d63d70a7-b018-4fa4-9f87-490ba30f221a"));

    public HungeringBladeItem() {
        super(Tiers.IRON, 3, -2.0F, new Item.Properties()
                .rarity(Rarity.RARE)
                .stacksTo(1));
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, Level level, @NotNull Entity entity, int slotID, boolean isSelected) {
        if (level.isClientSide()) return;
        if (!(entity instanceof Player player)) return;

        if (isSelected) {
            if (level.getGameTime() % SELF_DAMAGE_INTERVAL == 0) {
                player.hurt(player.damageSources().magic(), SELF_DAMAGE_AMOUNT);
                AttributeUtil.setTransientAttribute(player, Attributes.MOVEMENT_SPEED, SPEED_MODIFIER_INFO.name(), SPEED_MODIFIER_INFO.uuid(), SPEED_AMOUNT, AttributeModifier.Operation.MULTIPLY_BASE);
                // TODO: dialogue
            }
        } else {
            // don't remove if player has two blades, one in inventory ticking and other in hand
            if (!InventoryUtil.isHoldingItem(player, EliteItems.HUNGERING_BLADE.get())) {
                AttributeUtil.removeAttributeModifier(player, Attributes.MOVEMENT_SPEED, SPEED_MODIFIER_INFO.uuid());
            }
        }
    }

    @SubscribeEvent
    public static void onHurtEnemy(LivingHurtEvent event) {
        if (!(event.getSource().getEntity() instanceof Player player)) return;
        if (player.level().isClientSide()) return;
        if (InventoryUtil.isHoldingItem(player, EliteItems.HUNGERING_BLADE.get())) {
            double healthFraction = player.getHealth() / player.getMaxHealth();
            float bonusDamage = (float) ((1 - healthFraction) * MAX_MISSING_HEALTH_DAMAGE);
            event.setAmount(event.getAmount() + bonusDamage);
            player.heal(event.getAmount() * LIFESTEAL_FRACTION);
            // TODO: dialogue
        }
    }

}
