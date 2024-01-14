package dev.stormwatch.elite.effects;

import dev.stormwatch.elite.doc.SlotIndices;
import dev.stormwatch.elite.registry.EliteEffects;
import dev.stormwatch.elite.registry.EliteItems;
import dev.stormwatch.elite.util.InventoryUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MarkedEffect extends MobEffect {

    private static final float FLAT_DAMAGE_BONUS = 8.0f;
    private static final float DAMAGE_MODIFIER = 3.0f;

    public MarkedEffect() {
        super(MobEffectCategory.HARMFUL, 0x1e7550);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide()
                || !event.getEntity().hasEffect(EliteEffects.MARKED.get())
                || !(event.getSource().getEntity() instanceof Player player)
                || !InventoryUtil.hasArmorEquipped(player, EliteItems.PALE_FUR_HELMET.get(), SlotIndices.HELMET)) return;

        if (!isBackstab(event.getEntity(), player)) return;
        event.getEntity().removeEffect(EliteEffects.MARKED.get());
        event.setAmount(event.getAmount() * DAMAGE_MODIFIER + FLAT_DAMAGE_BONUS);
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 80, 1));
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 30, 2));
        player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 240));
        player.level().playSound(null, player.blockPosition(), SoundEvents.PHANTOM_HURT, SoundSource.PLAYERS, 1.0f, 1.2f);
    }

    private static boolean isBackstab(LivingEntity target, Player player) {
        if (target.hasEffect(EliteEffects.SMOKED.get())) return true;
        float leftBound = player.getYRot() - 100;
        float rightBound = player.getYRot() + 100;
        return target.getYRot() >= leftBound && target.getYRot() <= rightBound;
    }

}
