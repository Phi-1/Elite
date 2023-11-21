package dev.stormwatch.elite.items.armor;

import dev.stormwatch.elite.doc.SlotIndices;
import dev.stormwatch.elite.doc.SoundEventIndices;
import dev.stormwatch.elite.networking.EliteNetworking;
import dev.stormwatch.elite.networking.packets.PlaySoundS2CPacket;
import dev.stormwatch.elite.registry.EliteArmorMaterials;
import dev.stormwatch.elite.registry.EliteItems;
import dev.stormwatch.elite.util.AttributeUtil;
import dev.stormwatch.elite.util.InventoryUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class DarkIronArmorItem extends ArmorItem {

    private static final AttributeUtil.AttributeInfo LEGGINGS_SPEED_INFO = new AttributeUtil.AttributeInfo("dark_iron_leggings_speed", UUID.fromString("34a4aee7-a5da-47bf-9f96-bc6535b7d250"));
    private static final double LEGGINGS_SPEED_AMOUNT = 0.15;
    private static final float CHESTPLATE_DAMAGE_NEGATION_CHANCE = 0.3f;

    public DarkIronArmorItem(Type type) {
        super(EliteArmorMaterials.DARK_IRON, type, new Item.Properties()
                .rarity(Rarity.RARE)
                .stacksTo(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotIndex, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotIndex, isSelected);
        if (!(entity instanceof Player player)) return;

        if (slotIndex == SlotIndices.LEGGINGS) {
            processLeggingsAbility(player);
        } else if (slotIndex == SlotIndices.HELMET) {
            // TODO: chance to crit axe / arrow damage
        }

        // TODO: armor set abilty, bleed on axe damage, missing health damage on arrow damage
    }

    @SubscribeEvent
    public static void processBootsAbility(BlockEvent.BreakEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (!InventoryUtil.hasArmorEquipped(event.getPlayer(), EliteItems.DARK_IRON_BOOTS.get(), SlotIndices.BOOTS)) return;
        ItemStack axeItem = InventoryUtil.getHeldItemOfType(event.getPlayer(), AxeItem.class);
        if (axeItem == null) return;
        if (!event.getState().is(BlockTags.LOGS)) return;

        destroyLogsAround(event.getPos(), event.getLevel(), axeItem, event.getPlayer());
    }

    @SubscribeEvent
    public static void processChestplateAbility(LivingHurtEvent event) {
        // TODO: change this to death's dance kind of effect where projectile damage is taken over time instead, with 10-20% damage reduction
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!InventoryUtil.hasArmorEquipped(player, EliteItems.DARK_IRON_CHESTPLATE.get(), SlotIndices.CHESTPLATE)) return;
        if (!event.getSource().is(DamageTypeTags.IS_PROJECTILE)) return;
        if (ThreadLocalRandom.current().nextFloat() <= CHESTPLATE_DAMAGE_NEGATION_CHANCE) {
            event.setAmount(0);
            EliteNetworking.sendToPlayer(new PlaySoundS2CPacket(SoundEventIndices.NEGATE_DAMAGE), player);
        }
    }

    private static void destroyLogsAround(BlockPos pos, LevelAccessor level, ItemStack axeItem, Player player) {
        for (int i = -1; i < 2; i++) {
            for (BlockPos.MutableBlockPos spiralPos : BlockPos.spiralAround(new BlockPos(pos.getX(), pos.getY() + i, pos.getZ()), 1, Direction.EAST, Direction.SOUTH)) {
                if (level.getBlockState(spiralPos).is(BlockTags.LOGS)) {
                    level.destroyBlock(spiralPos, true);
                    axeItem.hurtAndBreak(1, player, (p) -> {});
                    // TODO: test that axe isn't broken, rest of tree should still be destroyed though because that makes sense
                    // TODO: though it doesn't seem to cause an issue if the axe breaks halfway through?
                    destroyLogsAround(spiralPos, level, axeItem, player);
                }
            }
        }
    }

    private void processLeggingsAbility(Player player) {
        if (player.level().isClientSide) return;

        if (InventoryUtil.hasArmorEquipped(player, EliteItems.DARK_IRON_LEGGINGS.get(), SlotIndices.LEGGINGS)
            && (InventoryUtil.getHeldItemOfType(player, AxeItem.class) != null
                || InventoryUtil.getHeldItemOfType(player, ProjectileWeaponItem.class) != null)) {
            AttributeUtil.setTransientAttribute(player, Attributes.MOVEMENT_SPEED, LEGGINGS_SPEED_INFO.name(), LEGGINGS_SPEED_INFO.uuid(), LEGGINGS_SPEED_AMOUNT, AttributeModifier.Operation.MULTIPLY_BASE);
        } else {
            AttributeUtil.removeAttributeModifier(player, Attributes.MOVEMENT_SPEED, LEGGINGS_SPEED_INFO.uuid());
        }
    }

}
