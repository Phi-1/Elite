package dev.stormwatch.elite.registry;

import dev.stormwatch.elite.Elite;
import dev.stormwatch.elite.doc.RuneTypes;
import dev.stormwatch.elite.items.armor.DarkIronArmorItem;
import dev.stormwatch.elite.items.armor.GildedArmorItem;
import dev.stormwatch.elite.items.armor.ShimmeringArmorItem;
import dev.stormwatch.elite.items.charms.*;
import dev.stormwatch.elite.items.tools.PearlescentCrystalStaffItem;
import dev.stormwatch.elite.items.tools.SledgeHammerPickaxeItem;
import dev.stormwatch.elite.items.weapons.BaseRuneStaffItem;
import dev.stormwatch.elite.items.weapons.HungeringBladeItem;
import dev.stormwatch.elite.items.weapons.PhantasmItem;
import dev.stormwatch.elite.items.weapons.TheHeraldItem;
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
    public static final RegistryObject<Item> DARK_IRON_SCRAP = ITEMS.register("dark_iron_scrap",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DARK_IRON_INGOT = ITEMS.register("dark_iron_ingot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GILDED_PLATE = ITEMS.register("gilded_plate",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HUNGERING_SPIRIT = ITEMS.register("hungering_spirit",
            () -> new Item(new Item.Properties()));

    // Tools
    public static final RegistryObject<Item> SLEDGEHAMMER = ITEMS.register("sledgehammer",
            SledgeHammerPickaxeItem::new);
    public static final RegistryObject<Item> PEARLESCENT_CRYSTAL_STAFF = ITEMS.register("pearlescent_crystal_staff",
            PearlescentCrystalStaffItem::new);

    // Weapons
    public static final RegistryObject<Item> HUNGERING_BLADE = ITEMS.register("hungering_blade",
            HungeringBladeItem::new);
    public static final RegistryObject<Item> THE_HERALD = ITEMS.register("the_herald",
            TheHeraldItem::new);
    public static final RegistryObject<Item> RUNE_STAFF_OF_OVERLOADING = ITEMS.register("rune_staff_of_overloading",
            () -> new BaseRuneStaffItem(RuneTypes.OVERLOADING));
    public static final RegistryObject<Item> RUNE_STAFF_OF_ALCHEMIZING = ITEMS.register("rune_staff_of_alchemizing",
            () -> new BaseRuneStaffItem(RuneTypes.ALCHEMIZING));
    // TODO: unknown staff
    public static final RegistryObject<Item> PHANTASM = ITEMS.register("phantasm",
            PhantasmItem::new);

    // Armor
    public static final RegistryObject<Item> SHIMMERING_BOOTS = ITEMS.register("shimmering_boots",
            () -> new ShimmeringArmorItem(ArmorItem.Type.BOOTS));
    public static final RegistryObject<Item> SHIMMERING_LEGGINGS = ITEMS.register("shimmering_leggings",
            () -> new ShimmeringArmorItem(ArmorItem.Type.LEGGINGS));
    public static final RegistryObject<Item> SHIMMERING_CHESTPLATE = ITEMS.register("shimmering_chestplate",
            () -> new ShimmeringArmorItem(ArmorItem.Type.CHESTPLATE));
    public static final RegistryObject<Item> SHIMMERING_HELMET = ITEMS.register("shimmering_helmet",
            () -> new ShimmeringArmorItem(ArmorItem.Type.HELMET));

    public static final RegistryObject<Item> DARK_IRON_BOOTS = ITEMS.register("dark_iron_boots",
            () -> new DarkIronArmorItem(ArmorItem.Type.BOOTS));
    public static final RegistryObject<Item> DARK_IRON_LEGGINGS = ITEMS.register("dark_iron_leggings",
            () -> new DarkIronArmorItem(ArmorItem.Type.LEGGINGS));
    public static final RegistryObject<Item> DARK_IRON_CHESTPLATE = ITEMS.register("dark_iron_chestplate",
            () -> new DarkIronArmorItem(ArmorItem.Type.CHESTPLATE));
    public static final RegistryObject<Item> DARK_IRON_HELMET = ITEMS.register("dark_iron_helmet",
            () -> new DarkIronArmorItem(ArmorItem.Type.HELMET));

    public static final RegistryObject<Item> GILDED_BOOTS = ITEMS.register("gilded_boots",
            () -> new GildedArmorItem(ArmorItem.Type.BOOTS));
    public static final RegistryObject<Item> GILDED_LEGGINGS = ITEMS.register("gilded_leggings",
            () -> new GildedArmorItem(ArmorItem.Type.LEGGINGS));
    public static final RegistryObject<Item> GILDED_CHESTPLATE = ITEMS.register("gilded_chestplate",
            () -> new GildedArmorItem(ArmorItem.Type.CHESTPLATE));
    public static final RegistryObject<Item> GILDED_HELMET = ITEMS.register("gilded_helmet",
            () -> new GildedArmorItem(ArmorItem.Type.HELMET));

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
    public static final RegistryObject<Item> ARIADNES_THREAD = ITEMS.register("ariadnes_thread",
            AriadnesThreadCharmItem::new);
    public static final RegistryObject<Item> POTION_BELT = ITEMS.register("potion_belt",
            PotionBeltCharmItem::new);
    public static final RegistryObject<Item> MARKSMANS_MEDAL = ITEMS.register("marksmans_medal",
            MarksmansMedalCharmItem::new);


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
