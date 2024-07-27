// `^`^`^`
// ```java
// /**
//  * This class represents an AI module for melee attacks within a Minecraft mod. It defines the behavior of an in-game
//  * monster entity (EntityIMMob) to perform melee attacks on a specified target entity class.
//  *
//  * Class: EntityAIMeleeAttack<T extends EntityLivingBase>
//  * - Extends Minecraft's EntityAIBase to integrate with the game's AI system.
//  *
//  * Constructor:
//  * - EntityAIMeleeAttack(EntityIMMob entity, Class<? extends T> targetClass, int attackDelay)
//  *   Initializes the AI with the attacking entity, the target class, and the delay between attacks.
//  *
//  * Methods:
//  * - boolean shouldExecute()
//  *   Determines if the AI should begin executing, based on the presence of a target and if the target is within range.
//  *
//  * - void updateTask()
//  *   Called every tick to manage the attack timing and execution against the target.
//  *
//  * - Class<? extends T> getTargetClass()
//  *   Returns the Class object representing the target's class.
//  *
//  * - protected void attackEntity(EntityLivingBase target)
//  *   Performs the actual attack on the target entity.
//  *
//  * - protected boolean canAttackEntity(EntityLivingBase target)
//  *   Checks if the entity can currently attack the target based on attack timing and distance.
//  *
//  * - protected int getAttackTime()
//  *   Retrieves the remaining time until the next attack is allowed.
//  *
//  * - protected void setAttackTime(int time)
//  *   Sets the cooldown time until the next attack can be performed.
//  *
//  * - protected int getAttackDelay()
//  *   Returns the configured delay between attacks.
//  *
//  * - protected void setAttackDelay(int time)
//  *   Sets the delay time between attacks.
//  *
//  * The AI uses a simple timing mechanism to add a delay between attacks and checks for the appropriate conditions
//  * before executing an attack, such as the target being within a certain range and being of the correct entity class.
//  */
// ```
// `^`^`^`

package invmod.entity.ai;

import invmod.entity.Goal;
import invmod.entity.monster.EntityIMMob;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIMeleeAttack<T extends EntityLivingBase> extends EntityAIBase {
	private EntityIMMob theEntity;
	private Class<? extends T> targetClass;
	private float attackRange;
	private int attackDelay;
	private int nextAttack;

	public EntityAIMeleeAttack(EntityIMMob entity, Class<? extends T> targetClass, int attackDelay) {
		this.theEntity = entity;
		this.targetClass = targetClass;
		this.attackDelay = attackDelay;
		this.attackRange = 0.6F;
		this.nextAttack = 0;
	}

	@Override
	public boolean shouldExecute() {
		EntityLivingBase target = this.theEntity.getAttackTarget();
		return (target != null)
				&& (this.theEntity.getAIGoal() == Goal.MELEE_TARGET) && (this.theEntity
						.getDistance(target) < (this.attackRange + this.theEntity.width + target.width) * 4.0F)
				&& (target.getClass().isAssignableFrom(this.targetClass));
	}

	@Override
	public void updateTask() {
		EntityLivingBase target = this.theEntity.getAttackTarget();
		if (this.canAttackEntity(target)) {
			this.attackEntity(target);
		}
		this.setAttackTime(this.getAttackTime() - 1);
	}

	public Class<? extends T> getTargetClass() {
		return this.targetClass;
	}

	protected void attackEntity(EntityLivingBase target) {
		this.theEntity.attackEntityAsMob(target);
		this.setAttackTime(this.getAttackDelay());
	}

	protected boolean canAttackEntity(EntityLivingBase target) {
		if (this.getAttackTime() <= 0) {
			double d = this.theEntity.width + this.attackRange;
			return this.theEntity.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ) < d * d;
		}
		return false;
	}

	protected int getAttackTime() {
		return this.nextAttack;
	}

	protected void setAttackTime(int time) {
		this.nextAttack = time;
	}

	protected int getAttackDelay() {
		return this.attackDelay;
	}

	protected void setAttackDelay(int time) {
		this.attackDelay = time;
	}
}