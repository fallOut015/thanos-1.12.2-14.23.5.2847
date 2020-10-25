package thanos.entity.effect;

import com.google.common.base.Optional;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityPortal extends Entity {
	private static final int MAXLIFE = 50;
	
	private static final DataParameter<Optional<BlockPos>> TO = EntityDataManager.createKey(EntityPortal.class, DataSerializers.OPTIONAL_BLOCK_POS);

	public EntityPortal(World worldIn) {
		this(worldIn, Optional.absent());
	}
	public EntityPortal(World worldIn, Optional<BlockPos> to) {
		super(worldIn);
		
		if(!to.isPresent()) {
			System.out.println("this is a to portal");
		} else {
			System.out.println("this is a from portal");
		}
		
		this.getDataManager().set(TO, to);
		this.setSize(4, 4);
		this.setEntityBoundingBox(this.getEntityBoundingBox().grow(1d, 1d, 0.025d));
	}

	@Override
	protected void entityInit() {
		this.getDataManager().register(TO, Optional.absent());
	}
	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		if(compound.hasKey("TO")) {
			this.getDataManager().set(TO, Optional.of(BlockPos.fromLong(compound.getLong("TO"))));
		}
	}
	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		if(this.getDataManager().get(TO).isPresent()) {
			compound.setLong("TO", this.getDataManager().get(TO).get().toLong());
		}
	}
	
	int getToX() {
		return this.getDataManager().get(TO).or(BlockPos.ORIGIN).getX();
	}
	int getToY() {
		return this.getDataManager().get(TO).or(BlockPos.ORIGIN).getY();
	}
	int getToZ() {
		return this.getDataManager().get(TO).or(BlockPos.ORIGIN).getZ();
	}
	
	public static EntityPortal from(World worldIn, EntityPortal entityIn) {
		return new EntityPortal(worldIn, Optional.of(new BlockPos(entityIn.posX, entityIn.posY, entityIn.posZ)));
	}
	
	@Override
	public void onUpdate() {
		if(this.ticksExisted >= 25) {
			this.getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox()).forEach(entityIn -> {
				if(entityIn instanceof EntityLiving && this.getDataManager().get(TO).isPresent()) {
					System.out.println("\"colliding\" with a FROM portal");
					entityIn.setPosition(this.getToX(), this.getToY(), this.getToZ());
				}
			});			
		}
		
		if(this.ticksExisted >= MAXLIFE) {
			this.setDead();
		}
		super.onUpdate();
	}
	
	public double scale(double x) {
        double b = MAXLIFE; // x2 intercept (when mandala disappears)
        double c = 2; // max scale
        double d = 10; // exponent
        return -c * Math.pow(((x - b / 2) / (b / 2)), d) + c;
	}
}