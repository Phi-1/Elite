package dev.stormwatch.elite.items.weapons;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableMap;
import dev.stormwatch.elite.blocks.RuneBlock;
import dev.stormwatch.elite.doc.RuneTypes;
import dev.stormwatch.elite.doc.SoundEventIndices;
import dev.stormwatch.elite.networking.EliteNetworking;
import dev.stormwatch.elite.networking.packets.PlaySoundS2CPacket;
import dev.stormwatch.elite.registry.EliteBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class BaseRuneStaffItem extends Item {

    private final int runeType;
    // TODO: cooldowns
    private static final ImmutableMap<Integer, Integer> RUNE_TYPE_COOLDOWNS = new ImmutableMap.Builder<Integer, Integer>()
            .put(RuneTypes.OVERLOADING, 20)
            .put(RuneTypes.ALCHEMIZING, 20)
            .put(RuneTypes.CHARMING, 20)
            .build();


    public BaseRuneStaffItem(int runeType) {
        super(new Item.Properties()
                .rarity(Rarity.RARE)
                .stacksTo(1));
        this.runeType = runeType;
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (!(context.getPlayer() instanceof ServerPlayer player)) return InteractionResult.SUCCESS;
        if (player.getCooldowns().isOnCooldown(this)) return InteractionResult.FAIL;

        BlockPos centerRunePos = isValidRuneSpot(context.getLevel(), context.getClickedPos())
                               ? context.getClickedPos()
                               : context.getClickedPos().relative(context.getClickedFace());

        List<Supplier<BlockPos>> runePositions = switch (this.runeType) {
            case RuneTypes.OVERLOADING -> getOverloadPlacement(centerRunePos);
            case RuneTypes.ALCHEMIZING -> getAlchemizePlacement(centerRunePos, player.getDirection());
            case RuneTypes.CHARMING -> getCharmingPlacement(centerRunePos, player.getDirection());
            default -> throw new IllegalStateException("Unexpected value: " + this.runeType);
        };

        for (Supplier<BlockPos> runePos : runePositions) {
            if (isValidRuneSpot(context.getLevel(), runePos.get())) {
                context.getLevel().setBlock(runePos.get(), EliteBlocks.RUNE_BLOCK.get().defaultBlockState().setValue(RuneBlock.RUNE_TYPE, this.runeType), 3);
            }
        }

        player.getCooldowns().addCooldown(this, RUNE_TYPE_COOLDOWNS.getOrDefault(this.runeType, 20));
        EliteNetworking.sendToPlayer(new PlaySoundS2CPacket(SoundEventIndices.RUNE_STAFF_ACTIVATE), player);
        return InteractionResult.SUCCESS;
    }

    private boolean isValidRuneSpot(Level level, BlockPos pos) {
        BlockState at = level.getBlockState(pos);
        BlockState below = level.getBlockState(pos.below());
        // TODO: find better solution
        return (at.is(Blocks.AIR) || at.is(BlockTags.FIRE) || at.is(Blocks.GRASS) || at.is(BlockTags.SNOW, (state) -> state.getValue(SnowLayerBlock.LAYERS) < 5) || at.is(BlockTags.FLOWERS))
                && (!below.is(Blocks.AIR) && !below.is(Blocks.WATER) && !below.is(BlockTags.FIRE));
    }

    private List<Supplier<BlockPos>> getOverloadPlacement(BlockPos centerPos) {
        return List.of(
                () -> centerPos.relative(Direction.NORTH),
                () -> centerPos.relative(Direction.NORTH, 2).relative(Direction.EAST, 2),
                () -> centerPos.relative(Direction.NORTH, 3).relative(Direction.EAST, 3),

                () -> centerPos.relative(Direction.EAST),
                () -> centerPos.relative(Direction.EAST, 2).relative(Direction.SOUTH, 2),
                () -> centerPos.relative(Direction.EAST, 3).relative(Direction.SOUTH, 3),

                () -> centerPos.relative(Direction.SOUTH),
                () -> centerPos.relative(Direction.SOUTH, 2).relative(Direction.WEST, 2),
                () -> centerPos.relative(Direction.SOUTH, 3).relative(Direction.WEST, 3),

                () -> centerPos.relative(Direction.WEST),
                () -> centerPos.relative(Direction.WEST, 2).relative(Direction.NORTH, 2),
                () -> centerPos.relative(Direction.WEST, 3).relative(Direction.NORTH, 3)
        );
    }

    private List<Supplier<BlockPos>> getAlchemizePlacement(BlockPos centerPos, Direction facing) {
        return List.of(
                () -> centerPos,

                () -> centerPos.relative(facing.getCounterClockWise(), 3),
                () -> centerPos.relative(facing.getCounterClockWise(), 5),

                () -> centerPos.relative(facing.getOpposite()).relative(facing.getCounterClockWise()),
                () -> centerPos.relative(facing.getOpposite()).relative(facing.getCounterClockWise(), 4),
                () -> centerPos.relative(facing.getOpposite()).relative(facing.getCounterClockWise(), 6),

                () -> centerPos.relative(facing).relative(facing.getCounterClockWise()),
                () -> centerPos.relative(facing).relative(facing.getCounterClockWise(), 4),
                () -> centerPos.relative(facing).relative(facing.getCounterClockWise(), 6),

                () -> centerPos.relative(facing.getClockWise(), 3),
                () -> centerPos.relative(facing.getClockWise(), 5),

                () -> centerPos.relative(facing.getOpposite()).relative(facing.getClockWise()),
                () -> centerPos.relative(facing.getOpposite()).relative(facing.getClockWise(), 4),
                () -> centerPos.relative(facing.getOpposite()).relative(facing.getClockWise(), 6),

                () -> centerPos.relative(facing).relative(facing.getClockWise()),
                () -> centerPos.relative(facing).relative(facing.getClockWise(), 4),
                () -> centerPos.relative(facing).relative(facing.getClockWise(), 6)
        );
    }

    private List<Supplier<BlockPos>> getCharmingPlacement(BlockPos centerPos, Direction facing) {
        return List.of(
                () -> centerPos.relative(facing),
                () -> centerPos.relative(facing.getClockWise()),
                () -> centerPos.relative(facing.getOpposite()),
                () -> centerPos.relative(facing.getCounterClockWise()),

                () -> centerPos.relative(facing, 3),
                () -> centerPos.relative(facing.getClockWise(), 2).relative(facing),
                () -> centerPos.relative(facing.getClockWise(), 3),
                () -> centerPos.relative(facing.getClockWise(), 2).relative(facing.getOpposite()),
                () -> centerPos.relative(facing.getOpposite(), 3),
                () -> centerPos.relative(facing.getCounterClockWise(), 2).relative(facing),
                () -> centerPos.relative(facing.getCounterClockWise(), 3),
                () -> centerPos.relative(facing.getCounterClockWise(), 2).relative(facing.getOpposite())
        );
    }

}
