package net.tslat.aoa3.mixin.common.patch;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Slime;
import net.neoforged.neoforge.event.EventHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.world.effect.OozingMobEffect")
public class OozingMobEffectMixin { // Because Mojang doesn't call Mob#finalizeSpawn, sigh.
    @WrapOperation(method = "spawnSlimeOffspring", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Slime;setSize(IZ)V"))
    public void aoa3$finalizeSpawn(Slime slime, int size, boolean resetHealth, Operation<Void> callback) {
        if (slime.level() instanceof ServerLevel level)
            EventHooks.finalizeMobSpawn(slime, level, level.getCurrentDifficultyAt(slime.blockPosition()), MobSpawnType.SPAWNER, null);

        callback.call(slime, size, resetHealth);
    }
}
