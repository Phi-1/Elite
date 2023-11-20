package dev.stormwatch.elite.networking.packets;

import dev.stormwatch.elite.Feedback;
import dev.stormwatch.elite.doc.SoundEventIndices;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlaySoundS2CPacket {

    private final int soundIndex;

    public PlaySoundS2CPacket(int soundIndex) {
        this.soundIndex = soundIndex;
    }

    public PlaySoundS2CPacket(FriendlyByteBuf buffer) {
        this.soundIndex = buffer.readInt();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeInt(this.soundIndex);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                LocalPlayer player = Minecraft.getInstance().player;
                if (player == null) return;
                player.level().playSound(player, player.blockPosition(), Feedback.getSoundForIndex(this.soundIndex), SoundSource.PLAYERS, 1.0f, 1.0f);
            });
        });
        context.get().setPacketHandled(true);
    }

}
