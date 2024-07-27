// `^`^`^`
// ```java
// /**
//  * This AI class is designed for entities in Minecraft to move towards a specific target entity. It extends the EntityAIBase class, which is part of Minecraft's AI system.
//  *
//  * Constructor EntityAIMoveToEntity(EntityIMLiving entity):
//  * Initializes the AI with the entity that will be moving and sets the target entity class to EntityLivingBase by default.
//  *
//  * Constructor EntityAIMoveToEntity(EntityIMLiving entity, Class<? extends T> target):
//  * Allows specifying a particular class of the target entity for the moving entity to approach.
//  *
//  * Method shouldExecute():
//  * Determines if the AI should start executing. It checks if the target entity is valid and assignable from the target class.
//  *
//  * Method shouldContinueExecuting():
//  * Checks if the AI should continue executing based on whether the target entity is still the current attack target.
//  *
//  * Method startExecuting():
//  * Called when the AI begins execution. It sets the targetMoves flag to true and attempts to set a path to the target.
//  *
//  * Method resetTask():
//  * Resets the task, setting the targetMoves flag to false.
//  *
//  * Method updateTask():
//  * Updates the task, recalculating the path to the target entity if necessary and handling path failure logic.
//  *
//  * Method setTargetMoves(boolean flag):
//  * Allows external modification of the targetMoves flag.
//  *
//  * Method getEntity():
//  * Provides access to the entity that is using this AI.
//  *
//  * Method getTarget():
//  * Provides access to the current target entity.
//  *
//  * Method setPath():
//  * Attempts to set a path to the target entity and handles path success and failure logic, including updating path request timing and failure count.
//  *
//  * The AI uses a timer to manage path requests and keeps track of the last known position of the target entity. It also counts pathfinding failures to adjust behavior accordingly.
//  */
// ```
// `^`^`^`

package invmod.entity.ai;

import invmod.entity.EntityIMLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIMoveToEntity<T extends EntityLivingBase> extends EntityAIBase {
	private EntityIMLiving theEntity;
	private T targetEntity;
	private Class<? extends T> targetClass;
	private boolean targetMoves;
	private double lastX;
	private double lastY;
	private double lastZ;
	private int pathRequestTimer;
	private int pathFailedCount;

	public EntityAIMoveToEntity(EntityIMLiving entity) {
		this(entity, (Class<? extends T>) EntityLivingBase.class);
	}

	public EntityAIMoveToEntity(EntityIMLiving entity, Class<? extends T> target) {
		this.targetClass = target;
		this.theEntity = entity;
		this.targetMoves = false;
		this.pathRequestTimer = 0;
		this.pathFailedCount = 0;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (--this.pathRequestTimer <= 0) {
			EntityLivingBase target = this.theEntity.getAttackTarget();
			if ((target != null) && (this.targetClass.isAssignableFrom(this.theEntity.getAttackTarget().getClass()))) {
				this.targetEntity = (T) (this.targetClass.cast(target));
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		EntityLivingBase target = this.theEntity.getAttackTarget();
		if ((target != null) && (target == this.targetEntity)) {
			return true;
		}
		return false;
	}

	@Override
	public void startExecuting() {
		this.targetMoves = true;
		this.setPath();
	}

	@Override
	public void resetTask() {
		this.targetMoves = false;
	}

	@Override
	public void updateTask() {
		if ((--this.pathRequestTimer <= 0) && (!this.theEntity.getNavigatorNew().isWaitingForTask())
				&& (this.targetMoves) && (this.targetEntity.getDistanceSq(this.lastX, this.lastY, this.lastZ) > 1.8D)) {
			this.setPath();
		}
		if (this.pathFailedCount > 3) {
			this.theEntity.getMoveHelper().setMoveTo(this.targetEntity.posX, this.targetEntity.posY,
					this.targetEntity.posZ, this.theEntity.getMoveSpeedStat());
		}
	}

	protected void setTargetMoves(boolean flag) {
		this.targetMoves = flag;
	}

	protected EntityIMLiving getEntity() {
		return this.theEntity;
	}

	protected T getTarget() {
		return this.targetEntity;
	}

	protected void setPath() {
		if (this.theEntity.getNavigatorNew().tryMoveToEntity(this.targetEntity, 0.0F,
				this.theEntity.getMoveSpeedStat())) {
			if (this.theEntity.getNavigatorNew().getLastPathDistanceToTarget() > 3.0F) {
				this.pathRequestTimer = (30 + this.theEntity.world.rand.nextInt(10));
				if (this.theEntity.getNavigatorNew().getPath().getCurrentPathLength() > 2)
					this.pathFailedCount = 0;
				else
					this.pathFailedCount += 1;
			} else {
				this.pathRequestTimer = (10 + this.theEntity.world.rand.nextInt(10));
				this.pathFailedCount = 0;
			}
		} else {
			this.pathFailedCount += 1;
			this.pathRequestTimer = (40 * this.pathFailedCount + this.theEntity.world.rand.nextInt(10));
		}

		this.lastX = this.targetEntity.posX;
		this.lastY = this.targetEntity.posY;
		this.lastZ = this.targetEntity.posZ;
	}
}