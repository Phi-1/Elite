package dev.stormwatch.elite.items;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface AbilityItem {

    void activateAbility(ItemStack stack, Player player);
    // TODO: get activation sound

}
