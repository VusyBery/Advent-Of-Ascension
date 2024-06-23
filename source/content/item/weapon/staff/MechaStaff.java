package net.tslat.aoa3.content.item.weapon.staff;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.Util;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.content.entity.projectile.staff.BaseEnergyShot;
import net.tslat.aoa3.content.entity.projectile.staff.LyonicShotEntity;
import net.tslat.aoa3.util.AttributeUtil;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.smartbrainlib.util.RandomUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MechaStaff extends BaseStaff<Object> {
	private static final AttributeModifier DEBUFF = new AttributeModifier(AdventOfAscension.id("mecha_staff_debuff"), -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

	public MechaStaff(Item.Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public SoundEvent getCastingSound() {
		return AoASounds.ITEM_STAFF_CAST.get();
	}

	public static Object2IntMap<Item> getDefaultRunes() {
		return Util.make(new Object2IntArrayMap<>(), runes -> {
			runes.put(AoAItems.WIND_RUNE.get(), 2);
			runes.put(AoAItems.DISTORTION_RUNE.get(), 1);
			runes.put(AoAItems.POWER_RUNE.get(), 1);
		});
	}

	@Override
	public void cast(Level world, ItemStack staff, LivingEntity caster, Object args) {
		world.addFreshEntity(new LyonicShotEntity(caster, this, 60));
	}

	@Override
	public boolean doEntityImpact(BaseEnergyShot shot, Entity target, LivingEntity shooter) {
		if (target instanceof LivingEntity entity) {
			if (!entity.level().isClientSide && AttributeUtil.getAttribute(entity, Attributes.ARMOR).filter(instance -> instance.getValue() > 0 && !instance.hasModifier(DEBUFF.id())).isPresent()) {
				AttributeUtil.applyTransientModifier(entity, Attributes.ARMOR, DEBUFF);
				AABB bounds = entity.getBoundingBox();

				for (int i = 0; i < 8; i++) {
					((ServerLevel)entity.level()).sendParticles(ParticleTypes.TOTEM_OF_UNDYING, bounds.minX + RandomUtil.randomValueUpTo(entity.getBbWidth()), bounds.maxY + 0.1d, bounds.minZ + RandomUtil.randomValueUpTo(entity.getBbWidth()), 1, 0, 0, 0, 0);
				}
			}

			return true;
		}

		return false;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
		super.appendHoverText(stack, context, tooltip, flag);
	}
}
