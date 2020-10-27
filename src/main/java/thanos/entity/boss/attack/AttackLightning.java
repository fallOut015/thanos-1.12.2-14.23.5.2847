package thanos.entity.boss.attack;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import thanos.entity.boss.EntityThanos;

public class AttackLightning extends Attack {
	BlockPos targetPos;
	
	public AttackLightning(EntityThanos entityThanos) {
		super(entityThanos, "lightning");
	}

	@Override
	void action(EntityLivingBase target) {
		this.canapply = true;
		this.targetPos = target.getPosition();
	}
	@Override
	public boolean canUse(EntityLivingBase target) {
		return this.thanosAlive() && this.isCooledDown() && this.hasUsesLeft();
	}
	@Override
	void apply() {
		if(this.runningTicks > 80) {
			this.canapply = false;
			this.runningTicks = -1;
		}
		if(this.runningTicks % 40 == 0) {
			World world = this.entityThanos.getEntityWorld();
			if(world instanceof WorldServer) {
				BlockPos thanospos = this.entityThanos.getPosition();
				
				List<BlockPos> lightningpositions = new ArrayList<BlockPos>();
				
				List<EntityPlayer> players = world.playerEntities;
				for (EntityPlayer player : players) {
					BlockPos playerpos = player.getPosition();
					double distance = thanospos.getDistance(playerpos.getX(), playerpos.getY(), playerpos.getZ());
					if (distance > 50) {
						continue;
					}
					
					lightningpositions.add(playerpos.toImmutable());
				}
				
				for (BlockPos lightningpos : lightningpositions) {
					((WorldServer) this.entityThanos.getEntityWorld()).addWeatherEffect(new EntityLightningBolt(this.entityThanos.getEntityWorld(), lightningpos.getX(), lightningpos.getY(), lightningpos.getZ(), false));
				}
			}
		}
	}
	@Override
	int getCooldown() {
		return 0;
	}
	@Override
	public void backfire() {
		this.entityThanos.getEntityWorld().addWeatherEffect(new EntityLightningBolt(this.entityThanos.getEntityWorld(), this.entityThanos.posX, this.entityThanos.posY, this.entityThanos.posZ, false));
	}
	
	public static double lerp(double a, double b, double t) {
		return a + (b - a) * t;
	}
}