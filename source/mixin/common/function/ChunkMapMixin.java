package net.tslat.aoa3.mixin.common.function;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.tslat.aoa3.advent.AdventOfAscension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChunkMap.class)
public class ChunkMapMixin {
	@ModifyExpressionValue(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getSeed()J"))
	public long aoa3$makePerDimSeed(long original, @Local(argsOnly = true, index = 1) ServerLevel level) {
		if (AdventOfAscension.isAoA(level.dimension().location()))
			return level.getSeed() + level.dimension().location().hashCode();

		return level.getSeed();
	}
}
