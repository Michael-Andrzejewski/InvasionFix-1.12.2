// `^`^`^`
// ```java
// /**
//  * This class is part of the AI system for the 'invmod' mod, which adds new behaviors to entities in Minecraft.
//  * It defines a wandering behavior for in-game entities that extend the EntityIMMob class.
//  *
//  * Class: EntityAIWanderIM
//  * Extends: EntityAIBase (Minecraft's base class for AI tasks)
//  * Package: invmod.entity.ai
//  *
//  * Purpose:
//  * - To provide a wandering behavior for EntityIMMob instances, allowing them to move randomly within the game world.
//  *
//  * Methods:
//  * - EntityAIWanderIM(EntityIMMob entity): Constructor that initializes the AI with the given EntityIMMob instance and sets the mutex bits to control AI execution.
//  *
//  * - shouldExecute(): Determines if the AI task should start executing. It randomly decides to create a new path every 120 ticks and sets the entity's navigator to follow this path if it's valid.
//  *   - It generates a random destination within a specified range (13 blocks on the horizontal axis and 4 blocks on the vertical axis).
//  *   - It then attempts to create a path to that destination using the entity's custom navigator.
//  *   - If a valid path is found, the entity is instructed to follow it, and the method returns true, allowing the AI task to execute.
//  *
//  * - shouldContinueExecuting(): Checks if the AI task should continue executing. It returns true if the entity has a path to follow and hasn't been stuck for too long (less than 40 ticks).
//  *
//  * Constants:
//  * - MIN_HORIZONTAL_PATH, MAX_HORIZONTAL_PATH, MAX_VERTICAL_PATH: Define the range for random path generation.
//  *
//  * Usage:
//  * - This AI task is used to make EntityIMMob instances wander randomly when they are not engaged in other activities.
//  */
// ```
// This summary provides an overview of the class's purpose, its methods, and how it fits into the entity's AI system. It also explains the usage of the class and the constants defined within it.
// `^`^`^`

package invmod.entity.ai;

import invmod.entity.ai.navigator.Path;
import invmod.entity.monster.EntityIMMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class EntityAIWanderIM extends EntityAIBase {
	private static final int MIN_HORIZONTAL_PATH = 1;
	private static final int MAX_HORIZONTAL_PATH = 6;
	private static final int MAX_VERTICAL_PATH = 4;
	private EntityIMMob theEntity;
	private BlockPos movePosition;

	public EntityAIWanderIM(EntityIMMob entity) {
		this.theEntity = entity;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (this.theEntity.getRNG().nextInt(120) == 0) {
			int x = this.theEntity.getPosition().getX() + this.theEntity.getRNG().nextInt(13) - 6;
			int z = this.theEntity.getPosition().getZ() + this.theEntity.getRNG().nextInt(13) - 6;
			Path path = this.theEntity.getNavigatorNew().getPathTowardsXZ(x, z, 1, 6, 4);
			if (path != null) {
				this.theEntity.getNavigatorNew().setPath(path, this.theEntity.getMoveSpeedStat());
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return (!this.theEntity.getNavigatorNew().noPath()) && (this.theEntity.getNavigatorNew().getStuckTime() < 40);
	}
}