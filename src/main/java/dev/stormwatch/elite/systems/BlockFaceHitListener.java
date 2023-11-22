package dev.stormwatch.elite.systems;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BlockFaceHitListener {

    private static final Map<UUID, Direction> LAST_HIT_FACE = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getSide() == LogicalSide.CLIENT) return;
        if (event.getAction() != PlayerInteractEvent.LeftClickBlock.Action.START) return;
        Direction face = event.getFace();
        LAST_HIT_FACE.put(event.getEntity().getUUID(), face);
    }

    @Nullable
    public static Direction getLastHitFace(Player player) {
        return LAST_HIT_FACE.get(player.getUUID());
    }

}
