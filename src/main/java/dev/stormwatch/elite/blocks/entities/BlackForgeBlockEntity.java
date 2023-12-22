package dev.stormwatch.elite.blocks.entities;

import dev.stormwatch.elite.registry.EliteBlockEntityTypes;
import dev.stormwatch.elite.registry.EliteItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlackForgeBlockEntity extends BlockEntity {

    private static final int METAL_CREATION_INTERVAL = 30 * 20;
    public int timeToNextShard = METAL_CREATION_INTERVAL;

    public BlackForgeBlockEntity(BlockPos pos, BlockState state) {
        super(EliteBlockEntityTypes.BLACK_FORGE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BlackForgeBlockEntity entity) {
        if (!level.isClientSide()) {
            if (level.getBlockState(pos.below()).is(Blocks.LAVA_CAULDRON)) entity.timeToNextShard--;
            if (entity.timeToNextShard <= 0) {
                ItemEntity metalEntity = new ItemEntity(level, pos.above().getX(), pos.above().getY(), pos.above().getZ(), new ItemStack(EliteItems.UNKNOWN_METAL_SHARD.get(), 1));
                metalEntity.setDefaultPickUpDelay();
                level.addFreshEntity(metalEntity);
                entity.timeToNextShard = METAL_CREATION_INTERVAL;
            }
        }
    }

}
