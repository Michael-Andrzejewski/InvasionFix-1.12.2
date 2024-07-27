// `^`^`^`
// ```java
// /**
//  * This class defines the AI behavior for a custom creeper entity in a Minecraft mod to control its swelling action.
//  * The creeper will swell (prepare to explode) when certain conditions are met, specifically targeting certain entities.
//  *
//  * Class: EntityAICreeperIMSwell
//  * Package: invmod.entity.ai
//  *
//  * Constructor:
//  * - EntityAICreeperIMSwell(EntityIMCreeper par1EntityCreeper): Initializes the AI with the given creeper entity and sets the mutex bits to control AI execution order.
//  *
//  * Methods:
//  * - boolean shouldExecute(): Determines if the AI should start executing. It checks if the creeper is already in the state of swelling or if a target entity is within a certain range and is of a specific type (player or allied entity).
//  * - void startExecuting(): Called when the AI should start executing. Clears the creeper's current path and sets the target entity.
//  * - void resetTask(): Resets the task, clearing the target entity.
//  * - void updateTask(): Updates the task each tick. If the target is null, out of range, or not visible, it stops the creeper from swelling. Otherwise, it sets the creeper state to swelling.
//  *
//  * This AI component is essential for the creeper's behavior, making it a critical part of the entity's interaction with the world and the player.
//  */
// ```
// `^`^`^`

package invmod.entity.ai;

//NOOB HAUS: Done 

import invmod.entity.ally.EntityIMWolf;
import invmod.entity.monster.EntityIMCreeper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class EntityAICreeperIMSwell extends EntityAIBase {
	EntityIMCreeper theEntity;
	EntityLivingBase targetEntity;

	public EntityAICreeperIMSwell(EntityIMCreeper par1EntityCreeper) {
		this.theEntity = par1EntityCreeper;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		EntityLivingBase entityliving = this.theEntity.getAttackTarget();

		return (this.theEntity.getCreeperState() > 0)
				|| ((entityliving != null) && (this.theEntity.getDistanceSq(entityliving) < 9.0D)
						&& ((entityliving.getClass() == EntityPlayer.class)
								|| (entityliving.getClass() == EntityIMWolf.class)
								|| (entityliving.getClass() == EntityPlayerMP.class)));
	}

	@Override
	public void startExecuting() {
		this.theEntity.getNavigatorNew().clearPath();
		this.targetEntity = this.theEntity.getAttackTarget();
	}

	@Override
	public void resetTask() {
		this.targetEntity = null;
	}

	@Override
	public void updateTask() {
		if (this.targetEntity == null) {
			this.theEntity.setCreeperState(-1);
			return;
		}

		if (this.theEntity.getDistanceSq(this.targetEntity) > 49.0D) {
			this.theEntity.setCreeperState(-1);
			return;
		}

		if (!this.theEntity.getEntitySenses().canSee(this.targetEntity)) {
			this.theEntity.setCreeperState(-1);
			return;
		}
		this.theEntity.setCreeperState(1);
	}
}