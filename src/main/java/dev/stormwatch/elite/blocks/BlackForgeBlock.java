package dev.stormwatch.elite.blocks;

import dev.stormwatch.elite.blocks.entities.BlackForgeBlockEntity;
import dev.stormwatch.elite.registry.EliteBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlackForgeBlock extends BaseEntityBlock {

    public BlackForgeBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                .lightLevel((state) -> 3));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlackForgeBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, EliteBlockEntityTypes.BLACK_FORGE.get(), BlackForgeBlockEntity::tick);
    }
}
