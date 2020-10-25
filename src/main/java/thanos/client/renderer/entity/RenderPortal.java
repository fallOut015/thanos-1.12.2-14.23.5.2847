package thanos.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thanos.Main;
import thanos.client.model.ModelPortal;
import thanos.entity.effect.EntityPortal;

@SideOnly(Side.CLIENT)
public class RenderPortal extends Render<EntityPortal> {
	private static final ResourceLocation[] TEXTURE = new ResourceLocation [] {
		new ResourceLocation(Main.MODID, "textures/entity/portal/portal_0.png"),
		new ResourceLocation(Main.MODID, "textures/entity/portal/portal_1.png"),
		new ResourceLocation(Main.MODID, "textures/entity/portal/portal_2.png"),
		new ResourceLocation(Main.MODID, "textures/entity/portal/portal_3.png"),
		new ResourceLocation(Main.MODID, "textures/entity/portal/portal_4.png"),
		new ResourceLocation(Main.MODID, "textures/entity/portal/portal_5.png"),
		new ResourceLocation(Main.MODID, "textures/entity/portal/portal_6.png"),
		new ResourceLocation(Main.MODID, "textures/entity/portal/portal_7.png")
	};
	private final ModelPortal model;
	
	public RenderPortal(RenderManager renderManager) {
		super(renderManager);
		
		this.model = new ModelPortal();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityPortal entity) {
		return TEXTURE[entity.ticksExisted / 4 % 8];
	}
	@Override
	public void doRender(EntityPortal entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		
        this.setupTranslation(x, y, z);
        this.setupRotation(entity, entityYaw, partialTicks);
        this.bindEntityTexture(entity);
        GlStateManager.enableAlpha();

        this.model.render(entity, 0.0f, 0.0F, 0.0f, 0.0F, 0.0F, 0.0625F);

        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	public void setupRotation(EntityPortal entity, float entityYaw, float partialTicks) {
        GlStateManager.rotate(partialTicks, 0.0F, 0.0F, 0.0F);
        
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(entity.scale(entity.ticksExisted), entity.scale(entity.ticksExisted), 1.0f);
    }
    public void setupTranslation(double x, double y, double z) {
        GlStateManager.translate((float) x, (float) y + 2f, (float) z);
    }
    @Override
    public boolean shouldRender(EntityPortal livingEntity, ICamera camera, double camX, double camY, double camZ) {
    	return true;
    }
}