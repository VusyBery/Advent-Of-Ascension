package net.tslat.aoa3.content.item.weapon.staff;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.Util;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.scheduling.AoAScheduler;
import net.tslat.aoa3.util.EntityUtil;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.effectslib.api.particle.ParticleBuilder;
import net.tslat.effectslib.api.particle.transitionworker.FollowEntityParticleTransition;
import net.tslat.effectslib.api.util.EffectBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AmberStaff extends BaseStaff<Object> {
	public AmberStaff(Item.Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public SoundEvent getCastingSound() {
		return AoASounds.ITEM_AMBER_STAFF_CAST.get();
	}

	public static Object2IntMap<Item> getDefaultRunes() {
		return Util.make(new Object2IntArrayMap<>(), runes -> {
			runes.put(AoAItems.KINETIC_RUNE.get(), 1);
			runes.put(AoAItems.ENERGY_RUNE.get(), 4);
		});
	}

	@Override
	public void cast(ServerLevel level, ItemStack staff, LivingEntity caster, Object args) {
		EntityUtil.applyPotions(caster, new EffectBuilder(MobEffects.ABSORPTION, 600).level(2));
	}

	@Override
	public void doCastFx(ServerLevel level, ItemStack staff, LivingEntity caster, Object args) {
		final int rings = Mth.ceil(caster.getBbHeight() * 4);

		for (int i = 0; i < rings; i++) {
			final int step = i;

			AoAScheduler.scheduleSyncronisedTask(() -> {
				ParticleBuilder.forPositionsInCircle(ParticleTypes.EXPLOSION, caster.position().add(0, step * 0.25f, 0), Mth.sin(step / (float)rings + 0.5f) * 0.25f, 32)
						.scaleMod(0.15f)
						.lifespan((rings + 3) - step)
						.colourOverride(0xF79400)
						.addTransition(FollowEntityParticleTransition.create(caster.getId()))
						.sendToAllPlayersTrackingEntity((ServerLevel)caster.level(), caster);
				ParticleBuilder.forPositionsInCircle(ParticleTypes.SPORE_BLOSSOM_AIR, caster.position().add(0, step * 0.25f, 0), Mth.sin(step / (float)rings + 0.5f) * 0.25f, 32)
						.scaleMod(0.25f)
						.lifespan(caster.getRandom().nextInt(10, 30))
						.colourOverride(0xFA9400)
						.sendToAllPlayersTrackingEntity((ServerLevel)caster.level(), caster);
			}, i + 1);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
		super.appendHoverText(stack, context, tooltip, flag);
	}
}
