package dev.stormwatch.elite.items.armor;

import dev.stormwatch.elite.registry.EliteArmorMaterials;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class GildedArmorItem extends ArmorItem {

    public GildedArmorItem(Type type) {
        super(EliteArmorMaterials.GILDED, type, new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.EPIC));
    }

}
