package net.tslat.aoa3.content.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.tslat.aoa3.content.entity.projectile.arrow.CustomArrowEntity;
import net.tslat.smartbrainlib.util.RandomUtil;
import org.jetbrains.annotations.Nullable;

public interface ArrowFiringWeapon {
    default CustomArrowEntity applyArrowMods(CustomArrowEntity arrow, @Nullable Entity shooter, ItemStack stack, boolean isCritical) {
        return arrow;
    }
    default void tickArrow(CustomArrowEntity arrow, @Nullable Entity shooter, ItemStack stack) {}
    default void onBlockImpact(CustomArrowEntity arrow, @Nullable Entity shooter, BlockHitResult hitResult, ItemStack stack) {}
    default void onEntityImpact(CustomArrowEntity arrow, @Nullable Entity shooter, EntityHitResult hitResult, ItemStack stack, float velocity) {}
    default float getArrowDamage(CustomArrowEntity arrow, @Nullable Entity shooter, EntityHitResult hitResult, ItemStack stack, float baseDamage, float velocity, boolean isCritical) {
        float damage = baseDamage * 0.5f * (velocity / 3f);

        if (isCritical)
            damage += damage + (damage * (float)RandomUtil.randomScaledGaussianValue(0.35f));

        return damage;
    }
}
