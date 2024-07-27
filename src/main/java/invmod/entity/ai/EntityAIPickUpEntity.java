// `^`^`^`
// ```java
// /**
//  * This class represents an AI behavior for the EntityIMBird entity to pick up other entities.
//  * It is designed to be used within the Minecraft mod 'invmod'.
//  *
//  * Constructor:
//  * EntityAIPickUpEntity(EntityIMBird entity, float pickupPointX, float pickupPointY, float pickupPointZ,
//  *                      float pickupRangeY, float pickupRangeXZ, int abortTime, float abortAngleYaw,
//  *                      float abortAnglePitch)
//  * - Initializes the AI with the bird entity, pickup coordinates, ranges, abort time, and abort angles.
//  *
//  * Methods:
//  * shouldExecute()
//  * - Determines if the AI should start executing, based on whether the bird has a target to pick up or is already holding an entity.
//  *
//  * startExecuting()
//  * - Sets up the AI to start executing, initializing the holding state and time counter.
//  *
//  * shouldContinueExecuting()
//  * - Checks if the AI should continue executing, based on the target's status and alignment. If conditions are not met, it aborts the task.
//  *
//  * updateTask()
//  * - Updates the AI task, handling the logic for picking up the target entity when aligned and releasing it after a set duration.
//  *
//  * isLinedUp(Entity target)
//  * - Helper method to determine if the bird is aligned with the target entity based on yaw and pitch angles.
//  *
//  * The AI uses a combination of time counters, spatial calculations, and angle checks to manage the behavior of picking up and holding entities.
//  * It integrates with the entity's navigation system to modify movement behavior during the task.
//  */
// ```
// `^`^`^`

package invmod.entity.ai;

import invmod.entity.Goal;
import invmod.entity.monster.EntityIMBird;
import invmod.util.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIPickUpEntity extends EntityAIBase {

	private EntityIMBird theEntity;
	private int time;
	private int holdTime;
	private int abortTime;
	private float pickupPointY;
	private float pickupRangeY;
	private float pickupPointX;
	private float pickupPointZ;
	private float pickupRangeXZ;
	private float abortAngleYaw;
	private float abortAnglePitch;
	private boolean isHoldingEntity;

	public EntityAIPickUpEntity(EntityIMBird entity, float pickupPointX, float pickupPointY, float pickupPointZ,
			float pickupRangeY, float pickupRangeXZ, int abortTime, float abortAngleYaw, float abortAnglePitch) {
		this.theEntity = entity;
		this.time = 0;
		this.holdTime = 70;
		this.pickupPointX = pickupPointX;
		this.pickupPointY = pickupPointY;
		this.pickupPointZ = pickupPointZ;
		this.pickupRangeY = pickupRangeY;
		this.pickupRangeXZ = pickupRangeXZ;
		this.abortTime = abortTime;
		this.abortAngleYaw = abortAngleYaw;
		this.abortAnglePitch = abortAnglePitch;
		this.isHoldingEntity = false;
	}

	@Override
	public boolean shouldExecute() {
		return (this.theEntity.getAIGoal() == Goal.PICK_UP_TARGET)
				|| (!this.theEntity.getPassengers().isEmpty())/* (this.theEntity.riddenByEntity != null) */;
	}

	@Override
	public void startExecuting() {
		this.isHoldingEntity = (!this.theEntity.getPassengers().isEmpty())/* (this.theEntity.riddenByEntity != null) */;
		this.time = 0;
	}

	@Override
	public boolean shouldContinueExecuting() {
		EntityLivingBase target = this.theEntity.getAttackTarget();
		if ((target != null) && (!target.isDead)) {
			if (!this.isHoldingEntity) {
				if ((this.time > this.abortTime) && (this.isLinedUp(target)))
					return true;
			} else if (this.theEntity.getPassengers().contains(target)/* this.theEntity.riddenByEntity == target */) {
				return true;
			}
		}
		this.theEntity.transitionAIGoal(Goal.NONE);
		this.theEntity.setClawsForward(false);
		return false;
	}

	@Override
	public void updateTask() {
		this.time += 1;
		if (!this.isHoldingEntity) {
			EntityLivingBase target = this.theEntity.getAttackTarget();
			double dY = target.prevPosY - this.theEntity.prevPosY;
			System.out.println(dY);
			if (Math.abs(dY - this.pickupPointY) < this.pickupRangeY) {
				double dAngle = this.theEntity.prevRotationYaw / 180.0F * 3.141592653589793D;
				double sinF = Math.sin(dAngle);
				double cosF = Math.cos(dAngle);
				double x = this.pickupPointX * cosF - this.pickupPointZ * sinF;
				double z = this.pickupPointZ * cosF + this.pickupPointX * sinF;

				double dX = target.prevPosX - (x + this.theEntity.prevPosX);
				double dZ = target.prevPosZ - (z + this.theEntity.prevPosZ);
				double dXZ = Math.sqrt(dX * dX + dZ * dZ);
				System.out.println(dXZ);
				if (dXZ < this.pickupRangeXZ) {
					// target.mountEntity(this.theEntity);
					target.startRiding(this.theEntity);
					this.isHoldingEntity = true;
					this.time = 0;
					this.theEntity.getNavigatorNew().clearPath();
					this.theEntity.getNavigatorNew().setPitchBias(20.0F, 1.5F);
				}
			}
		} else if (this.time == 45) {
			this.theEntity.getNavigatorNew().setPitchBias(0.0F, 0.0F);
		} else if (this.time > this.holdTime) {
			// this.theEntity.getAttackTarget().mountEntity(null);
			this.theEntity.removePassengers();
		}
	}

	private boolean isLinedUp(Entity target) {
		double dX = target.posX - this.theEntity.posX;
		double dY = target.posY - this.theEntity.posY;
		double dZ = target.posZ - this.theEntity.posZ;
		double dXZ = Math.sqrt(dX * dX + dZ * dZ);
		double yawToTarget = Math.atan2(dZ, dX) * 180.0D / 3.141592653589793D - 90.0D;
		double dYaw = MathUtil.boundAngle180Deg(yawToTarget - this.theEntity.rotationYaw);
		if ((dYaw < -this.abortAngleYaw) || (dYaw > this.abortAngleYaw)) {
			return false;
		}
		double dPitch = Math.atan(dY / dXZ) * 180.0D / 3.141592653589793D - this.theEntity.rotationPitch;
		if ((dPitch < -this.abortAnglePitch) || (dPitch > this.abortAnglePitch)) {
			return false;
		}
		return true;
	}
}