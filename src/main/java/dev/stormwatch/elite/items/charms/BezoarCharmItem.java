package dev.stormwatch.elite.items.charms;

import dev.stormwatch.elite.doc.SoundEventIndices;
import dev.stormwatch.elite.networking.EliteNetworking;
import dev.stormwatch.elite.networking.packets.PlaySoundS2CPacket;
import dev.stormwatch.elite.registry.EliteItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.concurrent.ThreadLocalRandom;

public class BezoarCharmItem extends CharmItem {

    private static final float EFFECT_NEGATE_CHANCE = 0.4f;

    public BezoarCharmItem() {
        super(new Item.Properties()
                .rarity(Rarity.UNCOMMON)
                .stacksTo(1));
    }

    @SubscribeEvent
    public static void negateEffect(MobEffectEvent.Applicable event) {
        if (!(event.getEffectInstance().getEffect().getCategory() == MobEffectCategory.HARMFUL)) return;
        if (!(ThreadLocalRandom.current().nextFloat() <= EFFECT_NEGATE_CHANCE)) return;
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        ICuriosItemHandler curios = CuriosApi.getCuriosInventory(player).orElseThrow(() -> new IllegalStateException("Player does not have a curios inventory"));
        ICurioStacksHandler charms = curios.getCurios().get("elite_charm");
        for (int slot = 0; slot < charms.getSlots(); slot++) {
            if (charms.getStacks().getStackInSlot(slot).is(EliteItems.BEZOAR.get())) {
                event.setResult(Event.Result.DENY);
                EliteNetworking.sendToPlayer(new PlaySoundS2CPacket(SoundEventIndices.BEZOAR_ACTIVATE), player);
                break;
            }
        }
    }

}
