package dev.stormwatch.elite.systems;

import dev.stormwatch.elite.Config;
import dev.stormwatch.elite.capabilities.EnemyEliteMarker;
import dev.stormwatch.elite.capabilities.EnemyEliteMarkerProvider;
import dev.stormwatch.elite.doc.MonsterEliteData;
import dev.stormwatch.elite.util.AttributeUtil;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class MonsterEnhancer {

    // TODO: put all elite abilities that trigger on events in one single eventlistener with a switch statement, so that it doesnt have to check if mob is an elite and then which elite type for every mob for every elite type

    private static final AttributeUtil.AttributeInfo BASE_MONSTER_HEALTH_INFO =
            new AttributeUtil.AttributeInfo("base_monster_health", UUID.fromString("e8622f37-6b00-4cb0-be6e-f4fe0c5a517c"));
    private static final AttributeUtil.AttributeInfo BASE_MONSTER_MOVEMENT_SPEED_INFO =
            new AttributeUtil.AttributeInfo("base_monster_movement_speed", UUID.fromString("57470bf0-ae87-4db1-a999-c8441b9b10ba"));
    // TODO: consider increasing armor/toughness, depending on whether unique items become op. Maybe on some types of elites?

    private static void setMonsterBaseStats(Monster monster) {
        AttributeUtil.setPermanentAttribute(monster, Attributes.MAX_HEALTH, BASE_MONSTER_HEALTH_INFO.name(), BASE_MONSTER_HEALTH_INFO.uuid(),
                Config.getBaseMonsterHealthIncrease(), AttributeModifier.Operation.MULTIPLY_BASE);
        AttributeUtil.setPermanentAttribute(monster, Attributes.MOVEMENT_SPEED, BASE_MONSTER_MOVEMENT_SPEED_INFO.name(), BASE_MONSTER_MOVEMENT_SPEED_INFO.uuid(),
                Config.getBaseMonsterMovementSpeedIncrease(), AttributeModifier.Operation.MULTIPLY_BASE);
    }

    private static void setMonsterEliteStats(Monster monster, MonsterEliteData.Type eliteType) {
        // TODO
    }

    private static void makeElite(Monster monster) {
        EnemyEliteMarker marker = monster.getCapability(EnemyEliteMarkerProvider.CAPABILITY_TYPE).orElse(EnemyEliteMarker.EMPTY);
        if (marker == EnemyEliteMarker.EMPTY) return;
        MonsterEliteData.Type eliteType = MonsterEliteData.getEliteTypeForMonster(monster);
        marker.setEliteType(eliteType);
        setMonsterEliteStats(monster, eliteType);
    }

    @SubscribeEvent
    public static void increaseMonsterDamage(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getSource().getEntity() instanceof Monster)) return;

        event.setAmount((float) (event.getAmount() + event.getAmount() * Config.getBaseMonsterDamageIncrease()));
    }

}
