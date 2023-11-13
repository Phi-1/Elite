package dev.stormwatch.elite.networking.packets;

import dev.stormwatch.elite.items.AbilityItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.function.Supplier;

public class ActivateCharmAbilityC2SPacket {

    private final int charmSlot;

    public ActivateCharmAbilityC2SPacket(int charmSlot) {
        this.charmSlot = charmSlot;
    }

    public ActivateCharmAbilityC2SPacket(FriendlyByteBuf buffer) {
        this.charmSlot = buffer.readInt();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeInt(this.charmSlot);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            ICuriosItemHandler curios = CuriosApi.getCuriosInventory(player).orElseThrow(() -> new IllegalStateException("Player does not have a curios inventory"));
            ICurioStacksHandler charms = curios.getCurios().get("elite_charm");
            if (charms == null) return;
            ItemStack charm = charms.getStacks().getStackInSlot(this.charmSlot);
            if (charm.getItem() instanceof AbilityItem abilityCharm) {
                abilityCharm.activateAbility(charm, player);
            }
        });
        context.get().setPacketHandled(true);
    }

}
