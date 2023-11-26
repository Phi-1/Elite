package dev.stormwatch.elite.util;

import dev.stormwatch.elite.doc.SoundEventIndices;
import dev.stormwatch.elite.items.charms.CharmItem;
import dev.stormwatch.elite.networking.EliteNetworking;
import dev.stormwatch.elite.networking.packets.PlaySoundS2CPacket;
import dev.stormwatch.elite.registry.EliteItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

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

    @Nullable
    public static <T extends CharmItem> ItemStack getEquippedCharmOfType(Player player, Class<T> type) {
        ICuriosItemHandler curios = CuriosApi.getCuriosInventory(player).orElseThrow(() -> new IllegalStateException("Player does not have a curios inventory"));
        ICurioStacksHandler charms = curios.getCurios().get("elite_charm");
        for (int slot = 0; slot < charms.getSlots(); slot++) {
            ItemStack stack = charms.getStacks().getStackInSlot(slot);
            if (type.isInstance(stack.getItem())) {
                return stack;
            }
        }
        return null;
    }

    public static boolean hasCharmEquipped(Player player, CharmItem charm) {
        ICuriosItemHandler curios = CuriosApi.getCuriosInventory(player).orElseThrow(() -> new IllegalStateException("Player does not have a curios inventory"));
        ICurioStacksHandler charms = curios.getCurios().get("elite_charm");
        for (int slot = 0; slot < charms.getSlots(); slot++) {
            ItemStack stack = charms.getStacks().getStackInSlot(slot);
            if (stack.is(charm)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static ItemStack getItemInInventory(Player player, Item item) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(item)) {
                return stack;
            }
        }
        return null;
    }

    @Nullable
    public static <T extends Item> ItemStack getItemOfTypeInInventory(Player player, Class<T> type) {
        for (ItemStack stack : player.getInventory().items) {
            if (type.isInstance(stack.getItem())) {
                return stack;
            }
        }
        return null;
    }

    public static boolean hasRoomForItemInInventory(Player player, ItemStack item) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.isEmpty()) return true;
            if (ItemStack.isSameItemSameTags(item, stack) && stack.getCount() < stack.getMaxStackSize()) {
                return true;
            }
        }
        return false;
    }

}
