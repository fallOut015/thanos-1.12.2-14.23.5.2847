package thanos.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thanos.Main;
import thanos.client.model.ModelThanos;
import thanos.client.renderer.entity.layers.LayerThanosDeath;
import thanos.entity.boss.EntityThanos;

@SideOnly(Side.CLIENT)
public class RenderThanos extends RenderLiving<EntityThanos> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(Main.MODID, "textures/entity/thanos/thanos.png");

	public RenderThanos(RenderManager manager) {
		super(manager, new ModelThanos(1f, false), 1.414f);
		
		this.addLayer(new LayerThanosDeath());
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityThanos arg0) {
		return TEXTURE;
	}
	@Override
	protected void preRenderCallback(EntityThanos entitylivingbaseIn, float partialTickTime) {
		super.preRenderCallback(entitylivingbaseIn, partialTickTime);
		
		GlStateManager.scale(2.5f, 2.5f, 2.5f);
		GlStateManager.translate(0, -0.125f, 0);
	}
	@Override
	public boolean shouldRender(EntityThanos livingEntity, ICamera camera, double camX, double camY, double camZ) {
		return true;
	}
}