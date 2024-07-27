// `^`^`^`
// ```java
// /**
//  * This class represents an AI behavior for a flying entity, specifically an EntityIMBird, to perform a swooping attack.
//  * The behavior includes identifying a target, aligning for the attack, and executing a dive towards the target.
//  *
//  * Constructor:
//  * - EntityAISwoop(EntityIMBird entity): Initializes the AI with the given EntityIMBird instance.
//  *
//  * Core Methods:
//  * - shouldExecute(): Determines if the AI should begin executing, based on whether a suitable attack opportunity exists.
//  * - shouldContinueExecuting(): Checks if the AI should continue executing, based on whether the attack conditions are still met.
//  * - startExecuting(): Called when the AI begins execution, setting up initial conditions for the swoop.
//  * - resetTask(): Resets the task, disabling any direct targeting and resetting AI goals if the swoop is finished or interrupted.
//  * - updateTask(): Updates the task each tick, managing the swoop's execution and transitioning to a strike if conditions are met.
//  *
//  * Helper Methods:
//  * - isSwoopPathClear(EntityLivingBase target, float diveAngle): Checks if the path for the swoop is clear of obstacles.
//  * - isFinalRunLinedUp(): Determines if the entity is properly aligned with the target to begin the final attack run.
//  * - isAcceptableDiveSpace(double entityPosY, double lowestCollideY, int hitCount): Checks if there is enough space to dive without hitting obstacles.
//  *
//  * The AI uses several parameters to control the swoop behavior, such as dive angle, strike distance, and minimum height requirements.
//  * It also manages state variables to track the progress of the swoop and whether the entity is committed to the final attack run.
//  */
// ```
// `^`^`^`

package invmod.entity.ai;

import invmod.entity.Goal;
import invmod.entity.INavigationFlying;
import invmod.entity.MoveState;
import invmod.entity.monster.EntityIMBird;
import invmod.util.MathUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class EntityAISwoop extends EntityAIBase {
	private static final int INITIAL_LINEUP_TIME = 25;
	private EntityIMBird theEntity;
	private float minDiveClearanceY;
	private EntityLivingBase swoopTarget;
	private float diveAngle;
	private float diveHeight;
	private float strikeDistance;
	private float minHeight;
	private float minXZDistance;
	private float maxSteepness;
	private float finalRunLength;
	private float finalRunArcLimit;
	private int time;
	private boolean isCommittedToFinalRun;
	private boolean endSwoop;
	private boolean usingClaws;

	public EntityAISwoop(EntityIMBird entity) {
		this.theEntity = entity;
		this.minDiveClearanceY = 0.0F;
		this.swoopTarget = null;
		this.diveAngle = 0.0F;
		this.diveHeight = 0.0F;
		this.maxSteepness = 40.0F;
		this.strikeDistance = (entity.width + 1.5F);
		this.minHeight = 6.0F;
		this.minXZDistance = 10.0F;
		this.finalRunLength = 4.0F;
		this.finalRunArcLimit = 15.0F;
		this.time = 0;
		this.isCommittedToFinalRun = false;
		this.endSwoop = false;
		this.usingClaws = false;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if ((this.theEntity.getAIGoal() == Goal.FIND_ATTACK_OPPORTUNITY)
				&& (this.theEntity.getAttackTarget() != null)) {
			this.swoopTarget = this.theEntity.getAttackTarget();
			double dX = this.swoopTarget.posX - this.theEntity.posX;
			double dY = this.swoopTarget.posY - this.theEntity.posY;
			double dZ = this.swoopTarget.posZ - this.theEntity.posZ;
			double dXZ = Math.sqrt(dX * dX + dZ * dZ);
			if ((-dY < this.minHeight) || (dXZ < this.minXZDistance)) {
				return false;
			}
			double pitchToTarget = Math.atan(dY / dXZ) * 180.0D / 3.141592653589793D;
			if (pitchToTarget > this.maxSteepness) {
				return false;
			}
			this.finalRunLength = ((float) (dXZ * 0.42D));
			if (this.finalRunLength > 18.0F)
				this.finalRunLength = 18.0F;
			else if (this.finalRunLength < 4.0F) {
				this.finalRunLength = 4.0F;
			}
			this.diveAngle = ((float) (Math.atan((dXZ - this.finalRunLength) / dY) * 180.0D / 3.141592653589793D));
			if ((this.swoopTarget != null) && (this.isSwoopPathClear(this.swoopTarget, this.diveAngle))) {
				this.diveHeight = ((float) -dY);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return (this.theEntity.getAttackTarget() == this.swoopTarget) && (!this.endSwoop)
				&& (this.theEntity.getMoveState() == MoveState.FLYING);
	}

	@Override
	public void startExecuting() {
		this.time = 0;
		this.theEntity.transitionAIGoal(Goal.SWOOP);
		this.theEntity.getNavigatorNew().setMovementType(INavigationFlying.MoveType.PREFER_FLYING);
		this.theEntity.getNavigatorNew().tryMoveToEntity(this.swoopTarget, 0.0F,
				this.theEntity.getMaxPoweredFlightSpeed());

		this.theEntity.doScreech();
	}

	@Override
	public void resetTask() {
		this.endSwoop = false;
		this.isCommittedToFinalRun = false;
		this.theEntity.getNavigatorNew().enableDirectTarget(false);
		if (this.theEntity.getAIGoal() == Goal.SWOOP) {
			this.theEntity.transitionAIGoal(Goal.NONE);
			this.theEntity.setClawsForward(false);
		}
	}

	@Override
	public void updateTask() {
		this.time += 1;
		if (!this.isCommittedToFinalRun) {
			if (this.theEntity.getDistance(this.swoopTarget) < this.finalRunLength) {
				this.theEntity.getNavigatorNew().setPitchBias(0.0F, 1.0F);
				if (this.isFinalRunLinedUp()) {
					this.usingClaws = (this.theEntity.world.rand.nextFloat() > 0.6F);

					this.theEntity.setClawsForward(true);

					this.theEntity.getNavigatorNew().enableDirectTarget(true);
					this.isCommittedToFinalRun = true;
				} else {
					this.theEntity.transitionAIGoal(Goal.NONE);
					this.endSwoop = true;
				}
			} else if (this.time > 25) {
				double dYp = -(this.swoopTarget.posY - this.theEntity.posY);
				if (dYp < 2.9D) {
					dYp = 0.0D;
				}
				this.theEntity.getNavigatorNew().setPitchBias(this.diveAngle * (float) (dYp / this.diveHeight),
						(float) (0.6D * (dYp / this.diveHeight)));
			}

		} else if (this.theEntity.getDistance(this.swoopTarget) < this.strikeDistance) {
			this.theEntity.transitionAIGoal(Goal.FLYING_STRIKE);

			this.theEntity.getNavigatorNew().enableDirectTarget(false);
			this.endSwoop = true;
		} else {
			double dX = this.swoopTarget.posX - this.theEntity.posX;
			double dZ = this.swoopTarget.posZ - this.theEntity.posZ;
			double yawToTarget = Math.atan2(dZ, dX) * 180.0D / 3.141592653589793D - 90.0D;
			if (Math.abs(MathUtil.boundAngle180Deg(yawToTarget - this.theEntity.rotationYaw)) > 90.0D) {
				this.theEntity.transitionAIGoal(Goal.NONE);
				this.theEntity.getNavigatorNew().enableDirectTarget(false);
				this.theEntity.setClawsForward(false);
				this.endSwoop = true;
			}
		}
	}

	private boolean isSwoopPathClear(EntityLivingBase target, float diveAngle) {
		double dX = target.posX - this.theEntity.posX;
		double dY = target.posY - this.theEntity.posY;
		double dZ = target.posZ - this.theEntity.posZ;
		double dXZ = Math.sqrt(dX * dX + dZ * dZ);
		double dRayY = 2.0D;
		int hitCount = 0;
		double lowestCollide = this.theEntity.posY;
		for (double y = this.theEntity.posY - dRayY; y > target.posY; y -= dRayY) {
			double dist = Math.tan(90.0F + diveAngle) * (this.theEntity.posY - y);
			double x = -Math.sin(this.theEntity.rotationYaw / 180.0F * 3.141592653589793D) * dist;
			double z = Math.cos(this.theEntity.rotationYaw / 180.0F * 3.141592653589793D) * dist;
			Vec3d source = new Vec3d(x, y, z);
			// MovingObjectPosition collide = this.theEntity.world.rayTraceBlocks(source,
			// target.getLook(1.0F));
			RayTraceResult collide = this.theEntity.world.rayTraceBlocks(source, target.getLook(1f), true, true, false);
			if (collide != null) {
				if (hitCount == 0) {
					lowestCollide = y;
				}
				hitCount++;
			}
		}

		if (this.isAcceptableDiveSpace(this.theEntity.posY, lowestCollide, hitCount)) {
			return true;
		}

		return false;
	}

	private boolean isFinalRunLinedUp() {
		double dX = this.swoopTarget.posX - this.theEntity.posX;
		double dY = this.swoopTarget.posY - this.theEntity.posY;
		double dZ = this.swoopTarget.posZ - this.theEntity.posZ;
		double dXZ = Math.sqrt(dX * dX + dZ * dZ);
		double yawToTarget = Math.atan2(dZ, dX) * 180.0D / 3.141592653589793D - 90.0D;
		double dYaw = MathUtil.boundAngle180Deg(yawToTarget - this.theEntity.rotationYaw);
		if ((dYaw < -this.finalRunArcLimit) || (dYaw > this.finalRunArcLimit)) {
			return false;
		}
		double dPitch = Math.atan(dY / dXZ) * 180.0D / 3.141592653589793D - this.theEntity.rotationPitch;
		if ((dPitch < -this.finalRunArcLimit) || (dPitch > this.finalRunArcLimit)) {
			return false;
		}
		return true;
	}

	protected boolean isAcceptableDiveSpace(double entityPosY, double lowestCollideY, int hitCount) {
		double clearanceY = entityPosY - lowestCollideY;
		if (clearanceY < this.minDiveClearanceY) {
			return false;
		}
		return true;
	}
}