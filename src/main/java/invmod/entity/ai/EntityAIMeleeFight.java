// `^`^`^`
// ```java
// /**
//  * This class represents an AI module for melee combat behavior in entities, specifically for the EntityIMMob class.
//  * It extends the EntityAIMeleeAttack class and provides additional functionality for retreat and disengagement based on health.
//  *
//  * Constructor:
//  * - EntityAIMeleeFight(EntityIMMob entity, Class<? extends T> targetClass, int attackDelay, float retreatHealthLossPercent):
//  *   Initializes the AI with the entity, target class, attack delay, and the percentage of health loss at which the entity should retreat.
//  *
//  * Methods:
//  * - shouldExecute(): Determines if the AI should start executing, based on whether the entity has a melee target.
//  * - shouldContinueExecuting(): Checks if the AI should continue executing, considering if the entity is waiting for a transition or still has a target.
//  * - startExecuting(): Resets internal counters and records the entity's starting health when the AI begins execution.
//  * - updateTask(): Updates the disengagement logic and pathfinding, and increments time if the entity has dealt or received damage.
//  * - updatePath(): Updates the entity's path to the current target if necessary.
//  * - updateDisengage(): Checks if the entity should disengage from melee combat and transitions the AI goal if needed.
//  * - isWaitingForTransition(): Checks if the entity is waiting to transition from a melee target goal to leaving melee.
//  * - attackEntity(EntityLivingBase target): Performs an attack on the target and updates damage dealt and invulnerability count.
//  * - shouldLeaveMelee(): Determines if the entity should leave melee combat based on time, damage received, damage dealt, and invulnerability count.
//  *
//  * The AI uses health-based logic to decide when to disengage from combat, providing a more dynamic and realistic combat experience.
//  */
// ```
// `^`^`^`

package invmod.entity.ai;

import invmod.entity.Goal;
import invmod.entity.INavigation;
import invmod.entity.monster.EntityIMMob;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class EntityAIMeleeFight<T extends EntityLivingBase> extends EntityAIMeleeAttack<T> {
	private EntityIMMob theEntity;
	private int time;
	private float startingHealth;
	private int damageDealt;
	private int invulnCount;
	private float retreatHealthLossPercent;

	public EntityAIMeleeFight(EntityIMMob entity, Class<? extends T> targetClass, int attackDelay,
			float retreatHealthLossPercent) {
		super(entity, targetClass, attackDelay);
		this.theEntity = entity;
		this.time = 0;
		this.startingHealth = 0.0F;
		this.damageDealt = 0;
		this.invulnCount = 0;
		this.retreatHealthLossPercent = retreatHealthLossPercent;
	}

	@Override
	public boolean shouldExecute() {
		Entity target = this.theEntity.getAttackTarget();
		return (this.theEntity.getAIGoal() == Goal.MELEE_TARGET) && (target != null)
				&& (target.getClass().isAssignableFrom(this.getTargetClass()));
	}

	@Override
	public boolean shouldContinueExecuting() {
		return ((this.theEntity.getAIGoal() == Goal.MELEE_TARGET) || (this.isWaitingForTransition()))
				&& (this.theEntity.getAttackTarget() != null);
	}

	@Override
	public void startExecuting() {
		this.time = 0;
		this.startingHealth = this.theEntity.getHealth();
		this.damageDealt = 0;
		this.invulnCount = 0;
	}

	@Override
	public void updateTask() {
		this.updateDisengage();
		this.updatePath();
		super.updateTask();
		if ((this.damageDealt > 0) || (this.startingHealth - this.theEntity.getHealth() > 0.0F))
			this.time += 1;
	}

	public void updatePath() {
		INavigation nav = this.theEntity.getNavigatorNew();
		if (this.theEntity.getAttackTarget() != nav.getTargetEntity()) {
			nav.clearPath();
			nav.autoPathToEntity(this.theEntity.getAttackTarget());
		}
	}

	protected void updateDisengage() {
		if ((this.theEntity.getAIGoal() == Goal.MELEE_TARGET) && (this.shouldLeaveMelee())) {
			this.theEntity.transitionAIGoal(Goal.LEAVE_MELEE);
		}
	}

	protected boolean isWaitingForTransition() {
		return (this.theEntity.getAIGoal() == Goal.LEAVE_MELEE)
				&& (this.theEntity.getPrevAIGoal() == Goal.MELEE_TARGET);
	}

	@Override
	protected void attackEntity(EntityLivingBase target) {
		float h = target.getHealth();
		super.attackEntity(target);
		h -= target.getHealth();
		if (h <= 0.0F) {
			this.invulnCount += 1;
		}
		this.damageDealt = ((int) (this.damageDealt + h));
	}

	protected boolean shouldLeaveMelee() {
		float damageReceived = this.startingHealth - this.theEntity.getHealth();
		if ((this.time > 40) && (damageReceived > this.theEntity.getMaxHealth() * this.retreatHealthLossPercent)) {
			return true;
		}
		if ((this.time > 100) && (damageReceived - this.damageDealt > this.theEntity.getMaxHealth() * 0.66F
				* this.retreatHealthLossPercent)) {
			return true;
		}
		if (this.invulnCount >= 2) {
			return true;
		}
		return false;
	}
}
