package dev.stormwatch.elite.items.armor;

import dev.stormwatch.elite.doc.SlotIndices;
import dev.stormwatch.elite.doc.SoundEventIndices;
import dev.stormwatch.elite.networking.EliteNetworking;
import dev.stormwatch.elite.networking.packets.PlaySoundS2CPacket;
import dev.stormwatch.elite.registry.EliteArmorMaterials;
import dev.stormwatch.elite.registry.EliteItems;
import dev.stormwatch.elite.util.InventoryUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.ToIntFunction;

public class GildedArmorItem extends ArmorItem {

    private static final int NUGGET_VALUE = 1;
    private static final int INGOT_VALUE = 9;
    private static final int BLOCK_VALUE = 81;

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

        float damageReduction = (float) goldConsumed / goldValuePerPointOfDamageReduction;
        event.setAmount(damageTaken - damageReduction);
        EliteNetworking.sendToPlayer(new PlaySoundS2CPacket(SoundEventIndices.GILDED_ARMOR_CHESTPLATE_REDUCE_DAMAGE), (ServerPlayer) player);
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
