package invmod.entity.ai;

import invmod.entity.monster.EntityIMThrower;
import invmod.tileentity.TileEntityNexus;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIRandomBoulder extends EntityAIBase {
	
	private final EntityIMThrower theEntity;
	private int randomAmmo;
	private int timer;

	public EntityAIRandomBoulder(EntityIMThrower entity, int ammo){
		this.theEntity = entity;
		this.randomAmmo = ammo;
		this.timer = 180;
	}

	@Override
	public boolean shouldExecute(){
		if ((this.theEntity.getNexus() != null) && (this.randomAmmo > 0) && (this.theEntity.canThrow())){
			if (--this.timer <= 0) return true;
		}
		return false;
	}

	@Override
	public boolean isInterruptible(){
		return false;
	}

	@Override
	public void startExecuting(){
		this.randomAmmo -= 1;
		this.timer = 240;
		TileEntityNexus nexus = this.theEntity.getNexus();
		int d = (int)(this.theEntity.findDistanceToNexus() * 0.37D);
		if(d == 0) d=1;
		double d0 = nexus.getPos().getX() - d + this.theEntity.getRNG().nextInt(2 * d);
		double d1 = nexus.getPos().getY() - 5 + this.theEntity.getRNG().nextInt(10);
		double d2 = nexus.getPos().getZ() - d + this.theEntity.getRNG().nextInt(2 * d);
		this.theEntity.throwBoulder(d0, d1, d2);
	}
	
}