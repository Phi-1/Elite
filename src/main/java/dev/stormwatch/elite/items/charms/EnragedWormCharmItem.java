package dev.stormwatch.elite.items.charms;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

public class EnragedWormCharmItem extends CharmItem {

    // The bones are their money
    public static final int LOOTING_LEVEL = 1;

    public EnragedWormCharmItem() {
        super(new Item.Properties()
                .rarity(Rarity.UNCOMMON)
                .stacksTo(1)
                .food(new FoodProperties.Builder()
                        .alwaysEat()
                        .saturationMod(0)
                        .nutrition(-4)
                        .effect(() -> new MobEffectInstance(MobEffects.BAD_OMEN, -1), 1)
                        .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 4), 1)
                        .effect(() -> new MobEffectInstance(MobEffects.WITHER, 200, 0), 1)
                        .build()));
    }

    @Override
    public int getLootingLevel(SlotContext slotContext, DamageSource source, LivingEntity target, int baseLooting, ItemStack stack) {
        return LOOTING_LEVEL + super.getLootingLevel(slotContext, source, target, baseLooting, stack);
    }

}
