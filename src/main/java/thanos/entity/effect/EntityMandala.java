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
			
			this.caster.addPotionEffect(new PotionEffect(MobEffects.SPEED, 10, 1, false, true));
		}
		
		this.getEntityWorld().getEntitiesWithinAABBExcludingEntity(this.caster, this.getEntityBoundingBox().grow(2.125 * this.scale(this.ticksExisted), 5, 2.125 * this.scale(this.ticksExisted))).forEach(entity -> {
			if(entity instanceof EntityLivingBase && entity != this.caster) {
				((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 10, 2, false, true));
			}
		});
		
		if(this.ticksExisted >= MAXLIFE) {
			if(!this.getEntityWorld().isRemote) {
				this.setDead();
			}
		}
		
//		if(this.world.isRemote && this.ticksExisted % 10 == 0) {
//			for(int i = 0; i < 360; i += 20) {
//				for(int j = 0; j < 5; ++ j) {
//					double x = Math.cos(Math.toRadians(i + this.rand.nextInt(11) - 5)) * (j + this.rand.nextFloat() - 0.5) * this.scale(this.ticksExisted);
//					double z = Math.sin(Math.toRadians(i + this.rand.nextInt(11) - 5)) * (j + this.rand.nextFloat() - 0.5) * this.scale(this.ticksExisted);
//					this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, x + this.posX, this.posY, z+ this.posZ , 0, 0, 0);
//				}
//			}
//			// TODO particles
//		}
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