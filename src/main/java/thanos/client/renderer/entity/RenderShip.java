package thanos.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thanos.Main;
import thanos.client.model.ModelShip;
import thanos.entity.effect.EntityShip;

@SideOnly(Side.CLIENT)
public class RenderShip extends Render<EntityShip> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(Main.MODID, "textures/entity/ship.png");
	private final ModelShip model;
	
	public RenderShip(RenderManager renderManager) {
		super(renderManager);
		
		this.model = new ModelShip();
		this.shadowSize = 2.0f;
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityShip entity) {
		return TEXTURE;
	}
	@Override
	public void doRender(EntityShip entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
			
		this.setupTranslation(x, y, z);
		this.setupRotation(entity, entityYaw, partialTicks);
		this.bindEntityTexture(entity);

		this.model.render(entity, 0, 0, 0, 0, 0, 0.0625F);
		
		GlStateManager.popMatrix();	
		
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	public void setupRotation(EntityShip entity, float entityYaw, float partialTicks) {
        GlStateManager.rotate(partialTicks, 0.0F, 0.0F, 0.0F);

        this.model.setRotationAngles(0, 0, entity.ticksExisted, entityYaw, 0, 0, entity);

        GlStateManager.scale(1.0F, 1.0F, 1.0F);
    }
    public void setupTranslation(double x, double y, double z) {
        GlStateManager.translate((float) x, (float) y, (float) z);
    }
    @Override
    public boolean shouldRender(EntityShip livingEntity, ICamera camera, double camX, double camY, double camZ) {
    	return true;
    }
}