package dev.stormwatch.elite.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MonsterEliteMarkerProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static final Capability<MonsterEliteMarker> CAPABILITY_TYPE = CapabilityManager.get(new CapabilityToken<MonsterEliteMarker>() {});
    private MonsterEliteMarker monsterEliteMarker = null;
    private final LazyOptional<MonsterEliteMarker> optional = LazyOptional.of(this::getOrCreateMonsterEliteMarker);

    private MonsterEliteMarker getOrCreateMonsterEliteMarker() {
        if (this.monsterEliteMarker == null) {
            this.monsterEliteMarker = new MonsterEliteMarker();
        }
        return this.monsterEliteMarker;
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
        this.getOrCreateMonsterEliteMarker().saveNBT(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.getOrCreateMonsterEliteMarker().loadNBT(nbt);
    }
}
