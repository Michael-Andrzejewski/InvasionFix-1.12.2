// `^`^`^`
// ```java
// /**
//  * This class is part of the AI system for the EntityIMFlying entity in the invmod mod, which adds new creatures and AI behaviors to Minecraft.
//  * The EntityAICircleTarget AI behavior enables the EntityIMFlying to circle around its target at a specified distance and height.
//  *
//  * Methods:
//  * - EntityAICircleTarget(EntityIMFlying, int, float, float): Constructor that initializes the AI with the entity, patience level, preferred height, and radius for circling.
//  * - shouldExecute(): Determines if the AI should start executing, which is true when the entity's goal is to stay at range and it has a target.
//  * - shouldContinueExecuting(): Checks if the AI should continue executing, based on the entity's current and previous goals and if it has a target.
//  * - startExecuting(): Sets up the circling behavior by configuring the entity's navigation to prefer flying and setting a circling path around the target.
//  * - updateTask(): Updates the AI's task, managing the patience timer and transitioning the entity's goal when necessary.
//  * - isWaitingForTransition(): Helper method to check if the entity is waiting to transition from staying at range to finding an attack opportunity.
//  *
//  * The AI uses a patience timer to determine how long the entity will circle the target before changing its goal to find an attack opportunity.
//  */
// package invmod.entity.ai;
// 
// // ... (rest of the code)
// ```
// `^`^`^`

package invmod.entity.ai;

import invmod.entity.Goal;
import invmod.entity.INavigationFlying;
import invmod.entity.monster.EntityIMFlying;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAICircleTarget extends EntityAIBase {
	private static final int ATTACK_SEARCH_TIME = 400;
	private EntityIMFlying theEntity;
	private int time;
	private int patienceTime;
	private int patience;
	private float preferredHeight;
	private float preferredRadius;

	public EntityAICircleTarget(EntityIMFlying entity, int patience, float preferredHeight, float preferredRadius) {
		this.theEntity = entity;
		this.time = 0;
		this.patienceTime = 0;
		this.patience = patience;
		this.preferredHeight = preferredHeight;
		this.preferredRadius = preferredRadius;
	}

	@Override
	public boolean shouldExecute() {
		return (this.theEntity.getAIGoal() == Goal.STAY_AT_RANGE) && (this.theEntity.getAttackTarget() != null);
	}

	@Override
	public boolean shouldContinueExecuting() {
		return ((this.theEntity.getAIGoal() == Goal.STAY_AT_RANGE) || (this.isWaitingForTransition()))
				&& (this.theEntity.getAttackTarget() != null);
	}

	@Override
	public void startExecuting() {
		INavigationFlying nav = this.theEntity.getNavigatorNew();
		nav.setMovementType(INavigationFlying.MoveType.PREFER_FLYING);
		nav.setCirclingPath(this.theEntity.getAttackTarget(), this.preferredHeight, this.preferredRadius);
		this.time = 0;
		int extraTime = (int) (4.0F * nav.getDistanceToCirclingRadius());
		if (extraTime < 0) {
			extraTime = 0;
		}
		this.patienceTime = (extraTime + this.theEntity.world.rand.nextInt(this.patience) + this.patience / 3);
	}

	@Override
	public void updateTask() {
		this.time += 1;
		if (this.theEntity.getAIGoal() == Goal.STAY_AT_RANGE) {
			this.patienceTime -= 1;
			if (this.patienceTime <= 0) {
				this.theEntity.transitionAIGoal(Goal.FIND_ATTACK_OPPORTUNITY);
				this.patienceTime = 400;
			}
		} else if (this.isWaitingForTransition()) {
			this.patienceTime -= 1;
			if (this.patienceTime > 0)
				;
		}
	}

	protected boolean isWaitingForTransition() {
		return (this.theEntity.getPrevAIGoal() == Goal.STAY_AT_RANGE)
				&& (this.theEntity.getAIGoal() == Goal.FIND_ATTACK_OPPORTUNITY);
	}
}