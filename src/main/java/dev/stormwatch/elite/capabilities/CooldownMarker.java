package dev.stormwatch.elite.capabilities;

import net.minecraft.nbt.CompoundTag;

public class CooldownMarker {

    private long lastUsed = 0;

    public boolean isOnCooldown(int cooldownMilliseconds) {
        return cooldownMilliseconds > System.currentTimeMillis() - this.lastUsed;
    }

    public void putOnCooldown() {
        this.lastUsed = System.currentTimeMillis();
    }

    public void saveNBT(CompoundTag nbt) {
        nbt.putLong("last_used", this.lastUsed);
    }

    public void loadNBT(CompoundTag nbt) {
        this.lastUsed = nbt.getLong("last_used");
    }

    public static final CooldownMarker EMPTY = new CooldownMarker();

}
