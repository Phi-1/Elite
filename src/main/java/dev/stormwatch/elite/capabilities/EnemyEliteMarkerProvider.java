package dev.stormwatch.elite.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnemyEliteMarkerProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static final Capability<EnemyEliteMarker> CAPABILITY_TYPE = CapabilityManager.get(new CapabilityToken<EnemyEliteMarker>() {});
    private EnemyEliteMarker enemyEliteMarker = null;
    private final LazyOptional<EnemyEliteMarker> optional = LazyOptional.of(this::getOrCreateEnemyEliteMarker);

    private EnemyEliteMarker getOrCreateEnemyEliteMarker() {
        if (this.enemyEliteMarker == null) {
            this.enemyEliteMarker = new EnemyEliteMarker();
        }
        return this.enemyEliteMarker;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CAPABILITY_TYPE) {
            return this.optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        this.getOrCreateEnemyEliteMarker().saveNBT(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.getOrCreateEnemyEliteMarker().loadNBT(nbt);
    }
}
