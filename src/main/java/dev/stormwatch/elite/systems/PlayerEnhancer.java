package dev.stormwatch.elite.systems;

import dev.stormwatch.elite.Elite;
import dev.stormwatch.elite.capabilities.PlayerLevelProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerEnhancer {

    public static void evaluatePlayerStats(Player player) {
        // get capability
        // calculate stats
        // set stats
    }

    @SubscribeEvent
    public static void setPlayerStatsOnLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().level().isClientSide()) return;

        evaluatePlayerStats(event.getEntity());
    }

    @SubscribeEvent
    public static void attachPlayerLevelCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(PlayerLevelProvider.CAPABILITY_TYPE).isPresent()) {
                event.addCapability(new ResourceLocation(Elite.MOD_ID, "player_level"), new PlayerLevelProvider());
            }
        }
    }

}
