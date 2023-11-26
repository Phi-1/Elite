package dev.stormwatch.elite.client.renderers;

import dev.stormwatch.elite.Elite;
import dev.stormwatch.elite.entities.projectiles.ResonantArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ResonantArrowRenderer extends ArrowRenderer<ResonantArrow> {

    public ResonantArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(ResonantArrow pEntity) {
        return new ResourceLocation(Elite.MOD_ID, "textures/projectiles/resonant_arrow.png");
    }

}
