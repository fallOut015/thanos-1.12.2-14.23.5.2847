package thanos.entity.boss.attack;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import thanos.entity.boss.EntityThanos;

public abstract class Attack {
	final EntityThanos entityThanos;
	final String id;
	int cooldown;
	int usesLeft;
	int runningTicks;
	boolean canapply;
	
	protected Attack(EntityThanos entityThanos, String id) {
		this.entityThanos = entityThanos;
		this.id = id;
		
		this.cooldown = 0;
		this.usesLeft = 100;
		this.runningTicks = -1;
		this.canapply = false;
	}
	
	public final void use(EntityLivingBase target) {
		if(this.canUse(target)) {
			this.cooldown = this.getCooldown() + this.usesLeft;
			-- this.usesLeft;
			this.runningTicks = 0;
			this.action(target);
		}
	}
	public final boolean stillUsing() {
		return this.canapply;
	}
	protected final boolean isCooledDown() {
		return this.cooldown <= 0;
	}
	protected final boolean hasUsesLeft() {
		return this.usesLeft > 0;
	}
	protected final boolean thanosAlive() {
		return this.entityThanos.getHealth() > 0;
	}
	protected final boolean thanosHurt() {
		return this.entityThanos.getHealth() < this.entityThanos.getMaxHealth() / 2;
	}
	protected final boolean thanosWeak() {
		return this.entityThanos.getHealth() < this.entityThanos.getMaxHealth() / 3;
	}
	public final void writeToNBT(NBTTagCompound compound) {
		compound.setInteger(this.id + ".cooldown", this.cooldown);
		compound.setInteger(this.id + ".usesLeft", this.usesLeft);
	}
	public final void readFromNBT(NBTTagCompound compound) {
		if(compound.hasKey(this.id + ".cooldown")) {
			this.cooldown = compound.getInteger(this.id + ".cooldown");
		}
		if(compound.hasKey(this.id + ".usesLeft")) {
			this.cooldown = compound.getInteger(this.id + ".usesLeft");
		}
	}
	public void update() {
		if(this.cooldown > 0) {
			-- this.cooldown;
		}
		
		++ this.runningTicks;
		
		if(this.canapply) {
			this.apply();
		}
	}
	protected void damageStone() {
		this.usesLeft -= 10;
	}

	abstract int getCooldown();
	abstract void action(EntityLivingBase target);
	abstract public boolean canUse(EntityLivingBase target);
	abstract void apply();
	abstract public void backfire();
}