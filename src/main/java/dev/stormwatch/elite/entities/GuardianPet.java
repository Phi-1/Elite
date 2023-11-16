package dev.stormwatch.elite.entities;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.Predicate;

public class GuardianPet extends Guardian {

    public GuardianPet(EntityType<? extends Guardian> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {

        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, new GuardianPet.GuardianPetAttackSelector(this)));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        amount = 0;
        return super.hurt(source, amount);
    }

    static class GuardianPetAttackSelector implements Predicate<LivingEntity> {

        private final GuardianPet guardian;

        public GuardianPetAttackSelector(GuardianPet guardian) {
            this.guardian = guardian;
        }

        public boolean test(@Nullable LivingEntity entity) {
            return (entity instanceof Monster) && entity.distanceToSqr(this.guardian) > 5.0D;
        }

    }

    // copied from Guardian

    private static final EntityDataAccessor<Integer> DATA_ID_ATTACK_TARGET = SynchedEntityData.defineId(GuardianPet.class, EntityDataSerializers.INT);

    void setActiveAttackTarget(int pActiveAttackTargetId) {
        this.entityData.set(DATA_ID_ATTACK_TARGET, pActiveAttackTargetId);
    }

    static class GuardianPetAttackGoal extends Goal {
        private final GuardianPet guardian;
        private int attackTime;
        private final boolean elder;

        public GuardianPetAttackGoal(GuardianPet pGuardian) {
            this.guardian = pGuardian;
            this.elder = false;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            LivingEntity livingentity = this.guardian.getTarget();
            return livingentity != null && livingentity.isAlive();
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            return super.canContinueToUse() && (this.elder || this.guardian.getTarget() != null && this.guardian.distanceToSqr(this.guardian.getTarget()) > 9.0D);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.attackTime = -10;
            this.guardian.getNavigation().stop();
            LivingEntity livingentity = this.guardian.getTarget();
            if (livingentity != null) {
                this.guardian.getLookControl().setLookAt(livingentity, 90.0F, 90.0F);
            }

            this.guardian.hasImpulse = true;
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            this.guardian.setActiveAttackTarget(0);
            this.guardian.setTarget((LivingEntity)null);
            this.guardian.randomStrollGoal.trigger();
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            LivingEntity livingentity = this.guardian.getTarget();
            if (livingentity != null) {
                this.guardian.getNavigation().stop();
                this.guardian.getLookControl().setLookAt(livingentity, 90.0F, 90.0F);
                if (!this.guardian.hasLineOfSight(livingentity)) {
                    this.guardian.setTarget((LivingEntity)null);
                } else {
                    ++this.attackTime;
                    if (this.attackTime == 0) {
                        this.guardian.setActiveAttackTarget(livingentity.getId());
                        if (!this.guardian.isSilent()) {
                            this.guardian.level().broadcastEntityEvent(this.guardian, (byte)21);
                        }
                    } else if (this.attackTime >= this.guardian.getAttackDuration()) {
                        float f = 1.0F;
                        if (this.guardian.level().getDifficulty() == Difficulty.HARD) {
                            f += 2.0F;
                        }

                        if (this.elder) {
                            f += 2.0F;
                        }

                        livingentity.hurt(this.guardian.damageSources().indirectMagic(this.guardian, this.guardian), f);
                        livingentity.hurt(this.guardian.damageSources().mobAttack(this.guardian), (float)this.guardian.getAttributeValue(Attributes.ATTACK_DAMAGE));
                        this.guardian.setTarget((LivingEntity)null);
                    }

                    super.tick();
                }
            }
        }
    }

}
