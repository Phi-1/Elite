package dev.stormwatch.elite.client.data;

import dev.stormwatch.elite.doc.EliteType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EliteTracker {

    // TODO: packet elites here on spawn and death
    // TODO: incorporate current health

    private static final Map<UUID, EliteType> ELITES = new HashMap<>();

    public static void trackElite(UUID uuid, EliteType type) {
        ELITES.put(uuid, type);
    }

    public static void untrackElite(UUID uuid) {
        ELITES.remove(uuid);
    }

    public static boolean isTrackingElite(UUID uuid) {
        return ELITES.containsKey(uuid);
    }

}
