package thanos.entity.boss.attack;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import thanos.entity.boss.EntityThanos;

public class AttackHeal extends Attack {
	int healamountleft;
	
	public AttackHeal(EntityThanos entityThanos) {
		super(entityThanos, "heal");
	}

	@Override
	void action(EntityLivingBase target) {
		this.canapply = true;
		this.healamountleft = 100;
	}
	@Override
	public boolean canUse(EntityLivingBase target) {
		return !this.stillUsing() && this.thanosAlive() && this.isCooledDown() && this.hasUsesLeft() && this.thanosHurt();
	}
	@Override
	void apply() {
		if(this.runningTicks > 100) {
			this.canapply = false;
			this.runningTicks = -1;
		}
		if(this.runningTicks % 10 == 0) {
			this.entityThanos.heal(10);
			this.healamountleft -= 10;
			this.entityThanos.getEntityWorld().spawnParticle(EnumParticleTypes.HEART, this.entityThanos.posX, this.entityThanos.posY + 4, this.entityThanos.posZ, 0, 1, 0);
		}		
	}
	@Override
	int getCooldown() {
		return 640;
	}
	@Override
	public void backfire() {
		this.canapply = false;
		this.runningTicks = -1;
		
		this.entityThanos.attackEntityFrom(DamageSource.MAGIC, Math.max(0, this.healamountleft));
	}
}