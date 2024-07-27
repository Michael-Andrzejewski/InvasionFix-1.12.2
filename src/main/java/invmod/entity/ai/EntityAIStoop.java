// `^`^`^`
// ```java
// /**
//  * This class is part of the AI module for the invmod mod, which adds new behaviors to entities in Minecraft.
//  * The EntityAIStoop class extends the EntityAIBase class, providing a specific behavior for EntityIMLiving entities.
//  * The behavior implemented in this class makes the entity "stoop" or sneak under certain conditions.
//  *
//  * Constructor:
//  * - EntityAIStoop(EntityIMLiving entity): Initializes the AI with the specified entity and sets the initial state.
//  *
//  * Core Methods:
//  * - shouldExecute(): Determines if the AI should start executing. It checks every 10 ticks if there is a block above
//  *   the entity that blocks movement, in which case the entity should start stooping.
//  * - shouldContinueExecuting(): Checks if the entity should continue stooping. It returns the inverse of stopStoop.
//  * - startExecuting(): Called when the AI begins execution. It sets the entity to a sneaking state and resets stopStoop.
//  * - updateTask(): Called every tick while the AI is executing. It checks every 10 ticks if there is no longer a block
//  *   above the entity that blocks movement, in which case it stops the entity from sneaking and sets stopStoop to true.
//  *
//  * Attributes:
//  * - EntityIMLiving theEntity: The entity that this AI behavior is associated with.
//  * - int updateTimer: A timer used to check conditions every 10 ticks.
//  * - boolean stopStoop: A flag indicating whether the entity should stop stooping.
//  */
// ```
// `^`^`^`

package invmod.entity.ai;

import invmod.entity.EntityIMLiving;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIStoop extends EntityAIBase {

	private EntityIMLiving theEntity;
	private int updateTimer;
	private boolean stopStoop;

	public EntityAIStoop(EntityIMLiving entity) {
		this.theEntity = entity;
		this.stopStoop = true;
	}

	@Override
	public boolean shouldExecute() {
		if (--this.updateTimer <= 0) {
			this.updateTimer = 10;
			if (this.theEntity.world.getBlockState(this.theEntity.getPosition().up(2)).getMaterial().blocksMovement()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return !this.stopStoop;
	}

	@Override
	public void startExecuting() {
		this.theEntity.setSneaking(true);
		this.stopStoop = false;
	}

	@Override
	public void updateTask() {
		if (--this.updateTimer <= 0) {
			this.updateTimer = 10;
			if (!this.theEntity.world.getBlockState(this.theEntity.getPosition().up()).getMaterial().blocksMovement()) {
				this.theEntity.setSneaking(false);
				this.stopStoop = true;
			}
		}
	}
}