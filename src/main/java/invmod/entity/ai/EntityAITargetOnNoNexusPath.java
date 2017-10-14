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
		if ((this.getEntity().getAIGoal() == Goal.BREAK_NEXUS) && (this.getEntity().getNavigatorNew().getLastPathDistanceToTarget() > 4.0F)) {
			return super.shouldExecute();
		}
		return false;
	}

	@Override
	public boolean continueExecuting() {
		if ((this.getEntity().getAIGoal() == Goal.BREAK_NEXUS) && (this.getEntity().getNavigatorNew().getLastPathDistanceToTarget() > 4.0F)) {
			return super.continueExecuting();
		}
		return false;
	}
}