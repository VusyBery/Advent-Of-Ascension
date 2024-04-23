package net.tslat.aoa3.mixin.client.patch;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.UseAnim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin { // Prevents the movement slowdown from affecting the player when using an item with no use animation
	@ModifyExpressionValue(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z", ordinal = 0))
	public boolean aoa3$cancelNoAnimUsage(boolean isUsingItem) {
		return isUsingItem && ((LocalPlayer)(Object)this).getUseItem().getUseAnimation() != UseAnim.NONE;
	}
}
