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

public class ShimmeringCrownMarkerProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final Capability<ShimmeringCrownMarker> CAPABILITY_TYPE = CapabilityManager.get(new CapabilityToken<>() {});
    private ShimmeringCrownMarker crownMarker = null;
    private final LazyOptional<ShimmeringCrownMarker> optional = LazyOptional.of(this::getOrCreateShimmeringCrownMarker);

    private ShimmeringCrownMarker getOrCreateShimmeringCrownMarker() {
        if (this.crownMarker == null) {
            this.crownMarker = new ShimmeringCrownMarker();
        }
        return this.crownMarker;
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
        this.getOrCreateShimmeringCrownMarker().saveNBT(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.getOrCreateShimmeringCrownMarker().loadNBT(nbt);
    }
}
