// `^`^`^`
// ```java
// /**
//  * This class is an AI behavior module for entities in a Minecraft mod that extends the EntityAIMoveToEntity class.
//  * It is designed to enable an entity to seek out and attack a specified target entity within the game.
//  *
//  * Class: EntityAIKillEntity<T extends EntityLivingBase>
//  * Purpose: To provide entities with the ability to identify, move towards, and attack a target entity.
//  *
//  * Constructor:
//  * - EntityAIKillEntity(EntityIMMob entity, Class<? extends T> targetClass, int attackDelay)
//  *   Initializes the AI with the attacking entity, the class of the target, and the delay between attacks.
//  *
//  * Methods:
//  * - updateTask()
//  *   Overrides the parent class method to decrement the attack timer and execute an attack when possible.
//  *
//  * - attackEntity(Entity target)
//  *   Commands the entity to perform an attack on the target entity and resets the attack timer.
//  *
//  * - canAttackEntity(Entity target)
//  *   Determines if the entity is in range and ready to attack the target based on the attack timer and distance.
//  *
//  * - getAttackTime()
//  *   Returns the remaining time until the next attack is allowed.
//  *
//  * - setAttackTime(int time)
//  *   Sets the attack timer to the specified time.
//  *
//  * - getAttackDelay()
//  *   Retrieves the configured delay between attacks.
//  *
//  * - setAttackDelay(int time)
//  *   Sets the delay between attacks to the specified time.
//  *
//  * Constants:
//  * - ATTACK_RANGE
//  *   Defines the range within which the entity can attack the target.
//  *
//  * Usage:
//  * This AI module should be attached to an entity to enable aggressive behavior towards a specific target type.
//  */
// ```
// `^`^`^`

package invmod.entity.ai;

import invmod.entity.monster.EntityIMMob;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class EntityAIKillEntity<T extends EntityLivingBase> extends EntityAIMoveToEntity<T> {
	private static final float ATTACK_RANGE = 1.0F;
	private int attackDelay;
	private int nextAttack;

	public EntityAIKillEntity(EntityIMMob entity, Class<? extends T> targetClass, int attackDelay) {
		super(entity, targetClass);
		this.attackDelay = attackDelay;
		this.nextAttack = 0;
	}

	@Override
	public void updateTask() {
		super.updateTask();
		this.setAttackTime(this.getAttackTime() - 1);
		Entity target = this.getTarget();
		if (this.canAttackEntity(target)) {
			this.attackEntity(target);
		}
	}

	protected void attackEntity(Entity target) {
		this.getEntity().attackEntityAsMob(this.getTarget());
		this.setAttackTime(this.getAttackDelay());
	}

	protected boolean canAttackEntity(Entity target) {
		if (this.getAttackTime() <= 0) {
			Entity entity = this.getEntity();
			double d = (entity.width + 1.0F) * (entity.width + 1.0F);

			return entity.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ) < d;
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