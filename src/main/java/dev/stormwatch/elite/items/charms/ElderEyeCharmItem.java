package dev.stormwatch.elite.items.charms;

import dev.stormwatch.elite.doc.TickRates;
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

public class ElderEyeCharmItem extends CharmItem {

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

        // TODO: consider making this a toggle that consumes something for every mob it affects. In that case increase wither amp?

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
}
