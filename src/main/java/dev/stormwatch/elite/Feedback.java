package dev.stormwatch.elite;

import net.minecraft.world.entity.player.Player;

public class Feedback {

    public static void onEliteSpawn() {
        // TODO: client config for turning this off. Menu option would be better
        // TODO: chat message
    }

    public static void onEliteKill(Player player) {
        // TODO: maybe broadcast this to all players?
    }

    public static void onPlayerLevelup(Player player) {
        // TODO
    }

}