package dev.stormwatch.elite.systems.elites;

import dev.stormwatch.elite.capabilities.EnemyEliteMarker;
import dev.stormwatch.elite.capabilities.EnemyEliteMarkerProvider;
import dev.stormwatch.elite.doc.EliteType;
import dev.stormwatch.elite.registry.EliteItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Necromancer {

    private static final String SUMMON_COOLDOWN_KEY = "summon_cooldown";
    private static final int SUMMON_COOLDOWN = 18 * 20;
    private static final int MAX_SUMMONS = 7;
    private static final int SUMMON_SPAWN_RANGE = 4;
    private static final float SUMMON_FAIL_HEAL_AMOUNT = 16;
    private static final float PROJECTILE_DAMAGE_TAKEN_MODIFIER = 0.2f;

    public static void onSpawn(Skeleton necromancer) {
        setValues(necromancer);
        giveEnchantedEquipment(necromancer);
    }

    public static void tick(Skeleton necromancer) {
        tickSummonAbility(necromancer);
    }

    public static void onHitByPlayer(Skeleton necromancer, LivingHurtEvent event) {
        reduceProjectileDamage(necromancer, event);
    }

    private static EnemyEliteMarker getEliteMarker(Skeleton necromancer) {
        return necromancer.getCapability(EnemyEliteMarkerProvider.CAPABILITY_TYPE).orElse(EnemyEliteMarker.EMPTY);
    }

    private static void setValues(Skeleton necromancer) {
        EnemyEliteMarker eliteMarker = getEliteMarker(necromancer);
        if (!eliteMarker.hasIntValue(SUMMON_COOLDOWN_KEY)) {
            eliteMarker.setIntValue(SUMMON_COOLDOWN_KEY, SUMMON_COOLDOWN);
        }
    }

    private static void giveEnchantedEquipment(Skeleton necromancer) {
        ItemStack plate = new ItemStack(Items.GOLDEN_CHESTPLATE);
        plate.enchant(Enchantments.THORNS, 1);
        necromancer.setItemSlot(EquipmentSlot.CHEST, plate);

        necromancer.getMainHandItem().enchant(Enchantments.FLAMING_ARROWS, 1);
    }

    private static void reduceProjectileDamage(Skeleton necromancer, LivingHurtEvent event) {
        if (event.getSource().is(DamageTypeTags.IS_PROJECTILE)) {
            event.setAmount(event.getAmount() * PROJECTILE_DAMAGE_TAKEN_MODIFIER);
            necromancer.level().playSound(null, necromancer.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.HOSTILE, 1.2f, 1.0f);
        }
    }

    private static void tickSummonAbility(Skeleton necromancer) {
        EnemyEliteMarker eliteMarker = getEliteMarker(necromancer);
        int cooldown = eliteMarker.getIntValue(SUMMON_COOLDOWN_KEY).orElse(SUMMON_COOLDOWN);
        if (cooldown > 0) {
            eliteMarker.setIntValue(SUMMON_COOLDOWN_KEY, cooldown - 1);
        } else if (necromancer.getTarget() instanceof Player) {
            summonSkeletons(necromancer);
            eliteMarker.setIntValue(SUMMON_COOLDOWN_KEY, SUMMON_COOLDOWN);
        }
    }

    private static void summonSkeletons(Skeleton necromancer) {
        int nSummons = ThreadLocalRandom.current().nextInt(1, MAX_SUMMONS + 1);
        List<BlockPos> spawnSpots = findValidSpawnSpots(necromancer.level(), necromancer.blockPosition(), SUMMON_SPAWN_RANGE, 3, nSummons);
        if (spawnSpots.size() == 0) {
            necromancer.heal(SUMMON_FAIL_HEAL_AMOUNT);
            // TODO particles
            return;
        }
        for (int i = 0; i < nSummons; i++) {
            Skeleton summon = EntityType.SKELETON.create(necromancer.level());
            if (summon == null) continue;
            giveSummonEquipment(summon);
            summon.moveTo(spawnSpots.get(Math.min(i, spawnSpots.size() - 1)).getCenter());
            necromancer.level().addFreshEntity(summon);
            // TODO: add particles (villager refresh)
        }
    }

    private static void giveSummonEquipment(Skeleton summon) {
        // Weapon rolls are exclusive, rolled from top to bottom
        final float woodenSwordChance = 0.2f;
        final float stickChance = 0.6f;
        final float bowlChance = 0.8f;
        final float nuggetChance = 0.9f;
        final float goldToothChance = 0.95f;

        final float leatherArmorChance = 0.1f;

        float weaponRoll = ThreadLocalRandom.current().nextFloat();
        if (weaponRoll <= woodenSwordChance) {
            summon.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.WOODEN_SWORD));
        } else if (weaponRoll <= stickChance) {
            summon.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STICK));
        } else if (weaponRoll <= bowlChance) {
            summon.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOWL));
        } else if (weaponRoll <= nuggetChance) {
            summon.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLD_NUGGET));
        } else if (weaponRoll <= goldToothChance) {
            summon.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(EliteItems.GOLD_TOOTH.get()));
        }

        float helmetRoll = ThreadLocalRandom.current().nextFloat();
        float chestplateRoll = ThreadLocalRandom.current().nextFloat();
        float leggingsRoll = ThreadLocalRandom.current().nextFloat();
        float bootsRoll = ThreadLocalRandom.current().nextFloat();
        if (helmetRoll <= leatherArmorChance) summon.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
        if (chestplateRoll <= leatherArmorChance) summon.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
        if (leggingsRoll <= leatherArmorChance) summon.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
        if (bootsRoll <= leatherArmorChance) summon.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
    }

    private static List<BlockPos> findValidSpawnSpots(Level level, BlockPos center, int hRange, int vRange, int nSpots) {
        List<BlockPos> spots = new ArrayList<>();
        for (BlockPos.MutableBlockPos spiralPos : BlockPos.spiralAround(center, hRange, Direction.NORTH, Direction.EAST)) {
            BlockPos.MutableBlockPos vPos = spiralPos.immutable().relative(Direction.DOWN, vRange).mutable();
            for (int i = 0; i < vRange * 2; i++) {
                if (isValidSpawnSpot(level, vPos)) {
                    spots.add(vPos.immutable());
                    if (spots.size() >= nSpots) return spots;
                    break;
                }
                vPos.move(Direction.UP);
            }
        }
        return spots;
    }

    private static boolean isValidSpawnSpot(Level level, BlockPos pos) {
        return !level.getBlockState(pos).isSuffocating(level, pos)
                && !level.getBlockState(pos.above()).isSuffocating(level, pos.above())
                && !level.isEmptyBlock(pos.below());
    }

    @SubscribeEvent
    public static void preventSkeletonsTargetingNecromancer(LivingEvent.LivingTickEvent event) {
        if (event.getEntity().level().isClientSide()
                || !(event.getEntity() instanceof Skeleton skeleton)) return;
        LivingEntity target = skeleton.getTarget();
        if (target == null) return;
        EnemyEliteMarker eliteMarker = target.getCapability(EnemyEliteMarkerProvider.CAPABILITY_TYPE).orElse(EnemyEliteMarker.EMPTY);
        if (eliteMarker == EnemyEliteMarker.EMPTY) return;
        if (eliteMarker.getEliteType() == EliteType.NECROMANCER) {
            Player nearestPlayer = skeleton.level().getNearestPlayer(skeleton, 32);
            skeleton.setTarget(nearestPlayer);
        }
    }

}
