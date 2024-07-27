// `^`^`^`
// ```java
// /**
//  * This class is part of the AI system for a modded entity in Minecraft, specifically for handling retaliation behavior.
//  * It extends EntityAISimpleTarget, indicating it is a specialized targeting behavior for AI-controlled entities.
//  *
//  * Purpose:
//  * - The primary purpose of this AI class is to enable an entity to target and retaliate against an attacker within a certain range.
//  *
//  * Constructor:
//  * - EntityAITargetRetaliate(EntityIMMob entity, Class<? extends EntityLivingBase> targetType, float distance):
//  *   Initializes the AI with the entity that will use this AI, the class type of the target it should retaliate against, and the maximum distance for retaliation.
//  *
//  * Methods:
//  * - shouldExecute():
//  *   Determines whether the AI should begin execution. It checks if there is an attacker and whether the attacker is within the specified aggro range and of the correct type. If the conditions are met, it sets the attacker as the target and returns true to start execution.
//  *
//  * Usage:
//  * - This AI component is used in the behavior tree of modded entities that need to react to being attacked by targeting their attacker. It is typically used in conjunction with other AI tasks to create complex behaviors.
//  */
// ```
// `^`^`^`

package invmod.entity.ai;

import invmod.entity.monster.EntityIMMob;
import net.minecraft.entity.EntityLivingBase;

public class EntityAITargetRetaliate extends EntityAISimpleTarget {
	public EntityAITargetRetaliate(EntityIMMob entity, Class<? extends EntityLivingBase> targetType, float distance) {
		super(entity, targetType, distance);
	}

	@Override
	public boolean shouldExecute() {
		EntityLivingBase attacker = this.getEntity().getAttackingEntity();
		if (attacker != null) {
			if ((this.getEntity().getDistance(attacker) <= this.getAggroRange())
					&& (this.getTargetType().isAssignableFrom(attacker.getClass()))) {
				this.setTarget(attacker);
				return true;
			}
		}
		return false;
	}
}