package thanos.entity.boss.attack;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.math.BlockPos;
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
		if(this.runningTicks % 5 == 0) {
			if(this.entityThanos.getEntityWorld() instanceof WorldServer) {
                double interpolation = (this.runningTicks + 20) / 100d;
				double x = lerp(this.entityThanos.posX, this.targetPos.getX(), interpolation);
				double y = lerp(this.entityThanos.posY, this.targetPos.getY(), interpolation);
				double z = lerp(this.entityThanos.posZ, this.targetPos.getZ(), interpolation);
				if(this.entityThanos.getPosition().getDistance((int) x, (int) y, (int) z) > 8) {
					((WorldServer) this.entityThanos.getEntityWorld()).addWeatherEffect(new EntityLightningBolt(this.entityThanos.getEntityWorld(), x, y, z, false));
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