package dev.stormwatch.elite.client.abilities;

import dev.stormwatch.elite.items.charms.AutoAnnihilationEngineCharmItem;
import dev.stormwatch.elite.networking.EliteNetworking;
import dev.stormwatch.elite.networking.packets.ActivateFlightEngineC2SPacket;
import dev.stormwatch.elite.util.InventoryUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.ThreadLocalRandom;

public class AutoAnnihilationEngineAbility {

    public static boolean holdingSpace = false;

    @SubscribeEvent
    public static void trackSpace(InputEvent.Key event) {
        if (event.getKey() == GLFW.GLFW_KEY_SPACE) {
            switch (event.getAction()) {
                case GLFW.GLFW_PRESS -> holdingSpace = true;
                case GLFW.GLFW_RELEASE -> holdingSpace = false;
            }
        }
    }

    @SubscribeEvent
    public static void flightCheck(TickEvent.PlayerTickEvent event) {
        if (!event.player.isLocalPlayer()) return;
        if (!holdingSpace) return;
        ItemStack engine = InventoryUtil.getEquippedCharmOfType(event.player, AutoAnnihilationEngineCharmItem.class);
        if (engine == null) return;
        EliteNetworking.sendToServer(new ActivateFlightEngineC2SPacket());
    }

}
