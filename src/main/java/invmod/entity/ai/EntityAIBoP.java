// `^`^`^`
// ```java
// /**
//  * This class is an AI behavior module for the EntityIMFlying entity in the invmod mod.
//  * It manages the entity's goals and attack strategies based on various conditions.
//  *
//  * Methods:
//  * - EntityAIBoP(EntityIMFlying entity): Constructor that initializes the AI with the given entity.
//  * - shouldExecute(): Always returns true, indicating that the AI should always be running.
//  * - startExecuting(): Resets the timers for tracking the duration of the current goal and target.
//  * - updateTask(): Updates the AI's state by checking for changes in goals and targets, managing the entity's behavior accordingly.
//  * - chooseTargetAction(EntityLivingBase target): Determines the appropriate action towards a target based on distance and movement state.
//  *
//  * The AI uses a patience timer to switch between different goals such as attacking or staying at range. It also tracks the health of the entity
//  * and changes in the attack target to adjust its strategy. If the entity has no target and a nexus is present, it will attempt to break the nexus;
//  * otherwise, it will enter a chill state. The AI prefers walking and landing paths when chilling. When a target is present, it chooses between
//  * melee attacks or staying at range based on proximity and a random chance.
//  */
// package invmod.entity.ai;
// 
// // Import statements...
// 
// public class EntityAIBoP extends EntityAIBase {
//     // Class fields and constructor...
//     
//     // Method implementations...
// }
// ```
// `^`^`^`

package invmod.entity.ai;

//NOOB HAUS: DONE - not certain if actually used ?

import invmod.entity.Goal;
import invmod.entity.INavigationFlying;
import invmod.entity.MoveState;
import invmod.entity.monster.EntityIMFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIBoP extends EntityAIBase {
	private static final int PATIENCE = 500;
	private EntityIMFlying theEntity;
	private int timeWithGoal;
	private int timeWithTarget;
	private int patienceTime;
	private float lastHealth;
	private Goal lastGoal;
	private EntityLivingBase lastTarget;

	public EntityAIBoP(EntityIMFlying entity) {
		this.theEntity = entity;
		this.timeWithGoal = 0;
		this.patienceTime = 0;
		this.lastHealth = entity.getHealth();
		this.lastGoal = entity.getAIGoal();
		this.lastTarget = entity.getAttackTarget();
	}

	@Override
	public boolean shouldExecute() {
		return true;
	}

	@Override
	public void startExecuting() {
		this.timeWithGoal = 0;
		this.patienceTime = 0;
	}

	@Override
	public void updateTask() {
		this.timeWithGoal += 1;
		if (this.theEntity.getAIGoal() != this.lastGoal) {
			this.lastGoal = this.theEntity.getAIGoal();
			this.timeWithGoal = 0;
		}

		this.timeWithTarget += 1;
		if (this.theEntity.getAttackTarget() != this.lastTarget) {
			this.lastTarget = this.theEntity.getAttackTarget();
			this.timeWithTarget = 0;
		}

		if (this.theEntity.getAttackTarget() == null) {
			if (this.theEntity.getNexus() != null) {
				if (this.theEntity.getAIGoal() != Goal.BREAK_NEXUS) {
					this.theEntity.transitionAIGoal(Goal.BREAK_NEXUS);
				}

			} else if (this.theEntity.getAIGoal() != Goal.CHILL) {
				this.theEntity.transitionAIGoal(Goal.CHILL);
				this.theEntity.getNavigatorNew().clearPath();
				this.theEntity.getNavigatorNew().setMovementType(INavigationFlying.MoveType.PREFER_WALKING);
				this.theEntity.getNavigatorNew().setLandingPath();
			}

		} else if ((this.theEntity.getAIGoal() == Goal.CHILL) || (this.theEntity.getAIGoal() == Goal.NONE)) {
			this.chooseTargetAction(this.theEntity.getAttackTarget());
		}

		if (this.theEntity.getAIGoal() != Goal.STAY_AT_RANGE) {
			if (this.theEntity.getAIGoal() == Goal.MELEE_TARGET) {
				if (this.timeWithGoal > 600) {
					this.theEntity.transitionAIGoal(Goal.STAY_AT_RANGE);
				}
			}
		}
	}

	protected void chooseTargetAction(EntityLivingBase target) {
		if (this.theEntity.getMoveState() != MoveState.FLYING) {
			if ((this.theEntity.getDistance(target) < 10.0F) && (this.theEntity.world.rand.nextFloat() > 0.3F)) {
				this.theEntity.transitionAIGoal(Goal.MELEE_TARGET);
				return;
			}
		}
		this.theEntity.transitionAIGoal(Goal.STAY_AT_RANGE);
	}
}