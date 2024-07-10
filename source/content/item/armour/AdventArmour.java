package net.tslat.aoa3.content.item.armour;

import com.google.common.base.Suppliers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.common.damagesource.IReductionFunction;
import net.neoforged.neoforge.event.entity.EntityInvulnerabilityCheckEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.tslat.aoa3.common.registration.AoATags;
import net.tslat.aoa3.common.registration.item.AoAArmour;
import net.tslat.aoa3.util.LocaleUtil;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public abstract class AdventArmour extends ArmorItem {
	public AdventArmour(Holder<ArmorMaterial> material, Type slot, int baseDurability) {
		this(material, slot, new Properties().durability(slot.getDurability(baseDurability)));
	}

	public AdventArmour(Holder<ArmorMaterial> material, Type slot, Properties properties) {
		super(material, slot, properties);

		this.defaultModifiers = createAttributes(getMaterial(), getType());
	}

	private Supplier<ItemAttributeModifiers> createAttributes(Holder<ArmorMaterial> material, ArmorItem.Type slot) {
		return Suppliers.memoize(() -> {
			final int armourValue = material.value().getDefense(slot);
			final float toughness = material.value().toughness();
			final float knockbackResistance = material.value().knockbackResistance();
			final ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
			final EquipmentSlotGroup slotGroup = EquipmentSlotGroup.bySlot(slot.getSlot());
			final ResourceLocation attributeId = ResourceLocation.withDefaultNamespace("armor." + slot.getName());

			builder.add(Attributes.ARMOR, new AttributeModifier(attributeId, armourValue, AttributeModifier.Operation.ADD_VALUE), slotGroup);
			builder.add(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(attributeId, toughness, AttributeModifier.Operation.ADD_VALUE), slotGroup);

			addArmourAttributes(slotGroup, (attribute, modifier) -> builder.add(attribute, modifier, slotGroup));

			if (knockbackResistance > 0)
				builder.add(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(attributeId, knockbackResistance, AttributeModifier.Operation.ADD_VALUE), slotGroup);

			return builder.build();
		});
	}

	public boolean isCompatibleWithAnySet() {
		return false;
	}

	/**
	 * Called once on item construction
	 * <p>Pass any additional {@link AttributeModifier AttributeModifiers} this armour piece possesses</p>
	 *
	 * @param slot The equipment slot this armour piece belongs to
	 * @param attributes The consumer to pass the attribute and relevant modifier to
	 */
	public void addArmourAttributes(EquipmentSlotGroup slot, BiConsumer<Holder<Attribute>, AttributeModifier> attributes) {}

	/**
	 * Called when the entity equips the item or has it placed on their body by a third party method.
	 * <p>Will be called twice in the event that the piece being equipped also creates a full set</p>
	 *
	 * @param entity The entity equipping the armour.
	 * @param piece The armour piece that is being equipped
	 * @param equippedPieces The set of pieces currently equipped (prior to adding the newly equipped one)
	 */
	public void onEquip(LivingEntity entity, Piece piece, EnumSet<Piece> equippedPieces) {}

	/**
	 * Called when the entity unequips the item or has it removed from their body by a third party method.
	 * <p>Will be called twice in the event that the piece being unequipped also breaks a full set</p>
	 *
	 * @param entity The entity equipping the armour.
	 * @param piece The armour piece that is being unequipped
	 * @param equippedPieces The set of pieces currently equipped (prior to removing the newly unequipped one)
	 */
	public void onUnequip(LivingEntity entity, Piece piece, EnumSet<Piece> equippedPieces) {}

	/**
	 * Called at the start of each tick for the entity wearing this armour. Called once per unique armour type.
	 *
	 * @param entity The entity unequipping the armour.
	 * @param equippedPieces The set of pieces currently equipped for this set (including any piece that {@link #isCompatibleWithAnySet})
	 */
	public void onArmourTick(LivingEntity entity, EnumSet<Piece> equippedPieces) {}

	/**
	 * Called when the entity wearing this armour is being checked for invulnerabilities.
	 * <p>Use this to apply/revoke statically applicable damage invulnerabilities (such as a fire-immune armour adding fire-immunity)<br>
	 * Dynamic invulnerabilities should be handled in {@link #handleIncomingDamage}</p>
	 *
	 * @param entity The entity that the armour is worn by.
	 * @param equippedPieces The set of pieces currently equipped for this set (including any piece that {@link #isCompatibleWithAnySet})
	 * @param ev The event responsible for this method call
	 */
	public void checkDamageInvulnerability(LivingEntity entity, EnumSet<Piece> equippedPieces, EntityInvulnerabilityCheckEvent ev) {}

	/**
	 * Called when the entity wearing this armour has incoming damage about to be applied.
	 * <p>Use this to modify the damage value, apply dynamic invulnerabilities, or apply armour-specific damage mitigation.<br>
	 * Armour-specific damage-mitigation should be done by adding {@link LivingIncomingDamageEvent#addReductionModifier(DamageContainer.Reduction, IReductionFunction) a modifier}
	 * for the {@link DamageContainer.Reduction#ARMOR} stage</p>
	 *
	 * @param entity The entity that the armour is worn by.
	 * @param equippedPieces The set of pieces currently equipped for this set (including any piece that {@link #isCompatibleWithAnySet})
	 * @param ev The event responsible for this method call
	 */
	public void handleIncomingDamage(LivingEntity entity, EnumSet<Piece> equippedPieces, LivingIncomingDamageEvent ev) {}

	/**
	 * Called when the entity wearing this armour is attacking another entity.
	 * <p>Use this to modify the damage value<br>
	 * Post-attack effects should be handled in {@link #afterOutgoingAttack}</p>
	 *
	 * @param entity The entity that the armour is worn by.
	 * @param equippedPieces The set of pieces currently equipped for this set (including any piece that {@link #isCompatibleWithAnySet})
	 * @param ev The event responsible for this method call
	 */
	public void handleOutgoingAttack(LivingEntity entity, EnumSet<Piece> equippedPieces, LivingIncomingDamageEvent ev) {}

	/**
	 * Called when the entity wearing this armour is about to take damage, after all reductions and modifiers have taken place
	 * <p>Use this to handle incoming damage related to the final possible value<br>
	 * NOTE: This method will be called even if the final damage amount is 0</p>
	 *
	 * @param entity The entity that the armour is worn by.
	 * @param equippedPieces The set of pieces currently equipped for this set (including any piece that {@link #isCompatibleWithAnySet})
	 * @param ev The event responsible for this method call
	 */
	public void beforeTakingDamage(LivingEntity entity, EnumSet<Piece> equippedPieces, LivingDamageEvent.Pre ev) {}

	/**
	 * Called when the entity wearing this armour takes damage.
	 * <p>Use this to trigger effects related to taking damage while wearing this armour<br>
	 * NOTE: This method will be called even if the final damage amount is 0</p>
	 *
	 * @param entity The entity that the armour is worn by.
	 * @param equippedPieces The set of pieces currently equipped for this set (including any piece that {@link #isCompatibleWithAnySet})
	 * @param ev The event responsible for this method call
	 */
	public void afterTakingDamage(LivingEntity entity, EnumSet<Piece> equippedPieces, LivingDamageEvent.Post ev) {}

	/**
	 * Called when the entity wearing this armour successfully attacks another entity.
	 * <p>Use this to trigger effects related to dealing damage while wearing this armour<br>
	 * NOTE: This method will be called even if the final damage amount is 0</p>
	 *
	 * @param entity The entity that the armour is worn by.
	 * @param equippedPieces The set of pieces currently equipped for this set (including any piece that {@link #isCompatibleWithAnySet})
	 * @param ev The event responsible for this method call
	 */
	public void afterOutgoingAttack(LivingEntity entity, EnumSet<Piece> equippedPieces, LivingDamageEvent.Post ev) {}

	/**
	 * Called when the entity wearing this armour dies.
	 * <p>Use this to trigger effects or handle other tasks related to dying while wearing this armour<br>
	 * NOTE: This event is not cancellable and should only be used as a listener</p>
	 *
	 * @param entity The entity that the armour is worn by.
	 * @param equippedPieces The set of pieces currently equipped for this set (including any piece that {@link #isCompatibleWithAnySet})
	 * @param ev The event responsible for this method call
	 */
	public void onEntityDeath(LivingEntity entity, EnumSet<Piece> equippedPieces, LivingDeathEvent ev) {}

	/**
	 * Called when the entity wearing this armour is about to have a {@link net.minecraft.world.effect.MobEffect MobEffect}  applied
	 * <p>Use this to determined eligibility of effects for the entity</p>
	 *
	 * @param entity The entity that the armour is worn by.
	 * @param equippedPieces The set of pieces currently equipped for this set (including any piece that {@link #isCompatibleWithAnySet})
	 * @param ev The event responsible for this method call
	 */
	public void onEffectApplication(LivingEntity entity, EnumSet<Piece> equippedPieces, MobEffectEvent.Applicable ev) {}

	@Override
	public boolean isValidRepairItem(ItemStack stack, ItemStack repairMaterial) {
		return false;
	}

	protected MutableComponent setEffectHeader() {
		return LocaleUtil.getLocaleMessage(LocaleUtil.Keys.ARMOUR_SET_HEADER, ChatFormatting.GOLD);
	}

	protected MutableComponent pieceEffectHeader() {
		return LocaleUtil.getLocaleMessage(LocaleUtil.Keys.ARMOUR_PIECE_HEADER, ChatFormatting.GRAY);
	}

	protected MutableComponent anySetEffectHeader() {
		return LocaleUtil.getLocaleMessage(LocaleUtil.Keys.ARMOUR_ANY_SET_HEADER, ChatFormatting.DARK_AQUA);
	}

	public boolean isHelmetAirTight(Player player) {
		return player.getItemBySlot(EquipmentSlot.HEAD).is(AoATags.Items.AIRTIGHT);
	}

	public void setArmourCooldown(Player player, AoAArmour.ArmourSet armourSet, int ticks) {
		ItemCooldowns cooldowns = player.getCooldowns();

		armourSet.forEach(item -> cooldowns.addCooldown(item, ticks));
	}

	public boolean isOnCooldown(Player player) {
		return getArmourCooldown(player) > 0;
	}

	public int getArmourCooldown(Player player) {
		ItemCooldowns cooldowns = player.getCooldowns();
		ItemCooldowns.CooldownInstance cooldown = cooldowns.cooldowns.get(this);

		if (cooldown == null)
			return 0;

		return cooldown.endTime - cooldowns.tickCount;
	}

	public int perPieceValue(EnumSet<Piece> equippedPieces, int perPiece) {
		return perPiece * (equippedPieces.size() - (equippedPieces.contains(Piece.FULL_SET) ? 1 : 0));
	}

	public float perPieceValue(EnumSet<Piece> equippedPieces, float perPiece) {
		return perPiece * (equippedPieces.size() - (equippedPieces.contains(Piece.FULL_SET) ? 1 : 0));
	}

	public enum Piece {
		BOOTS,
		LEGGINGS,
		CHESTPLATE,
		HELMET,
		FULL_SET;

		public static Piece fromVanillaSlot(EquipmentSlot slot) {
			return switch (slot) {
				case MAINHAND, OFFHAND -> throw new IllegalArgumentException("Attempted to convert non-armour vanilla slot to AoA armour slot?");
				case HEAD -> HELMET;
				case CHEST, BODY -> CHESTPLATE;
				case LEGS -> LEGGINGS;
				case FEET -> BOOTS;
			};
		}

		@Nullable
		public EquipmentSlot toVanillaSlot() {
			return switch (this) {
				case HELMET -> EquipmentSlot.HEAD;
				case CHESTPLATE -> EquipmentSlot.CHEST;
				case LEGGINGS -> EquipmentSlot.LEGS;
				case BOOTS -> EquipmentSlot.FEET;
				case FULL_SET -> null;
			};
		}
	}
}
