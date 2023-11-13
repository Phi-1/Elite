package dev.stormwatch.elite.registry;

import dev.stormwatch.elite.Elite;
import dev.stormwatch.elite.items.armor.ShimmeringArmorItem;
import dev.stormwatch.elite.items.charms.PhantomWingCharmItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EliteItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Elite.MOD_ID);

    // Materials
    public static final RegistryObject<Item> SHIMMERING_SCALE = ITEMS.register("shimmering_scale",
            () -> new Item(new Item.Properties()));

    // Armor
    public static final RegistryObject<Item> SHIMMERING_BOOTS = ITEMS.register("shimmering_boots",
            () -> new ShimmeringArmorItem(ArmorItem.Type.BOOTS));
    public static final RegistryObject<Item> SHIMMERING_LEGGINGS = ITEMS.register("shimmering_leggings",
            () -> new ShimmeringArmorItem(ArmorItem.Type.LEGGINGS));

    // Charms
    public static final RegistryObject<Item> PHANTOM_WING = ITEMS.register("phantom_wing",
            PhantomWingCharmItem::new);


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
