package dev.stormwatch.elite.registry;

import dev.stormwatch.elite.Elite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class EliteTags {
    public static class Blocks {
        public static final TagKey<Block> CONCRETE_POWDER = BlockTags.create(new ResourceLocation(Elite.MOD_ID, "concrete_powder"));
    }
}
