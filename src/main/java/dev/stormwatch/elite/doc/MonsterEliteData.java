package dev.stormwatch.elite.doc;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;

public class MonsterEliteData {

    public static Type getEliteTypeForMonster(Monster monster) {
        if (monster instanceof Zombie) return Type.MONSTROSITY;
        if (monster instanceof Skeleton) return Type.NECROMANCER;
        if (monster instanceof CaveSpider) return Type.CROOKED_SPAWN;
        else return Type.NONE;
    }

    public enum Type {
        NONE,
        MONSTROSITY,
        NECROMANCER,
        CROOKED_SPAWN
    }

}
