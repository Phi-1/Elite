package dev.stormwatch.elite;

import com.mojang.logging.LogUtils;
import dev.stormwatch.elite.client.ClientEliteEnemyEvents;
import dev.stormwatch.elite.client.EliteInputHandler;
import dev.stormwatch.elite.client.EliteItemProperties;
import dev.stormwatch.elite.client.EliteKeyMappings;
import dev.stormwatch.elite.client.abilities.AutoAnnihilationEngineAbility;
import dev.stormwatch.elite.client.renderers.ResonantArrowRenderer;
import dev.stormwatch.elite.client.renderers.ResonantBoomRenderer;
import dev.stormwatch.elite.effects.*;
import dev.stormwatch.elite.entities.projectiles.ResonantArrow;
import dev.stormwatch.elite.items.armor.DarkIronArmorItem;
import dev.stormwatch.elite.items.armor.GildedArmorItem;
import dev.stormwatch.elite.items.armor.ShimmeringArmorItem;
import dev.stormwatch.elite.items.charms.*;
import dev.stormwatch.elite.items.weapons.HungeringBladeItem;
import dev.stormwatch.elite.items.weapons.TheHeraldItem;
import dev.stormwatch.elite.networking.EliteNetworking;
import dev.stormwatch.elite.registry.*;
import dev.stormwatch.elite.systems.*;
import dev.stormwatch.elite.systems.elites.Necromancer;
import dev.stormwatch.elite.util.PotionRecipe;
import dev.stormwatch.elite.util.TickTasks;
import dev.stormwatch.elite.util.TickTimers;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
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

import java.util.List;

@Mod(Elite.MOD_ID)
public class Elite {
    // FIXME: HIGH PRIORITY, replace all Monster references with Enemy
    // TODO: all the livinghurtevent damage modifiers may go off in weird orders, add to static class instead and execute at end of tick
    // TODO: add armor items to trimmable tags, and datagen the armor models for each trim

    public static final String MOD_ID = "elite";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Elite() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        EliteItems.register(modEventBus);
        EliteBlocks.register(modEventBus);
        EliteEffects.register(modEventBus);
        ElitePotions.register(modEventBus);
        EliteEntityTypes.register(modEventBus);
        EliteBlockEntityTypes.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(TickTimers.class);
        MinecraftForge.EVENT_BUS.register(TickTasks.class);
        MinecraftForge.EVENT_BUS.register(GameRuleSettings.class);
        MinecraftForge.EVENT_BUS.register(EliteEnemyEvents.class);
        MinecraftForge.EVENT_BUS.register(PlayerEnhancer.class);
        MinecraftForge.EVENT_BUS.register(BlockFaceClickListener.class);
        MinecraftForge.EVENT_BUS.register(Necromancer.class);
        MinecraftForge.EVENT_BUS.register(ResonantArrow.class);
        MinecraftForge.EVENT_BUS.register(CharmItem.class);
        MinecraftForge.EVENT_BUS.register(BezoarCharmItem.class);
        MinecraftForge.EVENT_BUS.register(MarksmansMedalCharmItem.class);
        MinecraftForge.EVENT_BUS.register(BrickLayerCharmItem.class);
        MinecraftForge.EVENT_BUS.register(ShimmeringMembraneCharmItem.class);
        MinecraftForge.EVENT_BUS.register(AncientWreathCharmItem.class);
        MinecraftForge.EVENT_BUS.register(HungeringBladeItem.class);
        MinecraftForge.EVENT_BUS.register(TheHeraldItem.class);
        MinecraftForge.EVENT_BUS.register(DarkIronArmorItem.class);
        MinecraftForge.EVENT_BUS.register(ShimmeringArmorItem.class);
        MinecraftForge.EVENT_BUS.register(GildedArmorItem.class);
        MinecraftForge.EVENT_BUS.register(ExpansionEffect.class);
        MinecraftForge.EVENT_BUS.register(OverloadedEffect.class);
        MinecraftForge.EVENT_BUS.register(AlchemizedEffect.class);
        MinecraftForge.EVENT_BUS.register(EmptinessEffect.class);
        MinecraftForge.EVENT_BUS.register(ShimmeringCrownPassivesEffect.class);
        MinecraftForge.EVENT_BUS.register(InvincibilityEffect.class);
        MinecraftForge.EVENT_BUS.register(MarkedEffect.class);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            MinecraftForge.EVENT_BUS.register(EliteInputHandler.class);
            MinecraftForge.EVENT_BUS.register(AutoAnnihilationEngineAbility.class);
        });

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            EliteNetworking.register();
            registerPotionRecipes();
        });
    }

    private void registerPotionRecipes() {
        BrewingRecipeRegistry.addRecipe(new PotionRecipe(EliteItems.TOXIC_KINDLING.get(), Potions.WEAKNESS, ElitePotions.SMOKE_BOMB.get()));
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(EliteItems.LESSER_BLOOD_SHARD);
            event.accept(EliteItems.GREATER_BLOOD_SHARD);
            event.accept(EliteItems.GRAND_BLOOD_SHARD);
        }
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(EliteBlocks.PEARLESCENT_CRYSTAL);
            event.accept(EliteBlocks.BLACK_FORGE);
        }
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(EliteItems.PALE_FUR);
            event.accept(EliteItems.SHIMMERING_SCALE);
            event.accept(EliteItems.DARK_IRON_SCRAP);
            event.accept(EliteItems.DARK_IRON_INGOT);
            event.accept(EliteItems.UNKNOWN_METAL_SHARD);
            event.accept(EliteItems.GILDED_PLATE);
            event.accept(EliteItems.HUNGERING_SPIRIT);
        }
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(EliteItems.HUNGERING_BLADE);
            event.accept(EliteItems.GLASS_SWORD);
            event.accept(EliteItems.THE_HERALD);
            event.accept(EliteItems.RUNE_STAFF_OF_OVERLOADING);
            event.accept(EliteItems.RUNE_STAFF_OF_ALCHEMIZING);
            event.accept(EliteItems.RUNE_STAFF_OF_CHARMING);
            event.accept(EliteItems.PHANTASM);

            event.accept(EliteItems.PALE_FUR_BOOTS);
            event.accept(EliteItems.PALE_FUR_LEGGINGS);
            event.accept(EliteItems.PALE_FUR_CHESTPLATE);
            event.accept(EliteItems.PALE_FUR_HELMET);

            event.accept(EliteItems.DARK_IRON_BOOTS);
            event.accept(EliteItems.DARK_IRON_LEGGINGS);
            event.accept(EliteItems.DARK_IRON_CHESTPLATE);
            event.accept(EliteItems.DARK_IRON_HELMET);

            event.accept(EliteItems.SHIMMERING_BOOTS);
            event.accept(EliteItems.SHIMMERING_LEGGINGS);
            event.accept(EliteItems.SHIMMERING_CHESTPLATE);
            event.accept(EliteItems.SHIMMERING_HELMET);

            event.accept(EliteItems.GILDED_BOOTS);
            event.accept(EliteItems.GILDED_LEGGINGS);
            event.accept(EliteItems.GILDED_CHESTPLATE);
            event.accept(EliteItems.GILDED_HELMET);
        }
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(EliteItems.SLEDGEHAMMER);
            event.accept(EliteItems.EXCAVATOR);
            event.accept(EliteItems.PEARLESCENT_CRYSTAL_STAFF);

            event.accept(EliteItems.PHANTOM_WING);
            event.accept(EliteItems.ELDER_EYE);
            event.accept(EliteItems.ENRAGED_WORM);
            event.accept(EliteItems.GOLD_TOOTH);
            event.accept(EliteItems.ELEMENT_115);
            event.accept(EliteItems.TECHNICIAN_GUS);
            event.accept(EliteItems.BEZOAR);
            event.accept(EliteItems.ARIADNES_THREAD);
            event.accept(EliteItems.POTION_BELT);
            event.accept(EliteItems.MARKSMANS_MEDAL);
            event.accept(EliteItems.BRICKLAYER);
            event.accept(EliteItems.ANCIENT_WREATH);
            event.accept(EliteItems.EMPTINESS);
            event.accept(EliteItems.RESONANCE);
            event.accept(EliteItems.SHIMMERING_MEMBRANE);
            event.accept(EliteItems.AUTO_ANNIHILATION_ENGINE);
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(EliteEntityTypes.RESONANT_ARROW.get(), ResonantArrowRenderer::new);
            event.registerEntityRenderer(EliteEntityTypes.RESONANT_BOOM.get(), ResonantBoomRenderer::new);
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

        @SubscribeEvent
        public static void clientPlayerTick(TickEvent.PlayerTickEvent event) {
            ClientEliteEnemyEvents.trackNearestElite(event);
        }

        // TODO: find closest among them and set as tracked mob, along with health
        // TODO: on damage check if mob is tracked elite, and update tracked health
        // TODO: draw ui healthbar, lerp from current health to new health (track both)

    }
}
