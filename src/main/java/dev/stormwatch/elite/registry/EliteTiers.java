package dev.stormwatch.elite.registry;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public enum EliteTiers implements Tier {
    GLASS(3, 750, 12.0F, 0.0F, 1,
            () -> Ingredient.of(EliteItems.UNKNOWN_METAL_SHARD.get()),
            () -> BlockTags.NEEDS_DIAMOND_TOOL
    ),
    SLEDGEHAMMER(3, 2000, 2.0F, 4.0F, 10,
            () -> Ingredient.of(EliteItems.UNKNOWN_METAL_SHARD.get()),
            () -> BlockTags.NEEDS_DIAMOND_TOOL
    ),
    EXCAVATOR(3, 2000, 2.0F, 4.0F, 20,
            () -> Ingredient.of(EliteItems.SHIMMERING_SCALE.get()),
            () -> BlockTags.NEEDS_DIAMOND_TOOL
    );

    private final int level;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final LazyLoadedValue<Ingredient> repairIngredient;
    private final Supplier<TagKey<Block>> blockTag;

    EliteTiers(int level, int uses, float speed, float damage, int enchantmentValue, Supplier<Ingredient> repairIngredient, Supplier<TagKey<Block>> blockTag) {
        this.level = level;
        this.uses = uses;
        this.speed = speed;
        this.damage = damage;
        this.enchantmentValue = enchantmentValue;
        this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
        this.blockTag = blockTag;
    }

    public int getUses() {
        return this.uses;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getAttackDamageBonus() {
        return this.damage;
    }

    public int getLevel() {
        return this.level;
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Nullable
    public TagKey<Block> getTag() {
        return this.blockTag.get();
    }

}
