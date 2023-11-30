package dev.stormwatch.elite.networking;

import dev.stormwatch.elite.Elite;
import dev.stormwatch.elite.networking.packets.ActivateCharmAbilityC2SPacket;
import dev.stormwatch.elite.networking.packets.AddExplosionS2CPacket;
import dev.stormwatch.elite.networking.packets.PlaySoundS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class EliteNetworking {

    private static SimpleChannel INSTANCE;

    private static int packetID = 0;
    private static int id() { return packetID++; }

    public static void register() {
        INSTANCE = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Elite.MOD_ID, "networking"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE.messageBuilder(ActivateCharmAbilityC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ActivateCharmAbilityC2SPacket::new)
                .encoder(ActivateCharmAbilityC2SPacket::toBytes)
                .consumerMainThread(ActivateCharmAbilityC2SPacket::handle)
                .add();

        INSTANCE.messageBuilder(PlaySoundS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PlaySoundS2CPacket::new)
                .encoder(PlaySoundS2CPacket::toBytes)
                .consumerMainThread(PlaySoundS2CPacket::handle)
                .add();

        INSTANCE.messageBuilder(AddExplosionS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(AddExplosionS2CPacket::new)
                .encoder(AddExplosionS2CPacket::toBytes)
                .consumerMainThread(AddExplosionS2CPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClientsTrackingChunk(MSG message, LevelChunk chunk) {
        INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), message);
    }

    public static <MSG> void sendToAllConnectedClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }

}
