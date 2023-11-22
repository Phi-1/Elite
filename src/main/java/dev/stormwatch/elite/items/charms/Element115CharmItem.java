package dev.stormwatch.elite.items.charms;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class Element115CharmItem extends CharmItem {

    private static final int EFFECT_DIAMETER = 11;
    private static final double PULL_SPEED = 0.4;

    public Element115CharmItem() {
        super(new Item.Properties()
                .rarity(Rarity.UNCOMMON)
                .stacksTo(1));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        // TODO: only work if there's room in inventory, else this just eternally hovers around player
        if (!(slotContext.entity() instanceof Player player)) return;
        if (player.level().isClientSide()) return;

        List<ItemEntity> items = player.level().getEntitiesOfClass(ItemEntity.class, AABB.ofSize(player.blockPosition().getCenter(), EFFECT_DIAMETER, EFFECT_DIAMETER, EFFECT_DIAMETER));
        for (ItemEntity item : items) {
            Vec3 direction = player.position().subtract(item.position()).normalize();
            Vec3 speed = direction.scale(PULL_SPEED);
            item.setDeltaMovement(item.getDeltaMovement().x + speed.x, item.getDeltaMovement().y + speed.y, item.getDeltaMovement().z + speed.z);
            item.hurtMarked = true;
        }
    }
}
