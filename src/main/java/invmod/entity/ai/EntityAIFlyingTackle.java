// `^`^`^`
// ```java
// /**
//  * This class is part of the AI system for the EntityIMFlying entity, which is a flying monster entity in a Minecraft mod. The AI class, EntityAIFlyingTackle, extends the base class for AI tasks, EntityAIBase, and is responsible for handling the "Tackle" behavior of the flying entity.
//  *
//  * Constructor:
//  * - EntityAIFlyingTackle(EntityIMFlying entity): Initializes the AI with the given flying entity.
//  *
//  * Core Methods:
//  * - shouldExecute(): Returns true if the entity's current goal is to tackle a target, indicating that this AI task should start.
//  * - shouldContinueExecuting(): Checks if the target is still valid (not null and not dead) and if the goal is still to tackle the target. If not, it transitions the goal to NONE and returns false, stopping the task.
//  * - startExecuting(): Resets the time counter and sets the entity's navigation to prefer walking if there is a target.
//  * - updateTask(): If the entity is not flying, it transitions the AI goal to MELEE_TARGET, changing the behavior from tackling to melee attacking.
//  *
//  * This AI task is a part of the entity's behavior that allows it to switch between flying and tackling targets based on certain conditions, contributing to the dynamic nature of the entity's interactions with the player and the environment.
//  */
// ```
// `^`^`^`

package invmod.entity.ai;

//NOOB HAUS: Done

import invmod.entity.Goal;
import invmod.entity.INavigationFlying;
import invmod.entity.MoveState;
import invmod.entity.monster.EntityIMFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIFlyingTackle extends EntityAIBase {
	private EntityIMFlying theEntity;
	private int time;

	public EntityAIFlyingTackle(EntityIMFlying entity) {
		this.theEntity = entity;
		this.time = 0;
	}

	@Override
	public boolean shouldExecute() {
		return this.theEntity.getAIGoal() == Goal.TACKLE_TARGET;
	}

	@Override
	public boolean shouldContinueExecuting() {
		EntityLivingBase target = this.theEntity.getAttackTarget();
		if ((target == null) || (target.isDead)) {
			this.theEntity.transitionAIGoal(Goal.NONE);
			return false;
		}

		if (this.theEntity.getAIGoal() != Goal.TACKLE_TARGET) {
			return false;
		}
		return true;
	}

	@Override
	public void startExecuting() {
		this.time = 0;
		EntityLivingBase target = this.theEntity.getAttackTarget();
		if (target != null) {
			this.theEntity.getNavigatorNew().setMovementType(INavigationFlying.MoveType.PREFER_WALKING);
		}
	}

	@Override
	public void updateTask() {
		if (this.theEntity.getMoveState() != MoveState.FLYING) {
			this.theEntity.transitionAIGoal(Goal.MELEE_TARGET);
		}
	}
}