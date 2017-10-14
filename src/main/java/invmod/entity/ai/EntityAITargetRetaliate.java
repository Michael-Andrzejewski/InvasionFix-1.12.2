package invmod.entity.ai;

import invmod.entity.monster.EntityIMMob;
import net.minecraft.entity.EntityLivingBase;

public class EntityAITargetRetaliate extends EntityAISimpleTarget {
	public EntityAITargetRetaliate(EntityIMMob entity, Class<? extends EntityLivingBase> targetType, float distance) {
		super(entity, targetType, distance);
	}

	@Override
	public boolean shouldExecute() {
		EntityLivingBase attacker = getEntity().getAITarget();
		if (attacker != null) {
			if ((getEntity().getDistanceToEntity(attacker) <= getAggroRange()) && (getTargetType().isAssignableFrom(attacker.getClass()))) {
				setTarget(attacker);
				return true;
			}
		}
		return false;
	}
}