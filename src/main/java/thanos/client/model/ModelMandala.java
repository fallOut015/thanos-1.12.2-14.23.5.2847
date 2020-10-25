package thanos.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

// Made with Blockbench 3.6.5
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports

public class ModelMandala extends ModelBase {
	private final ModelRenderer cube;

	public ModelMandala() {
		textureWidth = 128;
		textureHeight = 128;

		cube = new ModelRenderer(this);
		cube.setRotationPoint(0.0F, 24.0F, 0.0F);
		cube.cubeList.add(new ModelBox(cube, -128, 0, -64.0F, 0.0F, -64.0F, 128, 0, 128, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		cube.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		
		this.cube.rotateAngleY = ageInTicks / 20;
	}
}