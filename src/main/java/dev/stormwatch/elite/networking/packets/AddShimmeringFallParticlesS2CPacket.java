package dev.stormwatch.elite.networking.packets;

import dev.stormwatch.elite.effects.ShimmeringCrownPassivesEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AddShimmeringFallParticlesS2CPacket {
    // TODO: move all particle effects to function map, use single packet like with sound

    private final Vec3 pos;

    public AddShimmeringFallParticlesS2CPacket(Vec3 pos) {
        this.pos = pos;
    }

    public AddShimmeringFallParticlesS2CPacket(FriendlyByteBuf buffer) {
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        this.pos = new Vec3(x, y, z);
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeDouble(this.pos.x);
        buffer.writeDouble(this.pos.y);
        buffer.writeDouble(this.pos.z);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                Player player = Minecraft.getInstance().player;
                if (player == null) return;
                ShimmeringCrownPassivesEffect.addFallNegationParticles(this.pos, player.level());
            });
        });
        context.get().setPacketHandled(true);
    }

}
