// `^`^`^`
// ```java
// /**
//  * This class is part of the AI module for the Invasion Mod, which extends the behavior of in-game entities.
//  * Specifically, EntityAILeaderTarget is an AI behavior for entities that allows them to target other entities
//  * based on leadership roles within a group of mobs.
//  *
//  * The class extends EntityAISimpleTarget, inheriting its basic targeting capabilities, and adds a check
//  * for whether the entity is ready to rally before executing the targeting behavior.
//  *
//  * Constructors:
//  * - EntityAILeaderTarget(EntityIMMob entity, Class<? extends EntityLiving> targetType, float distance):
//  *   Initializes the AI with a specific target type and distance, assuming line-of-sight is needed.
//  *
//  * - EntityAILeaderTarget(EntityIMMob entity, Class<? extends EntityLiving> targetType, float distance,
//  *   boolean needsLos): Allows specifying whether line-of-sight is necessary for targeting, in addition
//  *   to the target type and distance.
//  *
//  * Methods:
//  * - shouldExecute(): Overrides the base class method to include a check that determines if the entity
//  *   is in a state where it is ready to rally (e.g., gather or follow a leader) before it proceeds with
//  *   the targeting logic inherited from EntityAISimpleTarget.
//  *
//  * The class is used to create AI behaviors where certain mobs will only target other entities if they are
//  * in a state to follow a leader, adding a layer of strategic depth to the game's mob behavior.
//  */
// ```
// `^`^`^`

package invmod.entity.ai;

import invmod.entity.monster.EntityIMMob;
import net.minecraft.entity.EntityLiving;

public class EntityAILeaderTarget extends EntityAISimpleTarget {
	private final EntityIMMob theEntity;

	public EntityAILeaderTarget(EntityIMMob entity, Class<? extends EntityLiving> targetType, float distance) {
		this(entity, targetType, distance, true);
	}

	public EntityAILeaderTarget(EntityIMMob entity, Class<? extends EntityLiving> targetType, float distance,
			boolean needsLos) {
		super(entity, targetType, distance, needsLos);
		this.theEntity = entity;
	}

	@Override
	public boolean shouldExecute() {
		if (!this.theEntity.readyToRally()) {
			return false;
		}
		return super.shouldExecute();
	}
}