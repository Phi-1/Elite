package dev.stormwatch.elite.items.armor;

import dev.stormwatch.elite.doc.SlotIndices;
import dev.stormwatch.elite.registry.EliteArmorMaterials;
import dev.stormwatch.elite.registry.EliteItems;
import dev.stormwatch.elite.util.InventoryUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DarkIronArmorItem extends ArmorItem {

    public DarkIronArmorItem(Type type) {
        super(EliteArmorMaterials.DARK_IRON, type, new Item.Properties()
                .rarity(Rarity.RARE)
                .stacksTo(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotIndex, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotIndex, isSelected);
        if (!(entity instanceof Player player)) return;

        if (slotIndex == SlotIndices.BOOTS) {
            // TODO: cut down whole trees
        } else if (slotIndex == SlotIndices.LEGGINGS) {
            // TODO: movement speed when holding an axe or (cross)bow
        } else if (slotIndex == SlotIndices.CHESTPLATE) {
            // TODO: chance to negate part of projectile damage
        } else if (slotIndex == SlotIndices.HELMET) {
            // TODO: chance to crit axe / arrow damage
        }

        // TODO: armor set abilty, bleed on axe damage, missing health damage on arrow damage
    }

    @SubscribeEvent
    public static void bootsAbilityChopDownWholeTrees(BlockEvent.BreakEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (!InventoryUtil.hasArmorEquipped(event.getPlayer(), EliteItems.DARK_IRON_BOOTS.get(), SlotIndices.BOOTS)) return;
        ItemStack axeItem = InventoryUtil.isHoldingItemOfType(event.getPlayer(), AxeItem.class);
        if (axeItem == null) return;
        if (!event.getState().is(BlockTags.LOGS)) return;

        destroyLogsAround(event.getPos(), event.getLevel(), axeItem, event.getPlayer());
    }

    private static void destroyLogsAround(BlockPos pos, LevelAccessor level, ItemStack axeItem, Player player) {
        for (int i = -1; i < 2; i++) {
            for (BlockPos.MutableBlockPos spiralPos : BlockPos.spiralAround(new BlockPos(pos.getX(), pos.getY() + i, pos.getZ()), 1, Direction.EAST, Direction.SOUTH)) {
                if (level.getBlockState(spiralPos).is(BlockTags.LOGS)) {
                    level.destroyBlock(spiralPos, true);
                    axeItem.hurtAndBreak(1, player, (p) -> {});
                    destroyLogsAround(spiralPos, level, axeItem, player);
                }
            }
        }
    }

}
