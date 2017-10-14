package invmod.entity.ai;

import invmod.entity.monster.EntityIMMob;
import net.minecraft.entity.EntityLiving;


public class EntityAILeaderTarget extends EntityAISimpleTarget
{
	private final EntityIMMob theEntity;

	public EntityAILeaderTarget(EntityIMMob entity, Class<? extends EntityLiving> targetType, float distance)
	{
		this(entity, targetType, distance, true);
	}

	public EntityAILeaderTarget(EntityIMMob entity, Class<? extends EntityLiving> targetType, float distance, boolean needsLos)
	{
		super(entity, targetType, distance, needsLos);
		this.theEntity = entity;
	}

	@Override
	public boolean shouldExecute()
	{
		if (!this.theEntity.readyToRally())
		{
			return false;
		}
		return super.shouldExecute();
	}
}