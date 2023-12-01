package dev.stormwatch.elite.entities.projectiles;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

public class ResonantArrow extends AbstractArrow {

    public static final float WARDEN_DAMAGE_MULTIPLIER = 4.0f;
    public static final float NORMAL_ENEMY_DAMAGE_MULTIPLIER = 0.2f;
    public static final int MOVEMENT_DELAY_MILLIS = 900;

    private final long timeCreated;

    public ResonantArrow(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        this.setBaseDamage(2.0D);
        this.timeCreated = System.currentTimeMillis();
    }

    @Override
    public void tick() {
        if (System.currentTimeMillis() - this.timeCreated >= MOVEMENT_DELAY_MILLIS) {
            super.tick();
        }
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @SubscribeEvent
    public static void increaseDamageAgainstWardens(LivingHurtEvent event) {
        if (!(event.getSource().getDirectEntity() instanceof ResonantArrow)
                || event.getEntity().level().isClientSide()) return;
        // TODO: test
        if (event.getEntity() instanceof Warden) {
            // TODO: sound
            event.setAmount(event.getAmount() * WARDEN_DAMAGE_MULTIPLIER);
        } else {
            event.setAmount(event.getAmount() * NORMAL_ENEMY_DAMAGE_MULTIPLIER);
        }

    }

}
