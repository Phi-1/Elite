package dev.stormwatch.elite.networking.packets;

import dev.stormwatch.elite.items.charms.AutoAnnihilationEngineCharmItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ActivateFlightEngineC2SPacket {

    public ActivateFlightEngineC2SPacket() {}
    public ActivateFlightEngineC2SPacket(FriendlyByteBuf buf) {}

    public void toBytes(FriendlyByteBuf buf) {}

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player == null) return;
            AutoAnnihilationEngineCharmItem.tryFly(player);
        });
        context.get().setPacketHandled(true);
    }

}
