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

public class ModeSelectorMarkerProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static final Capability<ModeSelectorMarker> CAPABILITY_TYPE = CapabilityManager.get(new CapabilityToken<>() {});
    private ModeSelectorMarker modeSelectorMarker = null;
    private final int modes;
    private final LazyOptional<ModeSelectorMarker> optional = LazyOptional.of(this::getOrCreateModeSelectorMarker);

    public ModeSelectorMarkerProvider(int modes) {
        this.modes = modes;
    }

    private ModeSelectorMarker getOrCreateModeSelectorMarker() {
        if (this.modeSelectorMarker == null) {
            this.modeSelectorMarker = new ModeSelectorMarker(this.modes);
        }
        return this.modeSelectorMarker;
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
        this.getOrCreateModeSelectorMarker().saveNBT(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.getOrCreateModeSelectorMarker().loadNBT(nbt);
    }

}
