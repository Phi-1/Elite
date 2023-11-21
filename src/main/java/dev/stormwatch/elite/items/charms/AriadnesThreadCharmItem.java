package dev.stormwatch.elite.items.charms;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import top.theillusivec4.curios.api.SlotContext;

public class AriadnesThreadCharmItem extends CharmItem {

    public AriadnesThreadCharmItem() {
        super(new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.UNCOMMON));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        // TODO: consider making this a toggle, though maybe toggles should be reserved for abilities that consume materials
        if (!(slotContext.entity() instanceof Player player)) return;
        if (!player.level().isClientSide()) return;

        // Copied from Adventurer's Backpack spider ability
        if (player.horizontalCollision && !player.isInFluidType()) {
            if (!player.onGround() && player.isCrouching()) {
                player.setDeltaMovement(player.getDeltaMovement().x, 0.0D, player.getDeltaMovement().z);
            } else {
                player.setDeltaMovement(player.getDeltaMovement().x, 0.20D, player.getDeltaMovement().z);

                Level level = player.level();
                BlockState state = level.getBlockState(player.blockPosition().relative(player.getDirection()));
                player.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state).setPos(player.blockPosition()),
                        player.getX() + (level.random.nextDouble() - 0.5D) * (double) player.getDimensions(Pose.STANDING).width,
                        player.getY() + 0.1D,
                        player.getZ() + (level.random.nextDouble() - 0.5D) * (double) player.getDimensions(Pose.STANDING).width,
                        0.0D, 1.5D, 0.0D);
            }
        }
    }
}
