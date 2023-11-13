package dev.stormwatch.elite.items.charms;

import dev.stormwatch.elite.capabilities.CooldownMarkerProvider;
import dev.stormwatch.elite.items.CooldownAbilityItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class CharmItem extends Item implements ICurioItem {

    public CharmItem(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        if (this instanceof CooldownAbilityItem) {
            return new CooldownMarkerProvider();
        }
        return super.initCapabilities(stack, nbt);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        // FIXME: slotContext.identifier == "elite_charm" doesnt work
        return true;
    }

    @NotNull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.AMETHYST_BLOCK_STEP, 1.0f, 1.0f);
    }
}
