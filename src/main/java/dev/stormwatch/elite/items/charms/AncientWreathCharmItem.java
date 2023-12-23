package dev.stormwatch.elite.items.charms;

import dev.stormwatch.elite.registry.EliteItems;
import dev.stormwatch.elite.util.InventoryUtil;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AncientWreathCharmItem extends CharmItem {

    private static final int RETALIATION_COOLDOWN_TICKS = 100;

    public AncientWreathCharmItem() {
        super(new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.UNCOMMON));
    }

    @SubscribeEvent
    public static void onDamage(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player player)
                || player.level().isClientSide()
                || !InventoryUtil.hasCharmEquipped(player, (CharmItem) EliteItems.ANCIENT_WREATH.get())) return;

        if (event.getSource().is(DamageTypeTags.IS_LIGHTNING)) {
            event.setCanceled(true);
            return;
        }

        if (event.getSource().getEntity() == null) return;
        if (player.getCooldowns().isOnCooldown(EliteItems.ANCIENT_WREATH.get())) return;
        LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(player.level());
        if (bolt == null) return;
        bolt.moveTo(event.getSource().getEntity().position());
        bolt.setVisualOnly(false);
        player.level().addFreshEntity(bolt);
        player.getCooldowns().addCooldown(EliteItems.ANCIENT_WREATH.get(), RETALIATION_COOLDOWN_TICKS);
    }

}
