package dev.stormwatch.elite.items.charms;

import dev.stormwatch.elite.capabilities.CooldownMarker;
import dev.stormwatch.elite.capabilities.CooldownMarkerProvider;
import dev.stormwatch.elite.entities.projectiles.ResonantBoom;
import dev.stormwatch.elite.items.CooldownAbilityItem;
import dev.stormwatch.elite.registry.EliteEntityTypes;
import dev.stormwatch.elite.util.TickTimers;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class ResonanceCharmItem extends CharmItem implements CooldownAbilityItem {

    private static final int COOLDOWN_MILLIS = 24 * 1000;
    private static final int COOLDOWN_TICKS = COOLDOWN_MILLIS / 1000 * 20;

    public ResonanceCharmItem() {
        super(new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.EPIC)
                .fireResistant());
    }

    @Override
    public void activateAbility(ItemStack stack, Player player) {
        CooldownMarker cooldownMarker = stack.getCapability(CooldownMarkerProvider.CAPABILITY_TYPE).orElseThrow(() -> new IllegalStateException("Resonance does not have a cooldown marker"));
        if (cooldownMarker.isOnCooldown(getCooldownMillis())) return; // TODO: cooldown feedback
        player.level().playSound(null, player.blockPosition(), SoundEvents.WARDEN_SONIC_CHARGE, SoundSource.PLAYERS, 0.8f, 1.2f);

        int delay = 50;
        for (LivingEntity living : player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(64))) {
            living.addEffect(new MobEffectInstance(MobEffects.GLOWING, delay + 20));
        }
        TickTimers.schedule(() -> {
            ResonantBoom boom = EliteEntityTypes.RESONANT_BOOM.get().create(player.level());
            if (boom == null) return;
            boom.moveTo(player.blockPosition().above().getCenter());
            boom.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 6, 1.0f);
            boom.setOwner(player);
            player.level().addFreshEntity(boom);
            player.level().playSound(null, player.blockPosition(), SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 1.0f, 1.2f);
            player.setDeltaMovement(player.getDeltaMovement().add(boom.getDeltaMovement().scale(-0.4)));
            player.hurtMarked = true;
        }, delay);
        cooldownMarker.putOnCooldown();
        player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
    }

    @Override
    public int getCooldownMillis() {
        return COOLDOWN_MILLIS;
    }
}
