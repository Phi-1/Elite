package dev.stormwatch.elite.util;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class InventoryUtil {

    public static boolean hasArmorEquipped(Player player, Item armorItem, int slot) {
        return player.getInventory().getArmor(slot).is(armorItem);
    }

    public static boolean isHoldingItem(Player player, Item item) {
        return player.getItemInHand(InteractionHand.MAIN_HAND).is(item) || player.getItemInHand(InteractionHand.OFF_HAND).is(item);
    }

    @Nullable
    public static <T extends Item> ItemStack getHeldItemOfType(Player player, Class<T> type) {
        if (type.isInstance(player.getItemInHand(InteractionHand.MAIN_HAND).getItem())) {
            return player.getItemInHand(InteractionHand.MAIN_HAND);
        }
        if (type.isInstance(player.getItemInHand(InteractionHand.OFF_HAND).getItem())) {
            return player.getItemInHand(InteractionHand.OFF_HAND);
        }
        return null;
    }

}
