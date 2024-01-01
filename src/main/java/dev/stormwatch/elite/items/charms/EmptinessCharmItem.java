package dev.stormwatch.elite.items.charms;

import dev.stormwatch.elite.capabilities.CooldownMarker;
import dev.stormwatch.elite.capabilities.CooldownMarkerProvider;
import dev.stormwatch.elite.items.CooldownAbilityItem;
import dev.stormwatch.elite.registry.EliteEffects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class EmptinessCharmItem extends CharmItem implements CooldownAbilityItem {

    public static final int COOLDOWN_MILLIS = 12 * 1000;
    public static final int COOLDOWN_TICKS = COOLDOWN_MILLIS / 1000 * 20;

    public EmptinessCharmItem() {
        super(new Item.Properties()
                .rarity(Rarity.RARE)
                .stacksTo(1));
    }

    @Override
    public void activateAbility(ItemStack stack, Player player) {
        CooldownMarker cooldownMarker = stack.getCapability(CooldownMarkerProvider.CAPABILITY_TYPE).orElse(CooldownMarker.EMPTY);
        if (cooldownMarker == CooldownMarker.EMPTY) return;
        if (cooldownMarker.isOnCooldown(getCooldownMillis())) return; // TODO: cooldown feedback
        player.addEffect(new MobEffectInstance(EliteEffects.EMPTINESS.get(), 20, 0, true, false, true));
        player.level().playSound(null, player.blockPosition(), SoundEvents.WARDEN_SONIC_CHARGE, SoundSource.PLAYERS, 1.0f, 5.0f);
        cooldownMarker.putOnCooldown();
        player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
    }

    @Override
    public int getCooldownMillis() {
        return COOLDOWN_MILLIS;
    }
}
