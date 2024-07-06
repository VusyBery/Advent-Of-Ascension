package net.tslat.aoa3.mixin.common.patch;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Silverfish;
import net.neoforged.neoforge.event.EventHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.world.effect.InfestedMobEffect")
public class InfestedMobEffectMixin { // Because Mojang doesn't call Mob#finalizeSpawn, sigh.
    @WrapOperation(method = "spawnSilverfish", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Silverfish;moveTo(DDDFF)V"))
    public void aoa3$finalizeSpawn(Silverfish silverfish, double x, double y, double z, float yRot, float xRot, Operation<Void> callback) {
        if (silverfish.level() instanceof ServerLevel level)
            EventHooks.finalizeMobSpawn(silverfish, level, level.getCurrentDifficultyAt(BlockPos.containing(x, y, z)), MobSpawnType.SPAWNER, null);

        callback.call(silverfish, x, y, z, yRot, xRot);
    }
}
