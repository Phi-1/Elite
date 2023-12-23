package dev.stormwatch.elite.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CharmedEffect extends MobEffect {

    private static final int EFFECT_RADIUS = 16;

    public CharmedEffect() {
        super(MobEffectCategory.HARMFUL, 0xf23597);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amp) {
        return duration % 20 == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amp) {
        if (!(living instanceof Mob mob)) return;
        if (mob.getTarget() == null || mob.getTarget() instanceof Player) {
            List<LivingEntity> nearbyMobs = mob.level().getEntitiesOfClass(LivingEntity.class, mob.getBoundingBox().inflate(EFFECT_RADIUS), (entity) -> entity instanceof Mob && entity != mob);
            if (nearbyMobs.size() == 0) return;
            int index = ThreadLocalRandom.current().nextInt(nearbyMobs.size());
            mob.setTarget(nearbyMobs.get(index));
        }
    }

}
