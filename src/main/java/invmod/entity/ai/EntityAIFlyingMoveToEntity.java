// `^`^`^`
// ```java
// /**
//  * This class is part of the entity AI (Artificial Intelligence) system and defines the behavior for a flying entity to move towards a target entity. It extends the base class for entity AI tasks in Minecraft.
//  *
//  * Constructor:
//  * - EntityAIFlyingMoveToEntity(EntityIMFlying entity): Initializes the AI with the flying entity that will be using this AI task.
//  *
//  * Methods:
//  * - shouldExecute(): Determines if the AI task should start executing. It checks if the entity's current goal is to go to an entity and if it has a valid attack target.
//  * - startExecuting(): Called once when the AI task should start executing. It retrieves the entity's navigation component and sets up a path to the attack target. It also adjusts the movement type based on the distance to the target.
//  * - updateTask(): Called every tick while the AI task is executing. In this implementation, it is empty and can be overridden with specific update behavior if needed.
//  *
//  * This AI task is used by flying entities to navigate the world and move towards a specific target, such as when chasing an enemy or following an ally.
//  */
// ```
// `^`^`^`

package invmod.entity.ai;

//NOOB HAUS: Done

import invmod.entity.Goal;
import invmod.entity.INavigationFlying;
import invmod.entity.ai.navigator.Path;
import invmod.entity.monster.EntityIMFlying;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIFlyingMoveToEntity extends EntityAIBase {
	private EntityIMFlying theEntity;

	public EntityAIFlyingMoveToEntity(EntityIMFlying entity) {
		this.theEntity = entity;
	}

	@Override
	public boolean shouldExecute() {
		return (this.theEntity.getAIGoal() == Goal.GOTO_ENTITY) && (this.theEntity.getAttackTarget() != null);
	}

	@Override
	public void startExecuting() {
		INavigationFlying nav = this.theEntity.getNavigatorNew();
		Entity target = this.theEntity.getAttackTarget();
		if (target != nav.getTargetEntity()) {
			nav.clearPath();
			nav.setMovementType(INavigationFlying.MoveType.PREFER_WALKING);
			Path path = nav.getPathToEntity(target, 0.0F);
			if (path.getCurrentPathLength() > 2.0D * this.theEntity.getDistance(target)) {
				nav.setMovementType(INavigationFlying.MoveType.MIXED);
			}
			nav.autoPathToEntity(target);
		}
	}

	@Override
	public void updateTask() {
	}
}