package dev.stormwatch.elite.capabilities;

import net.minecraft.nbt.CompoundTag;

public class PlayerLevel {

    private int level = 0;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void saveNBT(CompoundTag nbt) {
        nbt.putInt("level", this.level);
    }

    public void loadNBT(CompoundTag nbt) {
        this.level = nbt.getInt("level");
    }

    public static final PlayerLevel EMPTY = new PlayerLevel();

}
