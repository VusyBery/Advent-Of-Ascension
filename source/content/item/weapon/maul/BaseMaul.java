package net.tslat.aoa3.content.item.weapon.maul;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.registration.item.AoADataComponents;
import net.tslat.aoa3.util.AttributeUtil;
import net.tslat.aoa3.util.ItemUtil;

public class BaseMaul extends Item {
	protected static final ResourceLocation SCALED_KNOCKBACK_ID = AdventOfAscension.id("maul_scaled_knockback");
	protected static final ResourceLocation BASE_ATTACK_REACH_ID = AdventOfAscension.id("maul_attack_reach");

	private final Supplier<ItemAttributeModifiers> attributeModifiers;
	protected final float baseDamage;
	protected final double attackSpeed;
	protected final double knockback;

	public BaseMaul(float baseDmg, double attackSpeed, double knockback, final int durability) {
		super(new Item.Properties().durability(durability));

		this.baseDamage = baseDmg;
		this.attackSpeed = attackSpeed;
		this.knockback = knockback;
		this.attributeModifiers = buildDefaultAttributes();
	}

	public float getAttackDamage() {
		return baseDamage;
	}

	public double getAttackSpeed() {
		return attackSpeed;
	}

	public double getBaseKnockback() {
		return knockback;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BLOCK;
	}

	protected Supplier<ItemAttributeModifiers> buildDefaultAttributes() {
		return Suppliers.memoize(() -> {
			ImmutableList.Builder<ItemAttributeModifiers.Entry> modifiers = ImmutableList.builder();

			modifiers.add(new ItemAttributeModifiers.Entry(
					Attributes.ATTACK_DAMAGE,
					new AttributeModifier(BASE_ATTACK_DAMAGE_ID, getAttackDamage(), AttributeModifier.Operation.ADD_VALUE),
					EquipmentSlotGroup.MAINHAND
			));
			modifiers.add(new ItemAttributeModifiers.Entry(
					Attributes.ATTACK_SPEED,
					new AttributeModifier(BASE_ATTACK_SPEED_ID, getAttackSpeed(), AttributeModifier.Operation.ADD_VALUE),
					EquipmentSlotGroup.MAINHAND
			));
			modifiers.add(new ItemAttributeModifiers.Entry(
					Attributes.ATTACK_KNOCKBACK,
					getKnockbackModifier(1),
					EquipmentSlotGroup.MAINHAND
			));
			modifiers.add(new ItemAttributeModifiers.Entry(
					Attributes.ENTITY_INTERACTION_RANGE,
					new AttributeModifier(BASE_ATTACK_REACH_ID, 0.5f, AttributeModifier.Operation.ADD_VALUE),
					EquipmentSlotGroup.MAINHAND
			));

			return new ItemAttributeModifiers(modifiers.build(), true);
		});
	}

	protected AttributeModifier getKnockbackModifier(float attackStrengthMod) {
		return new AttributeModifier(SCALED_KNOCKBACK_ID, getBaseKnockback() * attackStrengthMod, AttributeModifier.Operation.ADD_VALUE);
	}

	@Override
	public boolean canAttackBlock(BlockState state, Level worldIn, BlockPos pos, Player player) {
		return !player.isCreative();
	}

	@Override
	public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
		return true;
	}

	@Override
	public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity user) {
		if (!level.isClientSide && (double)state.getDestroySpeed(level, pos) != 0.0D)
			ItemUtil.damageItemForUser((ServerLevel)level, stack, state.getSoundType(level, pos, user) == SoundType.STONE ? 1 : 2, user, EquipmentSlot.MAINHAND);

		return true;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
		float attackStr = player.getAttackStrengthScale(0.0f);

		stack.set(AoADataComponents.MELEE_SWING_STRENGTH, attackStr);
		AttributeUtil.applyTransientModifier(player, Attributes.ATTACK_KNOCKBACK, getKnockbackModifier(attackStr));

		return false;
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (attacker.level() instanceof ServerLevel level) {
			doMeleeEffect(stack, target, attacker, stack.getOrDefault(AoADataComponents.MELEE_SWING_STRENGTH, 1f));
			ItemUtil.damageItemForUser(level, stack, attacker, InteractionHand.MAIN_HAND);
			AttributeUtil.applyTransientModifier(attacker, Attributes.ATTACK_KNOCKBACK, getKnockbackModifier(1));
		}

		return true;
	}

	@Override
	public boolean isPrimaryItemFor(ItemStack stack, Holder<Enchantment> enchantment) {
		return enchantment.is(Enchantments.LOOTING) || enchantment.is(Enchantments.KNOCKBACK) || super.isPrimaryItemFor(stack, enchantment);
	}

	protected void doMeleeEffect(ItemStack stack, Entity target, LivingEntity attacker, float attackCooldown) {}

	@Override
	public int getEnchantmentValue() {
		return 8;
	}

	@Override
	public ItemAttributeModifiers getDefaultAttributeModifiers() {
		return this.attributeModifiers.get();
	}
}
