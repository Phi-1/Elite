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

public class FuelLevelProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static final Capability<FuelLevel> CAPABILITY_TYPE = CapabilityManager.get(new CapabilityToken<>() {});
    private FuelLevel fuelLevel = null;
    private final LazyOptional<FuelLevel> optional = LazyOptional.of(this::getOrCreateFuelLevel);

    private FuelLevel getOrCreateFuelLevel() {
        if (this.fuelLevel == null) {
            this.fuelLevel = new FuelLevel();
        }
        return this.fuelLevel;
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
        this.getOrCreateFuelLevel().saveNBT(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.getOrCreateFuelLevel().loadNBT(nbt);
    }
}
