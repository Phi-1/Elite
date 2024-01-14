package dev.stormwatch.elite.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ambient.Bat;

import java.util.List;

public class SmokedEffect extends MobEffect {

    private final int BAT_CHECKING_RANGE = 16;
    private final int MAX_BATS = 7;

    public SmokedEffect() {
        super(MobEffectCategory.HARMFUL, 0x333333);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amp) {
        return duration % 10 == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amp) {
        if (living instanceof Mob mob) {
            List<Bat> nearbyBats = living.level().getEntitiesOfClass(Bat.class, living.getBoundingBox().inflate(BAT_CHECKING_RANGE));
            if (nearbyBats.size() >= MAX_BATS) {
                if (!(mob.getTarget() instanceof Bat)) {
                    mob.setTarget(nearbyBats.get(0));
                }
                return;
            }
            Bat bat = EntityType.BAT.create(living.level());
            if (bat == null) return;
            bat.moveTo(living.blockPosition().above(2).getCenter());
            living.level().addFreshEntity(bat);
            mob.setTarget(bat);
        }
    }
}
