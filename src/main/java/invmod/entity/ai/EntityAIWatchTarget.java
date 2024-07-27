// `^`^`^`
// ```java
// /**
//  * This class is part of the AI module for entities in a Minecraft mod. It defines an AI behavior for entities to watch their current attack target.
//  *
//  * Class: EntityAIWatchTarget
//  * Package: invmod.entity.ai
//  *
//  * The class extends EntityAIBase, making it a specific type of AI behavior that can be added to an entity's AI tasks.
//  *
//  * Constructor:
//  * - EntityAIWatchTarget(EntityLiving entity): Initializes the AI with the specified entity as the one that will perform the watching behavior.
//  *
//  * Methods:
//  * - boolean shouldExecute(): Determines if the AI task should begin executing. It checks if the entity has a current attack target, returning true if it does, indicating that the task should start.
//  *
//  * - void updateTask(): Updates the task each tick while it's executing. It directs the entity to look at its current attack target, using the setLookPositionWithEntity method from the entity's LookHelper class. The parameters 2.0F for both the yaw and pitch speed indicate how quickly the entity should turn its head to keep its gaze on the target.
//  *
//  * This AI task is typically used to make an entity visually track its target, which is useful for creating more immersive and intelligent entity behaviors, especially for entities that are meant to engage in combat or interact with other entities.
//  */
// package invmod.entity.ai;
// 
// // ... (rest of the code)
// ```
// `^`^`^`

package invmod.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIWatchTarget extends EntityAIBase {
	private EntityLiving theEntity;

	public EntityAIWatchTarget(EntityLiving entity) {
		this.theEntity = entity;
	}

	@Override
	public boolean shouldExecute() {
		return this.theEntity.getAttackTarget() != null;
	}

	@Override
	public void updateTask() {
		this.theEntity.getLookHelper().setLookPositionWithEntity(this.theEntity.getAttackTarget(), 2.0F, 2.0F);
	}
}