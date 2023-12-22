package dev.stormwatch.elite.networking.packets;

import dev.stormwatch.elite.items.charms.AutoAnnihilationEngineCharmItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AddEngineParticlesS2CPacket {

    private final Vec3 playerPos;
    private final float yRot;

    public AddEngineParticlesS2CPacket(Vec3 playerPos, float yRot) {
        this.playerPos = playerPos;
        this.yRot = yRot;
    }

    public AddEngineParticlesS2CPacket(FriendlyByteBuf buffer) {
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        this.playerPos = new Vec3(x, y, z);
        this.yRot = buffer.readFloat();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeDouble(this.playerPos.x);
        buffer.writeDouble(this.playerPos.y);
        buffer.writeDouble(this.playerPos.z);
        buffer.writeFloat(this.yRot);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                LocalPlayer player = Minecraft.getInstance().player;
                if (player == null) return;
                AutoAnnihilationEngineCharmItem.addParticles(player.level(), this.playerPos, this.yRot);
            });
        });
        context.get().setPacketHandled(true);
    }

}
