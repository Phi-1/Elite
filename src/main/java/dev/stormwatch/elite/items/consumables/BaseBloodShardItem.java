package dev.stormwatch.elite.items.consumables;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class BaseBloodShardItem extends Item {

    private final float healAmount;
    private static final int COOLDOWN = 24 * 20;

    public BaseBloodShardItem(float healAmount, Rarity rarity) {
        super(new Item.Properties()
                .stacksTo(256)
                .rarity(rarity));
        this.healAmount = healAmount;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!(stack.getItem() instanceof BaseBloodShardItem)
                || player.getCooldowns().isOnCooldown(this)) return InteractionResultHolder.fail(stack);

        if (level.isClientSide()) {
            level.playSound(player, player.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.8f, 5.0f);
        } else {
            player.heal(this.healAmount);
            stack.shrink(1);
            player.getCooldowns().addCooldown(this, COOLDOWN);
        }

        return InteractionResultHolder.success(stack);
    }
}
