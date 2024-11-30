package net.tslat.aoa3.client.clientextension.item;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.tslat.aoa3.client.AoAArmPoses;
import org.jetbrains.annotations.Nullable;

public class LargeGunClientExtension implements IClientItemExtensions {
    @Nullable
    @Override
    public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
        return AoAArmPoses.LARGE_GUN.getValue();
    }
}
