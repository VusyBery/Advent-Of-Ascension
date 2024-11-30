package net.tslat.aoa3.mixin.client.function;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.tslat.aoa3.client.render.shader.AoAPostProcessing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "shutdownEffect", at = @At("TAIL"), require = 0)
    public void aoa3$shutdownPostProcessing(CallbackInfo ci) {
        AoAPostProcessing.shutdownShaders();
    }

    @Inject(method = "resize", at = @At("HEAD"), require = 0)
    public void aoa3$resizePostProcessing(int width, int height, CallbackInfo ci) {
        AoAPostProcessing.resizeShaders(width, height);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;bindWrite(Z)V"), require = 0)
    public void aoa$renderPostProcessing(DeltaTracker deltaTracker, boolean setViewport, CallbackInfo ci) {
        AoAPostProcessing.doShaderProcessing(deltaTracker.getGameTimeDeltaTicks());
    }
}
