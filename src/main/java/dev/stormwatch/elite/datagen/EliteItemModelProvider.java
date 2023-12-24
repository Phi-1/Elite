package dev.stormwatch.elite.datagen;

import dev.stormwatch.elite.Elite;
import dev.stormwatch.elite.registry.EliteItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class EliteItemModelProvider extends ItemModelProvider {

    public EliteItemModelProvider(PackOutput output, String modID, ExistingFileHelper existingFileHelper) {
        super(output, modID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(EliteItems.ANCIENT_WREATH);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Elite.MOD_ID, "item/" + item.getId().getPath()));
    }

}
