package dev.stormwatch.elite.blocks;

import dev.stormwatch.elite.blocks.entities.PearlescentCrystalBlockEntity;
import dev.stormwatch.elite.registry.EliteBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class PearlescentCrystalBlock extends BaseEntityBlock {

    public static final int DURATION_MILLIS = 30 * 1000;

    public PearlescentCrystalBlock() {
        super(BlockBehaviour.Properties.of()
                .lightLevel((state) -> 3)
                .sound(SoundType.GLASS)
                .mapColor(MapColor.COLOR_PINK)
                .instabreak()
                .explosionResistance(1200f)
                .friction(0.9f)
                .noLootTable()
                .speedFactor(1.1f));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return true;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return Shapes.empty();
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PearlescentCrystalBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, EliteBlockEntityTypes.PEARLESCENT_CRYSTAL.get(), PearlescentCrystalBlockEntity::tick);
    }
}
