package dev.stormwatch.elite.items.weapons;

import dev.stormwatch.elite.registry.EliteEffects;
import dev.stormwatch.elite.registry.EliteTiers;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;

public class GlassSwordItem extends SwordItem {

    public GlassSwordItem() {
        super(EliteTiers.GLASS, 1, -1.8f, new Item.Properties()
                .fireResistant()
                .stacksTo(1)
                .rarity(Rarity.EPIC));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.level().isClientSide() && attacker instanceof Player player && player.getAttackStrengthScale(0) == 1) {
            int amp = -1;
            MobEffectInstance instance = target.getEffect(EliteEffects.BLEED.get());
            if (instance != null) {
                amp = instance.getAmplifier();
            }
            target.addEffect(new MobEffectInstance(EliteEffects.BLEED.get(), 100, amp + 1));
        }
        return super.hurtEnemy(stack, target, attacker);
    }
}
