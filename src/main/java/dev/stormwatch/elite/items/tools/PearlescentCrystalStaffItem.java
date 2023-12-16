package dev.stormwatch.elite.items.tools;

import dev.stormwatch.elite.doc.SoundEventIndices;
import dev.stormwatch.elite.networking.EliteNetworking;
import dev.stormwatch.elite.networking.packets.PlaySoundS2CPacket;
import dev.stormwatch.elite.registry.EliteBlocks;
import dev.stormwatch.elite.util.TickTimers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class PearlescentCrystalStaffItem extends Item {

    private final int USE_COOLDOWN = 600;
    private final int MAX_BRIDGE_LENGTH = 64;

    public PearlescentCrystalStaffItem() {
        super(new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.RARE));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.getCooldowns().isOnCooldown(this)) return InteractionResultHolder.fail(stack);
        if (!level.isClientSide()) {
            if (!tryPlaceBridge(level, player)) {
                EliteNetworking.sendToPlayer(new PlaySoundS2CPacket(SoundEventIndices.ITEM_USE_FAIL), (ServerPlayer) player);
                return InteractionResultHolder.fail(stack);
            }
            player.getCooldowns().addCooldown(this, USE_COOLDOWN);
            EliteNetworking.sendToPlayer(new PlaySoundS2CPacket(SoundEventIndices.PEARLESCENT_STAFF_ACTIVATE), (ServerPlayer) player);
        }

        return InteractionResultHolder.success(stack);
    }

    private boolean tryPlaceBridge(Level level, Player player) {
        Direction facing = player.getDirection();
        // scan 3x3 in front of player for valid start to bridge
        BlockPos scanCenter = player.blockPosition().below().relative(facing, 2);
        boolean hasValidBridgeStart = false;
        for (BlockPos.MutableBlockPos spiralPos : BlockPos.spiralAround(scanCenter, 1, facing.getClockWise(), facing)) {
            if (isBridgeReplaceable(level.getBlockState(spiralPos))) {
                hasValidBridgeStart = true;
                break;
            }
        }
        if (!hasValidBridgeStart) return false;

        // bridge out each column until it hits an obstruction
        BlockPos leftColumnStart = player.blockPosition().below().relative(facing).relative(facing.getCounterClockWise());
        BlockPos.MutableBlockPos bridgeCursor = leftColumnStart.mutable();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < MAX_BRIDGE_LENGTH; j++) {
                if (!isBridgeReplaceable(level.getBlockState(bridgeCursor))) {
                    if (j > 2) break;
                    bridgeCursor.move(facing);
                    continue;
                }
                final BlockPos toPlace = bridgeCursor.immutable();
                TickTimers.schedule(() -> {
                    level.setBlock(toPlace, EliteBlocks.PEARLESCENT_CRYSTAL.get().defaultBlockState(), 3);
                }, i + j * 2 + 1);
                bridgeCursor.move(facing);
            }
            bridgeCursor = leftColumnStart.mutable().move(facing.getClockWise(), i + 1);
        }
        return true;
    }

    private boolean isBridgeReplaceable(BlockState state) {
        return state.is(Blocks.AIR) || state.is(BlockTags.FLOWERS) || state.is(BlockTags.SNOW)
                || state.is(BlockTags.LEAVES) || state.is(BlockTags.FIRE) || state.is(Blocks.GRASS);
    }

}
