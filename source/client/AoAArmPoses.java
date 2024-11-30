package net.tslat.aoa3.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;
import net.tslat.aoa3.common.registration.item.AoAEnchantments;
import net.tslat.aoa3.common.registration.item.AoATools;
import net.tslat.aoa3.util.EnchantmentUtil;

public final class AoAArmPoses {
    public static final EnumProxy<HumanoidModel.ArmPose> ATTUNING_BOWL = new EnumProxy<>(HumanoidModel.ArmPose.class, true, (IArmPoseTransformer)AoAArmPoses::attuningBowlTransformer);
    public static final EnumProxy<HumanoidModel.ArmPose> SMALL_GUN = new EnumProxy<>(HumanoidModel.ArmPose.class, true, (IArmPoseTransformer)AoAArmPoses::smallGunArmTransformer);
    public static final EnumProxy<HumanoidModel.ArmPose> LARGE_GUN = new EnumProxy<>(HumanoidModel.ArmPose.class, true, (IArmPoseTransformer)AoAArmPoses::largeGunArmTransformer);

    private static void attuningBowlTransformer(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {
        if (entity.getUseItem().is(AoATools.ATTUNING_BOWL)) {
            model.rightArm.xRot = -75 * Mth.DEG_TO_RAD;
            model.rightArm.yRot = -27 * Mth.DEG_TO_RAD;
            model.leftArm.xRot = -75 * Mth.DEG_TO_RAD;
            model.leftArm.yRot = 27 * Mth.DEG_TO_RAD;
        }
    }

    private static void smallGunArmTransformer(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {
        boolean rightHand = arm == HumanoidArm.RIGHT;
        ModelPart armModel = rightHand ? model.rightArm : model.leftArm;

        armModel.yRot = model.head.yRot;
        armModel.xRot = -Mth.HALF_PI + model.head.xRot + 0.1f;

        if (arm == (entity instanceof Mob mob && mob.isLeftHanded() ? HumanoidArm.LEFT : HumanoidArm.RIGHT) && EnchantmentUtil.hasEnchantment(entity.level(), entity.getOffhandItem(), AoAEnchantments.BRACE)) {
            armModel = !rightHand ? model.rightArm : model.leftArm;

            armModel.yRot = model.head.yRot;
            armModel.xRot = -Mth.HALF_PI + model.head.xRot + 0.1f;
        }
    }

    private static void largeGunArmTransformer(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {
        HumanoidArm mainHand = entity instanceof Mob mob && mob.isLeftHanded() ? HumanoidArm.LEFT : HumanoidArm.RIGHT;

        if (mainHand != arm)
            return;

        boolean rightHand = arm == HumanoidArm.RIGHT;
        ModelPart armModel = rightHand ? model.rightArm : model.leftArm;

        armModel.yRot = model.head.yRot;
        armModel.xRot = -Mth.HALF_PI + model.head.xRot + 0.1f;

        if (entity.getItemInHand(InteractionHand.OFF_HAND).isEmpty()) {
            ModelPart supportArmModel = rightHand ? model.leftArm : model.rightArm;

            supportArmModel.yRot = model.head.yRot + (rightHand ? 0.9f : -0.9f);
            supportArmModel.xRot = -Mth.HALF_PI + model.head.xRot + 0.2f;
        }
    }
}
