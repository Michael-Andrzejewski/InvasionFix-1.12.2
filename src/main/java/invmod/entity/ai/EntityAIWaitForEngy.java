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