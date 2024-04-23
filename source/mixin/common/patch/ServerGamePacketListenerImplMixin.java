package net.tslat.aoa3.mixin.common.patch;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
	@Shadow
	public ServerPlayer player;

	// Patches vanilla bug that fails to trigger jump on server when on block edge by checking if the player is in the fall state that would only occur when the player starts to fall but before ticking
	@ModifyExpressionValue(method = "handleMovePlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;onGround()Z"))
	public boolean aoa3$addCoyoteTime(boolean onGround, @Local(argsOnly = true, index = 1) ServerboundMovePlayerPacket packet) {
		return onGround || (packet.getY(this.player.getY()) == this.player.getY() + this.player.getJumpPower() && this.player.getDeltaMovement().y() < -this.player.getAttributeValue(NeoForgeMod.ENTITY_GRAVITY.value()));
	}
}
