package dev.stormwatch.elite.registry;

import dev.stormwatch.elite.Elite;
import dev.stormwatch.elite.effects.BleedEffect;
import dev.stormwatch.elite.effects.ExecutionEffect;
import dev.stormwatch.elite.effects.ExpansionEffect;
import dev.stormwatch.elite.effects.SwiftSwimEffect;
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

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }

}
