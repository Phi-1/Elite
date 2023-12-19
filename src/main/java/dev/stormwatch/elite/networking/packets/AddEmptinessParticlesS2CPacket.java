package dev.stormwatch.elite.networking.packets;

import dev.stormwatch.elite.effects.EmptinessEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AddEmptinessParticlesS2CPacket {

    private final BlockPos pos;

    public AddEmptinessParticlesS2CPacket(BlockPos center) {
        this.pos = center;
    }

    public AddEmptinessParticlesS2CPacket(FriendlyByteBuf buffer) {
        int x = buffer.readInt();
        int y = buffer.readInt();
        int z = buffer.readInt();
        this.pos = new BlockPos(x, y, z);
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeInt(this.pos.getX());
        buffer.writeInt(this.pos.getY());
        buffer.writeInt(this.pos.getZ());
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                Player player = Minecraft.getInstance().player;
                if (player == null) return;
                EmptinessEffect.createExplosionParticles(player.level(), this.pos);
            });
        });
        context.get().setPacketHandled(true);
    }

}
