package invmod.entity.ai;

import invmod.entity.EntityIMLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class EntityAIStoop extends EntityAIBase {
	
	private EntityIMLiving theEntity;
	private int updateTimer;
	private boolean stopStoop;

	public EntityAIStoop(EntityIMLiving entity){
		this.theEntity = entity;
		this.stopStoop = true;
	}

	@Override
	public boolean shouldExecute(){
		if (--this.updateTimer <= 0){
			this.updateTimer = 10;
			if (this.theEntity.worldObj.getBlockState(this.theEntity.getPosition().up(2)).getMaterial().blocksMovement()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean continueExecuting(){
		return !this.stopStoop;
	}

	@Override
	public void startExecuting(){
		this.theEntity.setSneaking(true);
		this.stopStoop = false;
	}

	@Override
	public void updateTask(){
		if (--this.updateTimer <= 0){
			this.updateTimer = 10;
			if (!this.theEntity.worldObj.getBlockState(this.theEntity.getPosition().up()).getMaterial().blocksMovement()){
				this.theEntity.setSneaking(false);
				this.stopStoop = true;
			}
		}
	}
}