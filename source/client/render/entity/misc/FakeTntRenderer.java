package net.tslat.aoa3.client.render.entity.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.tslat.aoa3.content.entity.misc.FakeTntEntity;

public class FakeTntRenderer extends EntityRenderer<FakeTntEntity> {
    private final BlockRenderDispatcher blockRenderer;

    public FakeTntRenderer(EntityRendererProvider.Context context) {
        super(context);

        this.shadowRadius = 0.5F;
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(FakeTntEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0, 0.5f, 0);

        final float fuseTick = (float)entity.getFuse();

        if (fuseTick - partialTicks + 1f < 10) {
            float swell = Mth.clamp(1f - (fuseTick - partialTicks + 1f) / 10f, 0, 1f);
            float scale = 1f + swell * swell * swell * 0.3f;

            poseStack.scale(scale, scale, scale);
        }

        poseStack.mulPose(Axis.YP.rotationDegrees(-90f));
        poseStack.translate(-0.5f, -0.5f, 0.5f);
        poseStack.mulPose(Axis.YP.rotationDegrees(90f));
        TntMinecartRenderer.renderWhiteSolidBlock(this.blockRenderer, Blocks.TNT.defaultBlockState(), poseStack, buffer, packedLight, ((int)fuseTick) / 5 % 2 == 0);
        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(FakeTntEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
