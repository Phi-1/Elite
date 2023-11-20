package dev.stormwatch.elite.items.charms;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.phys.AABB;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class Element115CharmItem extends CharmItem {

    private static final int EFFECT_DIAMETER = 9;

    public Element115CharmItem() {
        super(new Item.Properties()
                .rarity(Rarity.UNCOMMON)
                .stacksTo(1));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (!(slotContext.entity() instanceof Player player)) return;
        if (player.level().isClientSide()) return;

        // TODO: consider making this a toggle
        List<ItemEntity> items = player.level().getEntitiesOfClass(ItemEntity.class, AABB.ofSize(player.blockPosition().getCenter(), EFFECT_DIAMETER, EFFECT_DIAMETER, EFFECT_DIAMETER));
        for (ItemEntity item : items) {
            item.setDeltaMovement(item.getDeltaMovement().x, 0.2D, item.getDeltaMovement().z); // TODO: make item accelerate towards player
            item.hurtMarked = true;
        }
    }
}
