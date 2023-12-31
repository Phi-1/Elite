package dev.stormwatch.elite.capabilities;


import dev.stormwatch.elite.doc.EliteType;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class EnemyEliteMarker {

    private static final String INT_KEY_PREFIX = "intvalue_";

    private EliteType eliteType = EliteType.NONE;
    private final Map<String, Integer> intValues = new HashMap<>();

    public EliteType getEliteType() {
        return eliteType;
    }

    public void setEliteType(EliteType eliteType) {
        this.eliteType = eliteType;
    }

    public Optional<Integer> getIntValue(String key) {
        Integer value = intValues.get(INT_KEY_PREFIX + key);
        return value == null ? Optional.empty() : Optional.of(value);
    }

    public void setIntValue(String key, int value) {
        intValues.put(INT_KEY_PREFIX + key, value);
    }

    public boolean hasIntValue(String key) {
        return intValues.containsKey(key);
    }

    public void saveNBT(CompoundTag nbt) {
        nbt.putString("eliteType", this.eliteType.name());
        for (String key : intValues.keySet()) {
            nbt.putInt(key, intValues.get(key));
        }
    }

    public void loadNBT(CompoundTag nbt) {
        this.eliteType = EliteType.valueOf(nbt.getString("eliteType"));
        loadIntValues(nbt);
    }

    private void loadIntValues(CompoundTag nbt) {
        Set<String> allKeys = nbt.getAllKeys();
        for (String key : allKeys) {
            if (key.matches("^" + INT_KEY_PREFIX + "[A-Za-z0-9\s_]*")) {
                int value = nbt.getInt(key);
                intValues.put(key, value);
            }
        }
    }
}
