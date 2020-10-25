package thanos.entity.boss.attack;

import net.minecraft.entity.EntityLivingBase;
import thanos.entity.boss.EntityThanos;
import thanos.entity.effect.EntityPortal;

public class AttackTeleport extends Attack {
	public AttackTeleport(EntityThanos entityThanos) {
		super(entityThanos, "teleport");
	}

	@Override
	void action(EntityLivingBase target) {
		EntityPortal to = new EntityPortal(target.getEntityWorld());
		to.setPosition(target.posX, target.posY, target.posZ);
		target.getEntityWorld().spawnEntity(to);
		EntityPortal from = EntityPortal.from(this.entityThanos.getEntityWorld(), to);
		from.setPosition(this.entityThanos.posX, this.entityThanos.posY, this.entityThanos.posZ);
		this.entityThanos.getEntityWorld().spawnEntity(from);
	}
	@Override
	public boolean canUse(EntityLivingBase target) {
		return this.thanosAlive() && this.isCooledDown() && this.hasUsesLeft() && this.thanosWeak() && target.getDistance(this.entityThanos) > 30;
	}
	@Override
	void apply() {
		// TODO Auto-generated method stub
		
	}
	@Override
	int getCooldown() {
		return 160;
	}
	@Override
	public void backfire() {
		// TODO Auto-generated method stub
		
	}
}