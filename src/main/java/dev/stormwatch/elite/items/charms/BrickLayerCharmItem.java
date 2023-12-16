package dev.stormwatch.elite.items.charms;

import com.google.common.collect.ImmutableMap;
import dev.stormwatch.elite.capabilities.ModeSelectorMarker;
import dev.stormwatch.elite.capabilities.ModeSelectorMarkerProvider;
import dev.stormwatch.elite.items.ModeAbilityItem;
import dev.stormwatch.elite.systems.BlockFaceClickListener;
import dev.stormwatch.elite.util.InventoryUtil;
import dev.stormwatch.elite.util.TickTimers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.function.Supplier;

public class BrickLayerCharmItem extends CharmItem implements ModeAbilityItem {

    public static final ImmutableMap<BrickLayerMode, MutableComponent> MODE_SELECTION_COMPONENTS = new ImmutableMap.Builder<BrickLayerMode, MutableComponent>()
            .put(BrickLayerMode.OFF, Component.translatable("message.elite.bricklayer_mode_off"))
            .put(BrickLayerMode.PARALLEL_COLUMN, Component.translatable("message.elite.bricklayer_mode_parallelcolumn"))
            .put(BrickLayerMode.PARALLEL_WALL, Component.translatable("message.elite.bricklayer_mode_parallelwall"))
            .put(BrickLayerMode.PERPENDICULAR_COLUMN, Component.translatable("message.elite.bricklayer_mode_perpendicularcolumn"))
            .put(BrickLayerMode.PERPENDICULAR_WALL, Component.translatable("message.elite.bricklayer_mode_perpendicularwall"))
            .build();

    public BrickLayerCharmItem() {
        super(new Item.Properties()
                .rarity(Rarity.UNCOMMON)
                .stacksTo(1));
    }

    @Override
    public void activateAbility(ItemStack stack, Player player) {
        ModeSelectorMarker modeSelector = stack.getCapability(ModeSelectorMarkerProvider.CAPABILITY_TYPE).orElseThrow(() -> new IllegalStateException("Brick Layer does not have a mode selector marker"));
        modeSelector.increaseIndex();
        BrickLayerMode mode = BrickLayerMode.values()[modeSelector.getMode()];
        player.sendSystemMessage(MODE_SELECTION_COMPONENTS.get(mode));
    }

    @SubscribeEvent
    public static void onPlayerPlaceBlock(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getEntity() instanceof Player player)
                || player.level().isClientSide()) return;
        ItemStack brickLayer = InventoryUtil.getEquippedCharmOfType(player, BrickLayerCharmItem.class);
        if (brickLayer == null) return;

        ModeSelectorMarker modeSelector = brickLayer.getCapability(ModeSelectorMarkerProvider.CAPABILITY_TYPE).orElseThrow(() -> new IllegalStateException("Brick layer does not have a mode selector marker"));
        BrickLayerMode mode = BrickLayerMode.values()[modeSelector.getMode()];

        int requiredBlocks = switch (mode) {
            case OFF -> 0;
            case PARALLEL_COLUMN, PERPENDICULAR_COLUMN -> 2;
            case PARALLEL_WALL, PERPENDICULAR_WALL -> 8;
        };

        if (!hasEnoughBlocksOfType(player, event.getPlacedBlock(), requiredBlocks)) return;

        Direction clickedFace = BlockFaceClickListener.getLastRightClickedFace(player);
        int placedBlocks = placeExtraBlocks(player.level(), event.getPlacedBlock(), event.getPos(), mode, clickedFace, player.getDirection());
        consumeBlocksFromInventory(player, event.getPlacedBlock(), placedBlocks);
    }

    private static boolean hasEnoughBlocksOfType(Player player, BlockState block, int requiredBlocks) {
        int foundItems = 0;
        for (ItemStack stack : InventoryUtil.getAllStacksOfItemInInventory(player, block.getBlock().asItem())) {
            foundItems += stack.getCount();
            if (foundItems >= requiredBlocks) {
                return true;
            }
        }
        return false;
    }

    private static void consumeBlocksFromInventory(Player player, BlockState block, int amount) {
        TickTimers.schedule(() -> {
            int consumed = 0;
            for (ItemStack stack : InventoryUtil.getAllStacksOfItemInInventory(player, block.getBlock().asItem())) {
                while (stack.getCount() > 0) {
                    stack.shrink(1);
                    consumed++;
                    if (consumed >= amount) return;
                }
            }
        }, 1);
    }

    private static int placeExtraBlocks(Level level, BlockState placedBlock, BlockPos clickedPos, BrickLayerMode mode, Direction clickedFace, Direction horizontalFacing) {
        List<Supplier<BlockPos>> blockPlacement = switch (mode) {
            case OFF -> null;
            case PARALLEL_COLUMN -> getParallelColumnPlacement(clickedPos, clickedFace, horizontalFacing);
            case PARALLEL_WALL -> getParallelWallPlacement(clickedPos, clickedFace, horizontalFacing);
            case PERPENDICULAR_COLUMN -> getPerpendicularColumnPlacement(clickedPos, clickedFace);
            case PERPENDICULAR_WALL -> getPerpendicularWallPlacement(clickedPos, clickedFace, horizontalFacing);
        };
        if (blockPlacement == null) return 0;

        int blocksPlaced = 0;
        for (Supplier<BlockPos> pos : blockPlacement) {
            if (level.getBlockState(pos.get()).isAir()) {
                level.setBlock(pos.get(), placedBlock, 3);
                blocksPlaced++;
            }
        }
        return blocksPlaced;
    }

    private static List<Supplier<BlockPos>> getParallelColumnPlacement(BlockPos clickedPos, Direction clickedFace, Direction horizontalFacing) {
        Direction outward = clickedFace.getAxis().isHorizontal() ? Direction.UP : horizontalFacing.getClockWise();
        return List.of(
                () -> clickedPos.relative(outward),
                () -> clickedPos.relative(outward.getOpposite())
        );
    }

    private static List<Supplier<BlockPos>> getParallelWallPlacement(BlockPos clickedPos, Direction clickedFace, Direction horizontalFacing) {
        boolean vertical = clickedFace.getAxis().isHorizontal();
        Direction outward = vertical ? Direction.UP : horizontalFacing;
        return List.of(
                () -> clickedPos.relative(outward),
                () -> clickedPos.relative(outward.getOpposite()),

                () -> clickedPos.relative(vertical ? horizontalFacing.getClockWise() : outward.getClockWise()),
                () -> clickedPos.relative(vertical ? horizontalFacing.getClockWise() : outward.getClockWise()).relative(outward),
                () -> clickedPos.relative(vertical ? horizontalFacing.getClockWise() : outward.getClockWise()).relative(outward.getOpposite()),

                () -> clickedPos.relative(vertical ? horizontalFacing.getCounterClockWise() : outward.getCounterClockWise()),
                () -> clickedPos.relative(vertical ? horizontalFacing.getCounterClockWise() : outward.getCounterClockWise()).relative(outward),
                () -> clickedPos.relative(vertical ? horizontalFacing.getCounterClockWise() : outward.getCounterClockWise()).relative(outward.getOpposite())
        );
    }

    private static List<Supplier<BlockPos>> getPerpendicularColumnPlacement(BlockPos clickedPos, Direction clickedFace) {
        return List.of(
                () -> clickedPos.relative(clickedFace),
                () -> clickedPos.relative(clickedFace, 2)
        );
    }

    private static List<Supplier<BlockPos>> getPerpendicularWallPlacement(BlockPos clickedPos, Direction clickedFace, Direction horizontalFacing) {
        boolean vertical = !clickedFace.getAxis().isHorizontal();
        return List.of(
                () -> clickedPos.relative(clickedFace),
                () -> clickedPos.relative(clickedFace, 2),

                () -> clickedPos.relative(vertical ? horizontalFacing.getClockWise() : clickedFace.getClockWise()),
                () -> clickedPos.relative(vertical ? horizontalFacing.getClockWise() : clickedFace.getClockWise()).relative(clickedFace),
                () -> clickedPos.relative(vertical ? horizontalFacing.getClockWise() : clickedFace.getClockWise()).relative(clickedFace, 2),

                () -> clickedPos.relative(vertical ? horizontalFacing.getCounterClockWise() : clickedFace.getCounterClockWise()),
                () -> clickedPos.relative(vertical ? horizontalFacing.getCounterClockWise() : clickedFace.getCounterClockWise()).relative(clickedFace),
                () -> clickedPos.relative(vertical ? horizontalFacing.getCounterClockWise() : clickedFace.getCounterClockWise()).relative(clickedFace, 2)
        );
    }

    @Override
    public int getModes() {
        return BrickLayerMode.values().length;
    }

    public enum BrickLayerMode {
        OFF,
        PARALLEL_COLUMN,
        PARALLEL_WALL,
        PERPENDICULAR_COLUMN,
        PERPENDICULAR_WALL
    }

}
