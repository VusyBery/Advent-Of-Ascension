package net.tslat.aoa3.content.item.weapon.bow;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.content.entity.projectile.arrow.CustomArrowEntity;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.WorldUtil;
import net.tslat.effectslib.api.particle.ParticleBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SkydriverBow extends BaseBow {
	public SkydriverBow(Item.Properties properties) {
		super(properties);
	}

	@Override
	public void tickArrow(CustomArrowEntity arrow, @Nullable Entity shooter, ItemStack stack) {
		if (!arrow.level().isClientSide && !arrow.inGround && arrow.tickCount > 1) {
			ParticleBuilder.forRandomPosInEntity(ParticleTypes.SPIT, arrow)
					.colourOverride(0xA53A00)
					.velocity(0, -0.1f, 0)
					.lifespan(20)
					.sendToAllPlayersTrackingEntity((ServerLevel)arrow.level(), arrow);

			BlockPos.MutableBlockPos testPos = arrow.blockPosition().mutable();

			while (testPos.move(Direction.DOWN).getY() >= arrow.level().getMinBuildHeight() && arrow.level().isEmptyBlock(testPos)) {
				;
			}

			if (arrow.level().getBlockState(testPos).isFaceSturdy(arrow.level(), testPos, Direction.UP) && arrow.level().getBlockState(testPos.above()).canBeReplaced() && WorldUtil.canPlaceBlock(arrow.level(), testPos.above(), shooter, null))
				arrow.level().setBlockAndUpdate(testPos.above(), AoABlocks.ORANGE_ACID.get().defaultBlockState());
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
		super.appendHoverText(stack, context, tooltip, flag);
	}
}
