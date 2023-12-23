package dev.stormwatch.elite.capabilities;

import net.minecraft.nbt.CompoundTag;

public class ShimmeringCrownMarker {

    public int charge = 0;

    public void saveNBT(CompoundTag nbt) {
        nbt.putInt("charge", charge);
    }

    public void loadNBT(CompoundTag nbt) {
        this.charge = nbt.getInt("charge");
    }

}
