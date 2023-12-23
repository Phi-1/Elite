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
    private final float volume;
    private final float pitch;

    public PlaySoundS2CPacket(int soundIndex) {
        this(soundIndex, 1.0f, 1.0f);
    }

    public PlaySoundS2CPacket(int soundIndex, float volume, float pitch) {
        this.soundIndex = soundIndex;
        this.volume = volume;
        this.pitch = pitch;
    }

    public PlaySoundS2CPacket(FriendlyByteBuf buffer) {
        this.soundIndex = buffer.readInt();
        this.volume = buffer.readFloat();
        this.pitch = buffer.readFloat();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeInt(this.soundIndex);
        buffer.writeFloat(this.volume);
        buffer.writeFloat(this.pitch);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                LocalPlayer player = Minecraft.getInstance().player;
                if (player == null) return;
                player.level().playSound(player, player.blockPosition(), SoundEventIndices.getSoundForIndex(this.soundIndex), SoundSource.PLAYERS, this.volume, this.pitch);
            });
        });
        context.get().setPacketHandled(true);
    }

}
