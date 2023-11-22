package dev.stormwatch.elite.items.charms;

import dev.stormwatch.elite.capabilities.ToggleMarker;
import dev.stormwatch.elite.capabilities.ToggleMarkerProvider;
import dev.stormwatch.elite.doc.TickRates;
import dev.stormwatch.elite.items.ToggleAbilityItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.phys.AABB;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class ElderEyeCharmItem extends CharmItem implements ToggleAbilityItem {

    public static final int EFFECT_RADIUS = 10;

    public ElderEyeCharmItem() {
        super(new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.EPIC)
                .fireResistant());
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (!(slotContext.entity() instanceof Player player)) return;
        if (player.level().isClientSide()) return;
        if (!(player.level().getGameTime() % TickRates.HIGH == 0)) return;

        ToggleMarker toggleMarker = stack.getCapability(ToggleMarkerProvider.CAPABILITY_TYPE).orElseThrow(() -> new IllegalStateException("Toggle ability item does not have a toggle marker"));
        if (!toggleMarker.isActive()) return;

        // TODO: consider making this consume something for every mob it affects. In that case increase wither amp?

        List<Monster> nearbyMonsters = player.level().getEntitiesOfClass(Monster.class, new AABB(player.getBlockX() - EFFECT_RADIUS, player.getBlockY() - EFFECT_RADIUS, player.getBlockZ() - EFFECT_RADIUS, player.getBlockX() + EFFECT_RADIUS, player.getBlockY() + EFFECT_RADIUS, player.getBlockZ() + EFFECT_RADIUS));
        for (Monster monster : nearbyMonsters) {
            monster.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
            monster.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0)); // TODO: consider moving this to Warden artifact
            // constantly reapplying wither makes it never take damage, so only reapply when it runs out
            if (!monster.hasEffect(MobEffects.WITHER)) {
                monster.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 0));
            }
        }
    }

    @Override
    public void activateAbility(ItemStack stack, Player player) {
        // TODO: sound
        ToggleMarker toggleMarker = stack.getCapability(ToggleMarkerProvider.CAPABILITY_TYPE).orElseThrow(() -> new IllegalStateException("Toggle ability item does not have a toggle marker"));
        toggleMarker.toggle();
    }

}
