package net.tslat.aoa3.mixin.client.patch;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.tslat.aoa3.common.registration.AoATags;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@Shadow
	@Final
    Minecraft minecraft;

	@WrapOperation(method = "bobHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;sin(F)F"))
	private float aoa3$injectReducedFlinchTag(float value, Operation<Float> original) {
		if (this.minecraft.player.getLastDamageSource() != null && this.minecraft.player.getLastDamageSource().is(AoATags.DamageTypes.REDUCED_FLINCH))
			value *= 0.1f;

		return Mth.sin(value);
	}
}
