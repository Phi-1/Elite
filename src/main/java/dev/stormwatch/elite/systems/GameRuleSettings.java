package dev.stormwatch.elite.systems;

import dev.stormwatch.elite.Elite;
import dev.stormwatch.elite.capabilities.CooldownMarkerProvider;
import dev.stormwatch.elite.items.CooldownAbilityItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GameRuleSettings {

    @SubscribeEvent
    public static void setGameRules(LevelEvent.Load event) {
        MinecraftServer server = event.getLevel().getServer();
        if (server == null) return;
        server.getGameRules().getRule(GameRules.RULE_NATURAL_REGENERATION).set(false, server);
        server.getGameRules().getRule(GameRules.RULE_PLAYERS_SLEEPING_PERCENTAGE).set(30, server);
    }

}
