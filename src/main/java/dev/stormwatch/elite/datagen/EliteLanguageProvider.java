package dev.stormwatch.elite.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class EliteLanguageProvider extends LanguageProvider {

    public EliteLanguageProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    @Override
    protected void addTranslations() {
        // TODO: items
        // TODO: elite monster names
        // TODO: hotkeys
        // TODO: brick layer modes
    }
}
