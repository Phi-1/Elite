package dev.stormwatch.elite.systems.elites;

import dev.stormwatch.elite.capabilities.EnemyEliteMarker;
import dev.stormwatch.elite.capabilities.EnemyEliteMarkerProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class Necromancer {

    private static final String SUMMON_COOLDOWN_KEY = "summon_cooldown";
    private static final int SUMMON_COOLDOWN = 18 * 20;
    private static final int MAX_SUMMONS = 3;
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
        return necromancer.getCapability(EnemyEliteMarkerProvider.CAPABILITY_TYPE).orElseThrow(() -> new IllegalStateException("Necromancer does not have an elite marker"));
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
        int cooldown = eliteMarker.getIntValue(SUMMON_COOLDOWN_KEY).orElseThrow(() -> new IllegalStateException("Necromancer cooldown value was not initialized properly"));
        if (cooldown > 0) {
            eliteMarker.setIntValue(SUMMON_COOLDOWN_KEY, cooldown - 1);
        } else if (necromancer.getTarget() instanceof Player) {
            summonSkeletons(necromancer);
            eliteMarker.setIntValue(SUMMON_COOLDOWN_KEY, SUMMON_COOLDOWN);
        }
    }

    private static void summonSkeletons(Skeleton necromancer) {
        int nSummons = ThreadLocalRandom.current().nextInt(1, MAX_SUMMONS + 1);
        for (int i = 0; i < nSummons; i++) {
            Skeleton summon = EntityType.SKELETON.create(necromancer.level());
            if (summon == null) continue;
            // TODO: find multiple different spawn spots
            Optional<BlockPos> spawnSpot = findValidSpawnSpot(necromancer.level(), necromancer.blockPosition(), SUMMON_SPAWN_RANGE, SUMMON_SPAWN_RANGE);
            if (spawnSpot.isEmpty()) {
                necromancer.heal(SUMMON_FAIL_HEAL_AMOUNT);
                break;
            }
            summon.moveTo(spawnSpot.get().getCenter());
            necromancer.level().addFreshEntity(summon);
            // TODO: add particles (villager refresh)
        }
    }

    private static Optional<BlockPos> findValidSpawnSpot(Level level, BlockPos center, int hRange, int vRange) {
        for (BlockPos.MutableBlockPos spiralPos : BlockPos.spiralAround(center, hRange, Direction.NORTH, Direction.EAST)) {
            if (isValidSpawnSpot(level, spiralPos)) {
                return Optional.of(spiralPos.immutable());
            } else {
                BlockPos.MutableBlockPos vPos = spiralPos.immutable().relative(Direction.DOWN, vRange).mutable();
                for (int i = 0; i < vRange * 2; i++) {
                    if (isValidSpawnSpot(level, vPos)) {
                        return Optional.of(vPos.immutable());
                    }
                    vPos.move(Direction.UP);
                }
            }
        }
        return Optional.empty();
    }

    private static boolean isValidSpawnSpot(Level level, BlockPos pos) {
        return !level.getBlockState(pos).isSuffocating(level, pos)
                && !level.getBlockState(pos.above()).isSuffocating(level, pos.above())
                && !level.isEmptyBlock(pos.below());
    }

}
