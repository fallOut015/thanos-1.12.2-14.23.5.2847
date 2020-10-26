package thanos.entity.boss.attack;

import net.minecraft.entity.EntityLivingBase;
import thanos.entity.boss.EntityThanos;
import thanos.entity.effect.EntityMandala;

public class AttackTimeWarp extends Attack {
	public AttackTimeWarp(EntityThanos entityThanos) {
		super(entityThanos, "timeWarp");
	}

	@Override
	void action(EntityLivingBase target) {
		EntityMandala mandala = new EntityMandala(this.entityThanos.getEntityWorld(), this.entityThanos);
		mandala.setPosition(this.entityThanos.posX, this.entityThanos.posY, this.entityThanos.posZ);
		this.entityThanos.getEntityWorld().spawnEntity(mandala);
	}
	@Override
	public boolean canUse(EntityLivingBase target) {
		return this.thanosAlive() && this.isCooledDown() && this.hasUsesLeft() && this.thanosHurt();
	}
	@Override
	void apply() {
	}
	@Override
	int getCooldown() {
		return 480;
	}
	@Override
	public void backfire() {
	}
}