// `^`^`^`
// ```java
// /**
//  * This class is part of the AI system for the EntityIMFlying entity in the invmod mod, which adds various features to Minecraft.
//  * It defines the AI behavior for stabilizing the flight of the EntityIMFlying entities.
//  *
//  * Class: EntityAIStabiliseFlying
//  * Package: invmod.entity.ai
//  * Superclass: EntityAIBase (Minecraft class for AI behaviors)
//  *
//  * Methods:
//  * - EntityAIStabiliseFlying(EntityIMFlying entity, int stabiliseTime): Constructor that initializes the AI with the flying entity and the time it should take to stabilize.
//  * - shouldExecute(): Checks if the AI should start executing, which is true if the entity's current goal is to stabilize.
//  * - shouldContinueExecuting(): Determines if the AI should continue executing. It stops the AI if the stabilization time has been reached, resetting the entity's goal and pitch bias.
//  * - startExecuting(): Called when the AI starts executing. It resets the time counter, clears any existing path, sets the movement type to prefer flying, and applies an initial pitch bias to simulate the entity stabilizing.
//  * - updateTask(): Called every tick while the AI is executing. It increments the time counter and resets the pitch bias after an initial period to simulate the entity having stabilized.
//  *
//  * Usage:
//  * This AI component is used to make the EntityIMFlying stabilize in the air after certain actions or events. It is part of the entity's AI management system and interacts with other AI components and the entity's navigation system.
//  */
// ```
// `^`^`^`

package invmod.entity.ai;

import invmod.entity.Goal;
import invmod.entity.INavigationFlying;
import invmod.entity.monster.EntityIMFlying;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIStabiliseFlying extends EntityAIBase {
	private static int INITIAL_STABILISE_TIME = 50;
	private EntityIMFlying theEntity;
	private int time;
	private int stabiliseTime;

	public EntityAIStabiliseFlying(EntityIMFlying entity, int stabiliseTime) {
		this.theEntity = entity;
		this.time = 0;
		this.stabiliseTime = stabiliseTime;
	}

	@Override
	public boolean shouldExecute() {
		return this.theEntity.getAIGoal() == Goal.STABILISE;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (this.time >= this.stabiliseTime) {
			this.theEntity.transitionAIGoal(Goal.NONE);
			this.theEntity.getNavigatorNew().setPitchBias(0.0F, 0.0F);
			return false;
		}
		return true;
	}

	@Override
	public void startExecuting() {
		this.time = 0;
		INavigationFlying nav = this.theEntity.getNavigatorNew();
		nav.clearPath();
		nav.setMovementType(INavigationFlying.MoveType.PREFER_FLYING);
		nav.setPitchBias(20.0F, 0.5F);
	}

	@Override
	public void updateTask() {
		this.time += 1;
		if (this.time == INITIAL_STABILISE_TIME) {
			this.theEntity.getNavigatorNew().setPitchBias(0.0F, 0.0F);
		}
	}
}