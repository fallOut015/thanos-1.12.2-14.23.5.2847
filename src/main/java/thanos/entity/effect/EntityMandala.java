package thanos.entity.effect;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class EntityMandala extends Entity {
	private static final int MAXLIFE = 500;
	
	@Nullable
	private EntityLivingBase caster;
	
	public EntityMandala(World worldIn) {
		this(worldIn, null);
	}
	public EntityMandala(World worldIn, @Nullable EntityLivingBase caster) {
		super(worldIn);
		
		this.setSize(4f, 0);
		this.caster = caster;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		
		if(this.caster != null) {
			this.posX = this.caster.posX;
			this.posY = this.caster.posY;
			this.posZ = this.caster.posZ;
			
			this.caster.addPotionEffect(new PotionEffect(MobEffects.SPEED, 10, 1, true, true));
		}
		
		this.getEntityWorld().getEntitiesWithinAABBExcludingEntity(this.caster, this.getEntityBoundingBox().grow(2.125 * this.scale(this.ticksExisted), 5, 2.125 * this.scale(this.ticksExisted))).forEach(entity -> {
			if(entity instanceof EntityLivingBase && entity != this.caster) {
				((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 10, 2, false, false));
			}
		});
		
		if(this.ticksExisted >= MAXLIFE) {
			if(!this.getEntityWorld().isRemote) {
				this.setDead();
			}
		}
		
		if(this.world.isRemote) {
//			for(int i = 0; i < 11.25; ++ i) {
//			for(int j = 0; j < 360; ++ j) {
//				double i = rand.nextInt(360);
//				double x = this.posX + this.rand.nextInt((int) Math.max(1, Math.cos(i) * 4.25d * this.scale(this.ticksExisted)) * 2) - (Math.cos(i) * 4.25d * this.scale(this.ticksExisted)) - 0.5;
////				double x = this.posX + (this.rand.nextInt((int) Math.max(1, ((4.25d * this.scale(this.ticksExisted))) * 2d)) - 4.25 * this.scale(this.ticksExisted)) + 1;
//				double y = this.posY;
//				double z = this.posZ + this.rand.nextInt((int) Math.max(1, Math.sin(i) * 4.25d * this.scale(this.ticksExisted)) * 2) - (Math.sin(i) * 4.25d * this.scale(this.ticksExisted)) - 0.5;
////				double z = this.posZ + (this.rand.nextInt((int) Math.max(1, ((4.25d * this.scale(this.ticksExisted))) * 2d)) - 4.25 * this.scale(this.ticksExisted)) + 1;
//	            this.world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, x, y, z, 0, 0.25f, 0);
//			}
		}
	}

	@Override
	protected void entityInit() {
	}
	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
	}
	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
	}
	
	public double scale(double x) {
        double b = MAXLIFE; // x2 intercept (when mandala disappears)
        double c = 2; // max scale
        double d = 10; // exponent
        return -c * Math.pow(((x - b / 2) / (b / 2)), d) + c;
	}
	public static double y(double x, double ymax, double xint2, double exponent) {
        return -ymax * Math.pow(((x - xint2 / 2) / (xint2 / 2)), exponent) + ymax;
	}
}