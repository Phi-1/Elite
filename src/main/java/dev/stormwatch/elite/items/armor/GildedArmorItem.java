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
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.ToIntFunction;

public class GildedArmorItem extends ArmorItem {

    private static final int NUGGET_VALUE = 1;
    private static final int INGOT_VALUE = 9;
    private static final int BLOCK_VALUE = 81;

    public static final AttributeUtil.AttributeInfo BOOTS_MOVEMENTSPEED = new AttributeUtil.AttributeInfo("gilded_boots_movementspeed", UUID.fromString("1f4b2f64-a4a4-4b1b-adcd-799554267013"));
    public static final AttributeUtil.AttributeInfo BOOTS_STEPHEIGHT = new AttributeUtil.AttributeInfo("gilded_boots_stepheight", UUID.fromString("f17f7606-ae9c-4d28-b11b-5710b24b1cfa"));
    private static final double BOOTS_MOVEMENTSPEED_AMOUNT = 0.2;
    private static final double BOOTS_STEPHEIGHT_AMOUNT = 0.5;
    private static final int BOOTS_HEAL_INTERVAL = 5 * 20;

    public GildedArmorItem(Type type) {
        super(EliteArmorMaterials.GILDED, type, new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.EPIC)
                .fireResistant());
    }
    // TODO: gold costs should have high maximums so its a strategic choice what to use gold on

    // TODO: chestplate reduces damage taken by consuming gold

    // TODO: leggings increases melee damage dealt by consuming gold

    // TODO: absurd movement speed, jump height, step height, and fall damage immunity on gold blocks


    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotIndex, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotIndex, isSelected);
        if (!(entity instanceof Player player)) return;

        if (slotIndex == SlotIndices.BOOTS) {
            processBootsSpeedAndStepHeightAbility(player, level);
        }
    }

    @SubscribeEvent
    public static void processHelmetAbility(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Enemy)
                || event.getEntity().level().isClientSide()
                || !(event.getSource().getEntity() instanceof Player player)
                || !InventoryUtil.hasArmorEquipped(player, EliteItems.GILDED_HELMET.get(), SlotIndices.HELMET)) return;

        final float goldDropChance = 0.3f;
        final float extraGoldDropChance = 0.3f;

        if (ThreadLocalRandom.current().nextFloat() <= goldDropChance) {
            int nGold = 1;
            while (ThreadLocalRandom.current().nextFloat() <= extraGoldDropChance) {
                nGold++;
            }
            ItemStack goldStack = new ItemStack(Items.GOLD_INGOT, nGold);
            ItemEntity goldEntity = new ItemEntity(event.getEntity().level(), event.getEntity().blockPosition().getX(), event.getEntity().blockPosition().getY(), event.getEntity().blockPosition().getZ(), goldStack);
            goldEntity.setDefaultPickUpDelay();
            event.getEntity().level().addFreshEntity(goldEntity);
        }
    }

    @SubscribeEvent
    public static void processChestplateAbility(LivingHurtEvent event) {
        // TODO: test. it seems to consume a weird amount of gold
        if (!(event.getEntity() instanceof Player player)
                || player.level().isClientSide()
                || !InventoryUtil.hasArmorEquipped(player, EliteItems.GILDED_CHESTPLATE.get(), SlotIndices.CHESTPLATE)) return;

        final float maxPercentageDamageReduction = 0.8f;
        final float goldConsumptionPercent = 0.3f;
        final int goldValuePerPointOfDamageReduction = 4 * INGOT_VALUE;

        float damageTaken = event.getAmount();
        float maxReducibleDamage = damageTaken * maxPercentageDamageReduction;

        int goldConsumed = consumeGold(player, (value) -> {
            int toConsume = (int) (value * goldConsumptionPercent);
            if (toConsume > maxReducibleDamage * goldValuePerPointOfDamageReduction) {
                return (int) (maxReducibleDamage * goldValuePerPointOfDamageReduction);
            }
            return toConsume;
        });

        if (goldConsumed == 0) return;

        float damageReduction = (float) goldConsumed / goldValuePerPointOfDamageReduction;
        event.setAmount(damageTaken - damageReduction);
        EliteNetworking.sendToPlayer(new PlaySoundS2CPacket(SoundEventIndices.GILDED_ARMOR_CHESTPLATE_REDUCE_DAMAGE), (ServerPlayer) player);
    }

    @SubscribeEvent
    public static void processLeggingsAbility(LivingHurtEvent event) {
        // TODO: test. With 3 gold blocks in inventory it doesn't consume anything
        if (!(event.getSource().getEntity() instanceof Player player)
                || event.getSource().is(DamageTypeTags.IS_PROJECTILE)
                || player.level().isClientSide()
                || !InventoryUtil.hasArmorEquipped(player, EliteItems.GILDED_LEGGINGS.get(), SlotIndices.LEGGINGS)) return;

        final float maxPercentageDamageReduction = 0.8f;
        final float goldConsumptionPercent = 0.3f;
        final float goldValuePerPercentOfBonusDamage = (float) (64 * INGOT_VALUE) / 100;

        int goldConsumed = consumeGold(player, (value) -> {
            return (int) (value * goldConsumptionPercent);
        });

        if (goldConsumed == 0) return;

        float damageModifier = 1 + goldConsumed * goldValuePerPercentOfBonusDamage / 100;
        event.setAmount(event.getAmount() * damageModifier);
        EliteNetworking.sendToPlayer(new PlaySoundS2CPacket(SoundEventIndices.GILDED_ARMOR_LEGGINGS_INCREASE_DAMAGE), (ServerPlayer) player);
    }

    private void processBootsSpeedAndStepHeightAbility(Player player, Level level) {
        if (isNearGold(player)) {
            if (level.getGameTime() % BOOTS_HEAL_INTERVAL == 0) {
                player.heal(1);
            }
            AttributeUtil.setTransientAttribute(player, Attributes.MOVEMENT_SPEED, BOOTS_MOVEMENTSPEED.name(), BOOTS_MOVEMENTSPEED.uuid(), BOOTS_MOVEMENTSPEED_AMOUNT, AttributeModifier.Operation.MULTIPLY_BASE);
            AttributeUtil.setTransientAttribute(player, ForgeMod.STEP_HEIGHT_ADDITION.get(), BOOTS_STEPHEIGHT.name(), BOOTS_STEPHEIGHT.uuid(), BOOTS_STEPHEIGHT_AMOUNT, AttributeModifier.Operation.ADDITION);
        } else {
            AttributeUtil.removeAttributeModifier(player, Attributes.MOVEMENT_SPEED, BOOTS_MOVEMENTSPEED.uuid());
            AttributeUtil.removeAttributeModifier(player, ForgeMod.STEP_HEIGHT_ADDITION.get(), BOOTS_STEPHEIGHT.uuid());
        }
    }

    @SubscribeEvent
    public static void bootsNegateFallDamageOnGold(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player player)
                || player.level().isClientSide()
                || !InventoryUtil.hasArmorEquipped(player, EliteItems.GILDED_BOOTS.get(), SlotIndices.BOOTS)
                || !event.getSource().is(DamageTypeTags.IS_FALL)
                || !countsAsGoldBlock(player.level().getBlockState(player.blockPosition().below()))) return;

        event.setAmount(0);
    }

    private static boolean countsAsGoldBlock(BlockState state) {
        return state.is(Blocks.GOLD_BLOCK) || state.is(Blocks.GOLD_ORE) || state.is(Blocks.RAW_GOLD_BLOCK) || state.is(Blocks.NETHER_GOLD_ORE)
                || state.is(Blocks.DEEPSLATE_GOLD_ORE) || state.is(Blocks.GILDED_BLACKSTONE);
    }

    private static boolean isNearGold(Player player) {
        final int radius = 5;
        BlockPos.MutableBlockPos center = player.blockPosition().relative(Direction.UP, radius).mutable();
        for (int i = 0; i < radius * 2 + 1; i++) {
            for (BlockPos spiralPos : BlockPos.spiralAround(center, radius, Direction.EAST, Direction.NORTH)) {
                if (countsAsGoldBlock(player.level().getBlockState(spiralPos))) {
                    return true;
                }
            }
            center.move(Direction.DOWN);
        }
        return false;
    }

    @SubscribeEvent
    public static void processSetAbility(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Player player)
                || player.level().isClientSide()
                ||   !(InventoryUtil.hasArmorEquipped(player, EliteItems.GILDED_BOOTS.get(), SlotIndices.BOOTS)
                    && InventoryUtil.hasArmorEquipped(player, EliteItems.GILDED_LEGGINGS.get(), SlotIndices.LEGGINGS)
                    && InventoryUtil.hasArmorEquipped(player, EliteItems.GILDED_CHESTPLATE.get(), SlotIndices.CHESTPLATE)
                    && InventoryUtil.hasArmorEquipped(player, EliteItems.GILDED_HELMET.get(), SlotIndices.HELMET))) return;

        final int minGoldValueTrigger = 64 * INGOT_VALUE;

        int goldValue = consumeGold(player, (value) -> value >= minGoldValueTrigger ? value : 0);
        if (goldValue < minGoldValueTrigger) return;

        final int goldPerAmpLevel = 3 * INGOT_VALUE;
        final float durationTicksPerGold = 0.3f;

        int ampLevel = goldValue / goldPerAmpLevel;
        int duration = Math.round(goldValue * durationTicksPerGold);

        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, ampLevel));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, duration, ampLevel));
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, duration, ampLevel));
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, duration, ampLevel));
        player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, duration, ampLevel));
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, duration, ampLevel));

        player.setHealth(1);

        EliteNetworking.sendToPlayer(new PlaySoundS2CPacket(SoundEventIndices.GILDED_ARMOR_SET_BONUS_ACTIVATE), (ServerPlayer) player);

        event.setCanceled(true);
    }

    private static int consumeGold(Player player, ToIntFunction<Integer> toConsume) {
        int goldValue = 0;

        List<ItemStack> nuggets = InventoryUtil.getAllStacksOfItemInInventory(player, Items.GOLD_NUGGET);
        List<ItemStack> ingots = InventoryUtil.getAllStacksOfItemInInventory(player, Items.GOLD_INGOT);
        List<ItemStack> blocks = InventoryUtil.getAllStacksOfItemInInventory(player, Items.GOLD_BLOCK);

        for (ItemStack stack : nuggets) goldValue += stack.getCount() * NUGGET_VALUE;
        for (ItemStack stack : ingots)  goldValue += stack.getCount() * INGOT_VALUE;
        for (ItemStack stack : blocks)  goldValue += stack.getCount() * BLOCK_VALUE;

        int goldToConsume = Math.min(toConsume.applyAsInt(goldValue), goldValue);
        if (goldToConsume == goldValue) {
            for (ItemStack stack : nuggets) stack.setCount(0);
            for (ItemStack stack : ingots)  stack.setCount(0);
            for (ItemStack stack : blocks)  stack.setCount(0);
            return goldValue;
        }

        int goldConsumed = 0;
        goldConsumed = tickDownGoldStacks(blocks, BLOCK_VALUE, goldToConsume, goldConsumed);
        goldConsumed = tickDownGoldStacks(ingots, INGOT_VALUE, goldToConsume, goldConsumed);
        goldConsumed = tickDownGoldStacks(nuggets, NUGGET_VALUE, goldToConsume, goldConsumed);

        return goldConsumed;
    }

    private static int tickDownGoldStacks(List<ItemStack> stacks, int valuePerItem, int maxValue, int currentValue) {
        for (ItemStack stack : stacks) {
            while (stack.getCount() > 0 && maxValue - currentValue >= valuePerItem) {
                stack.shrink(1);
                currentValue += valuePerItem;
            }
        }
        return currentValue;
    }

    @Override
    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return true;
    }
}
