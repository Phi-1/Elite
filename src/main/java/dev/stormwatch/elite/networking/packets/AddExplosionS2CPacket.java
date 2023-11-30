package dev.stormwatch.elite.networking.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class AddExplosionS2CPacket {

    private final BlockPos position;

    public AddExplosionS2CPacket(BlockPos position) {
        this.position = position;
    }

    public AddExplosionS2CPacket(FriendlyByteBuf buffer) {
        int x = buffer.readInt();
        int y = buffer.readInt();
        int z = buffer.readInt();
        this.position = new BlockPos(x, y, z);
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeInt(position.getX());
        buffer.writeInt(position.getY());
        buffer.writeInt(position.getZ());
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                LocalPlayer player = Minecraft.getInstance().player;
                if (player == null) return;
                Level level = player.level();
                if (!level.isLoaded(position)) return;
                for (int i = 0; i < 20; i++) {
                    float dX = (ThreadLocalRandom.current().nextFloat() < 0.5 ? i : -i) * ThreadLocalRandom.current().nextFloat();
                    float dZ = (ThreadLocalRandom.current().nextFloat() < 0.5 ? i : -i) * ThreadLocalRandom.current().nextFloat();
                    level.addParticle(ParticleTypes.EXPLOSION, position.getX() + dX, position.getY() + i / 2, position.getZ() + dZ, 1.0D, 0.0D, 0.0D);
                }
            });
        });
        context.get().setPacketHandled(true);
    }

}
