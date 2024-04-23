package net.tslat.aoa3.mixin.common.patch;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	/*@Redirect(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;is(Lnet/minecraft/tags/TagKey;)Z", ordinal = 7), require = 0)
	private boolean wrapKnockbackCheck(DamageSource damageSource, TagKey<DamageType> explosionTag) {
		return damageSource.is(explosionTag) || damageSource.is(DamageTypeTags.NO_IMPACT);
	}*/
}
