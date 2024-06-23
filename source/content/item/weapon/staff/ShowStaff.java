package net.tslat.aoa3.content.item.weapon.staff;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.Util;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.util.EntityUtil;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.effectslib.api.util.EffectBuilder;
import net.tslat.smartbrainlib.util.EntityRetrievalUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class ShowStaff extends BaseStaff<List<LivingEntity>> {
	public ShowStaff(Item.Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public SoundEvent getCastingSound() {
		return AoASounds.ITEM_SHOW_STAFF_CAST.get();
	}

	public static Object2IntMap<Item> getDefaultRunes() {
		return Util.make(new Object2IntArrayMap<>(), runes -> {
			runes.put(AoAItems.COMPASS_RUNE.get(), 3);
			runes.put(AoAItems.POWER_RUNE.get(), 3);
		});
	}

	@Override
	public Optional<List<LivingEntity>> checkPreconditions(LivingEntity caster, ItemStack staff) {
		List<LivingEntity> targets = EntityRetrievalUtil.getEntities(caster, 30, entity -> entity instanceof LivingEntity livingEntity && EntityUtil.isHostileMob(livingEntity));

		return Optional.ofNullable(targets.isEmpty() ? null : targets);
	}

	@Override
	public void cast(Level world, ItemStack staff, LivingEntity caster, List<LivingEntity> args) {
		for (LivingEntity entity : args) {
			entity.igniteForSeconds(5);
			EntityUtil.applyPotions(entity, new EffectBuilder(MobEffects.GLOWING, 100));
			world.addFreshEntity(new FireworkRocketEntity(world, entity.getX(), entity.getBoundingBox().maxY, entity.getZ(), makeFireworksStack()));
		}
	}

	private ItemStack makeFireworksStack() {
		ItemStack fireworks = new ItemStack(Items.FIREWORK_ROCKET, 1);

		fireworks.set(DataComponents.FIREWORKS, new Fireworks(3, List.of(new FireworkExplosion(FireworkExplosion.Shape.BURST, IntList.of(0), IntList.of(), true, false))));

		return fireworks;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
		super.appendHoverText(stack, context, tooltip, flag);
	}
}
