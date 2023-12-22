package dev.stormwatch.elite.items.charms;

import dev.stormwatch.elite.capabilities.FuelLevel;
import dev.stormwatch.elite.capabilities.FuelLevelProvider;
import dev.stormwatch.elite.networking.EliteNetworking;
import dev.stormwatch.elite.networking.packets.AddEngineParticlesS2CPacket;
import dev.stormwatch.elite.util.InventoryUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ThreadLocalRandom;

public class AutoAnnihilationEngineCharmItem extends CharmItem {

    // TODO: crafted from blaze drop (infernal core), ghast drop (cryogenic core), and some kind of metal (probs netherite + ...)
    // I I I
    // C N C
    // I   I

    private static final int MAX_FUEL_LEVEL = 120;
    private static final double ACCELERATION = 0.05;
    private static final double MAX_SPEED = 0.8;

    public AutoAnnihilationEngineCharmItem() {
        super(new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.EPIC)
                .fireResistant());
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new FuelLevelProvider();
    }

    public static void tryFly(Player player) {
        ItemStack engine = InventoryUtil.getEquippedCharmOfType(player, AutoAnnihilationEngineCharmItem.class);
        if (engine == null) return;

        FuelLevel fuel = engine.getCapability(FuelLevelProvider.CAPABILITY_TYPE).orElseThrow(() -> new IllegalStateException("Annihilation Engine does not have a fuel level"));
        if (player.onGround()) {
            fuel.fuelLevel = MAX_FUEL_LEVEL;
        }
        if (fuel.fuelLevel <= 0) return;

        Vec3 d = player.getDeltaMovement();
        player.setDeltaMovement(d.x, Math.min(d.y + ACCELERATION, MAX_SPEED), d.z);
        player.hurtMarked = true;
        fuel.fuelLevel--;

        EliteNetworking.sendToClientsTrackingChunk(new AddEngineParticlesS2CPacket(player.position(), player.getYRot()), player.level().getChunkAt(player.blockPosition()));
    }

    public static void addParticles(Level level, Vec3 playerPos, float yRot) {
        double x = playerPos.x;
        double y = playerPos.y + 1;
        double z = playerPos.z;
        double dX = ThreadLocalRandom.current().nextDouble(-0.2, 0.2);
        double dZ = ThreadLocalRandom.current().nextDouble(-0.2, 0.2);
        level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, x + Math.sin((yRot + 160) / 180 * Math.PI) / 2, y, z + Math.cos((yRot + 160) / 180 * Math.PI) / 2, dX, 0, dZ);
        level.addParticle(ParticleTypes.FLAME, x + Math.sin((yRot + 200) / 180 * Math.PI) / 2, y, z + Math.cos((yRot + 200) / 180 * Math.PI) / 2, dX, 0, dZ);
    }
}
