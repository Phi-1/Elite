package dev.stormwatch.elite.doc;

import com.google.common.collect.ImmutableMap;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class SoundEventIndices {

    public static final int BEZOAR_ACTIVATE = 0;
    public static final int NEGATE_DAMAGE = 1;
    public static final int DEAL_CRIT_DAMAGE = 2;
    public static final int EXECUTION_TRIGGER = 3;
    public static final int POTION_BELT_ACTIVATE = 4;

    private static final ImmutableMap<Integer, SoundEvent> SOUND_EVENTS = new ImmutableMap.Builder<Integer, SoundEvent>()
            .put(BEZOAR_ACTIVATE, SoundEvents.AMETHYST_BLOCK_HIT)
            .put(NEGATE_DAMAGE, SoundEvents.SHIELD_BLOCK)
            .put(DEAL_CRIT_DAMAGE, SoundEvents.TRIDENT_HIT_GROUND)
            .put(EXECUTION_TRIGGER, SoundEvents.TRIDENT_RIPTIDE_1)
            .put(POTION_BELT_ACTIVATE, SoundEvents.BREWING_STAND_BREW)
            .build();
    private static final SoundEvent DEFAULT_SOUND = SoundEvents.ALLAY_AMBIENT_WITH_ITEM;

    public static SoundEvent getSoundForIndex(int soundIndex) {
        return SOUND_EVENTS.getOrDefault(soundIndex, DEFAULT_SOUND);
    }

}
