// `^`^`^`
// ```java
// /**
//  * This class represents an AI behavior for a flying entity, specifically the EntityIMBird, to perform
//  * different types of attacks while in flight. The AI includes decision-making for executing a flying strike,
//  * a fly-by attack, or other aggressive maneuvers.
//  *
//  * Methods:
//  * - EntityAIFlyingStrike(EntityIMBird entity): Constructor that initializes the AI with the given bird entity.
//  * - shouldExecute(): Determines if the AI should begin executing, based on the entity's current goal.
//  * - shouldContinueExecuting(): Checks if the AI should continue executing, which is the same condition as shouldExecute().
//  * - updateTask(): Updates the task each tick, deciding if a flying strike should be performed.
//  * - doStrike(): Handles the logic for performing a flying strike, including choosing the type of attack based on chances.
//  * - doFlyByAttack(EntityLivingBase entity): Performs a fly-by attack on the target entity, dealing damage.
//  *
//  * The AI uses a simple decision-making process based on random chances to determine which action to take when
//  * attacking a target. It supports transitioning between different AI goals such as stabilizing flight after an attack,
//  * tackling a target, or picking up a target.
//  */
// ```
// 
// This summary provides an overview of the class's purpose and describes each method's functionality within the `EntityAIFlyingStrike` class. It outlines the decision-making process for the flying entity's attacks and the conditions under which the AI decides to execute or continue an attack.
// `^`^`^`

package invmod.entity.ai;

//NOOB HAUS:Done

import invmod.entity.Goal;
import invmod.entity.monster.EntityIMBird;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIFlyingStrike extends EntityAIBase {
	private EntityIMBird theEntity;

	public EntityAIFlyingStrike(EntityIMBird entity) {
		this.theEntity = entity;
	}

	@Override
	public boolean shouldExecute() {
		return (this.theEntity.getAIGoal() == Goal.FLYING_STRIKE) || (this.theEntity.getAIGoal() == Goal.SWOOP);
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.shouldExecute();
	}

	@Override
	public void updateTask() {
		if (this.theEntity.getAIGoal() == Goal.FLYING_STRIKE)
			this.doStrike();
	}

	private void doStrike() {
		EntityLivingBase target = this.theEntity.getAttackTarget();
		if (target == null) {
			this.theEntity.transitionAIGoal(Goal.NONE);
			return;
		}

		float flyByChance = 1.0F;
		float tackleChance = 0.0F;
		float pickUpChance = 0.0F;
		if (this.theEntity.getClawsForward()) {
			flyByChance = 0.5F;
			tackleChance = 100.0F;
			pickUpChance = 1.0F;
		}

		float pE = flyByChance + tackleChance + pickUpChance;
		float r = this.theEntity.world.rand.nextFloat();
		if (r <= flyByChance / pE) {
			this.doFlyByAttack(target);
			this.theEntity.transitionAIGoal(Goal.STABILISE);
			this.theEntity.setClawsForward(false);
		} else if (r <= (flyByChance + tackleChance) / pE) {
			this.theEntity.transitionAIGoal(Goal.TACKLE_TARGET);
			this.theEntity.setClawsForward(false);
		} else {
			this.theEntity.transitionAIGoal(Goal.PICK_UP_TARGET);
		}
	}

	private void doFlyByAttack(EntityLivingBase entity) {
		this.theEntity.attackEntityAsMob(entity, 5);
	}
}