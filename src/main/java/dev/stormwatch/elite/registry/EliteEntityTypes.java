package dev.stormwatch.elite.registry;

import dev.stormwatch.elite.Elite;
import dev.stormwatch.elite.entities.GuardianPet;
import dev.stormwatch.elite.entities.projectiles.ResonantArrow;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EliteEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Elite.MOD_ID);

    public static final RegistryObject<EntityType<ResonantArrow>> RESONANT_ARROW = ENTITY_TYPES.register("resonant_arrow",
            () -> EntityType.Builder.of(ResonantArrow::new, MobCategory.MISC)
                    .sized(1f, 1f).build("resonant_arrow"));
    public static final RegistryObject<EntityType<GuardianPet>> GUARDIAN_PET = ENTITY_TYPES.register("guardian_pet",
            () -> EntityType.Builder.of(GuardianPet::new, MobCategory.WATER_CREATURE)
            .sized(1f, 1f).build("guardian_pet"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}
