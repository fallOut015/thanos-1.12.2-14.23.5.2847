package thanos.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thanos.Main;
import thanos.client.model.ModelMandala;
import thanos.entity.effect.EntityMandala;

@SideOnly(Side.CLIENT)
public class RenderMandala extends Render<EntityMandala> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(Main.MODID, "textures/entity/mandala.png");
	private final ModelMandala model;
	
	public RenderMandala(RenderManager renderManager) {
		super(renderManager);
		
		this.model = new ModelMandala();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityMandala entity) {
		return TEXTURE;
	}
	@Override
	public void doRender(EntityMandala entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		
        this.setupTranslation(x, y, z);
        this.setupRotation(entity, entityYaw, partialTicks);
        this.bindEntityTexture(entity);
        GlStateManager.enableAlpha();

        this.model.render(entity, 0.0f, 0.0F, 0.0f, 0.0F, 0.0F, 0.0625F);

        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	public void setupRotation(EntityMandala entity, float entityYaw, float partialTicks) {
        GlStateManager.rotate(partialTicks, 0.0F, 0.0F, 0.0F);
        
        this.model.setRotationAngles(0, 0, entity.ticksExisted, entityYaw, 0, 0, entity);
             
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(entity.scale(entity.ticksExisted), 1.0F, entity.scale(entity.ticksExisted));
    }
    public void setupTranslation(double x, double y, double z) {
        GlStateManager.translate((float) x, (float) y - 1.275F, (float) z);
    }
    @Override
    public boolean shouldRender(EntityMandala livingEntity, ICamera camera, double camX, double camY, double camZ) {
    	return true;
    }
}