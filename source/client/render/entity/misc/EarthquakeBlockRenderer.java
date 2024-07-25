package net.tslat.aoa3.client.render.entity.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.tslat.aoa3.content.entity.misc.EarthquakeBlockEntity;

public class EarthquakeBlockRenderer extends EntityRenderer<EarthquakeBlockEntity> {
    private final BlockRenderDispatcher dispatcher;

    public EarthquakeBlockRenderer(EntityRendererProvider.Context context) {
        super(context);

        this.shadowRadius = 0.5f;
        this.dispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(EarthquakeBlockEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        final BlockState blockState = entity.getBlock();

        if (blockState.getRenderShape() != RenderShape.MODEL)
            return;

        BlockPos blockPos = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
        BakedModel model = this.dispatcher.getBlockModel(blockState);
        long seed = blockState.getSeed(entity.getBlockOrigin());

        poseStack.pushPose();
        poseStack.translate(-0.5f, 0, -0.5f);

        for (var renderType : model.getRenderTypes(blockState, RandomSource.create(seed), ModelData.EMPTY)) {
            this.dispatcher.getModelRenderer().tesselateBlock(entity.level(), model, blockState, blockPos, poseStack, buffer.getBuffer(RenderTypeHelper.getMovingBlockRenderType(renderType)), false, RandomSource.create(), seed, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType);
        }

        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(EarthquakeBlockEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
