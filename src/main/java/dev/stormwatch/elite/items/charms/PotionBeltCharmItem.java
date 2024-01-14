package dev.stormwatch.elite.items.charms;

import dev.stormwatch.elite.doc.SoundEventIndices;
import dev.stormwatch.elite.items.AbilityItem;
import dev.stormwatch.elite.networking.EliteNetworking;
import dev.stormwatch.elite.networking.packets.PlaySoundS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.Rarity;

public class PotionBeltCharmItem extends CharmItem implements AbilityItem {

    public PotionBeltCharmItem() {
        super(new Item.Properties()
                .rarity(Rarity.UNCOMMON)
                .stacksTo(1));
    }

    @Override
    public void activateAbility(ItemStack stack, Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        boolean usedPotion = false;
        for (ItemStack inventoryStack : player.getInventory().items) {
            if (inventoryStack.getItem() instanceof PotionItem potion) {
                // FIXME: dont consume splash and lingering potions
                // TODO: dont consume negative effects?
                ItemStack leftover = potion.finishUsingItem(inventoryStack, player.level(), player);
                player.getInventory().add(leftover);
                usedPotion = true;
            }
        }
        if (usedPotion) {
            EliteNetworking.sendToPlayer(new PlaySoundS2CPacket(SoundEventIndices.POTION_BELT_ACTIVATE), serverPlayer);
        }
    }
}
