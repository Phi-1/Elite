package dev.stormwatch.elite.items.charms;

import dev.stormwatch.elite.capabilities.CooldownMarker;
import dev.stormwatch.elite.capabilities.CooldownMarkerProvider;
import dev.stormwatch.elite.doc.SoundEventIndices;
import dev.stormwatch.elite.items.CooldownAbilityItem;
import dev.stormwatch.elite.networking.EliteNetworking;
import dev.stormwatch.elite.networking.packets.PlaySoundS2CPacket;
import dev.stormwatch.elite.registry.EliteItems;
import dev.stormwatch.elite.util.InventoryUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ShimmeringMembraneCharmItem extends CharmItem implements CooldownAbilityItem {

    private static final int COOLDOWN_MILLIS = 120 * 1000;
    private static final int COOLDOWN_TICKS = COOLDOWN_MILLIS / 1000 * 20;

    public ShimmeringMembraneCharmItem() {
        super(new Properties()
                .fireResistant()
                .rarity(Rarity.EPIC)
                .stacksTo(1));
    }

    @SubscribeEvent
    public static void onMagicOrExplosionDamage(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player player)
                || player.level().isClientSide()
                || !(event.getSource().is(DamageTypeTags.IS_EXPLOSION) || event.getSource().is(DamageTypeTags.BYPASSES_ARMOR) || event.getSource().is(DamageTypeTags.IS_LIGHTNING))) return;

        ItemStack charm = InventoryUtil.getEquippedCharmOfType(player, ShimmeringMembraneCharmItem.class);
        if (charm == null) return;
        CooldownMarker cooldownMarker = charm.getCapability(CooldownMarkerProvider.CAPABILITY_TYPE).orElseThrow(() -> new IllegalStateException("Shimmering Membrane does not have a cooldown marker"));
        if (cooldownMarker.isOnCooldown(COOLDOWN_MILLIS)) return;

        event.setCanceled(true);
        player.getCooldowns().addCooldown(EliteItems.SHIMMERING_MEMBRANE.get(), COOLDOWN_TICKS);
        cooldownMarker.putOnCooldown();
        EliteNetworking.sendToPlayer(new PlaySoundS2CPacket(SoundEventIndices.NEGATE_DAMAGE), (ServerPlayer) player);
    }

    @Override
    public void activateAbility(ItemStack stack, Player player) {
    }

    @Override
    public int getCooldownMillis() {
        return COOLDOWN_MILLIS;
    }

}
