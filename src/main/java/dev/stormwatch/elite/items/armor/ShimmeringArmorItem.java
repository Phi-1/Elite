package dev.stormwatch.elite.items.armor;

import dev.stormwatch.elite.doc.SlotIndices;
import dev.stormwatch.elite.doc.TickRates;
import dev.stormwatch.elite.registry.EliteArmorMaterials;
import dev.stormwatch.elite.registry.EliteItems;
import dev.stormwatch.elite.util.AttributeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class ShimmeringArmorItem extends ArmorItem {

    private static final double LEGGINGS_SWIM_SPEED_MODIFIER = 2;
    private static final AttributeUtil.AttributeInfo LEGGINGS_SWIM_SPEED_INFO = new AttributeUtil.AttributeInfo("shimmering_leggings_swim_speed", UUID.fromString("842e09e5-f07f-41ae-8a4c-130880192781"));

    private static final double WATER_WALKING_JUMP_STRENGTH = 0.7;
    private static final float WATER_WALKING_ACCELERATION = 0.05f;
    private static final double WATER_WALKING_MAX_SPEED = 0.6;

    public ShimmeringArmorItem(Type type) {
        super(EliteArmorMaterials.SHIMMERING, type,
                new Item.Properties()
                        .rarity(Rarity.EPIC)
                        .fireResistant()
                        .stacksTo(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotIndex, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotIndex, isSelected);
        if (!(entity instanceof Player player)) return;

        if (slotIndex == SlotIndices.BOOTS) {
            processBootsAbility(level);
        }
    }

    private void processBootsAbility(Level level) {
        if (!level.isClientSide()) return;

        LocalPlayer clientPlayer = Minecraft.getInstance().player;
        if (clientPlayer == null) return;

        if (shouldPlayerWaterWalk(clientPlayer, level)) {

            clientPlayer.setDeltaMovement(clientPlayer.getDeltaMovement().x, 0.0D, clientPlayer.getDeltaMovement().z);
            setPlayerWaterWalkingSpeed(clientPlayer);

            if (clientPlayer.input.jumping) {
                clientPlayer.setDeltaMovement(clientPlayer.getDeltaMovement().x, WATER_WALKING_JUMP_STRENGTH, clientPlayer.getDeltaMovement().z);
            } else {
                double distanceToSurface = clientPlayer.getY() - (clientPlayer.blockPosition().below().getY() + 1.1D);
                if (distanceToSurface > 0) {
                    clientPlayer.setDeltaMovement(clientPlayer.getDeltaMovement().x,-distanceToSurface / 2, clientPlayer.getDeltaMovement().z);
                }
            }
        }
    }

    private boolean shouldPlayerWaterWalk(LocalPlayer player, Level level) {
        return !player.isInFluidType()
                && countsAsWater(level.getBlockState(player.blockPosition().below()))
                && !player.input.shiftKeyDown;
    }

    private boolean countsAsWater(BlockState state) {
        return state.is(Blocks.WATER) || state.is(Blocks.KELP_PLANT) || state.is(Blocks.KELP);
    }

    private void setPlayerWaterWalkingSpeed(LocalPlayer player) {
        if (!player.input.up && !player.input.down && !player.input.left && !player.input.right) return;

        double xDelta = 0;
        double zDelta = 0;
        float rot = player.getYRot();

        double theta = rot / 180 * Math.PI;

        // NOTE: rotation is 0 towards positive Z, so world X coordinate corresponds to Y coordinate in the trigonometry world, and world Z to trig X
        // NOTE: also 90 degrees is negative X, so addition and subtraction are opposites for X and Z
        if (player.input.up) {
            xDelta -= Math.sin(theta);
            zDelta += Math.cos(theta);
        }
        if (player.input.down) {
            xDelta += Math.sin(theta);
            zDelta -= Math.cos(theta);
        }
        if (player.input.left) {
            xDelta += Math.sin(theta + Math.PI / 2);
            zDelta -= Math.cos(theta + Math.PI / 2);
        }
        if (player.input.right) {
            xDelta -= Math.sin(theta + Math.PI / 2);
            zDelta += Math.cos(theta + Math.PI / 2);
        }

        Vec2 waterWalkingSpeed = new Vec2((float) xDelta, (float) zDelta).normalized().scale(WATER_WALKING_ACCELERATION);

        double          xSpeed = player.getDeltaMovement().x + waterWalkingSpeed.x;
        // FIXME: i think these max speed limits nullify the normalization process, normalize max speed based on which directions are held?
        if (xSpeed > 0) xSpeed = Math.min(xSpeed, WATER_WALKING_MAX_SPEED);
        else            xSpeed = Math.max(xSpeed, -WATER_WALKING_MAX_SPEED);

        double          zSpeed = player.getDeltaMovement().z + waterWalkingSpeed.y;
        if (zSpeed > 0) zSpeed = Math.min(zSpeed, WATER_WALKING_MAX_SPEED);
        else            zSpeed = Math.max(zSpeed, -WATER_WALKING_MAX_SPEED);

        player.setDeltaMovement(xSpeed, player.getDeltaMovement().y, zSpeed);
    }

    @SubscribeEvent
    public static void processLeggingsAbility(TickEvent.PlayerTickEvent event) {
        if (event.player.level().isClientSide()) return;
        if (event.phase == TickEvent.Phase.END) return;
        if (!(event.player.level().getGameTime() % TickRates.LOW == 0)) return;

        if (event.player.getInventory().getArmor(SlotIndices.LEGGINGS).is(EliteItems.SHIMMERING_LEGGINGS.get())) {
            if (!AttributeUtil.hasAttributeModifier(event.player, ForgeMod.SWIM_SPEED.get(), LEGGINGS_SWIM_SPEED_INFO.uuid())) {
                AttributeUtil.setTransientAttribute(event.player, ForgeMod.SWIM_SPEED.get(), LEGGINGS_SWIM_SPEED_INFO.name(), LEGGINGS_SWIM_SPEED_INFO.uuid(), LEGGINGS_SWIM_SPEED_MODIFIER, AttributeModifier.Operation.MULTIPLY_BASE);
            }
        } else if (AttributeUtil.hasAttributeModifier(event.player, ForgeMod.SWIM_SPEED.get(), LEGGINGS_SWIM_SPEED_INFO.uuid())) {
            AttributeUtil.removeAttributeModifier(event.player, ForgeMod.SWIM_SPEED.get(), LEGGINGS_SWIM_SPEED_INFO.uuid());
        }
    }

}
