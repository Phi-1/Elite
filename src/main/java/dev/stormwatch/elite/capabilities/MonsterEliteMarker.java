package dev.stormwatch.elite.capabilities;


import dev.stormwatch.elite.doc.MonsterEliteData;
import net.minecraft.nbt.CompoundTag;

public class MonsterEliteMarker {

    private MonsterEliteData.Type eliteType = MonsterEliteData.Type.NONE;


    public MonsterEliteData.Type getEliteType() {
        return eliteType;
    }

    public void setEliteType(MonsterEliteData.Type eliteType) {
        this.eliteType = eliteType;
    }

    public void saveNBT(CompoundTag nbt) {
        nbt.putString("eliteType", this.eliteType.name());
    }

    public void loadNBT(CompoundTag nbt) {
        this.eliteType = MonsterEliteData.Type.valueOf(nbt.getString("eliteType"));
    }

    public static final MonsterEliteMarker EMPTY = new MonsterEliteMarker();
}
