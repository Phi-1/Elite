package dev.stormwatch.elite.registry;

import dev.stormwatch.elite.Elite;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ElitePotions {

    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, Elite.MOD_ID);

    public static final RegistryObject<Potion> SMOKE_BOMB = POTIONS.register("smoke_bomb",
            () -> new Potion(new MobEffectInstance(EliteEffects.SMOKED.get(), 140)));

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }

}
