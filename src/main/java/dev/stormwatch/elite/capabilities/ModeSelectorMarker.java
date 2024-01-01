package dev.stormwatch.elite.capabilities;

import net.minecraft.nbt.CompoundTag;

public class ModeSelectorMarker {

    private final int modes;
    private int index = 0;

    public ModeSelectorMarker(int modes) {
        this.modes = modes;
    }

    public void increaseIndex() {
        this.index++;
    }

    public void decreaseIndex() {
        this.index--;
    }

    public int getMode() {
        return this.index % this.modes;
    }

    public void saveNBT(CompoundTag nbt) {
        nbt.putInt("mode", this.getMode());
    }

    public void loadNBT(CompoundTag nbt) {
        this.index = nbt.getInt("mode");
    }

    public static final ModeSelectorMarker EMPTY = new ModeSelectorMarker(0);

}
