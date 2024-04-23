package net.tslat.aoa3.mixin.client.function;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.tslat.aoa3.client.render.dimension.AoADimensionEffectsRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {
    @Shadow public abstract DimensionSpecialEffects effects();

    @WrapOperation(method = "doAnimateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;getAmbientParticle()Ljava/util/Optional;"))
    public Optional<AmbientParticleSettings> aoa3$injectCustomAmbientParticles(Biome biome, Operation<Optional<AmbientParticleSettings>> original, @Local(argsOnly = true, index = 7) BlockPos.MutableBlockPos pos) {
        if (effects() instanceof AoADimensionEffectsRenderer aoaEffects)
            aoaEffects.spawnAmbientParticle((ClientLevel)(Object)this, pos, biome);

        return original.call(biome);
    }
}
