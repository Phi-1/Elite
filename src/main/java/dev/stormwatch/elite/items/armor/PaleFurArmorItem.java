package dev.stormwatch.elite.items.armor;

import dev.stormwatch.elite.doc.SlotIndices;
import dev.stormwatch.elite.registry.EliteArmorMaterials;
import dev.stormwatch.elite.registry.EliteEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PaleFurArmorItem extends ArmorItem {

    // interval is relatively long, so it doesn't wreck a single high health enemy
    private final int HELMET_MARKING_INTERVAL_TICKS = 6 * 20;
    private final int HELMET_MARKING_RADIUS = 16;
    private final int HELMET_MARK_DURATION_TICKS = 12 * 20;

    public PaleFurArmorItem(Type type) {
        super(EliteArmorMaterials.PALE_FUR, type, new Properties()
                .rarity(Rarity.UNCOMMON));
    }

    // TODO: all effects trigger at night
    // TODO: chance to dodge damage at night and turn invisible (untargetable?) for a bit
    // TODO: ms and step height at night
    // TODO: night set effect: midair jump that goes either really high if not moving or really far in the direction youre moving


    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotID, boolean isSelected) {
        if (!(entity instanceof LivingEntity living)) return;
        if (slotID == SlotIndices.HELMET) {
            processHelmetAbility(level, living);
        }
    }

    private void processHelmetAbility(Level level, LivingEntity living) {
        if (level.isClientSide() || !isNight(level)
                 || level.getGameTime() % HELMET_MARKING_INTERVAL_TICKS != 0) return;

        List<LivingEntity> nearbyEnemies = level.getEntities(living, living.getBoundingBox().inflate(HELMET_MARKING_RADIUS),
                (entity) -> entity instanceof Enemy && entity instanceof LivingEntity)
                .stream().map((entity) -> (LivingEntity) entity).toList();

        if (nearbyEnemies.size() == 0) return;
        for (LivingEntity enemy : nearbyEnemies) {
            if (enemy.hasEffect(EliteEffects.MARKED.get())) return;
        }

        int randomIndex = ThreadLocalRandom.current().nextInt(0, nearbyEnemies.size());
        LivingEntity randomEnemy = nearbyEnemies.get(randomIndex);

        randomEnemy.addEffect(new MobEffectInstance(MobEffects.GLOWING, HELMET_MARK_DURATION_TICKS));
        randomEnemy.addEffect(new MobEffectInstance(EliteEffects.MARKED.get(), HELMET_MARK_DURATION_TICKS));
    }

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return canFly(stack, entity);
    }

    @Override
    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        // TODO: durability? something else on tick?
        return canFly(stack, entity);
    }

    private boolean canFly(ItemStack stack, LivingEntity entity) {
        return isNight(entity.level());
    }

    private boolean isNight(Level level) {
        return level.getDayTime() >= 13000 || level.getDayTime() < 1000 || level.dimension().equals(Level.NETHER) || level.dimension().equals(Level.END);
    }
}
