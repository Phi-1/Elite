package dev.stormwatch.elite.registry;

import dev.stormwatch.elite.Elite;
import dev.stormwatch.elite.entities.projectiles.ResonantArrow;
import dev.stormwatch.elite.entities.projectiles.ResonantBoom;
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
    public static final RegistryObject<EntityType<ResonantBoom>> RESONANT_BOOM = ENTITY_TYPES.register("resonant_boom",
            () -> EntityType.Builder.of(ResonantBoom::new, MobCategory.MISC)
                    .sized(1f, 1f).build("resonant_arrow"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}
