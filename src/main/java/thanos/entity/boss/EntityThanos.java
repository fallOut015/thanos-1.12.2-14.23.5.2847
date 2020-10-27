package thanos.entity.boss;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.commodorethrawn.revivemod.common.handler.AltarHandler;
import com.commodorethrawn.revivemod.common.util.ActionScheduler;
import com.daposeidonguy.teamsmod.common.storage.StorageHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import thanos.Main;
import thanos.MainConfig;
import thanos.entity.boss.attack.Attack;
import thanos.entity.boss.attack.AttackHeal;
import thanos.entity.boss.attack.AttackLightning;
import thanos.entity.boss.attack.AttackSummon;
import thanos.entity.boss.attack.AttackTeleport;
import thanos.entity.boss.attack.AttackTimeWarp;
import thanos.entity.effect.EntityMandala;
import thanos.init.ItemsThanos;

public class EntityThanos extends EntityMob implements IRangedAttackMob {
	public static final List<EntityThanos> THANI = new LinkedList<EntityThanos>();
	
	private static final DataParameter<Boolean> CASTING = EntityDataManager.createKey(EntityThanos.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> CASTING_TIME = EntityDataManager.createKey(EntityThanos.class, DataSerializers.VARINT);
	
	private final BossInfoServer bossInfo;
    
    private static final DataParameter<Boolean> ACTIVE = EntityDataManager.createKey(EntityThanos.class, DataSerializers.BOOLEAN);
	
	private static final DataParameter<Boolean> SNAPPING = EntityDataManager.createKey(EntityThanos.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> SNAPPING_TIME = EntityDataManager.createKey(EntityThanos.class, DataSerializers.VARINT);
    
	@Nullable
	private Attack attack;
	private final Attack heal;
	private final Attack lightning;
	private final Attack summon;
	private final Attack teleport;
	private final Attack timeWarp;
	
	public EntityThanos(World worldIn) {
		super(worldIn);
		
		this.bossInfo = (BossInfoServer)(new BossInfoServer(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.NOTCHED_6)).setDarkenSky(true);
		this.isImmuneToFire = true;
		
		this.attack = null;
		this.heal = new AttackHeal(this);
		this.lightning = new AttackLightning(this);
		this.summon = new AttackSummon(this);
		this.teleport = new AttackTeleport(this);
		this.timeWarp = new AttackTimeWarp(this);
		
		this.setSize(1.5f, 5f);
	}
	
	@Override
	public void onAddedToWorld() {
		if(!this.world.isRemote) {
			THANI.add(this);
		}
		super.onAddedToWorld();
	}
	@Override
	public void onRemovedFromWorld() {
		if(!this.world.isRemote) {
			THANI.remove(this);
		}
		super.onRemovedFromWorld();
	}

	@Override
	protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MainConfig.thanos_hp);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3300000238418579D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(50.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(6.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MainConfig.thanos_attack);
//        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).setBaseValue(4.0D);
    }
	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, false));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.tasks.addTask(3, new EntityAIAttackMelee(this, 1.5D, false) {
        	@Override
        	public boolean shouldContinueExecuting() {
        		EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();

        		if(entitylivingbase != null && entitylivingbase.getDistance(this.attacker) > 5) {
        			return false;
        		}
        		return super.shouldContinueExecuting();
        	}
        	@Override
        	public boolean shouldExecute() {
        		EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();

        		if(entitylivingbase != null && entitylivingbase.getDistance(this.attacker) > 5) {
        			return false;
        		}
        		return super.shouldExecute();
        	}
        });
        this.tasks.addTask(4, new EntityAIAttackRanged(this, 1.0D, 60, 40.0F));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
	}
	@Override
	protected void entityInit() {
		super.entityInit();
		
		this.getDataManager().register(CASTING, false);
		this.getDataManager().register(CASTING_TIME, 0);
		this.getDataManager().register(SNAPPING, false);
		this.getDataManager().register(SNAPPING_TIME, 0);
		this.getDataManager().register(ACTIVE, true);
	}
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		if(!this.getDataManager().get(ACTIVE).booleanValue() || this.isSnapping()) return;
		
		if(target != null) {
			this.attack = this.chooseAttack(target);
			if(this.attack == null) {
				return;
			} else if(this.attack == this.heal) {
				this.attack.use(this);
			} else {
				this.attack.use(target);
			}
			this.setCasting(true);
			this.attack = null;
		}
	}
	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		this.knockBack(entityIn, 2, 1.0, 1.0);
		
		return super.attackEntityAsMob(entityIn);
	}
	@Override
	public void setSwingingArms(boolean swingingArms) {
	}
	@Override
	public void addTrackingPlayer(EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        
        this.bossInfo.addPlayer(player);
    }
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);

        if(this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
        
        this.heal.readFromNBT(compound);
        this.lightning.readFromNBT(compound);
        this.summon.readFromNBT(compound);
        this.teleport.readFromNBT(compound);
        this.timeWarp.readFromNBT(compound);
        
        if(compound.hasKey("casting")) {
            this.getDataManager().set(CASTING, compound.getBoolean("casting"));
        }
        if(compound.hasKey("casting_time")) {
            this.getDataManager().set(CASTING_TIME, compound.getInteger("casting_time"));
        }
        if(compound.hasKey("active")) {
        	this.getDataManager().set(ACTIVE, compound.getBoolean("active"));
        }
    }
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		
        this.heal.writeToNBT(compound);
        this.lightning.writeToNBT(compound);
        this.summon.writeToNBT(compound);
        this.teleport.writeToNBT(compound);
        this.timeWarp.writeToNBT(compound);
        
        compound.setBoolean("casting", this.getDataManager().get(CASTING));
        compound.setInteger("casting_time", this.getDataManager().get(CASTING_TIME));
        compound.setBoolean("active", this.getDataManager().get(ACTIVE));
	}
	@Override
	public void removeTrackingPlayer(EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        
        this.bossInfo.removePlayer(player);
    }
	@Override
	public void setCustomNameTag(String name) {
        super.setCustomNameTag(name);
        
        this.bossInfo.setName(this.getDisplayName());
    }
	@Override
	protected void updateAITasks() {
		super.updateAITasks();
		
		if(!this.getDataManager().get(ACTIVE)) {
			this.bossInfo.setPercent(0);
		}
		
		this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
		
		if(this.isSnapping()) {
			EntityPlayer entityIn = this.world.getClosestPlayerToEntity(this, 256);
			if(entityIn != null) {
				this.getLookHelper().setLookPositionWithEntity(entityIn, 30f, 30f);
			}
		}
	}
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		
		if(this.getCastingTime() > 50) {
			this.setCasting(false);
		}
		if(this.getDataManager().get(CASTING)) {
			this.setCastingTime(this.getCastingTime() + 1);
		} else {
			this.setCastingTime(0);
		}
		if(this.getSnappingTime() < 200 && this.isSnapping()) {
			this.setSnappingTime(this.getSnappingTime() + 1);
		} else {
			this.setSnapping(false);
			this.setSnappingTime(200);
		}
		
		this.heal.update();
		this.lightning.update();
		this.summon.update();
		this.teleport.update();
		this.timeWarp.update();
	}
	@Override
	public boolean hitByEntity(Entity entityIn) {
		if(this.heal.stillUsing()) {
			this.heal.backfire();
		}
		if(this.lightning.stillUsing()) {
			this.lightning.backfire();
		}
		
		this.getDataManager().set(ACTIVE, true);
		
		return super.hitByEntity(entityIn);
	}
	
	public Attack chooseAttack(EntityLivingBase target) {
		List<Attack> attacks = new LinkedList<Attack>();

		if(this.lightning.canUse(target)) {
			attacks.add(this.lightning);
		}
		if(this.summon.canUse(target)) {
			attacks.add(this.summon);
		}
		if(this.timeWarp.canUse(target)) {
			attacks.add(this.timeWarp);
		}
		if(this.teleport.canUse(target)) {
			attacks.add(this.teleport);
		}
		if(this.heal.canUse(target)) {
			attacks.add(this.heal);
		}
		
		try {
			return attacks.get(this.rand.nextInt(attacks.size()));
		} catch(IllegalArgumentException exception) {
			return null;
		}
	}
	public void setCastingTime(int value) {
		this.getDataManager().set(CASTING_TIME, value);
	}
	public int getCastingTime() {
		return this.getDataManager().get(CASTING_TIME).intValue();
	}
	public void setCasting(boolean value) {
		this.getDataManager().set(CASTING, value);
	}
	public boolean isCasting() {
		return this.getDataManager().get(CASTING).booleanValue();
	}
	public void setSnappingTime(int value) {
		this.getDataManager().set(SNAPPING_TIME, value);
	}
	public int getSnappingTime() {
		return this.getDataManager().get(SNAPPING_TIME).intValue();
	}
	public void setSnapping(boolean value) {
		this.getDataManager().set(SNAPPING, value);
	}
	public boolean isSnapping() {
		return this.getDataManager().get(SNAPPING).booleanValue();
	}

	@Override
	public boolean isNonBoss() {
		return false;
	}
	public void snap() {
		EntityPlayer entityIn = this.world.getClosestPlayerToEntity(this, 512);
		if(entityIn != null) {
			this.getLookHelper().setLookPositionWithEntity(entityIn, 0, 0);
		}
		
		this.setSnapping(true);
		this.setSnappingTime(0);
		this.getDataManager().set(ACTIVE, false);
		
		// thanks to revive mod for the action scheduler lol
		ActionScheduler.scheduleTask(75, () -> {
			this.world.playSound(null, this.getPosition(), Main.SNAP, SoundCategory.NEUTRAL, 200.0f, 1.0f);
		});
		ActionScheduler.scheduleTask(100, () -> {
			if(!this.world.isRemote) {
				Iterator<String> it = StorageHelper.getTeamSet().iterator();
				try {
					while(it.hasNext()) {
						List<UUID> players = StorageHelper.getTeamPlayers(it.next());
						if(players.size() > 1) {
							UUID uuid = players.get(Math.max(0, this.rand.nextInt(players.size())));
							EntityPlayer player = this.world.getPlayerEntityByUUID(uuid);
							if(player == null) {
								this.getServer().sendMessage(new TextComponentTranslation("entity.thanos.killerr", uuid));
							} else {
								if(this.getEntityWorld() instanceof WorldServer) {
									((WorldServer) this.getEntityWorld()).addWeatherEffect(new EntityLightningBolt(this.getEntityWorld(), player.posX, player.posY, player.posZ, true));
								}
								player.onKillCommand();
							}
						}
					}
				} catch(ConcurrentModificationException e) {
					e.printStackTrace();
				}
			}
			this.getDataManager().set(ACTIVE, true);
		});
	}
	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		
		AltarHandler.showStar = true;
	}
	@Override
	protected void onDeathUpdate() {
        if(this.deathTime >= 90 && this.deathTime <= 200) {
    		this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX, this.posY, this.posZ, 0, 0, 0);
        }

        boolean flag = this.world.getGameRules().getBoolean("doMobLoot");
        int i = 500;

        if(!this.world.isRemote) {
            if(this.deathTime > 150 && this.deathTime % 5 == 0 && flag) {
                this.dropExperience(MathHelper.floor((float)i * 0.08F));
            }
            
            if(this.deathTime == 1) {
                this.world.playBroadcastSound(1028, new BlockPos(this), 0);
            }
        }
        
		if(this.deathTime == 200 && !this.world.isRemote) {
			for(int j = 0; j < 6; ++ j) {
				double angle = 60 * j;
				Entity entityIn = new EntityItem(this.world, this.posX, this.posY, this.posZ) {
					@Override
					public void onEntityUpdate() {
						this.addVelocity(0, EntityMandala.y(this.ticksExisted, 0.1f, 128, 2), 0);
						
						super.onEntityUpdate();
					}
				};
				((EntityItem) entityIn).setPickupDelay(64);
				switch(j) {
					case 0:
						((EntityItem) entityIn).setItem(new ItemStack(ItemsThanos.MIND_STONE));
						break;
					case 1:
						((EntityItem) entityIn).setItem(new ItemStack(ItemsThanos.POWER_STONE));
						break;
					case 2:
						((EntityItem) entityIn).setItem(new ItemStack(ItemsThanos.REALITY_STONE));
						break;
					case 3:
						((EntityItem) entityIn).setItem(new ItemStack(ItemsThanos.SOUL_STONE));
						break;
					case 4:
						((EntityItem) entityIn).setItem(new ItemStack(ItemsThanos.SPACE_STONE));
						break;
					case 5:
					default:
						((EntityItem) entityIn).setItem(new ItemStack(ItemsThanos.TIME_STONE));
						break;
				}
				entityIn.addVelocity(Math.cos(angle) / 2, 0.01f, Math.sin(angle) / 2);
				this.world.spawnEntity(entityIn);
			}	
		}

        
        if(this.deathTime == 200 && !this.world.isRemote) {
            if(flag) {
                this.dropExperience(MathHelper.floor((float)i * 0.2F));
            }

            this.setDead();
        }
        
        ++this.deathTime;
	}
	private void dropExperience(int count) {
        while (count > 0) {
            int i = EntityXPOrb.getXPSplit(count);
            count -= i;
            this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, i));
        }
    }
	@Override
	protected boolean canDespawn() {
		return false;
	}
}