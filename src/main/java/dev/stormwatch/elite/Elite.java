package dev.stormwatch.elite;

import com.mojang.logging.LogUtils;
import dev.stormwatch.elite.client.EliteInputHandler;
import dev.stormwatch.elite.client.EliteItemProperties;
import dev.stormwatch.elite.client.EliteKeyMappings;
import dev.stormwatch.elite.client.renderers.ResonantArrowRenderer;
import dev.stormwatch.elite.effects.AlchemizedEffect;
import dev.stormwatch.elite.effects.ExpansionEffect;
import dev.stormwatch.elite.effects.OverloadedEffect;
import dev.stormwatch.elite.entities.projectiles.ResonantArrow;
import dev.stormwatch.elite.items.armor.DarkIronArmorItem;
import dev.stormwatch.elite.items.armor.GildedArmorItem;
import dev.stormwatch.elite.items.charms.BezoarCharmItem;
import dev.stormwatch.elite.items.charms.BrickLayerCharmItem;
import dev.stormwatch.elite.items.charms.CharmItem;
import dev.stormwatch.elite.items.charms.MarksmansMedalCharmItem;
import dev.stormwatch.elite.items.weapons.HungeringBladeItem;
import dev.stormwatch.elite.items.weapons.TheHeraldItem;
import dev.stormwatch.elite.networking.EliteNetworking;
import dev.stormwatch.elite.registry.*;
import dev.stormwatch.elite.systems.BlockFaceClickListener;
import dev.stormwatch.elite.systems.GameRuleSettings;
import dev.stormwatch.elite.systems.MonsterEnhancer;
import dev.stormwatch.elite.systems.PlayerEnhancer;
import dev.stormwatch.elite.util.TickTimers;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Elite.MOD_ID)
public class Elite {
    // FIXME: HIGH PRIORITY, replace all Monster references with Enemy

    public static final String MOD_ID = "elite";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Elite() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        EliteItems.register(modEventBus);
        EliteBlocks.register(modEventBus);
        EliteEffects.register(modEventBus);
        EliteEntityTypes.register(modEventBus);
        EliteBlockEntityTypes.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(TickTimers.class);
        MinecraftForge.EVENT_BUS.register(GameRuleSettings.class);
        MinecraftForge.EVENT_BUS.register(MonsterEnhancer.class);
        MinecraftForge.EVENT_BUS.register(PlayerEnhancer.class);
        MinecraftForge.EVENT_BUS.register(BlockFaceClickListener.class);
        MinecraftForge.EVENT_BUS.register(ResonantArrow.class);
        MinecraftForge.EVENT_BUS.register(CharmItem.class);
        MinecraftForge.EVENT_BUS.register(BezoarCharmItem.class);
        MinecraftForge.EVENT_BUS.register(MarksmansMedalCharmItem.class);
        MinecraftForge.EVENT_BUS.register(BrickLayerCharmItem.class);
        MinecraftForge.EVENT_BUS.register(HungeringBladeItem.class);
        MinecraftForge.EVENT_BUS.register(TheHeraldItem.class);
        MinecraftForge.EVENT_BUS.register(DarkIronArmorItem.class);
        MinecraftForge.EVENT_BUS.register(GildedArmorItem.class);
        MinecraftForge.EVENT_BUS.register(ExpansionEffect.class);
        MinecraftForge.EVENT_BUS.register(OverloadedEffect.class);
        MinecraftForge.EVENT_BUS.register(AlchemizedEffect.class);

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
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(EliteItems.LESSER_BLOOD_SHARD);
            event.accept(EliteItems.GREATER_BLOOD_SHARD);
            event.accept(EliteItems.GRAND_BLOOD_SHARD);
        }
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(EliteBlocks.PEARLESCENT_CRYSTAL);
        }
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(EliteItems.SHIMMERING_SCALE);
            event.accept(EliteItems.DARK_IRON_SCRAP);
            event.accept(EliteItems.DARK_IRON_INGOT);
            event.accept(EliteItems.GILDED_PLATE);
            event.accept(EliteItems.HUNGERING_SPIRIT);
        }
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(EliteItems.HUNGERING_BLADE);
            event.accept(EliteItems.THE_HERALD);
            event.accept(EliteItems.RUNE_STAFF_OF_OVERLOADING);
            event.accept(EliteItems.RUNE_STAFF_OF_ALCHEMIZING);
            event.accept(EliteItems.PHANTASM);

            event.accept(EliteItems.SHIMMERING_BOOTS);
            event.accept(EliteItems.SHIMMERING_LEGGINGS);
            event.accept(EliteItems.SHIMMERING_CHESTPLATE);
            event.accept(EliteItems.SHIMMERING_HELMET);

            event.accept(EliteItems.DARK_IRON_BOOTS);
            event.accept(EliteItems.DARK_IRON_LEGGINGS);
            event.accept(EliteItems.DARK_IRON_CHESTPLATE);
            event.accept(EliteItems.DARK_IRON_HELMET);

            event.accept(EliteItems.GILDED_BOOTS);
            event.accept(EliteItems.GILDED_LEGGINGS);
            event.accept(EliteItems.GILDED_CHESTPLATE);
            event.accept(EliteItems.GILDED_HELMET);
        }
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(EliteItems.SLEDGEHAMMER);
            event.accept(EliteItems.PEARLESCENT_CRYSTAL_STAFF);

            event.accept(EliteItems.PHANTOM_WING);
            event.accept(EliteItems.ELDER_EYE);
            event.accept(EliteItems.ENRAGED_WORM);
            event.accept(EliteItems.ELEMENT_115);
            event.accept(EliteItems.TECHNICIAN_GUS);
            event.accept(EliteItems.BEZOAR);
            event.accept(EliteItems.ARIADNES_THREAD);
            event.accept(EliteItems.POTION_BELT);
            event.accept(EliteItems.MARKSMANS_MEDAL);
            event.accept(EliteItems.BRICKLAYER);
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(EliteEntityTypes.RESONANT_ARROW.get(), ResonantArrowRenderer::new);
        }

        @SubscribeEvent
        public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(EliteKeyMappings.CHARM_ABILITY_1.get());
            event.register(EliteKeyMappings.CHARM_ABILITY_2.get());
            event.register(EliteKeyMappings.CHARM_ABILITY_3.get());
        }

        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            EliteItemProperties.register();
        }

    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientForgeEvents {



    }
}
