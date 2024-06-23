package net.tslat.aoa3.common.registration;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.tslat.aoa3.advent.AdventOfAscension;

public final class AoAAttributes {
	public static void init() {}

	public static final DeferredHolder<Attribute, Attribute> RANGED_ATTACK_DAMAGE = register("ranged_attack_damage", "aoa3.rangedAttackDamage", 0, 0, Double.MAX_VALUE, false);
	public static final DeferredHolder<Attribute, Attribute> AGGRO_RANGE = register("aggro_range", "aoa3.aggroRange", 8, 0, Double.MAX_VALUE, false);

	public static final AttributeModifier NIGHT_AGGRO_MODIFIER = new AttributeModifier(AdventOfAscension.id("night_time_pacification"), -0.6d, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

	private static DeferredHolder<Attribute, Attribute> register(String id, String name, double defaultValue, double minValue, double maxValue, boolean syncedWithClient) {
		return AoARegistries.ENTITY_ATTRIBUTES.register(id, () -> new RangedAttribute(name, defaultValue, minValue, maxValue).setSyncable(syncedWithClient));
	}
}