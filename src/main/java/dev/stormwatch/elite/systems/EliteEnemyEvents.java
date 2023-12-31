package dev.stormwatch.elite.systems;

import dev.stormwatch.elite.Config;
import dev.stormwatch.elite.Elite;
import dev.stormwatch.elite.capabilities.EnemyEliteMarker;
import dev.stormwatch.elite.capabilities.EnemyEliteMarkerProvider;
import dev.stormwatch.elite.doc.EliteType;
import dev.stormwatch.elite.systems.elites.Monstrosity;
import dev.stormwatch.elite.systems.elites.Necromancer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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

    // TODO: hook up elite on spawn functions (necro armor)
    // TODO: hook up elite on damage and deal damage events

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

        EnemyEliteMarker eliteMarker = living.getCapability(EnemyEliteMarkerProvider.CAPABILITY_TYPE).orElseThrow(() -> new IllegalStateException("Enemy does not have an elite marker"));
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
        if (ThreadLocalRandom.current().nextFloat() <= Config.getEliteMonsterSpawnChance()) {
            EliteType eliteType = EliteType.getTypeForEntity(event.getEntity());
            event.getEntity().setPersistenceRequired();
            makeElite(event.getEntity(), eliteType);
            // TODO: modify base stat modifiers
            runEliteSpawnFunctionForType(eliteType, event.getEntity());
        }
        // TODO: apply stat modifiers
    }

    @SubscribeEvent
    public static void eliteDamageEvents(LivingHurtEvent event) {
        if (!event.getEntity().level().isClientSide()) return;
        if (event.getEntity() instanceof Enemy && event.getSource().getEntity() instanceof Player player) {
            EnemyEliteMarker marker = event.getEntity().getCapability(EnemyEliteMarkerProvider.CAPABILITY_TYPE).orElseThrow(() -> new IllegalStateException("Enemy does not have an elite marker"));
            EliteType type = marker.getEliteType();
            if (type == EliteType.NONE) return;
            runOnPlayerHitEliteFunctionForType(type, event, player, event.getEntity());
        } else if (event.getEntity() instanceof Player player && event.getSource().getEntity() instanceof Enemy) {
            EnemyEliteMarker marker = event.getSource().getEntity().getCapability(EnemyEliteMarkerProvider.CAPABILITY_TYPE).orElseThrow(() -> new IllegalStateException("Enemy does not have an elite marker"));
            EliteType type = marker.getEliteType();
            if (type == EliteType.NONE) return;
            runOnEliteHitPlayerFunctionForType(type, event, player, (LivingEntity) event.getSource().getEntity());
        }
    }

    private static void runEliteTickerForType(EliteType type, LivingEntity elite) {
        switch (type) {
            case MONSTROSITY -> Monstrosity.tick((Zombie) elite);
            case NECROMANCER -> Necromancer.tick((Skeleton) elite);
        }
    }

    private static void runEliteSpawnFunctionForType(EliteType type, LivingEntity elite) {
        switch (type) {
            case NECROMANCER -> Necromancer.onSpawn((Skeleton) elite);
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
        }
    }

    private static void makeElite(LivingEntity enemy, EliteType type) {
        EnemyEliteMarker eliteMarker = enemy.getCapability(EnemyEliteMarkerProvider.CAPABILITY_TYPE).orElseThrow(() -> new IllegalStateException("Enemy does not have an elite marker"));
        eliteMarker.setEliteType(type);
    }

}
