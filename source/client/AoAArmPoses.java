package net.tslat.aoa3.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;
import net.tslat.aoa3.common.registration.item.AoATools;
import net.tslat.aoa3.content.item.weapon.gun.BaseGun;

public final class AoAArmPoses {
    public static final EnumProxy<HumanoidModel.ArmPose> ATTUNING_BOWL = new EnumProxy<>(HumanoidModel.ArmPose.class, true, (IArmPoseTransformer)AoAArmPoses::attuningBowlTransformer);
    public static final EnumProxy<HumanoidModel.ArmPose> SMALL_GUN = new EnumProxy<>(HumanoidModel.ArmPose.class, true, (IArmPoseTransformer)AoAArmPoses::smallGunArmTransformer);

    private static void attuningBowlTransformer(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {
        if (entity.getUseItem().is(AoATools.ATTUNING_BOWL)) {
            model.rightArm.xRot = -75 * Mth.DEG_TO_RAD;
            model.rightArm.yRot = -27 * Mth.DEG_TO_RAD;
            model.leftArm.xRot = -75 * Mth.DEG_TO_RAD;
            model.leftArm.yRot = 27 * Mth.DEG_TO_RAD;
        }
    }

    private static void smallGunArmTransformer(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {
        if (entity.getUseItem().getItem() instanceof BaseGun) {
            model.rightArm.xRot = -75 * Mth.DEG_TO_RAD;
            model.rightArm.yRot = -27 * Mth.DEG_TO_RAD;
            model.leftArm.xRot = -75 * Mth.DEG_TO_RAD;
            model.leftArm.yRot = 27 * Mth.DEG_TO_RAD;
        }
    }
}
