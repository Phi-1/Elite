package dev.stormwatch.elite.items.tools;

import dev.stormwatch.elite.registry.EliteTiers;
import dev.stormwatch.elite.systems.BlockFaceClickListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;

public class SledgeHammerPickaxeItem extends PickaxeItem {

    public SledgeHammerPickaxeItem() {
        super(EliteTiers.SLEDGEHAMMER, 5, -3.2f, new Item.Properties()
                .stacksTo(1)
                .fireResistant()
                .rarity(Rarity.RARE));
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity livingEntity) {
        if (!(livingEntity instanceof Player player)) return false;

        if (!level.isClientSide && state.getDestroySpeed(level, pos) != 0.0F) {
            int blocksBroken = 1;
            Direction blockFace = BlockFaceClickListener.getLastLeftClickedFace(player);
            if (blockFace == null) blockFace = player.getDirection();
            Tuple<Direction, Direction> directions = getRotationAndExpansionDirections(blockFace);
            for (BlockPos.MutableBlockPos spiralPos : BlockPos.spiralAround(pos, 1, directions.getA(), directions.getB())) {
                if (isMineable(level.getBlockState(spiralPos))) {
                    Block.dropResources(level.getBlockState(spiralPos), level, spiralPos, null, player, stack);
                    level.destroyBlock(spiralPos, false);
                    blocksBroken++;
                }
            }
            stack.hurtAndBreak(blocksBroken, livingEntity, (e) -> {
                e.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }

        return true;
    }

    private boolean isMineable(BlockState state) {
        return state.is(Tags.Blocks.STONE)
                || state.is(Tags.Blocks.ORES)
                || state.is(Tags.Blocks.COBBLESTONE)
                || state.is(Tags.Blocks.OBSIDIAN) // TODO: if it's diamond tier should mine obsidian
                || state.is(Tags.Blocks.END_STONES)
                || state.is(Tags.Blocks.STORAGE_BLOCKS);
    }

    private Tuple<Direction, Direction> getRotationAndExpansionDirections(Direction blockFace) {
        Direction rotation = switch (blockFace) {
            case UP, DOWN -> Direction.EAST;
            case NORTH, EAST, SOUTH, WEST -> blockFace.getClockWise();
        };
        Direction expansion = switch (blockFace) {
            case UP, DOWN -> Direction.NORTH;
            case NORTH, EAST, SOUTH, WEST -> Direction.UP;
        };
        return new Tuple<>(rotation, expansion);
    }

}
