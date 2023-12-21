package dev.stormwatch.elite.entities.projectiles;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class ResonantBoom extends Projectile {

    private int lifespanTicks = 6 * 20;
    private static final float DAMAGE = 40;

    public ResonantBoom(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    @Override
    public void tick() {
        super.tick();
        double dX = this.getDeltaMovement().x;
        double dY = this.getDeltaMovement().y;
        double dZ = this.getDeltaMovement().z;
        if (this.level().isClientSide()) {
            this.level().addParticle(ParticleTypes.SONIC_BOOM, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            this.level().addParticle(ParticleTypes.SONIC_BOOM, this.getX() + dX / 2, this.getY() + dY / 2, this.getZ() + dZ / 2, 0, 0, 0);
        } else {
            Vec3 position = this.position();
            Vec3 nextPosition = position.add(this.getDeltaMovement());
            List<LivingEntity> hitEntities = new ArrayList<>();
            EntityHitResult hitResult;
            do {
                hitResult = ProjectileUtil.getEntityHitResult(this.level(), this, position, nextPosition,
                        this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(3.0D),
                        (entity) -> entity instanceof LivingEntity && entity != this.getOwner() && !hitEntities.contains(entity));
                if (hitResult != null) {
                    this.onHit(hitResult);
                    hitEntities.add((LivingEntity) hitResult.getEntity());
                }
            } while (hitResult != null);

            ProjectileUtil.rotateTowardsMovement(this, 5.0f);
            this.setPos(this.getX() + dX, this.getY() + dY, this.getZ() + dZ);

            this.lifespanTicks--;
            if (this.lifespanTicks <= 0) {
                this.remove(RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (!(result.getEntity() instanceof LivingEntity living)) return;
        living.hurt(living.damageSources().magic(), DAMAGE);
        living.knockback(2, -this.getDeltaMovement().x, -this.getDeltaMovement().z);
    }

    @Override
    protected void defineSynchedData() {

    }
}
