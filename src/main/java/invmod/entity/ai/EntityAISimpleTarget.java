// `^`^`^`
// ```java
// /**
//  * This class represents an AI targeting behavior for in-game entities in Minecraft. It extends the EntityAIBase class,
//  * providing a simple targeting mechanism for entities to find and track targets within a specified range and with
//  * optional line-of-sight requirements.
//  *
//  * Methods:
//  * - EntityAISimpleTarget(EntityIMMob, Class<? extends EntityLivingBase>, float): Constructor to initialize the AI with a target type and range.
//  * - EntityAISimpleTarget(EntityIMMob, Class<? extends EntityLivingBase>, float, boolean): Overloaded constructor allowing to specify if line-of-sight is needed.
//  * - getEntity(): Returns the entity using this AI.
//  * - shouldExecute(): Determines if the AI should begin execution, finding the closest player or entity of the specified class within range as a target.
//  * - shouldContinueExecuting(): Checks if the AI should continue executing, verifying the target's presence, life status, distance, and line-of-sight.
//  * - startExecuting(): Called when the AI begins executing, setting the found target as the entity's attack target.
//  * - resetTask(): Resets the task, clearing the current attack target.
//  * - getTargetType(): Returns the class type of the target.
//  * - getAggroRange(): Returns the aggression range within which the entity will target others.
//  * - setTarget(EntityLivingBase): Sets the given entity as the current target.
//  * - isValidTarget(EntityLivingBase): Validates if the given entity is a suitable target, checking for null, self-targeting, life status, and line-of-sight.
//  *
//  * The AI uses a combination of distance checks and line-of-sight checks to determine valid targets, and it can specifically target players or other entity classes.
//  * It also respects players in creative mode by not targeting them if they have damage disabled.
//  */
// ```
// `^`^`^`

package invmod.entity.ai;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import invmod.entity.monster.EntityIMMob;
import invmod.util.ComparatorEntityDistanceFrom;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAISimpleTarget extends EntityAIBase {
	private final EntityIMMob theEntity;
	private EntityLivingBase targetEntity;
	private Class<? extends EntityLivingBase> targetClass;
	private int outOfLosTimer;
	private float distance;
	private boolean needsLos;

	public EntityAISimpleTarget(EntityIMMob entity, Class<? extends EntityLivingBase> targetType, float distance) {
		this(entity, targetType, distance, true);
	}

	public EntityAISimpleTarget(EntityIMMob entity, Class<? extends EntityLivingBase> targetType, float distance,
			boolean needsLoS) {
		this.theEntity = entity;
		this.targetClass = targetType;
		this.outOfLosTimer = 0;
		this.distance = distance;
		this.needsLos = needsLoS;
		this.setMutexBits(1);
	}

	public EntityIMMob getEntity() {
		return this.theEntity;
	}

	@Override
	public boolean shouldExecute() {
		if (this.targetClass == EntityPlayer.class) {
			EntityPlayer entityplayer = this.theEntity.world.getClosestPlayerToEntity(this.theEntity, this.distance);
			if (this.isValidTarget(entityplayer)) {
				this.targetEntity = entityplayer;
				return true;
			}
		}

		List list = this.theEntity.world.getEntitiesWithinAABB(this.targetClass,
				this.theEntity.getEntityBoundingBox().expand(this.distance, this.distance / 2.0F, this.distance));
		Comparator comp = new ComparatorEntityDistanceFrom(this.theEntity.posX, this.theEntity.posY,
				this.theEntity.posZ);
		Collections.sort(list, comp);

		boolean foundEntity = false;
		while (list.size() > 0) {
			EntityLivingBase entity = (EntityLivingBase) list.remove(list.size() - 1);
			if (this.isValidTarget(entity)) {
				this.targetEntity = entity;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		EntityLivingBase entityliving = this.theEntity.getAttackTarget();
		if (entityliving == null) {
			return false;
		}
		if (!entityliving.isEntityAlive()) {
			return false;
		}
		if (this.theEntity.getDistanceSq(entityliving) > this.distance * this.distance) {
			return false;
		}
		if (this.needsLos) {
			if (!this.theEntity.getEntitySenses().canSee(entityliving)) {
				if (++this.outOfLosTimer > 60) {
					return false;
				}
			} else {
				this.outOfLosTimer = 0;
			}
		}

		return true;
	}

	@Override
	public void startExecuting() {
		this.theEntity.setAttackTarget(this.targetEntity);
		this.outOfLosTimer = 0;
	}

	@Override
	public void resetTask() {
		this.theEntity.setAttackTarget(null);
	}

	public Class<? extends EntityLivingBase> getTargetType() {
		return this.targetClass;
	}

	public float getAggroRange() {
		return this.distance;
	}

	protected void setTarget(EntityLivingBase entity) {
		this.targetEntity = entity;
	}

	protected boolean isValidTarget(EntityLivingBase entity) {
		if (entity == null) {
			return false;
		}
		if (entity == this.theEntity) {
			return false;
		}
		if (!entity.isEntityAlive()) {
			return false;
		}

		// players in creative mode won't be targeted
		if (this.targetClass == EntityPlayer.class) {
			if (((EntityPlayer) entity).capabilities.disableDamage) {
				return false;
			}
		}

		if ((this.needsLos) && (!this.theEntity.getEntitySenses().canSee(entity))) {
			return false;
		}
		return true;
	}
}