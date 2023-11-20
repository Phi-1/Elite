package dev.stormwatch.elite;

import com.google.common.collect.ImmutableMap;
import dev.stormwatch.elite.doc.SoundEventIndices;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;


public class Feedback {

    private static final ImmutableMap<Integer, SoundEvent> SOUND_EVENTS = new ImmutableMap.Builder<Integer, SoundEvent>()
            .put(SoundEventIndices.BEZOAR_ACTIVATE, SoundEvents.AMETHYST_BLOCK_HIT)
            .build();
    private static final SoundEvent DEFAULT_SOUND = SoundEvents.ALLAY_AMBIENT_WITH_ITEM;

    public static SoundEvent getSoundForIndex(int soundIndex) {
        SoundEvent sound = SOUND_EVENTS.get(soundIndex);
        return sound == null ? DEFAULT_SOUND : sound;
    }

    public static void onToggleAbility() {
        // TODO
    }

    public static void onUseAbility() {
        // TODO
    }

    public static void onItemIsOnCooldown() {
        // TODO
    }

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
