package net.tslat.aoa3.content.item.weapon.bow;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.EntityHitResult;
import net.tslat.aoa3.content.entity.projectile.arrow.CustomArrowEntity;
import net.tslat.aoa3.util.EntityUtil;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.effectslib.api.util.EffectBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DeepBow extends BaseBow {
	public DeepBow(Item.Properties properties) {
		super(properties);
	}

	@Override
	public void tickArrow(CustomArrowEntity arrow, @Nullable Entity shooter, ItemStack stack) {
		if (arrow.level() instanceof ServerLevel serverLevel)
			serverLevel.sendParticles(ParticleTypes.FIREWORK, arrow.getX(), arrow.getY() + 0.1, arrow.getZ(), 1, 0, 0, 0, 0);
	}

	@Override
	public void onEntityImpact(CustomArrowEntity arrow, @Nullable Entity shooter, EntityHitResult hitResult, ItemStack stack, float velocity) {
		if (hitResult.getEntity() instanceof LivingEntity target)
			EntityUtil.applyPotions(target, new EffectBuilder(MobEffects.GLOWING, 200));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
		super.appendHoverText(stack, context, tooltip, flag);
	}
}
