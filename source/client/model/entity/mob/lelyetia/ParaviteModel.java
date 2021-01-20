package net.tslat.aoa3.client.model.entity.mob.lelyetia;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.MathHelper;

public class ParaviteModel extends EntityModel<MobEntity> {
	private final ModelRenderer la1;
	private final ModelRenderer ra1;
	private final ModelRenderer la2;
	private final ModelRenderer ra2;
	private final ModelRenderer la3;
	private final ModelRenderer ra3;
	private final ModelRenderer leg1;
	private final ModelRenderer leg2;
	private final ModelRenderer leg3;
	private final ModelRenderer leg4;
	private final ModelRenderer leg4p;
	private final ModelRenderer leg3p;
	private final ModelRenderer leg1p;
	private final ModelRenderer leg2p;
	private final ModelRenderer body;
	private final ModelRenderer body2;
	private final ModelRenderer body3;
	private final ModelRenderer head5;
	private final ModelRenderer head1;
	private final ModelRenderer head3;
	private final ModelRenderer head6;
	private final ModelRenderer head4;
	private final ModelRenderer head;
	private final ModelRenderer head2;

	public ParaviteModel() {
		textureWidth = 64;
		textureHeight = 64;
		(la1 = new ModelRenderer(this, 0, 30)).addBox(0.5f, 6.0f, 2.0f, 0, 2, 5);
		la1.setRotationPoint(6.0f, 8.0f, 1.0f);
		la1.setTextureSize(64, 64);
		la1.mirror = true;
		setRotation(la1, -2.443461f, 0.0f, 0.0f);
		(ra1 = new ModelRenderer(this, 0, 30)).addBox(0.5f, 6.0f, 2.0f, 0, 2, 5);
		ra1.setRotationPoint(-7.0f, 8.0f, 1.0f);
		ra1.setTextureSize(64, 64);
		ra1.mirror = true;
		setRotation(ra1, -2.443461f, 0.0f, 0.0f);
		(la2 = new ModelRenderer(this, 0, 15)).addBox(-1.0f, 0.0f, -1.0f, 3, 10, 3);
		la2.setRotationPoint(6.0f, 8.0f, 1.0f);
		la2.setTextureSize(64, 64);
		la2.mirror = true;
		setRotation(la2, -2.443461f, 0.0f, 0.0f);
		(ra2 = new ModelRenderer(this, 0, 15)).addBox(-1.0f, 0.0f, -1.0f, 3, 10, 3);
		ra2.setRotationPoint(-7.0f, 8.0f, 1.0f);
		ra2.setTextureSize(64, 64);
		ra2.mirror = true;
		setRotation(ra2, -2.443461f, 0.0f, 0.0f);
		(la3 = new ModelRenderer(this, 12, 34)).addBox(-1.0f, 8.0f, 2.0f, 3, 2, 5);
		la3.setRotationPoint(6.0f, 8.0f, 1.0f);
		la3.setTextureSize(64, 64);
		la3.mirror = true;
		setRotation(la3, -2.443461f, 0.0f, 0.0f);
		(ra3 = new ModelRenderer(this, 12, 34)).addBox(-1.0f, 8.0f, 2.0f, 3, 2, 5);
		ra3.setRotationPoint(-7.0f, 8.0f, 1.0f);
		ra3.setTextureSize(64, 64);
		ra3.mirror = true;
		setRotation(ra3, -2.443461f, 0.0f, 0.0f);
		(leg1 = new ModelRenderer(this, 5, 40)).addBox(-5.0f, 0.0f, 0.0f, 2, 10, 0);
		leg1.setRotationPoint(-4.0f, 14.0f, 6.0f);
		leg1.setTextureSize(64, 64);
		leg1.mirror = true;
		setRotation(leg1, 0.0f, 0.0f, 0.0f);
		(leg2 = new ModelRenderer(this, 0, 40)).addBox(3.0f, 0.0f, 0.0f, 2, 10, 0);
		leg2.setRotationPoint(4.0f, 14.0f, 6.0f);
		leg2.setTextureSize(64, 64);
		leg2.mirror = true;
		setRotation(leg2, 0.0f, 0.0f, 0.0f);
		(leg3 = new ModelRenderer(this, 5, 40)).addBox(-5.0f, 0.0f, -1.0f, 2, 10, 0);
		leg3.setRotationPoint(-4.0f, 14.0f, -3.0f);
		leg3.setTextureSize(64, 64);
		leg3.mirror = true;
		setRotation(leg3, 0.0f, 0.0f, 0.0f);
		(leg4 = new ModelRenderer(this, 0, 40)).addBox(3.0f, 0.0f, -1.0f, 2, 10, 0);
		leg4.setRotationPoint(4.0f, 14.0f, -3.0f);
		leg4.setTextureSize(64, 64);
		leg4.mirror = true;
		setRotation(leg4, 0.0f, 0.0f, 0.0f);
		(leg4p = new ModelRenderer(this, 0, 0)).addBox(-1.0f, 0.0f, -3.0f, 4, 10, 4);
		leg4p.setRotationPoint(4.0f, 14.0f, -3.0f);
		leg4p.setTextureSize(64, 64);
		leg4p.mirror = true;
		setRotation(leg4p, 0.0f, 0.0f, 0.0f);
		(leg3p = new ModelRenderer(this, 0, 0)).addBox(-3.0f, 0.0f, -3.0f, 4, 10, 4);
		leg3p.setRotationPoint(-4.0f, 14.0f, -3.0f);
		leg3p.setTextureSize(64, 64);
		leg3p.mirror = true;
		setRotation(leg3p, 0.0f, 0.0f, 0.0f);
		(leg1p = new ModelRenderer(this, 0, 0)).addBox(-3.0f, 0.0f, -2.0f, 4, 10, 4);
		leg1p.setRotationPoint(-4.0f, 14.0f, 6.0f);
		leg1p.setTextureSize(64, 64);
		leg1p.mirror = true;
		setRotation(leg1p, 0.0f, 0.0f, 0.0f);
		(leg2p = new ModelRenderer(this, 0, 0)).addBox(-1.0f, 0.0f, -2.0f, 4, 10, 4);
		leg2p.setRotationPoint(4.0f, 14.0f, 6.0f);
		leg2p.setTextureSize(64, 64);
		leg2p.mirror = true;
		setRotation(leg2p, 0.0f, 0.0f, 0.0f);
		(body = new ModelRenderer(this, 30, 42)).addBox(-10.0f, -9.0f, -4.0f, 10, 5, 0);
		body.setRotationPoint(5.0f, 10.0f, 5.0f);
		body.setTextureSize(64, 64);
		body.mirror = true;
		setRotation(body, 0.0f, 0.0f, 0.0f);
		(body2 = new ModelRenderer(this, 18, 0)).addBox(-6.0f, -8.0f, -5.0f, 10, 8, 10);
		body2.setRotationPoint(1.0f, 14.0f, 1.0f);
		body2.setTextureSize(64, 64);
		body2.mirror = true;
		setRotation(body2, 0.0f, 0.0f, 0.0f);
		(body3 = new ModelRenderer(this, 30, 34)).addBox(-6.0f, -8.0f, -5.0f, 2, 4, 2);
		body3.setRotationPoint(5.0f, 10.0f, 5.0f);
		body3.setTextureSize(64, 64);
		body3.mirror = true;
		setRotation(body3, 0.0f, 0.0f, 0.0f);
		(head5 = new ModelRenderer(this, 2, 35)).addBox(0.0f, -8.0f, -2.0f, 3, 2, 0);
		head5.setRotationPoint(0.0f, 2.0f, 1.0f);
		head5.setTextureSize(64, 64);
		head5.mirror = true;
		setRotation(head5, 0.0f, 0.0f, 0.0f);
		(head1 = new ModelRenderer(this, 18, 12)).addBox(1.0f, -1.0f, -4.0f, 1, 3, 1);
		head1.setRotationPoint(0.0f, 2.0f, 1.0f);
		head1.setTextureSize(64, 64);
		head1.mirror = true;
		setRotation(head1, 0.0f, 0.0f, 0.0f);
		(head3 = new ModelRenderer(this, 5, 42)).addBox(-5.0f, -6.0f, -2.0f, 2, 6, 0);
		head3.setRotationPoint(0.0f, 2.0f, 1.0f);
		head3.setTextureSize(64, 64);
		head3.mirror = true;
		setRotation(head3, 0.0f, 0.0f, 0.0f);
		(head6 = new ModelRenderer(this, 0, 42)).addBox(3.0f, -6.0f, -2.0f, 2, 6, 0);
		head6.setRotationPoint(0.0f, 2.0f, 1.0f);
		head6.setTextureSize(64, 64);
		head6.mirror = true;
		setRotation(head6, 0.0f, 0.0f, 0.0f);
		(head4 = new ModelRenderer(this, 2, 35)).addBox(-3.0f, -8.0f, -2.0f, 3, 2, 0);
		head4.setRotationPoint(0.0f, 2.0f, 1.0f);
		head4.setTextureSize(64, 64);
		head4.mirror = true;
		setRotation(head4, 0.0f, 0.0f, 0.0f);
		(head = new ModelRenderer(this, 18, 20)).addBox(-3.0f, -6.0f, -3.0f, 6, 6, 6);
		head.setRotationPoint(0.0f, 2.0f, 1.0f);
		head.setTextureSize(64, 64);
		head.mirror = true;
		setRotation(head, 0.0f, 0.0f, 0.0f);
		(head2 = new ModelRenderer(this, 18, 12)).addBox(-2.0f, -1.0f, -4.0f, 1, 3, 1);
		head2.setRotationPoint(0.0f, 2.0f, 1.0f);
		head2.setTextureSize(64, 64);
		head2.mirror = true;
		setRotation(head2, 0.0f, 0.0f, 0.0f);
	}

	@Override
	public void render(MatrixStack matrix, IVertexBuilder buffer, int light, int overlay, float red, float green, float blue, float alpha) {
		la1.render(matrix, buffer, light, overlay, red, green, blue, alpha);
		ra1.render(matrix, buffer, light, overlay, red, green, blue, alpha);
		la2.render(matrix, buffer, light, overlay, red, green, blue, alpha);
		ra2.render(matrix, buffer, light, overlay, red, green, blue, alpha);
		la3.render(matrix, buffer, light, overlay, red, green, blue, alpha);
		ra3.render(matrix, buffer, light, overlay, red, green, blue, alpha);
		leg1.render(matrix, buffer, light, overlay, red, green, blue, alpha);
		leg2.render(matrix, buffer, light, overlay, red, green, blue, alpha);
		leg3.render(matrix, buffer, light, overlay, red, green, blue, alpha);
		leg4.render(matrix, buffer, light, overlay, red, green, blue, alpha);
		leg4p.render(matrix, buffer, light, overlay, red, green, blue, alpha);
		leg3p.render(matrix, buffer, light, overlay, red, green, blue, alpha);
		leg1p.render(matrix, buffer, light, overlay, red, green, blue, alpha);
		leg2p.render(matrix, buffer, light, overlay, red, green, blue, alpha);
		body.render(matrix, buffer, light, overlay, red, green, blue, alpha);
		body2.render(matrix, buffer, light, overlay, red, green, blue, alpha);
		body3.render(matrix, buffer, light, overlay, red, green, blue, alpha);
		head5.render(matrix, buffer, light, overlay, red, green, blue, alpha);
		head1.render(matrix, buffer, light, overlay, red, green, blue, alpha);
		head3.render(matrix, buffer, light, overlay, red, green, blue, alpha);
		head6.render(matrix, buffer, light, overlay, red, green, blue, alpha);
		head4.render(matrix, buffer, light, overlay, red, green, blue, alpha);
		head.render(matrix, buffer, light, overlay, red, green, blue, alpha);
		head2.render(matrix, buffer, light, overlay, red, green, blue, alpha);
	}

	private void setRotation(final ModelRenderer model, final float x, final float y, final float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(MobEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		head.rotateAngleY = netHeadYaw / (float)(180f / Math.PI);
		head2.rotateAngleY = netHeadYaw / (float)(180f / Math.PI);
		head3.rotateAngleY = netHeadYaw / (float)(180f / Math.PI);
		head4.rotateAngleY = netHeadYaw / (float)(180f / Math.PI);
		head5.rotateAngleY = netHeadYaw / (float)(180f / Math.PI);
		head6.rotateAngleY = netHeadYaw / (float)(180f / Math.PI);
		ra1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + 3.1415927f) * 2.0f * limbSwingAmount * 0.5f - 2.44f;
		ra1.rotateAngleZ = 0.0f;
		ra2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + 3.1415927f) * 2.0f * limbSwingAmount * 0.5f - 2.44f;
		ra2.rotateAngleZ = 0.0f;
		ra3.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + 3.1415927f) * 2.0f * limbSwingAmount * 0.5f - 2.44f;
		ra3.rotateAngleZ = 0.0f;
		la1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 2.0f * limbSwingAmount * 0.5f - 2.44f;
		la1.rotateAngleZ = 0.0f;
		la2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 2.0f * limbSwingAmount * 0.5f - 2.44f;
		la2.rotateAngleZ = 0.0f;
		la3.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 2.0f * limbSwingAmount * 0.5f - 2.44f;
		la3.rotateAngleZ = 0.0f;
		leg1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount;
		leg1.rotateAngleY = 0.0f;
		leg3.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount;
		leg3.rotateAngleY = 0.0f;
		leg1p.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount;
		leg1p.rotateAngleY = 0.0f;
		leg3p.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount;
		leg3p.rotateAngleY = 0.0f;
		leg2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + 3.1415927f) * 1.4f * limbSwingAmount;
		leg4.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + 3.1415927f) * 1.4f * limbSwingAmount;
		leg2p.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + 3.1415927f) * 1.4f * limbSwingAmount;
		leg4p.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + 3.1415927f) * 1.4f * limbSwingAmount;
	}
}