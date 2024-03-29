package dev.stormwatch.elite.registry;

import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.function.Supplier;

public enum EliteArmorMaterials implements StringRepresentable, ArmorMaterial {

    PALE_FUR("pale_fur", 16, Util.make(new EnumMap<>(ArmorItem.Type.class), (protection) -> {
        protection.put(ArmorItem.Type.BOOTS, 2);
        protection.put(ArmorItem.Type.LEGGINGS, 3);
        protection.put(ArmorItem.Type.CHESTPLATE, 4);
        protection.put(ArmorItem.Type.HELMET, 2);
    }), 17, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> {
        return Ingredient.of(EliteItems.PALE_FUR.get());
    }),

    DARK_IRON("dark_iron", 20, Util.make(new EnumMap<>(ArmorItem.Type.class), (protection) -> {
        protection.put(ArmorItem.Type.BOOTS, 2);
        protection.put(ArmorItem.Type.LEGGINGS, 5);
        protection.put(ArmorItem.Type.CHESTPLATE, 6);
        protection.put(ArmorItem.Type.HELMET, 3);
    }), 8, SoundEvents.ARMOR_EQUIP_CHAIN, 1.0F, 0.1F, () -> {
        return Ingredient.of(EliteItems.DARK_IRON_SCRAP.get());
    }),

    SHIMMERING("shimmering", 30, Util.make(new EnumMap<>(ArmorItem.Type.class), (protection) -> {
        protection.put(ArmorItem.Type.BOOTS, 3);
        protection.put(ArmorItem.Type.LEGGINGS, 6);
        protection.put(ArmorItem.Type.CHESTPLATE, 8);
        protection.put(ArmorItem.Type.HELMET, 3);
    }), 20, SoundEvents.ARMOR_EQUIP_TURTLE, 2.0F, 0.0F, () -> {
        return Ingredient.of(EliteItems.SHIMMERING_SCALE.get());
    }),

    GILDED("gilded", 40, Util.make(new EnumMap<>(ArmorItem.Type.class), (protection) -> {
        protection.put(ArmorItem.Type.BOOTS, 3);
        protection.put(ArmorItem.Type.LEGGINGS, 6);
        protection.put(ArmorItem.Type.CHESTPLATE, 8);
        protection.put(ArmorItem.Type.HELMET, 3);
    }), 30, SoundEvents.ARMOR_EQUIP_GOLD, 0.0F, 0.0F, () -> {
        return Ingredient.of(EliteItems.GILDED_PLATE.get());
    });





//    Copied from ArmorMaterials

    public static final StringRepresentable.EnumCodec<EliteArmorMaterials> CODEC = StringRepresentable.fromEnum(EliteArmorMaterials::values);
    private static final EnumMap<ArmorItem.Type, Integer> HEALTH_FUNCTION_FOR_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266653_) -> {
        p_266653_.put(ArmorItem.Type.BOOTS, 13);
        p_266653_.put(ArmorItem.Type.LEGGINGS, 15);
        p_266653_.put(ArmorItem.Type.CHESTPLATE, 16);
        p_266653_.put(ArmorItem.Type.HELMET, 11);
    });
    private final String name;
    private final int durabilityMultiplier;
    private final EnumMap<ArmorItem.Type, Integer> protectionFunctionForType;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    private EliteArmorMaterials(String pName, int pDurabilityMultiplier, EnumMap<ArmorItem.Type, Integer> pProtectionFunctionForType, int pEnchantmentValue, SoundEvent pSound, float pToughness, float pKnockbackResistance, Supplier<Ingredient> pRepairIngredient) {
        this.name = pName;
        this.durabilityMultiplier = pDurabilityMultiplier;
        this.protectionFunctionForType = pProtectionFunctionForType;
        this.enchantmentValue = pEnchantmentValue;
        this.sound = pSound;
        this.toughness = pToughness;
        this.knockbackResistance = pKnockbackResistance;
        this.repairIngredient = new LazyLoadedValue<>(pRepairIngredient);
    }

    public int getDurabilityForType(ArmorItem.Type pType) {
        return HEALTH_FUNCTION_FOR_TYPE.get(pType) * this.durabilityMultiplier;
    }

    public int getDefenseForType(ArmorItem.Type pType) {
        return this.protectionFunctionForType.get(pType);
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public SoundEvent getEquipSound() {
        return this.sound;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    public String getName() {
        return this.name;
    }

    public float getToughness() {
        return this.toughness;
    }

    /**
     * Gets the percentage of knockback resistance provided by armor of the material.
     */
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }

    public String getSerializedName() {
        return this.name;
    }
}
