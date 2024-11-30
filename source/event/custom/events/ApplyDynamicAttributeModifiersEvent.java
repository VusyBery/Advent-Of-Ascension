package net.tslat.aoa3.event.custom.events;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.bus.api.Event;
import net.tslat.aoa3.util.AttributeUtil;

/**
 * Called on player login, and on player clone.
 * <p>
 * The purpose of this event is to apply attribute modifiers to entities when they spawn.
 * <p>
 * NOTE: Due to performance implications and the needs of AoA, this event is only fired for players currently
 */
public class ApplyDynamicAttributeModifiersEvent extends Event {
    private final LivingEntity entity;

    public ApplyDynamicAttributeModifiersEvent(LivingEntity entity) {
        this.entity = entity;
    }

    /**
     * Get the entity the attribute modifier(s) will apply to
     */
    public LivingEntity getEntity() {
        return this.entity;
    }

    /**
     * Apply a transient modifier to the entity in this event.
     * <p>
     * If using this event, it is recommended to use this method instead of doing it manually as this ensures safe application
     *
     * @return Whether the entity had the attribute and the modifier was applied
     */
    public boolean applyTransientModifier(final Holder<Attribute> attribute, final AttributeModifier modifier) {
        return AttributeUtil.applyTransientModifier(getEntity(), attribute, modifier);
    }
}
