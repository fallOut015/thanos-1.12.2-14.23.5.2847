package thanos.entity.effect;

import com.google.common.base.Optional;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import thanos.Main;
import thanos.entity.boss.EntityThanos;

public class EntityShip extends Entity {
	private static final DataParameter<Optional<BlockPos>> END = EntityDataManager.createKey(EntityShip.class, DataSerializers.OPTIONAL_BLOCK_POS);
	private static final DataParameter<Optional<BlockPos>> START = EntityDataManager.createKey(EntityShip.class, DataSerializers.OPTIONAL_BLOCK_POS);
	private static final DataParameter<Float> INTERPOLATION = EntityDataManager.createKey(EntityShip.class, DataSerializers.FLOAT);
	
	public EntityShip(World worldIn) {
		super(worldIn);
		
		this.setSize(8f, 8f);
		this.noClip = false;
		this.getTags().add("sound");
	}

	@Override
	protected void entityInit() {
		this.getDataManager().register(END, Optional.absent());
		this.getDataManager().register(START, Optional.absent());
		this.getDataManager().register(INTERPOLATION, 0f);
	}
	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		if(compound.hasKey("END")) {
			this.getDataManager().set(END, Optional.of(BlockPos.fromLong(compound.getLong("END"))));
		}
		if(compound.hasKey("START")) {
			this.getDataManager().set(START, Optional.of(BlockPos.fromLong(compound.getLong("START"))));
		}
		if(compound.hasKey("INTERPOLATION")) {
			this.getDataManager().set(INTERPOLATION, compound.getFloat("INTERPOLATION"));
		}
	}
	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		if(this.getDataManager().get(END).isPresent()) {
			compound.setLong("END", this.getDataManager().get(END).or(BlockPos.ORIGIN).toLong());
		}
		if(this.getDataManager().get(START).isPresent()) {
			compound.setLong("START", this.getDataManager().get(START).or(BlockPos.ORIGIN).toLong());
		}
		compound.setFloat("INTERPOLATION", this.getDataManager().get(INTERPOLATION).floatValue());
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		for(int i = 0; i < 6; ++ i) {
			this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, i - 7 + this.posX + this.width / 4, this.posY + this.height / 3, i - 11 + this.posZ + this.width / 2, 0, 0.5f, 0);
			this.world.spawnParticle(EnumParticleTypes.FLAME, i - 7 + this.posX + this.width / 4, this.posY + this.height / 3, i - 11 + this.posZ + this.width / 2, 0, 0.5f, 0);
			this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, i - 7 + this.posX + this.width / 4, this.posY + this.height / 3, -6 - i + this.posZ + this.width / 2, 0, 0.5f, 0);
			this.world.spawnParticle(EnumParticleTypes.FLAME, i - 7 + this.posX + this.width / 4, this.posY + this.height / 3, -6 - i + this.posZ + this.width / 2, 0, 0.5f, 0);
		}

		if(this.getDataManager().get(END).isPresent()) {
			double x = MathHelper.clampedLerp(this.startX(), this.endX(), this.getDataManager().get(INTERPOLATION).floatValue());
			double y = MathHelper.clampedLerp(this.startY(), this.endY(), this.getDataManager().get(INTERPOLATION).floatValue());
			double z = MathHelper.clampedLerp(this.startZ(), this.endZ(), this.getDataManager().get(INTERPOLATION).floatValue());
			
			this.posX = x;
			this.posY = y;
			this.posZ = z;
			
			if(this.getDataManager().get(INTERPOLATION).floatValue() >= 0.86f) {
				if(this.getDataManager().get(INTERPOLATION).floatValue() >= 1f) {
					if(!this.world.isRemote) {
						boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this);
						this.world.createExplosion(this, this.posX, this.posY, this.posZ, 3f, flag);
						this.setDead();
						EntityThanos thanos = new EntityThanos(this.getEntityWorld());
						thanos.setPosition(this.posX, this.posY, this.posZ);
						this.getEntityWorld().spawnEntity(thanos);
//						thanos.snap();
					}
				} else if(!this.world.isRemote && this.getTags().contains("sound")) {
					this.world.playSound(null, this.getPosition(), Main.LAND, SoundCategory.NEUTRAL, 100.0f, 1.0f);
					this.getTags().remove("sound");
				}				
			}
			this.getDataManager().set(INTERPOLATION, this.getDataManager().get(INTERPOLATION).floatValue() + 0.002f);
		}
	}
	
	int endX() {
		return this.getDataManager().get(END).or(BlockPos.ORIGIN).getX();
	}
	int startX() {
		return this.getDataManager().get(START).or(BlockPos.ORIGIN).getX();
	}
	int endY() {
		return this.getDataManager().get(END).or(BlockPos.ORIGIN).getY();
	}
	int startY() {
		return this.getDataManager().get(START).or(BlockPos.ORIGIN).getY();
	}
	int endZ() {
		return this.getDataManager().get(END).or(BlockPos.ORIGIN).getZ();
	}
	int startZ() {
		return this.getDataManager().get(START).or(BlockPos.ORIGIN).getZ();
	}
	
	public void setEnd(double x, double y, double z) {
		this.getDataManager().set(END, Optional.of(new BlockPos(x, y, z)));
	}
	public void setStart(double x, double y, double z) {
		this.getDataManager().set(START, Optional.of(new BlockPos(x, y, z)));
	}
	@Override
	public boolean canBeCollidedWith() {
		return true;
	}
	@Override
	public boolean canRenderOnFire() {
		return false;
	}
}