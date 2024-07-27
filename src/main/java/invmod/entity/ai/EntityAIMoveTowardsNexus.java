// `^`^`^`
// ```java
// /**
//  * This class is part of the 'invmod' package and extends the EntityAIBase class to define AI behavior for in-game entities.
//  * The AI behavior defined in this class is for entities to move towards a specific target, referred to as the Nexus.
//  *
//  * Class: EntityAIMoveTowardsNexus
//  * Package: invmod.entity.ai
//  * 
//  * Purpose:
//  * - To guide EntityIMLiving instances towards a designated Nexus point within the game world.
//  * 
//  * Methods:
//  * - EntityAIMoveTowardsNexus(EntityIMLiving creature): Constructor that initializes the AI with the given creature and sets the mutex bits to control AI execution.
//  * - shouldExecute(): Returns true if the Nexus exists, indicating that the AI should start executing.
//  * - shouldContinueExecuting(): Returns true if the entity is more than 4 blocks away from the target (16 square distance), indicating that the AI should keep executing.
//  * - startExecuting(): Sets the target to the Nexus's position and resets the pathSetTicks counter, preparing the entity to start moving towards the Nexus.
//  * - resetTask(): Resets the target to the origin point and pathSetTicks counter, effectively stopping the entity's movement towards the Nexus.
//  * - updateTask(): Updates the entity's path every 40 ticks to move towards the Nexus if the path is not already set.
//  *
//  * Usage:
//  * - This AI is used to make entities in the game move towards a central point, the Nexus, for strategic gameplay elements such as defense or attack.
//  */
// ```
// This summary provides an overview of the class's purpose and functionality, as well as descriptions of each method and their roles within the AI behavior.
// `^`^`^`

package invmod.entity.ai;

import invmod.entity.EntityIMLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class EntityAIMoveTowardsNexus extends EntityAIBase {

	private final EntityIMLiving theEntityIM;
	private BlockPos target = BlockPos.ORIGIN;
	private int pathSetTicks = 0;

	public EntityAIMoveTowardsNexus(EntityIMLiving creature) {
		this.theEntityIM = creature;
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		return this.theEntityIM.getNexus() != null;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.theEntityIM.getDistanceSq(this.target) >= 16d;
	}

	@Override
	public void startExecuting() {
		this.target = this.theEntityIM.getNexus().getPos();
		this.pathSetTicks = 0;
	}

	@Override
	public void resetTask() {
		this.target = BlockPos.ORIGIN;
		this.pathSetTicks = 0;
	}

	@Override
	public void updateTask() {
		if (this.pathSetTicks-- == 0) {
			boolean pathSet = this.theEntityIM.getNavigator().tryMoveToXYZ(this.target.getX(), this.target.getY(),
					this.target.getZ(), 1f);
			this.pathSetTicks = 40;
		}
	}

}
