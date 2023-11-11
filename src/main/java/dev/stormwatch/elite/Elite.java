package dev.stormwatch.elite;

import com.mojang.logging.LogUtils;
import dev.stormwatch.elite.items.armor.ShimmeringArmorItem;
import dev.stormwatch.elite.networking.EliteNetworking;
import dev.stormwatch.elite.registry.EliteItems;
import dev.stormwatch.elite.systems.MonsterEnhancer;
import dev.stormwatch.elite.systems.PlayerEnhancer;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
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

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(MonsterEnhancer.class);
        MinecraftForge.EVENT_BUS.register(PlayerEnhancer.class);
        MinecraftForge.EVENT_BUS.register(ShimmeringArmorItem.class);

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
        }
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(EliteItems.SHIMMERING_BOOTS);
            event.accept(EliteItems.SHIMMERING_LEGGINGS);
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

    }
}
