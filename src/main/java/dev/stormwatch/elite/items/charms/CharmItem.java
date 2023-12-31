package dev.stormwatch.elite.items.charms;

import dev.stormwatch.elite.capabilities.CooldownMarkerProvider;
import dev.stormwatch.elite.capabilities.ModeSelectorMarkerProvider;
import dev.stormwatch.elite.capabilities.ToggleMarkerProvider;
import dev.stormwatch.elite.items.CooldownAbilityItem;
import dev.stormwatch.elite.items.ModeAbilityItem;
import dev.stormwatch.elite.items.ToggleAbilityItem;
import dev.stormwatch.elite.util.InventoryUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.event.CurioEquipEvent;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.Objects;

public class CharmItem extends Item implements ICurioItem {

    public CharmItem(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        if (this instanceof CooldownAbilityItem) {
            return new CooldownMarkerProvider();
        } else if (this instanceof ToggleAbilityItem) {
            return new ToggleMarkerProvider();
        } else if (this instanceof ModeAbilityItem) {
            return new ModeSelectorMarkerProvider(((ModeAbilityItem) this).getModes());
        }
        return super.initCapabilities(stack, nbt);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        if (!(slotContext.entity() instanceof Player player)
                || !(stack.getItem() instanceof CharmItem charm)) return false;
        return Objects.equals(slotContext.identifier(), "elite_charm")
                && !InventoryUtil.hasCharmEquipped(player, charm);
    }

    @NotNull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.AMETHYST_BLOCK_STEP, 1.0f, 1.0f);
    }
}
