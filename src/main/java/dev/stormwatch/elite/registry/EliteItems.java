package dev.stormwatch.elite.registry;

import dev.stormwatch.elite.Elite;
import dev.stormwatch.elite.items.armor.ShimmeringArmorItem;
import dev.stormwatch.elite.items.charms.*;
import dev.stormwatch.elite.items.weapons.HungeringBladeItem;
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
    public static final RegistryObject<Item> HUNGERING_SPIRIT = ITEMS.register("hungering_spirit",
            () -> new Item(new Item.Properties()));

    // Weapons
    public static final RegistryObject<Item> HUNGERING_BLADE = ITEMS.register("hungering_blade",
            HungeringBladeItem::new);

    // Armor
    public static final RegistryObject<Item> SHIMMERING_BOOTS = ITEMS.register("shimmering_boots",
            () -> new ShimmeringArmorItem(ArmorItem.Type.BOOTS));
    public static final RegistryObject<Item> SHIMMERING_LEGGINGS = ITEMS.register("shimmering_leggings",
            () -> new ShimmeringArmorItem(ArmorItem.Type.LEGGINGS));
    public static final RegistryObject<Item> SHIMMERING_CHESTPLATE = ITEMS.register("shimmering_chestplate",
            () -> new ShimmeringArmorItem(ArmorItem.Type.CHESTPLATE));
    public static final RegistryObject<Item> SHIMMERING_HELMET = ITEMS.register("shimmering_helmet",
            () -> new ShimmeringArmorItem(ArmorItem.Type.HELMET));

    // Charms
    public static final RegistryObject<Item> ENRAGED_WORM = ITEMS.register("enraged_worm",
            EnragedWormCharmItem::new);
    public static final RegistryObject<Item> PHANTOM_WING = ITEMS.register("phantom_wing",
            PhantomWingCharmItem::new);
    public static final RegistryObject<Item> ELDER_EYE = ITEMS.register("elder_eye",
            ElderEyeCharmItem::new);
    public static final RegistryObject<Item> ELEMENT_115 = ITEMS.register("element_115",
            Element115CharmItem::new);
    public static final RegistryObject<Item> TECHNICIAN_GUS = ITEMS.register("technician_gus",
            TechnicianGusCharmItem::new);
    public static final RegistryObject<Item> BEZOAR = ITEMS.register("bezoar",
            BezoarCharmItem::new);


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
