package dev.stormwatch.elite.client;

import com.google.common.collect.ImmutableMap;
import dev.stormwatch.elite.doc.SlotIndices;
import dev.stormwatch.elite.networking.EliteNetworking;
import dev.stormwatch.elite.networking.packets.ActivateCharmAbilityC2SPacket;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EliteInputHandler {

    private static final ImmutableMap<Lazy<KeyMapping>, Runnable> BINDINGS = new ImmutableMap.Builder<Lazy<KeyMapping>, Runnable>()
            .put(EliteKeyMappings.CHARM_ABILITY_1, () -> activateCharmAbility(SlotIndices.CHARM_1))
            .put(EliteKeyMappings.CHARM_ABILITY_2, () -> activateCharmAbility(SlotIndices.CHARM_2))
            .put(EliteKeyMappings.CHARM_ABILITY_3, () -> activateCharmAbility(SlotIndices.CHARM_3))
            .build();

    @SubscribeEvent
    public static void handleInput(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        for (Lazy<KeyMapping> keyMapping : BINDINGS.keySet()) {
            while (keyMapping.get().consumeClick()) {
                Runnable binding = BINDINGS.get(keyMapping);
                if (binding != null) {
                    binding.run();
                }
            }
        }
    }

    private static void activateCharmAbility(int slot) {
        EliteNetworking.sendToServer(new ActivateCharmAbilityC2SPacket(slot));
    }

}
