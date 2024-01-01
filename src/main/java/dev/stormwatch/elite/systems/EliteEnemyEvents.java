package dev.stormwatch.elite.systems;

import dev.stormwatch.elite.Config;
import dev.stormwatch.elite.Elite;
import dev.stormwatch.elite.capabilities.EnemyEliteMarker;
import dev.stormwatch.elite.capabilities.EnemyEliteMarkerProvider;
import dev.stormwatch.elite.doc.EliteType;
import dev.stormwatch.elite.systems.elites.Monstrosity;
import dev.stormwatch.elite.systems.elites.Necromancer;
import dev.stormwatch.elite.systems.elites.Smiler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.concurrent.ThreadLocalRandom;

public class EliteEnemyEvents {

    // TODO: increase spawn rate

    @SubscribeEvent
    public static void attachEliteTypeCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Enemy) {
            if (!event.getObject().getCapability(EnemyEliteMarkerProvider.CAPABILITY_TYPE).isPresent()) {
                event.addCapability(new ResourceLocation(Elite.MOD_ID, "enemy_elite_marker"), new EnemyEliteMarkerProvider());
            }
        }
    }

    @SubscribeEvent
    public static void tickElites(LivingEvent.LivingTickEvent event) {
        LivingEntity living = event.getEntity();
        if (living.level().isClientSide()
                || !(living instanceof Enemy)) return;

        EnemyEliteMarker eliteMarker = living.getCapability(EnemyEliteMarkerProvider.CAPABILITY_TYPE).orElse(EnemyEliteMarker.EMPTY);
        if (eliteMarker == EnemyEliteMarker.EMPTY) return;
        EliteType eliteType = eliteMarker.getEliteType();
        if (eliteType == EliteType.NONE) return;
        runEliteTickerForType(eliteType, living);
        // TODO: spawn elite particles
    }

    @SubscribeEvent
    public static void onEnemySpawn(MobSpawnEvent.FinalizeSpawn event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof Enemy)) return;
        // TODO: use getMobSpawnType
        // TODO: collect monster base stat modifiers
        // TODO: always make bosses elite
        // TODO: separate elite chances for each mob?
        if (ThreadLocalRandom.current().nextFloat() <= Config.getEliteMonsterSpawnChance() || event.getSpawnType() == MobSpawnType.SPAWN_EGG) {
            EliteType eliteType = EliteType.getTypeForEntity(event.getEntity());
            // FIXME: println
            System.out.println(eliteType.getDisplayName(Integer.MAX_VALUE) + " spawned at " + event.getEntity().blockPosition());
            event.getEntity().setPersistenceRequired();
            makeElite(event.getEntity(), eliteType);
            // TODO: modify base stat modifiers
            // TODO: make sure necromancer summons cant be elite
            runEliteSpawnFunctionForType(eliteType, event.getEntity());
        }
        // TODO: apply stat modifiers
    }

    @SubscribeEvent
    public static void eliteDamageEvents(LivingHurtEvent event) {
        // TODO: count up damage tracker on elite for drops
        if (event.getEntity().level().isClientSide()) return;

        if (event.getEntity() instanceof Enemy && event.getSource().getEntity() instanceof Player player) {
            EnemyEliteMarker marker = event.getEntity().getCapability(EnemyEliteMarkerProvider.CAPABILITY_TYPE).orElse(EnemyEliteMarker.EMPTY);
            if (marker == EnemyEliteMarker.EMPTY) return;
            EliteType type = marker.getEliteType();
            if (type == EliteType.NONE) return;
            runOnPlayerHitEliteFunctionForType(type, event, player, event.getEntity());
        }
        else if (event.getEntity() instanceof Player player && event.getSource().getEntity() instanceof Enemy) {
            EnemyEliteMarker marker = event.getSource().getEntity().getCapability(EnemyEliteMarkerProvider.CAPABILITY_TYPE).orElse(EnemyEliteMarker.EMPTY);
            if (marker == EnemyEliteMarker.EMPTY) return;
            EliteType type = marker.getEliteType();
            if (type == EliteType.NONE) return;
            runOnEliteHitPlayerFunctionForType(type, event, player, (LivingEntity) event.getSource().getEntity());
        }
    }

    private static void runEliteSpawnFunctionForType(EliteType type, LivingEntity elite) {
        switch (type) {
            case NECROMANCER -> Necromancer.onSpawn((Skeleton) elite);
            case SMILER -> Smiler.onSpawn((Creeper) elite);
        }
    }

    private static void runEliteTickerForType(EliteType type, LivingEntity elite) {
        switch (type) {
            case MONSTROSITY -> Monstrosity.tick((Zombie) elite);
            case NECROMANCER -> Necromancer.tick((Skeleton) elite);
            case SMILER -> Smiler.tick((Creeper) elite);
        }
    }

    private static void runOnPlayerHitEliteFunctionForType(EliteType type, LivingHurtEvent event, Player player, LivingEntity elite) {
        switch (type) {
            case MONSTROSITY -> Monstrosity.onHitByPlayer(player);
            case NECROMANCER -> Necromancer.onHitByPlayer((Skeleton) elite, event);
        }
    }

    private static void runOnEliteHitPlayerFunctionForType(EliteType type, LivingHurtEvent event, Player player, LivingEntity elite) {
        switch (type) {
            case MONSTROSITY -> Monstrosity.onHitPlayer(player);
            case SMILER -> Smiler.onHitPlayer(player, event);
        }
    }

    private static void makeElite(LivingEntity enemy, EliteType type) {
        EnemyEliteMarker eliteMarker = enemy.getCapability(EnemyEliteMarkerProvider.CAPABILITY_TYPE).orElse(EnemyEliteMarker.EMPTY);
        if (eliteMarker == EnemyEliteMarker.EMPTY) return;
        eliteMarker.setEliteType(type);
    }

}
