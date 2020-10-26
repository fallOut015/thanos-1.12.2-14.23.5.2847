package thanos.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thanos.entity.boss.EntityThanos;
import thanos.entity.effect.EntityMandala;

@SideOnly(Side.CLIENT)
public class ModelThanos extends ModelBiped {
	public ModelRenderer bipedLeftArmwear;
    public ModelRenderer bipedRightArmwear;
    public ModelRenderer bipedLeftLegwear;
    public ModelRenderer bipedRightLegwear;
    public ModelRenderer bipedBodyWear;
    
	public ModelThanos() {
        this(0.0F, false);
    }
    public ModelThanos(float modelSize, boolean p_i1168_2_) {
        super(modelSize, 0.0F, 64, p_i1168_2_ ? 32 : 64);
        
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.addBox(-4.0F, -12.0F, -4.0F, 8, 8, 8, modelSize);
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        
        this.bipedHeadwear = new ModelRenderer(this, 32, 0);
        this.bipedHeadwear.addBox(-4.0F, -12.0F, -4.0F, 8, 8, 8, modelSize + 0.5F);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
        
        this.bipedLeftArm = new ModelRenderer(this, 32, 48);
        this.bipedLeftArm.addBox(1.0F, -4.0F, -2.0F, 4, 12, 4, modelSize);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        
        this.bipedRightArm = new ModelRenderer(this, 40, 16);
        this.bipedRightArm.addBox(-5.0F, -4.0F, -2.0F, 4, 12, 4, modelSize);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        
        this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
        this.bipedLeftArmwear.addBox(1.0F, -4.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
        this.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);
        
        this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
        this.bipedRightArmwear.addBox(-5.0F, -4.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
        this.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 10.0F);

        this.bipedLeftLeg = new ModelRenderer(this, 16, 48);
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        
        this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
        this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
        this.bipedLeftLegwear.setRotationPoint(1.9F, 12.0F, 0.0F);
        
        this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
        this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
        this.bipedRightLegwear.setRotationPoint(-1.9F, 12.0F, 0.0F);
        
        this.bipedBody = new ModelRenderer(this, 16, 16);
        this.bipedBody.addBox(-4.0F, -2.0F, -2.0F, 8, 12, 4, modelSize);
        this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);

        this.bipedBodyWear = new ModelRenderer(this, 16, 32);
        this.bipedBodyWear.addBox(-4.0F, -2.0F, -2.0F, 8, 12, 4, modelSize + 0.25F);
        this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, 0.0F);
    }
    
    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.pushMatrix();

        this.bipedLeftLegwear.render(scale);
        this.bipedRightLegwear.render(scale);
        this.bipedLeftArmwear.render(scale);
        this.bipedRightArmwear.render(scale);
        this.bipedBodyWear.render(scale);

        GlStateManager.popMatrix();
    }
    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
    	super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

    	if(((EntityThanos) entityIn).isSnapping()) {
    		double x = EntityMandala.y(((EntityThanos) entityIn).getSnappingTime(), -2, 200, 6);
    		if(((EntityThanos) entityIn).getSnappingTime() >= 90 && ((EntityThanos) entityIn).getSnappingTime() <= 110) {
        		this.bipedLeftArm.rotateAngleX = (float) Math.min(x - 0.5, 0);
    		} else {
        		this.bipedLeftArm.rotateAngleX = (float) Math.min(x, 0);
    		}
    		this.bipedLeftArm.rotateAngleY = (float) Math.max(-x / 2, 0);
    	} else if(((EntityThanos) entityIn).isCasting()) {
    		double x = EntityMandala.y(((EntityThanos) entityIn).getCastingTime(), -1.5, 50, 10);
    		this.bipedLeftArm.rotateAngleX = (float) Math.min(x, 0);
    	}
    	
        copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);
        copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);
        copyModelAngles(this.bipedLeftArm, this.bipedLeftArmwear);
        copyModelAngles(this.bipedRightArm, this.bipedRightArmwear);
        copyModelAngles(this.bipedBody, this.bipedBodyWear);
    }
}