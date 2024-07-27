// `^`^`^`
// ```java
// /**
//  * This class is part of the 'invmod' package and extends the EntityAISimpleTarget AI behavior for entities in Minecraft.
//  * Its primary function is to manage the targeting behavior of an EntityIMMob when its goal is to break a Nexus and it is not on a path to the Nexus.
//  *
//  * Class: EntityAITargetOnNoNexusPath
//  * Package: invmod.entity.ai
//  * Extends: EntityAISimpleTarget
//  *
//  * Constructor:
//  * - EntityAITargetOnNoNexusPath(EntityIMMob entity, Class<? extends EntityLiving> targetType, float distance)
//  *   Initializes a new instance for the specified entity with a target type and distance.
//  *
//  * Methods:
//  * - boolean shouldExecute()
//  *   Determines whether the AI should start executing. It checks if the entity's goal is to break a Nexus and if the entity is further than 4.0F from the target path.
//  *   If both conditions are met, it defers to the superclass's shouldExecute method.
//  *
//  * - boolean shouldContinueExecuting()
//  *   Checks if the AI should continue executing based on the same conditions as shouldExecute (entity's goal and distance from the target path).
//  *   If the conditions are satisfied, it defers to the superclass's shouldContinueExecuting method.
//  *
//  * Constants:
//  * - float PATH_DISTANCE_TRIGGER
//  *   A constant value set to 4.0F, representing the distance threshold for triggering targeting behavior when not on a path to the Nexus.
//  *
//  * Usage:
//  * This AI behavior is used for entities that have a specific goal to break a Nexus but are not currently on a direct path to it. It ensures that the entity
//  * will seek out a new target if it strays too far from its path, maintaining its aggressive behavior towards the Nexus.
//  */
// ```
// `^`^`^`

package invmod.entity.ai;

import invmod.entity.Goal;
import invmod.entity.monster.EntityIMMob;
import net.minecraft.entity.EntityLiving;

public class EntityAITargetOnNoNexusPath extends EntityAISimpleTarget {
	private final float PATH_DISTANCE_TRIGGER = 4.0F;

	public EntityAITargetOnNoNexusPath(EntityIMMob entity, Class<? extends EntityLiving> targetType, float distance) {
		super(entity, targetType, distance);
	}

	@Override
	public boolean shouldExecute() {
		if ((this.getEntity().getAIGoal() == Goal.BREAK_NEXUS)
				&& (this.getEntity().getNavigatorNew().getLastPathDistanceToTarget() > 4.0F)) {
			return super.shouldExecute();
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if ((this.getEntity().getAIGoal() == Goal.BREAK_NEXUS)
				&& (this.getEntity().getNavigatorNew().getLastPathDistanceToTarget() > 4.0F)) {
			return super.shouldContinueExecuting();
		}
		return false;
	}
}