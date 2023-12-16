package dev.stormwatch.elite;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = Elite.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

    private static double startingHearts;
    private static double startingMovementSpeed;
    private static double heartsPerLevel;
    private static double movementSpeedPerLevel;
    private static double miningSpeedPerLevel;
    private static double blockReachPerLevel;

    private static double baseMonsterHealthIncrease;
    private static double baseMonsterDamageIncrease;
    private static double baseMonsterMovementSpeedIncrease;

    private static double monsterSpawnRateIncrease;
    private static double eliteMonsterSpawnChance;

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.DoubleValue STARTING_HEARTS = BUILDER
            .comment("The number of hearts each player starts out with. Can be a fractional value")
            .defineInRange("startingHearts", 3, 0.5,  Double.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue STARTING_MOVEMENT_SPEED = BUILDER
            .comment("The amount of movement speed each player starts out with. 1 = regular speed.")
            .defineInRange("startingMovementSpeed", 0.8, 0.1,  Double.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue HEARTS_PER_LEVEL = BUILDER
            .comment("The number of hearts each player gains per elite level. Can be a fractional value")
            .defineInRange("heartsPerLevel", 1, 0,  Double.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue MOVEMENT_SPEED_PER_LEVEL = BUILDER
            .comment("The amount of movement speed each player gains per elite level. 0.1 = 10% extra, 0.5 = 50% and so on")
            .defineInRange("movementSpeedPerLevel", 0.05, 0,  Double.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue MINING_SPEED_PER_LEVEL = BUILDER
            .comment("The amount of mining speed each player gains per elite level. 0.1 = 10% extra, 0.5 = 50% and so on")
            .defineInRange("miningSpeedPerLevel", 0.1, 0,  Double.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue BLOCK_REACH_PER_LEVEL = BUILDER
            .comment("The amount of block reach each player gains per elite level. 0.1 = 10% extra, 0.5 = 50% and so on")
            .defineInRange("blockReachPerLevel", 0.1, 0,  Double.MAX_VALUE);

    private static final ForgeConfigSpec.DoubleValue BASE_MONSTER_HEALTH_INCREASE = BUILDER
            .comment("The base health increase of every hostile mob. 0.1 = 10% extra, 0.5 = 50% and so on")
            .defineInRange("baseMonsterHealthIncrease", 0.4, 0,  Double.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue BASE_MONSTER_DAMAGE_INCREASE = BUILDER
            .comment("The base damage increase of every hostile mob. 0.1 = 10% extra, 0.5 = 50% and so on")
            .defineInRange("baseMonsterDamageIncrease", 0.1, 0,  Double.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue BASE_MONSTER_MOVEMENT_SPEED_INCREASE = BUILDER
            .comment("The base movement speed increase of every hostile mob. 0.1 = 10% extra, 0.5 = 50% and so on")
            .defineInRange("baseMonsterMovementSpeedIncrease", 0.1, 0,  Double.MAX_VALUE);

    private static final ForgeConfigSpec.DoubleValue MONSTER_SPAWN_RATE_INCREASE = BUILDER
            .comment("The chance that an additional monster spawns with each monster spawn")
            .defineInRange("monsterSpawnRateIncrease", 0.5, 0,  Double.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue ELITE_MONSTER_SPAWN_CHANCE = BUILDER
            .comment("The chance for each hostile mob to become an elite. 0.1 = 10%, 0.5 = 50% and so on")
            .defineInRange("eliteMonsterSpawnChance", 0.01, 0,  Double.MAX_VALUE);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        startingHearts = STARTING_HEARTS.get();
        startingMovementSpeed = STARTING_MOVEMENT_SPEED.get();
        heartsPerLevel = HEARTS_PER_LEVEL.get();
        movementSpeedPerLevel = MOVEMENT_SPEED_PER_LEVEL.get();
        miningSpeedPerLevel = MINING_SPEED_PER_LEVEL.get();
        blockReachPerLevel = BLOCK_REACH_PER_LEVEL.get();

        baseMonsterHealthIncrease = BASE_MONSTER_HEALTH_INCREASE.get();
        baseMonsterDamageIncrease = BASE_MONSTER_DAMAGE_INCREASE.get();
        baseMonsterMovementSpeedIncrease = BASE_MONSTER_MOVEMENT_SPEED_INCREASE.get();

        monsterSpawnRateIncrease = MONSTER_SPAWN_RATE_INCREASE.get();
        eliteMonsterSpawnChance = ELITE_MONSTER_SPAWN_CHANCE.get();
    }

    public static double getStartingHearts() {
        return startingHearts;
    }

    public static double getHeartsPerLevel() {
        return heartsPerLevel;
    }

    public static double getMovementSpeedPerLevel() {
        return movementSpeedPerLevel;
    }

    public static double getMiningSpeedPerLevel() {
        return miningSpeedPerLevel;
    }

    public static double getBlockReachPerLevel() {
        return blockReachPerLevel;
    }

    public static double getBaseMonsterHealthIncrease() {
        return baseMonsterHealthIncrease;
    }

    public static double getBaseMonsterMovementSpeedIncrease() {
        return baseMonsterMovementSpeedIncrease;
    }

    public static double getBaseMonsterDamageIncrease() {
        return baseMonsterDamageIncrease;
    }

    public static double getEliteMonsterSpawnChance() {
        return eliteMonsterSpawnChance;
    }

    public static double getMonsterSpawnRateIncrease() {
        return monsterSpawnRateIncrease;
    }

    public static double getStartingMovementSpeed() {
        return startingMovementSpeed;
    }
}
