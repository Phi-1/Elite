package dev.stormwatch.elite.blocks;

import dev.stormwatch.elite.doc.RuneTypes;
import dev.stormwatch.elite.networking.EliteNetworking;
import dev.stormwatch.elite.networking.packets.AddExplosionS2CPacket;
import dev.stormwatch.elite.registry.EliteEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class RuneBlock extends Block {

    public static final IntegerProperty RUNE_TYPE = IntegerProperty.create("rune_type", 0, 2);
    public static final int EXPLOSION_DIAMETER = 9;

    public RuneBlock() {
        super(BlockBehaviour.Properties.of()
                .instabreak()
                .explosionResistance(1200f)
                .mapColor(MapColor.COLOR_CYAN)
                .sound(SoundType.GLASS)
                .lightLevel((state) -> 3));
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide()) return;
        if (entity instanceof Enemy) this.explode(state.getValue(RUNE_TYPE), level, pos);
    }

    public void explode(int runeType, Level level, BlockPos pos) {
        List<LivingEntity> nearbyEnemies = level.getEntitiesOfClass(LivingEntity.class,
                AABB.ofSize(pos.getCenter(), EXPLOSION_DIAMETER, EXPLOSION_DIAMETER, EXPLOSION_DIAMETER),
                (living) -> living instanceof Enemy);

        MobEffect effectType = switch (runeType) {
            case RuneTypes.OVERLOADING -> EliteEffects.OVERLOADED.get();
            case RuneTypes.ALCHEMIZING -> EliteEffects.ALCHEMIZED.get();
            // TODO: last rune type
            default -> throw new IllegalStateException("Unexpected value: " + runeType + ", rune type should be one of predefined values");
        };

        for (LivingEntity enemy : nearbyEnemies) {
            enemy.addEffect(new MobEffectInstance(effectType, 600, 1));
        }

        level.destroyBlock(pos, false);
        EliteNetworking.sendToClientsTrackingChunk(new AddExplosionS2CPacket(pos), level.getChunkAt(pos));
        level.playSound(null, pos, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.HOSTILE, 1.0f, 0.5f);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(RUNE_TYPE);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Shapes.box(0, 0, 0, 1, 0.1, 1);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return Shapes.empty();
    }

    @Override
    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return true;
    }

    @Override
    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return true;
    }
}
