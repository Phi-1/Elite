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

public class BlockFaceClickListener {

    private static final Map<UUID, Direction> LAST_LEFTCLICKED_FACE = new HashMap<>();
    private static final Map<UUID, Direction> LAST_RIGHTCLICKED_FACE = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getSide() == LogicalSide.CLIENT) return;
        if (event.getAction() != PlayerInteractEvent.LeftClickBlock.Action.START) return;
        Direction face = event.getFace();
        LAST_LEFTCLICKED_FACE.put(event.getEntity().getUUID(), face);
    }

    @SubscribeEvent
    public static void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getSide() == LogicalSide.CLIENT) return;
        Direction face = event.getFace();
        LAST_RIGHTCLICKED_FACE.put(event.getEntity().getUUID(), face);
    }

    @Nullable
    public static Direction getLastLeftClickedFace(Player player) {
        return LAST_LEFTCLICKED_FACE.get(player.getUUID());
    }

    @Nullable
    public static Direction getLastRightClickedFace(Player player) {
        return LAST_RIGHTCLICKED_FACE.get(player.getUUID());
    }

}
