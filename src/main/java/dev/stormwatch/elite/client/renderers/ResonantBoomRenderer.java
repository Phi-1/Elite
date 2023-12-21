package dev.stormwatch.elite.client.renderers;

import dev.stormwatch.elite.Elite;
import dev.stormwatch.elite.entities.projectiles.ResonantBoom;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ResonantBoomRenderer extends EntityRenderer<ResonantBoom> {

    public ResonantBoomRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(ResonantBoom entity) {
        return new ResourceLocation(Elite.MOD_ID, "textures/projectiles/resonant_boom.png");
    }


}
