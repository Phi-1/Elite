package dev.stormwatch.elite.capabilities;

import net.minecraft.nbt.CompoundTag;

public class FuelLevel {

    public int fuelLevel = 0;

    public void saveNBT(CompoundTag nbt) {
        nbt.putInt("fuel", fuelLevel);
    }

    public void loadNBT(CompoundTag nbt) {
        this.fuelLevel = nbt.getInt("fuel");
    }

}
