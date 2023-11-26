package dev.stormwatch.elite.items.charms;

import dev.stormwatch.elite.Feedback;
import dev.stormwatch.elite.capabilities.CooldownMarker;
import dev.stormwatch.elite.capabilities.CooldownMarkerProvider;
import dev.stormwatch.elite.items.CooldownAbilityItem;
import dev.stormwatch.elite.registry.EliteEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class PhantomWingCharmItem extends CharmItem implements CooldownAbilityItem {

    public static final int COOLDOWN_MILLISECONDS = 90 * 1000;
    public static final int EFFECT_DURATION_SECONDS = 30 * 20;

    public PhantomWingCharmItem() {
        super(new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.RARE));
    }

    @Override
    public void activateAbility(ItemStack stack, Player player) {
        CooldownMarker cooldownMarker = stack.getCapability(CooldownMarkerProvider.CAPABILITY_TYPE).orElseThrow(() -> new IllegalStateException("Phantom Wing does not have a cooldown marker"));
        if (cooldownMarker.onCooldown(this.getCooldownMillis())) {
            Feedback.onItemIsOnCooldown();
            return;
        }
        player.addEffect(new MobEffectInstance(EliteEffects.EXPANSION.get(), EFFECT_DURATION_SECONDS, 0, true, true, true));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, EFFECT_DURATION_SECONDS, 2, true, true, true));
        player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, EFFECT_DURATION_SECONDS, 0, true, true, true));
        cooldownMarker.putOnCooldown();
        player.getCooldowns().addCooldown(this, this.getCooldownMillis() / 50);
        // TODO: sound
    }

    @Override
    public int getCooldownMillis() {
        return COOLDOWN_MILLISECONDS;
    }

}
