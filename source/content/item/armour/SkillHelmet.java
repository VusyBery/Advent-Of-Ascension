package net.tslat.aoa3.content.item.armour;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Unbreakable;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.client.model.armor.AoAMiscModels;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.client.player.ClientPlayerDataManager;
import net.tslat.aoa3.player.ServerPlayerDataManager;
import net.tslat.aoa3.player.skill.AoASkill;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.RegistryUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SkillHelmet extends AdventArmour {
	private final Supplier<AoASkill> skill;

	public SkillHelmet(Supplier<AoASkill> skill) {
		super(AoAArmourMaterials.SKILL_HELMET, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(50)).component(DataComponents.UNBREAKABLE, new Unbreakable(false)));

		this.skill = skill;
	}

	public AoASkill getSkill() {
		return this.skill.get();
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		return 0;
	}

	@Override
	public Type getSetType() {
		return Type.ALL;
	}

	@Override
	public boolean isEnchantable(ItemStack pStack) {
		return true;
	}

	@Override
	public void onEquip(ServerPlayerDataManager plData, @Nullable EquipmentSlot slot) {
		plData.getSkill(getSkill()).applyXpModifier(0.5f);
	}

	@Override
	public void onUnequip(ServerPlayerDataManager plData, @Nullable EquipmentSlot slot) {
		plData.getSkill(getSkill()).removeXpModifier(0.5f);
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@NotNull
			@Override
			public Model getGenericArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> defaultModel) {
				return AoAMiscModels.getSkillHelmetModel(getSkill(), entityLiving instanceof Player && ClientPlayerDataManager.get().getSkill(getSkill()).hasLevel(1000), defaultModel);
			}
		});
	}

	@Nullable
	@Override
	public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
		return AdventOfAscension.id("textures/models/armor/custom/" + RegistryUtil.getId(stack.getItem()).getPath() + (ClientPlayerDataManager.get().getSkill(getSkill()).getLevel(true) == 1000 ? "_trim" : "") + ".png");
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag pFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.UNBREAKABLE, LocaleUtil.ItemDescriptionType.UNIQUE));
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.XP_BONUS, LocaleUtil.ItemDescriptionType.ITEM_TYPE_INFO, LocaleUtil.numToComponent(50), getSkill().getName()));
		tooltip.add(anySetEffectHeader());
	}
}
