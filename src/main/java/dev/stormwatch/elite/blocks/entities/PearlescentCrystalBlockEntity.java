package dev.stormwatch.elite.blocks.entities;

import dev.stormwatch.elite.blocks.PearlescentCrystalBlock;
import dev.stormwatch.elite.registry.EliteBlockEntityTypes;
import dev.stormwatch.elite.registry.EliteBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PearlescentCrystalBlockEntity extends BlockEntity {

    private long timePlaced;

    public PearlescentCrystalBlockEntity(BlockPos pos, BlockState state) {
        super(EliteBlockEntityTypes.PEARLESCENT_CRYSTAL.get(), pos, state);
        this.timePlaced = System.currentTimeMillis();
    }

    public long getTimePlaced() {
        return timePlaced;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PearlescentCrystalBlockEntity entity) {
        if (System.currentTimeMillis() - entity.getTimePlaced() >= PearlescentCrystalBlock.DURATION_MILLIS
                && level.getBlockState(pos).is(EliteBlocks.PEARLESCENT_CRYSTAL.get())) {
            level.destroyBlock(pos, false);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putLong("timePlaced", this.timePlaced);
    }

    @Override
    public void load(CompoundTag nbt) {
        this.timePlaced = nbt.getLong("timePlaced");
    }
}
