package thanos.entity.boss.attack;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntitySpider;
import thanos.entity.boss.EntityThanos;

public class AttackSummon extends Attack {
	public AttackSummon(EntityThanos entityThanos) {
		super(entityThanos, "summon");
	}

	@Override
	void action(EntityLivingBase target) {
		for(int i = 0; i < 8; ++ i) {
			float angle = i * 45;
			EntityLiving entityIn;
			if(i % 2 == 0) {
				entityIn = new EntitySpider(target.getEntityWorld());
				entityIn.setAttackTarget(target);
			} else {
				entityIn = new EntityHusk(target.getEntityWorld());
				entityIn.setAttackTarget(target);
			}
			double x = (this.entityThanos.posX + Math.cos(angle) * 5d);
			double z = (this.entityThanos.posZ + Math.sin(angle) * 5d);
			float y = this.entityThanos.getEntityWorld().getHeight((int) x, (int) z);
			entityIn.setPosition(x, y, z);
			if(!target.getEntityWorld().isRemote) {
				target.getEntityWorld().spawnEntity(entityIn);
			}
		}
	}
	@Override
	public boolean canUse(EntityLivingBase target) {
		return this.thanosAlive() && this.isCooledDown() && this.hasUsesLeft() && this.thanosWeak();
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