package dev.stormwatch.elite.items.armor;

import dev.stormwatch.elite.client.abilities.ClientShimmeringArmorAbilities;
import dev.stormwatch.elite.doc.SlotIndices;
import dev.stormwatch.elite.doc.TickRates;
import dev.stormwatch.elite.registry.EliteArmorMaterials;
import dev.stormwatch.elite.registry.EliteItems;
import dev.stormwatch.elite.util.AttributeUtil;
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
        } if (slotIndex == SlotIndices.HELMET) {
            // TODO: when underwater, water breathing and night vision, + something else cool
        }
    }

    @SubscribeEvent
    public static void processLeggingsAbility(TickEvent.PlayerTickEvent event) {
        if (event.player.level().isClientSide()) return;
        if (event.phase == TickEvent.Phase.END) return;
        if (!(event.player.level().getGameTime() % TickRates.LOW == 0)) return;

        if (event.player.getInventory().getArmor(SlotIndices.LEGGINGS).is(EliteItems.SHIMMERING_LEGGINGS.get())) {
            if (!AttributeUtil.hasAttributeModifier(event.player, ForgeMod.SWIM_SPEED.get(), LEGGINGS_SWIM_SPEED_INFO.uuid())) {
                AttributeUtil.setTransientAttribute(event.player, ForgeMod.SWIM_SPEED.get(), LEGGINGS_SWIM_SPEED_INFO.name(), LEGGINGS_SWIM_SPEED_INFO.uuid(), LEGGINGS_SWIM_SPEED_MODIFIER, AttributeModifier.Operation.MULTIPLY_BASE);
            }
        } else if (AttributeUtil.hasAttributeModifier(event.player, ForgeMod.SWIM_SPEED.get(), LEGGINGS_SWIM_SPEED_INFO.uuid())) {
            AttributeUtil.removeAttributeModifier(event.player, ForgeMod.SWIM_SPEED.get(), LEGGINGS_SWIM_SPEED_INFO.uuid());
        }
    }

}
