package dev.stormwatch.elite.items.armor;

import dev.stormwatch.elite.doc.SlotIndices;
import dev.stormwatch.elite.registry.EliteArmorMaterials;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import org.joml.Vector2d;

public class ShimmeringArmorItem extends ArmorItem {

    private final double WATER_WALKING_JUMP_STRENGTH = 0.7;
    private final float WATER_WALKING_SPEED_MODIFIER = 0.05f;
    private final double WATER_WALKING_MAX_SPEED = 0.6;

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

        Vec2 waterWalkingSpeed = new Vec2((float) xDelta, (float) zDelta).normalized().scale(WATER_WALKING_SPEED_MODIFIER);

        double          xSpeed = player.getDeltaMovement().x + waterWalkingSpeed.x;
        // FIXME: i think these max speed limits nullify the normalization process, normalize max speed based on which directions are held?
        if (xSpeed > 0) xSpeed = Math.min(xSpeed, WATER_WALKING_MAX_SPEED);
        else            xSpeed = Math.max(xSpeed, -WATER_WALKING_MAX_SPEED);

        double          zSpeed = player.getDeltaMovement().z + waterWalkingSpeed.y;
        if (zSpeed > 0) zSpeed = Math.min(zSpeed, WATER_WALKING_MAX_SPEED);
        else            zSpeed = Math.max(zSpeed, -WATER_WALKING_MAX_SPEED);

        player.setDeltaMovement(xSpeed, player.getDeltaMovement().y, zSpeed);
    }

}
