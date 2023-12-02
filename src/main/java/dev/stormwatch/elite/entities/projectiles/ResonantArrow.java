package dev.stormwatch.elite.entities.projectiles;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

public class ResonantArrow extends AbstractArrow {

    public static final float WARDEN_DAMAGE_MULTIPLIER = 4.0f;
    public static final float NORMAL_ENEMY_DAMAGE_MULTIPLIER = 0.2f;
    public static final int MOVEMENT_DELAY_MILLIS = 900;

    private final long timeCreated;
    private boolean isMoving;

    public ResonantArrow(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        this.setBaseDamage(2.0D);
        this.timeCreated = System.currentTimeMillis();
        this.isMoving = false;
    }

    @Override
    public void tick() {
        if (this.isMoving()) {
            super.tick();
            if (this.isCritArrow()) {
                Vec3 vec3 = this.getDeltaMovement();
                double d5 = vec3.x;
                double d6 = vec3.y;
                double d1 = vec3.z;
                this.level().addParticle(ParticleTypes.SONIC_BOOM, this.getX() + d5, this.getY() + d6, this.getZ() + d1, -d5, -d6 + 0.2D, -d1);
            }
        } else {
            if (System.currentTimeMillis() - this.timeCreated >= MOVEMENT_DELAY_MILLIS) {
                this.isMoving = true;
                if (this.isCritArrow()) {
                    this.level().playSound(null, this.blockPosition(), SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 0.2F, 2.0F / (this.level().getRandom().nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);
                }
            }
        }
    }

    public boolean isMoving() {
        return this.isMoving;
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
