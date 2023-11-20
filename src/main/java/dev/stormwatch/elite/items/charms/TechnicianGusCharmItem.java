package dev.stormwatch.elite.items.charms;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

public class TechnicianGusCharmItem extends CharmItem {
    // TODO: He's a fun guy. Good ol' fun Gus

    private static final int EFFECT_INTERVAL = 15 * 20;
    private static final float HEAL_AMOUNT = 1f;
    private static final int DURABILITY_RESTORE_AMOUNT = 1;

    public TechnicianGusCharmItem() {
        super(new Item.Properties()
                .rarity(Rarity.UNCOMMON)
                .stacksTo(1));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (!(slotContext.entity() instanceof Player player)) return;
        if (player.level().isClientSide()) return;
        if (player.level().getGameTime() % EFFECT_INTERVAL == 0) {
            player.heal(HEAL_AMOUNT);
            restoreDurabilityForToolsInInventory(player, DURABILITY_RESTORE_AMOUNT);
        }
    }

    private void restoreDurabilityForToolsInInventory(Player player, int durabilityRestore) {
        for (ItemStack item : player.getInventory().items) {
            if (item.isDamaged()) {
                item.setDamageValue(Math.max(item.getDamageValue() - durabilityRestore, 0));
            }
        }
        for (ItemStack armor : player.getInventory().armor) {
            if (armor.isDamaged()) {
                armor.setDamageValue(Math.max(armor.getDamageValue() - durabilityRestore, 0));
            }
        }
    }

}
