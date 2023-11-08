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

public class PlayerLevelProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static final Capability<PlayerLevel> CAPABILITY_TYPE = CapabilityManager.get(new CapabilityToken<PlayerLevel>() {});
    private PlayerLevel playerLevel = null;
    private final LazyOptional<PlayerLevel> optional = LazyOptional.of(this::getOrCreatePlayerLevel);

    private PlayerLevel getOrCreatePlayerLevel() {
        if (this.playerLevel == null) {
            this.playerLevel = new PlayerLevel();
        }
        return this.playerLevel;
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
        this.getOrCreatePlayerLevel().saveNBT(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.getOrCreatePlayerLevel().loadNBT(nbt);
    }
}
