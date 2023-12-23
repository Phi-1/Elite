package dev.stormwatch.elite.registry;

import dev.stormwatch.elite.Elite;
import dev.stormwatch.elite.effects.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EliteEffects {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Elite.MOD_ID);

    public static final RegistryObject<MobEffect> EXPANSION = EFFECTS.register("expansion", ExpansionEffect::new);
    public static final RegistryObject<MobEffect> SWIFT_SWIM = EFFECTS.register("swift_swim", SwiftSwimEffect::new);
    public static final RegistryObject<MobEffect> BLEED = EFFECTS.register("bleed", BleedEffect::new);
    public static final RegistryObject<MobEffect> EXECUTION = EFFECTS.register("execution", ExecutionEffect::new);
    public static final RegistryObject<MobEffect> OVERLOADED = EFFECTS.register("overloaded", OverloadedEffect::new);
    public static final RegistryObject<MobEffect> ALCHEMIZED = EFFECTS.register("alchemized", AlchemizedEffect::new);
    public static final RegistryObject<MobEffect> CHARMED = EFFECTS.register("charmed", CharmedEffect::new);
    public static final RegistryObject<MobEffect> EMPTINESS = EFFECTS.register("emptiness", EmptinessEffect::new);
    public static final RegistryObject<MobEffect> SHIMMERING_CROWN_PASSIVES = EFFECTS.register("shimmering_crown_passives", ShimmeringCrownPassivesEffect::new);

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }

}
