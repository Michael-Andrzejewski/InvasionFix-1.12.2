// `^`^`^`
// ```java
// /**
//  * This class is part of the invmod mod, providing AI behavior for entities to follow other entities.
//  * It extends the EntityAIMoveToEntity class, inheriting its movement capabilities towards a target entity.
//  *
//  * Class: EntityAIFollowEntity<T extends EntityLivingBase>
//  * Package: invmod.entity.ai
//  *
//  * Purpose:
//  * - To create an AI task that allows an instance of EntityIMLiving to follow a specified target entity.
//  * - The target entity is of type EntityLivingBase or a subclass of it.
//  *
//  * Methods:
//  * - EntityAIFollowEntity(EntityIMLiving entity, float followDistance):
//  *   Constructs the AI task with a default target entity class and a specified follow distance.
//  *
//  * - EntityAIFollowEntity(EntityIMLiving entity, Class<? extends T> target, float followDistance):
//  *   Constructs the AI task with a specified target entity class and follow distance.
//  *
//  * - startExecuting():
//  *   Called when the AI task is initiated. It informs the EntityIMLiving instance to start following the target entity.
//  *
//  * - resetTask():
//  *   Called when the AI task is reset. It informs the EntityIMLiving instance to stop following the target entity.
//  *
//  * - updateTask():
//  *   Called each tick to update the AI task. It checks the distance to the target entity and halts the entity's movement
//  *   if it is within the specified follow distance.
//  *
//  * Usage:
//  * - This AI task is used to make an EntityIMLiving instance follow another entity within a certain range.
//  * - It is useful for entities that need to keep a certain distance from their target, such as pets following their owner.
//  */
// ```
// `^`^`^`

package invmod.entity.ai;

import invmod.entity.EntityIMLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class EntityAIFollowEntity<T extends EntityLivingBase> extends EntityAIMoveToEntity<T> {
	private float followDistanceSq;

	public EntityAIFollowEntity(EntityIMLiving entity, float followDistance) {
		this(entity, (Class<? extends T>) EntityLivingBase.class, followDistance);
	}

	public EntityAIFollowEntity(EntityIMLiving entity, Class<? extends T> target, float followDistance) {
		super(entity, target);
		this.followDistanceSq = (followDistance * followDistance);
	}

	@Override
	public void startExecuting() {
		this.getEntity().onFollowingEntity(this.getTarget());
		super.startExecuting();
	}

	@Override
	public void resetTask() {
		this.getEntity().onFollowingEntity(null);
		super.resetTask();
	}

	@Override
	public void updateTask() {
		super.updateTask();
		Entity entity = this.getTarget();
		if (this.getEntity().getDistanceSq(entity.posX, entity.getEntityBoundingBox().minY,
				entity.posZ) < this.followDistanceSq)
			this.getEntity().getNavigatorNew().haltForTick();
	}
}