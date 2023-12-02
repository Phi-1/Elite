package dev.stormwatch.elite.client.renderers;

import dev.stormwatch.elite.Elite;
import dev.stormwatch.elite.entities.projectiles.ResonantArrow;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ResonantArrowRenderer extends ArrowRenderer<ResonantArrow> {

    public ResonantArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(ResonantArrow entity) {
        return new ResourceLocation(Elite.MOD_ID, "textures/projectiles/resonant_arrow.png");
    }

    @Override
    public boolean shouldRender(ResonantArrow resonantArrow, Frustum camera, double camX, double camY, double camZ) {
        if (!resonantArrow.isMoving()) return false;
        return super.shouldRender(resonantArrow, camera, camX, camY, camZ);
    }
}
