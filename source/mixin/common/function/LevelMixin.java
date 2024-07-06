package net.tslat.aoa3.mixin.common.function;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.WritableLevelData;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public abstract class LevelMixin {
    @Shadow public abstract ResourceKey<Level> dimension();

    @Shadow @Final protected WritableLevelData levelData;

    @Inject(method = "getGameRules", at = @At("HEAD"), cancellable = true)
    public void aoa3$wrapNowhereGamerules(CallbackInfoReturnable<GameRules> cir) {
        if (this.dimension() == AoADimensions.NOWHERE)
            cir.setReturnValue(new GameRules(this.levelData.getGameRules().rules) {
                @Override
                public boolean getBoolean(Key<BooleanValue> key) {
                    if (key == GameRules.RULE_MOBGRIEFING)
                        return false;

                    return super.getBoolean(key);
                }

                @Override
                public int getInt(Key<IntegerValue> key) {
                    if (key == GameRules.RULE_RANDOMTICKING)
                        return 0;

                    return super.getInt(key);
                }
            });
    }
}
