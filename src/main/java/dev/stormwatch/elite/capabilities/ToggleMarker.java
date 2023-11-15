package dev.stormwatch.elite.capabilities;

import net.minecraft.nbt.CompoundTag;

public class ToggleMarker {

    private boolean active = false;

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void toggle() {
        this.active = !this.active;
    }

    public void saveNBT(CompoundTag nbt) {
        nbt.putBoolean("active", this.active);
    }

    public void loadNBT(CompoundTag nbt) {
        this.active = nbt.getBoolean("active");
    }

}
