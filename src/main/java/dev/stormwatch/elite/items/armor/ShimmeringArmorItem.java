package dev.stormwatch.elite.items.armor;

import dev.stormwatch.elite.Elite;
import dev.stormwatch.elite.capabilities.ShimmeringCrownMarker;
import dev.stormwatch.elite.capabilities.ShimmeringCrownMarkerProvider;
import dev.stormwatch.elite.client.abilities.ClientShimmeringArmorAbilities;
import dev.stormwatch.elite.doc.SlotIndices;
import dev.stormwatch.elite.doc.TickRates;
import dev.stormwatch.elite.registry.EliteArmorMaterials;
import dev.stormwatch.elite.registry.EliteEffects;
import dev.stormwatch.elite.registry.EliteItems;
import dev.stormwatch.elite.util.InventoryUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ShimmeringArmorItem extends ArmorItem {

    // TODO: helmet requires heart of the sea to craft

    private static final int CROWN_UNDERWATER_GAIN = 3;
    private static final int CROWN_RAIN_GAIN = 1;
    private static final int MAX_CROWN_POWER = 10 * 60 * 20 * CROWN_UNDERWATER_GAIN; // fully charges in 10 minutes underwater
    private static final int CROWN_CHARGE_HEALING_COST = MAX_CROWN_POWER / 60; // 3 health bars on a full charge

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

        if (InventoryUtil.hasArmorEquipped(player, EliteItems.SHIMMERING_BOOTS.get(), SlotIndices.BOOTS)
                && InventoryUtil.hasArmorEquipped(player, EliteItems.SHIMMERING_LEGGINGS.get(), SlotIndices.LEGGINGS)
                && InventoryUtil.hasArmorEquipped(player, EliteItems.SHIMMERING_CHESTPLATE.get(), SlotIndices.CHESTPLATE)
                && InventoryUtil.hasArmorEquipped(player, EliteItems.SHIMMERING_HELMET.get(), SlotIndices.HELMET)
                && !level.isClientSide()) {
            ShimmeringCrownMarker crown = player.getCapability(ShimmeringCrownMarkerProvider.CAPABILITY_TYPE).orElseThrow(() -> new IllegalStateException("Player does not have a crown marker"));
            gainCrownCharge(player, crown);
            crownHealPlayer(player, crown);
            conferCrownPassiveBuffs(player, crown);
        }
    }

    private void processLeggingsAbility(Player player, Level level) {
        if (level.isClientSide()) return;
        if (!(level.getGameTime() % TickRates.HIGH == 0)) return;
        if (player.isInFluidType()) {
            player.addEffect(new MobEffectInstance(EliteEffects.SWIFT_SWIM.get(), 30, 1, true, false, true));
        }
    }

    private void processChestplateAbility(Player player, Level level) {
        if (level.isClientSide()) return;
        if (!(level.getGameTime() % TickRates.HIGH == 0)) return;
        if (player.isInFluidType()) {
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 30, 1, true, false, true));
        }
    }

    private void processHelmetAbility(Player player, Level level) {
        if (level.isClientSide()) return;
        if (!(level.getGameTime() % TickRates.HIGH == 0)) return;
        if (player.isInFluidType()) {
            player.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, 30, 0, true, false, true));
        }
    }

    private void gainCrownCharge(Player player, ShimmeringCrownMarker crown) {
        int gain = 0;
        if (player.isInWater()) {
            gain = CROWN_UNDERWATER_GAIN;
        } else if (player.isInWaterOrRain()) {
            gain = CROWN_RAIN_GAIN;
        }
        crown.charge += Math.min(gain, MAX_CROWN_POWER);
    }

    private void crownHealPlayer(Player player, ShimmeringCrownMarker crown) {
        if (player.getMaxHealth() - player.getHealth() >= 1 && crown.charge >= CROWN_CHARGE_HEALING_COST) {
            player.heal(1);
            crown.charge -= CROWN_CHARGE_HEALING_COST;
        }
    }

    private void conferCrownPassiveBuffs(Player player, ShimmeringCrownMarker crown) {
        final int nStages = 3;
        int stagesUnlocked = crown.charge / (MAX_CROWN_POWER / nStages);
        if (stagesUnlocked > 0) {
            player.addEffect(new MobEffectInstance(EliteEffects.SHIMMERING_CROWN_PASSIVES.get(), -1, stagesUnlocked - 1, true, false, true));
        } else if (player.hasEffect(EliteEffects.SHIMMERING_CROWN_PASSIVES.get())) {
            player.removeEffect(EliteEffects.SHIMMERING_CROWN_PASSIVES.get());
        }
    }

    @SubscribeEvent
    public static void attachCrownCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(ShimmeringCrownMarkerProvider.CAPABILITY_TYPE).isPresent()) {
                event.addCapability(new ResourceLocation(Elite.MOD_ID, "shimmering_crown_marker"), new ShimmeringCrownMarkerProvider());
            }
        }
    }

}
