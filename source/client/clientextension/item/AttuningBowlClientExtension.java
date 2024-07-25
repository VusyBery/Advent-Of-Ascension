package net.tslat.aoa3.client.clientextension.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.tslat.aoa3.client.AoAArmPoses;
import org.jetbrains.annotations.Nullable;

public class AttuningBowlClientExtension implements IClientItemExtensions {
    @Nullable
    @Override
    public HumanoidModel.ArmPose getArmPose(LivingEntity entity, InteractionHand hand, ItemStack stack) {
        return AoAArmPoses.ATTUNING_BOWL.getValue();
    }

    @Override
    public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack stack, float partialTick, float equipProcess, float swingProcess) {
        if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0) {
            poseStack.translate(0.725f, -0.3f, -0.15f);
            poseStack.mulPose(Axis.XP.rotationDegrees(-5));
            poseStack.mulPose(Axis.ZN.rotationDegrees(25));
            poseStack.mulPose(Axis.YP.rotationDegrees(90));
        }

        return false;
    }
}
