package dev.stormwatch.elite.items.charms;

import dev.stormwatch.elite.capabilities.ToggleMarker;
import dev.stormwatch.elite.capabilities.ToggleMarkerProvider;
import dev.stormwatch.elite.doc.SoundEventIndices;
import dev.stormwatch.elite.items.ToggleAbilityItem;
import dev.stormwatch.elite.networking.EliteNetworking;
import dev.stormwatch.elite.networking.packets.PlaySoundS2CPacket;
import dev.stormwatch.elite.registry.EliteItems;
import dev.stormwatch.elite.util.InventoryUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MarksmansMedalCharmItem extends CharmItem implements ToggleAbilityItem {

    private static final int SCRAP_COST = 3;
    private static final float DAMAGE_MULTIPLIER = 2f;

    public MarksmansMedalCharmItem() {
        super(new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.RARE));
    }

    @Override
    public void activateAbility(ItemStack stack, Player player) {
        // TODO: sound
        ToggleMarker toggleMarker = stack.getCapability(ToggleMarkerProvider.CAPABILITY_TYPE).orElse(ToggleMarker.EMPTY);
        if (toggleMarker == ToggleMarker.EMPTY) return;
        toggleMarker.toggle();
    }

    @SubscribeEvent
    public static void enhanceArrowDamage(LivingHurtEvent event) {
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) return;
        if (!event.getSource().is(DamageTypeTags.IS_PROJECTILE)) return;
        ItemStack charm = InventoryUtil.getEquippedCharmOfType(player, MarksmansMedalCharmItem.class);
        if (charm == null) return;
        ToggleMarker toggleMarker = charm.getCapability(ToggleMarkerProvider.CAPABILITY_TYPE).orElse(ToggleMarker.EMPTY);
        if (toggleMarker == ToggleMarker.EMPTY) return;
        if (!toggleMarker.isActive()) return;
        ItemStack darkIronScrap = InventoryUtil.getItemInInventory(player, EliteItems.DARK_IRON_SCRAP.get());
        if (darkIronScrap == null || darkIronScrap.getCount() < SCRAP_COST) {
            // TODO: feedback
            return;
        }
        event.setAmount(event.getAmount() * DAMAGE_MULTIPLIER);
        darkIronScrap.shrink(SCRAP_COST);
        EliteNetworking.sendToPlayer(new PlaySoundS2CPacket(SoundEventIndices.DEAL_CRIT_DAMAGE), player);
    }

}
