// `^`^`^`
// ```java
// /**
//  * This class represents an AI behavior for winged monster entities in a Minecraft mod. It extends the EntityAIMeleeAttack class, providing a specialized melee attack strategy for the EntityIMBird class.
//  *
//  * Class: EntityAIWingAttack
//  * Package: invmod.entity.ai
//  * Extends: EntityAIMeleeAttack
//  *
//  * Purpose:
//  * - To define a wing attack behavior for the EntityIMBird entities against a specified target class.
//  * - To manage the attack timing and range for the wing attack.
//  *
//  * Methods:
//  * - EntityAIWingAttack(EntityIMBird entity, Class<? extends EntityLivingBase> targetClass, int attackDelay):
//  *   Constructor to initialize the AI with the bird entity, target class, and attack delay.
//  *
//  * - updateTask():
//  *   Overrides the EntityAIMeleeAttack's updateTask method. It sets the bird entity to attack with wings when the attack time is right and within melee range.
//  *
//  * - resetTask():
//  *   Overrides the EntityAIMeleeAttack's resetTask method. It resets the wing attack state when the task is no longer relevant or is interrupted.
//  *
//  * - isInStartMeleeRange():
//  *   A protected method that checks if the target is within the starting range for a melee attack, considering the entity's size and attack range.
//  *
//  * Usage:
//  * - This AI is used to add more dynamic combat abilities to bird-like monsters, allowing them to perform wing attacks on their targets.
//  * - It should be attached to the EntityIMBird class or subclasses thereof to function properly.
//  */
// ```
// `^`^`^`

package invmod.entity.ai;

import invmod.entity.monster.EntityIMBird;
import net.minecraft.entity.EntityLivingBase;

public class EntityAIWingAttack extends EntityAIMeleeAttack {
	private EntityIMBird theEntity;

	public EntityAIWingAttack(EntityIMBird entity, Class<? extends EntityLivingBase> targetClass, int attackDelay) {
		super(entity, targetClass, attackDelay);
		this.theEntity = entity;
	}

	@Override
	public void updateTask() {
		if (this.getAttackTime() == 0) {
			this.theEntity.setAttackingWithWings(this.isInStartMeleeRange());
		}
		super.updateTask();
	}

	@Override
	public void resetTask() {
		this.theEntity.setAttackingWithWings(false);
	}

	protected boolean isInStartMeleeRange() {
		EntityLivingBase target = this.theEntity.getAttackTarget();
		if (target == null) {
			return false;
		}
		double d = this.theEntity.width + this.theEntity.getAttackRange() + 3.0D;
		return this.theEntity.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ) < d * d;
	}
}