package dev.stormwatch.elite.items.charms;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.storage.loot.LootContext;
import top.theillusivec4.curios.api.SlotContext;

public class GoldToothCharmItem extends CharmItem {

    // TODO: but so are the worms
    private static final int FORTUNE_LEVEL = 1;

    public GoldToothCharmItem() {
        super(new Item.Properties()
                .rarity(Rarity.UNCOMMON)
                .stacksTo(1));
    }

    @Override
    public int getFortuneLevel(SlotContext slotContext, LootContext lootContext, ItemStack stack) {
        return FORTUNE_LEVEL + super.getFortuneLevel(slotContext, lootContext, stack);
    }
}
