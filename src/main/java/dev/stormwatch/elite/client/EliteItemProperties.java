package dev.stormwatch.elite.client;

import dev.stormwatch.elite.registry.EliteItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class EliteItemProperties {

    public static void register() {
        registerBows();
    }

    private static void registerBows() {
        registerBowProperties(EliteItems.PHANTASM.get());
    }

    private static void registerBowProperties(Item bow) {
        ItemProperties.register(bow, new ResourceLocation("pull"), (stack, level, living, p_174638_) -> {
            if (living == null) {
                return 0.0F;
            } else {
                return living.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration() - living.getUseItemRemainingTicks()) / 20.0F;
            }
        });
        ItemProperties.register(bow, new ResourceLocation("pulling"), (stack, level, living, p_174633_) -> {
            return living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0F : 0.0F;
        });
    }

}
