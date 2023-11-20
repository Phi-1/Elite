package dev.stormwatch.elite;

import com.mojang.logging.LogUtils;
import dev.stormwatch.elite.client.EliteInputHandler;
import dev.stormwatch.elite.client.EliteKeyMappings;
import dev.stormwatch.elite.effects.ExpansionEffect;
import dev.stormwatch.elite.items.CooldownAbilityItem;
import dev.stormwatch.elite.items.armor.ShimmeringArmorItem;
import dev.stormwatch.elite.items.charms.BezoarCharmItem;
import dev.stormwatch.elite.items.charms.CharmItem;
import dev.stormwatch.elite.items.weapons.HungeringBladeItem;
import dev.stormwatch.elite.networking.EliteNetworking;
import dev.stormwatch.elite.registry.EliteEffects;
import dev.stormwatch.elite.registry.EliteEntityTypes;
import dev.stormwatch.elite.registry.EliteItems;
import dev.stormwatch.elite.systems.GameRuleSettings;
import dev.stormwatch.elite.systems.MonsterEnhancer;
import dev.stormwatch.elite.systems.PlayerEnhancer;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Elite.MOD_ID)
public class Elite {

    public static final String MOD_ID = "elite";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Elite() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        EliteItems.register(modEventBus);
        EliteEffects.register(modEventBus);
        EliteEntityTypes.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(GameRuleSettings.class);
        MinecraftForge.EVENT_BUS.register(MonsterEnhancer.class);
        MinecraftForge.EVENT_BUS.register(PlayerEnhancer.class);
        MinecraftForge.EVENT_BUS.register(CharmItem.class);
        MinecraftForge.EVENT_BUS.register(BezoarCharmItem.class);
        MinecraftForge.EVENT_BUS.register(HungeringBladeItem.class);
        MinecraftForge.EVENT_BUS.register(ExpansionEffect.class);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            MinecraftForge.EVENT_BUS.register(EliteInputHandler.class);
        });

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            EliteNetworking.register();
        });
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(EliteItems.SHIMMERING_SCALE);
            event.accept(EliteItems.HUNGERING_SPIRIT);
        }
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(EliteItems.HUNGERING_BLADE);

            event.accept(EliteItems.SHIMMERING_BOOTS);
            event.accept(EliteItems.SHIMMERING_LEGGINGS);
            event.accept(EliteItems.SHIMMERING_CHESTPLATE);
            event.accept(EliteItems.SHIMMERING_HELMET);
        }
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(EliteItems.PHANTOM_WING);
            event.accept(EliteItems.ELDER_EYE);
            event.accept(EliteItems.ENRAGED_WORM);
            event.accept(EliteItems.ELEMENT_115);
            event.accept(EliteItems.TECHNICIAN_GUS);
            event.accept(EliteItems.BEZOAR);
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(EliteKeyMappings.CHARM_ABILITY_1.get());
            event.register(EliteKeyMappings.CHARM_ABILITY_2.get());
            event.register(EliteKeyMappings.CHARM_ABILITY_3.get());
        }

    }
}
