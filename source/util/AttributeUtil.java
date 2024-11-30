package net.tslat.aoa3.util;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Optional;

public final class AttributeUtil {
    public static Optional<AttributeInstance> getAttribute(LivingEntity entity, Holder<Attribute> attribute) {
        return Optional.ofNullable(entity.getAttribute(attribute));
    }

    public static boolean hasAttribute(LivingEntity entity, Holder<Attribute> attribute) {
        return getAttribute(entity, attribute).isPresent();
    }

    public static boolean hasModifier(LivingEntity entity, Holder<Attribute> attribute, AttributeModifier modifier) {
        return hasModifier(entity, attribute, modifier.id());
    }

    public static boolean hasModifier(LivingEntity entity, Holder<Attribute> attribute, ResourceLocation modifierId) {
        return getAttribute(entity, attribute).map(instance -> instance.hasModifier(modifierId)).orElse(false);
    }

    public static boolean applyTransientModifier(LivingEntity entity, Holder<Attribute> attribute, AttributeModifier modifier) {
        return applyTransientModifier(entity, attribute, modifier, false);
    }

    public static boolean applyTransientModifier(LivingEntity entity, Holder<Attribute> attribute, AttributeModifier modifier, boolean skipIfPresent) {
        return getAttribute(entity, attribute).filter(instance -> !skipIfPresent || !instance.hasModifier(modifier.id())).map(instance -> {
            instance.addOrUpdateTransientModifier(modifier);
            checkResidualHealthAfterAttribute(attribute, entity);

            return true;
        }).orElse(false);
    }

    public static void applyPermanentModifier(LivingEntity entity, Holder<Attribute> attribute, AttributeModifier modifier) {
        applyPermanentModifier(entity, attribute, modifier, false);
    }

    public static void applyPermanentModifier(LivingEntity entity, Holder<Attribute> attribute, AttributeModifier modifier, boolean skipIfPresent) {
        getAttribute(entity, attribute).filter(instance -> !skipIfPresent || !instance.hasModifier(modifier.id())).ifPresent(instance -> {
            instance.addOrReplacePermanentModifier(modifier);
            checkResidualHealthAfterAttribute(attribute, entity);
        });
    }

    public static void removeModifier(LivingEntity entity, Holder<Attribute> attribute, AttributeModifier modifier) {
        removeModifier(entity, attribute, modifier.id());
    }

    public static void removeModifier(LivingEntity entity, Holder<Attribute> attribute, ResourceLocation modifierId) {
        getAttribute(entity, attribute).ifPresent(instance -> {
            if (instance.removeModifier(modifierId))
                checkResidualHealthAfterAttribute(attribute, entity);
        });
    }

    public static double getAttributeValue(LivingEntity entity, Holder<Attribute> attribute) {
        return getAttributeValueOrDefault(entity, attribute, 0);
    }

    public static double getAttributeValueOrDefault(LivingEntity entity, Holder<Attribute> attribute, double fallback) {
        return getAttribute(entity, attribute).map(AttributeInstance::getValue).orElse(fallback);
    }

    private static void checkResidualHealthAfterAttribute(Holder<Attribute> attribute, LivingEntity entity) {
        if (attribute == Attributes.MAX_HEALTH && entity.getHealth() > entity.getMaxHealth())
            entity.setHealth(entity.getMaxHealth());
    }
}
