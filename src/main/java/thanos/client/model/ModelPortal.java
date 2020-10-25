package thanos.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

// Made with Blockbench 3.6.5
// Exported for Minecraft version 1.14
// Paste this class into your mod and generate all required imports

public class ModelPortal extends ModelBase {
	private final ModelRenderer cube;

	public ModelPortal() {
		textureWidth = 128;
		textureHeight = 64;

		cube = new ModelRenderer(this);
		cube.setRotationPoint(0.0F, 24.0F, 0.0F);
		cube.cubeList.add(new ModelBox(cube, 0, 0, -32.0F, -64.0F, 0.0F, 64, 64, 0, 0.0F, false));
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
}