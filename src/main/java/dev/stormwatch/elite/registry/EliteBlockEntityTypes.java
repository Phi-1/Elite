package dev.stormwatch.elite.registry;

import dev.stormwatch.elite.Elite;
import dev.stormwatch.elite.blocks.entities.BlackForgeBlockEntity;
import dev.stormwatch.elite.blocks.entities.PearlescentCrystalBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EliteBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Elite.MOD_ID);

    public static final RegistryObject<BlockEntityType<PearlescentCrystalBlockEntity>> PEARLESCENT_CRYSTAL = BLOCK_ENTITY_TYPES.register("pearlescent_crystal",
            () -> BlockEntityType.Builder.of(PearlescentCrystalBlockEntity::new, EliteBlocks.PEARLESCENT_CRYSTAL.get()).build(null));
    public static final RegistryObject<BlockEntityType<BlackForgeBlockEntity>> BLACK_FORGE = BLOCK_ENTITY_TYPES.register("black_forge",
            () -> BlockEntityType.Builder.of(BlackForgeBlockEntity::new, EliteBlocks.BLACK_FORGE.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }

}
