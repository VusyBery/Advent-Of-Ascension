package net.tslat.aoa3.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;

public final class AoAArmPoses {
    public static final EnumProxy<HumanoidModel.ArmPose> ATTUNING_BOWL = new EnumProxy<>(HumanoidModel.ArmPose.class, true, (IArmPoseTransformer)AoAArmPoses::attuningBowlTransformer);

    private static void attuningBowlTransformer(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {
        if (entity.isUsingItem()) {
            model.rightArm.xRot = -75 * Mth.DEG_TO_RAD;
            model.rightArm.yRot = -27 * Mth.DEG_TO_RAD;
            model.leftArm.xRot = -75 * Mth.DEG_TO_RAD;
            model.leftArm.yRot = 27 * Mth.DEG_TO_RAD;
        }
    }
}
