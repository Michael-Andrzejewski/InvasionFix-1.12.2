// `^`^`^`
// ```java
// /**
//  * This class is part of the AI system for a modded entity in a Minecraft mod. It defines the behavior of an entity to wait for and potentially assist another entity, specifically an EntityIMPigEngy.
//  *
//  * Class: EntityAIWaitForEngy
//  * Extends: EntityAIFollowEntity<EntityIMPigEngy>
//  * Purpose: To create an AI task where the entity will follow and wait for a specific type of entity (EntityIMPigEngy) and provide assistance if possible.
//  *
//  * Constructor:
//  * - EntityAIWaitForEngy(EntityIMLiving entity, float followDistance, boolean canHelp)
//  *   Initializes the AI with the entity to follow, the distance at which to follow, and whether the entity can provide assistance.
//  *
//  * Methods:
//  * - updateTask()
//  *   Overrides the updateTask method from the parent class to include behavior that allows the entity to support the target (EntityIMPigEngy) if the 'canHelp' flag is true.
//  *
//  * Fields:
//  * - PATH_DISTANCE_TRIGGER: A constant float value that defines the distance at which the entity will trigger following behavior.
//  * - canHelp: A boolean flag indicating whether the entity is capable of providing assistance to the target entity.
//  *
//  * The AI task is triggered when the entity is within a certain distance from the EntityIMPigEngy, and if 'canHelp' is true, the entity will perform a support action each tick.
//  */
// ```
// 
// `^`^`^`

package invmod.entity.ai;

import invmod.entity.EntityIMLiving;
import invmod.entity.monster.EntityIMPigEngy;

public class EntityAIWaitForEngy extends EntityAIFollowEntity<EntityIMPigEngy> {
	private final float PATH_DISTANCE_TRIGGER = 4.0F;
	private boolean canHelp;

	public EntityAIWaitForEngy(EntityIMLiving entity, float followDistance, boolean canHelp) {
		super(entity, EntityIMPigEngy.class, followDistance);
		this.canHelp = canHelp;
	}

	@Override
	public void updateTask() {
		super.updateTask();
		if (this.canHelp) {
			this.getTarget().supportForTick(this.getEntity(), 1.0F);
		}
	}
}