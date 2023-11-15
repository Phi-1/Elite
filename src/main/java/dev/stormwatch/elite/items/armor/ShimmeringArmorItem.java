package dev.stormwatch.elite.items.armor;

import dev.stormwatch.elite.client.abilities.ClientShimmeringArmorAbilities;
import dev.stormwatch.elite.doc.SlotIndices;
import dev.stormwatch.elite.doc.TickRates;
import dev.stormwatch.elite.registry.EliteArmorMaterials;
import dev.stormwatch.elite.registry.EliteEffects;
import dev.stormwatch.elite.registry.EliteItems;
import dev.stormwatch.elite.util.AttributeUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

// FIXME: separate client stuff so server doesnt load client classes
public class ShimmeringArmorItem extends ArmorItem {

    private static final double LEGGINGS_SWIM_SPEED_MODIFIER = 2;
    private static final AttributeUtil.AttributeInfo LEGGINGS_SWIM_SPEED_INFO = new AttributeUtil.AttributeInfo("shimmering_leggings_swim_speed", UUID.fromString("842e09e5-f07f-41ae-8a4c-130880192781"));

    public ShimmeringArmorItem(Type type) {
        super(EliteArmorMaterials.SHIMMERING, type,
                new Item.Properties()
                        .rarity(Rarity.RARE)
                        .fireResistant()
                        .stacksTo(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotIndex, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotIndex, isSelected);
        if (!(entity instanceof Player player)) return;

        if (slotIndex == SlotIndices.BOOTS) {
            if (level.isClientSide()) {
                ClientShimmeringArmorAbilities.processBootsAbility(level);
            }
        } else if (slotIndex == SlotIndices.LEGGINGS) {
            processLeggingsAbility(player, level);
        } else if (slotIndex == SlotIndices.CHESTPLATE) {
            processChestplateAbility(player, level);
        } else if (slotIndex == SlotIndices.HELMET) {
            processHelmetAbility(player, level);
        }

        // TODO: armor set abilty
    }

    private void processLeggingsAbility(Player player, Level level) {
        if (level.isClientSide()) return;
        if (!(level.getGameTime() % TickRates.HIGH == 0)) return;
        if (player.isInFluidType()) {
            player.addEffect(new MobEffectInstance(EliteEffects.SWIFT_SWIM.get(), 200, 1, true, false, true));
        }
    }

    private void processChestplateAbility(Player player, Level level) {
        if (level.isClientSide()) return;
        if (!(level.getGameTime() % TickRates.HIGH == 0)) return;
        if (player.isInFluidType()) {
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 1, true, false, true));
        }
    }

    private void processHelmetAbility(Player player, Level level) {
        if (level.isClientSide()) return;
        if (!(level.getGameTime() % TickRates.HIGH == 0)) return;
        if (player.isInFluidType()) {
            player.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, 200, 0, true, false, true));
        }
    }

}
