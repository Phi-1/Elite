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

public class CooldownMarkerProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static final Capability<CooldownMarker> CAPABILITY_TYPE = CapabilityManager.get(new CapabilityToken<>() {});
    private CooldownMarker cooldownMarker = null;
    private final LazyOptional<CooldownMarker> optional = LazyOptional.of(this::getOrCreateCooldownMarker);

    private CooldownMarker getOrCreateCooldownMarker() {
        if (this.cooldownMarker == null) {
            this.cooldownMarker = new CooldownMarker();
        }
        return this.cooldownMarker;
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
        this.getOrCreateCooldownMarker().saveNBT(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.getOrCreateCooldownMarker().loadNBT(nbt);
    }
}
