package dev.stormwatch.elite.doc;

import com.google.common.collect.ImmutableMap;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;

public enum EliteType {

    NONE(Component.translatable("elite.displayname.none"), null),
    MONSTROSITY(Component.translatable("elite.displayname.monstrosity"), Zombie.class),
    NECROMANCER(Component.translatable("elite.displayname.necromancer"), Skeleton.class),
    CROOKED_SPAWN(Component.translatable("elite.displayname.crooked_spawn"), CaveSpider.class);

    private static final ImmutableMap<Class<? extends LivingEntity>, EliteType> TYPE_BY_ENTITY = new ImmutableMap.Builder<Class<? extends LivingEntity>, EliteType>()
            .put(Zombie.class, MONSTROSITY)
            .put(Skeleton.class, NECROMANCER)
            .put(CaveSpider.class, CROOKED_SPAWN)
            .build();

    public static EliteType getTypeForEntity(LivingEntity entity) {
        for (Class<? extends LivingEntity> cls : TYPE_BY_ENTITY.keySet()) {
            if (cls.isInstance(entity)) {
                return TYPE_BY_ENTITY.getOrDefault(cls, NONE);
            }
        }
        return NONE;
    }

    private final MutableComponent displayName;
    private final Class<? extends LivingEntity> entityType;

    EliteType(MutableComponent displayName, Class<? extends LivingEntity> entityType) {
        this.displayName = displayName;
        this.entityType = entityType;
    }

    public String getDisplayName(int maxCharacters) {
        return displayName.getString(maxCharacters);
    }

    public Class<? extends LivingEntity> getEntityType() {
        return entityType;
    }
}
