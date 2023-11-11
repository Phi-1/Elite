package dev.stormwatch.elite.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class AttributeUtil {

    public static void setTransientAttribute(LivingEntity entity, Attribute attribute, String name, UUID uuid, double amount, AttributeModifier.Operation operation) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null) {
            AttributeModifier modifier =  instance.getModifier(uuid);
            if (modifier != null) {
                instance.removeModifier(uuid);
            }
            instance.addTransientModifier(new AttributeModifier(uuid, name, amount, operation));
        }
    }

    public static void setPermanentAttribute(LivingEntity entity, Attribute attribute, String name, UUID uuid, double amount, AttributeModifier.Operation operation) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null) {
            AttributeModifier modifier = instance.getModifier(uuid);
            if (modifier != null) {
                instance.removeModifier(uuid);
            }
            instance.addPermanentModifier(new AttributeModifier(uuid, name, amount, operation));
        }
    }

    public static boolean hasAttributeModifier(LivingEntity entity, Attribute attribute, UUID uuid) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null) {
            AttributeModifier modifier = instance.getModifier(uuid);
            return modifier != null;
        }
        return false;
    }

    public static void removeAttributeModifier(LivingEntity entity, Attribute attribute, UUID uuid) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null) {
            AttributeModifier modifier = instance.getModifier(uuid);
            if (modifier != null) {
                instance.removeModifier(uuid);
            }
        }
    }

    public record AttributeInfo(String name, UUID uuid) {}

}
