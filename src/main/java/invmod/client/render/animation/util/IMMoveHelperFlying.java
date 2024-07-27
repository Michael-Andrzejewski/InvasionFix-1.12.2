// `^`^`^`
// ```java
// /**
//  * This code defines the IMMoveHelperFlying class, which extends the IMMoveHelper class and is designed to control the flying behavior of the EntityIMFlying entity in a Minecraft mod. The class manages the entity's movement in the air, including takeoff, flying, and landing sequences, as well as ground movement when not flying.
// 
//  * Class IMMoveHelperFlying:
//  * - EntityIMFlying a: The flying entity being controlled.
//  * - double targetFlySpeed: The desired speed for the entity while flying.
//  * - boolean wantsToBeFlying: A flag indicating whether the entity wants to be in flight.
// 
//  * Constructor:
//  * - IMMoveHelperFlying(EntityIMFlying entity): Initializes the helper with the specified flying entity.
// 
//  * Methods:
//  * - setHeading(float yaw, float pitch, float idealSpeed, int time): Sets the entity's heading based on yaw and pitch angles, desired speed, and time.
//  * - setWantsToBeFlying(boolean flag): Sets the wantsToBeFlying flag to indicate if the entity should attempt to fly.
//  * - onUpdateMoveHelper(): Updates the entity's movement state each tick, handling transitions between flying, takeoff, and landing.
//  * - doGroundMovement(): Handles the entity's ground movement when not flying.
//  * - doFlying(): Manages the flying behavior when the entity is in the air.
//  * - fly(): Calculates and applies forces for flying, including lift, thrust, and banking.
//  * - doTakeOff(): Manages the takeoff sequence, preparing the entity for flight.
// 
//  * The code uses various mathematical calculations to simulate realistic flying dynamics, including acceleration, turning, and pitch adjustments. It also accounts for environmental factors such as being in water or lava.
//  */
// ```
// This code appears to be part of a flight control system for an entity within a game or simulation environment, possibly for an aircraft or flying creature. The code manages different states of flight such as taking off, flying, and landing, and calculates the necessary thrust for movement.
// 
// - `setJumping()`: This method is likely to initiate the takeoff sequence or to set the entity in a pre-flight state, preparing it for liftoff.
// 
// - `calcThrust(double desiredVThrustRatio)`: This method calculates the thrust vector based on a desired vertical thrust ratio. It adjusts the horizontal and vertical components of the thrust within specified minimum and maximum ratios, and then calculates the acceleration in the x, y, and z directions based on the entity's current yaw (direction).
// 
// - `doTakeoff()`: This method handles the takeoff procedure. It calculates the entity's current speed and determines if the lift force generated is sufficient for takeoff. If the lift force is greater than gravity, the entity enters the FLYING state; otherwise, it remains in the TAKEOFF state.
// 
// - `doLanding()`: This method manages the landing process. It sets ground friction, checks the blocks beneath the entity to adjust the target flight speed for a smooth landing, and then calls the `fly()` method to continue flying or transition to landing. It ensures that the entity sets its thrust on and checks if the entity has touched the ground with a speed below a certain threshold to transition to the GROUNDED state, or if further adjustments are needed for a TOUCHDOWN state.
// 
// Overall, the code is designed to provide a realistic flight experience by dynamically adjusting the entity's flight behavior based on its speed, orientation, and interaction with the environment. It uses a state machine approach to transition between different phases of flight.
// `^`^`^`

package invmod.client.render.animation.util;

import invmod.entity.MoveState;
import invmod.entity.monster.EntityIMFlying;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class IMMoveHelperFlying extends IMMoveHelper {
	private EntityIMFlying a;
	private double targetFlySpeed;
	private boolean wantsToBeFlying;

	public IMMoveHelperFlying(EntityIMFlying entity) {
		super(entity);
		this.a = entity;
		this.wantsToBeFlying = false;
	}

	public void setHeading(float yaw, float pitch, float idealSpeed, int time) {
		double x = this.a.posX + Math.sin(yaw / 180.0F * 3.141592653589793D) * idealSpeed * time;
		double y = this.a.posY + Math.sin(pitch / 180.0F * 3.141592653589793D) * idealSpeed * time;
		double z = this.a.posZ + Math.cos(yaw / 180.0F * 3.141592653589793D) * idealSpeed * time;
		this.setMoveTo(x, y, z, idealSpeed);
	}

	public void setWantsToBeFlying(boolean flag) {
		this.wantsToBeFlying = flag;
	}

	@Override
	public void onUpdateMoveHelper() {
		this.a.setMoveForward(0.0F);
		this.a.setFlightAccelerationVector(0.0F, 0.0F, 0.0F);
		if ((!this.needsUpdate) && (this.a.getMoveState() != MoveState.FLYING)) {
			this.a.setMoveState(MoveState.STANDING);
			this.a.setFlyState(FlyState.GROUNDED);
			this.a.rotationPitch = this.correctRotation(this.a.rotationPitch, 50.0F, 4.0F);
			return;
		}
		this.needsUpdate = false;

		if (this.wantsToBeFlying) {
			if (this.a.getFlyState() == FlyState.GROUNDED) {
				this.a.setMoveState(MoveState.RUNNING);
				this.a.setFlyState(FlyState.TAKEOFF);
			} else if (this.a.getFlyState() == FlyState.FLYING) {
				this.a.setMoveState(MoveState.FLYING);
			}

		} else if (this.a.getFlyState() == FlyState.FLYING) {
			this.a.setFlyState(FlyState.LANDING);
		}

		if (this.a.getFlyState() == FlyState.FLYING) {
			FlyState result = this.doFlying();
			if (result == FlyState.GROUNDED)
				this.a.setMoveState(MoveState.STANDING);
			else if (result == FlyState.FLYING) {
				this.a.setMoveState(MoveState.FLYING);
			}
			this.a.setFlyState(result);
		} else if (this.a.getFlyState() == FlyState.TAKEOFF) {
			FlyState result = this.doTakeOff();
			if (result == FlyState.GROUNDED)
				this.a.setMoveState(MoveState.STANDING);
			else if (result == FlyState.TAKEOFF)
				this.a.setMoveState(MoveState.RUNNING);
			else if (result == FlyState.FLYING) {
				this.a.setMoveState(MoveState.FLYING);
			}
			this.a.setFlyState(result);
		} else if ((this.a.getFlyState() == FlyState.LANDING) || (this.a.getFlyState() == FlyState.TOUCHDOWN)) {
			FlyState result = this.doLanding();
			if ((result == FlyState.GROUNDED) || (result == FlyState.TOUCHDOWN)) {
				this.a.setMoveState(MoveState.RUNNING);
			}
			this.a.setFlyState(result);
		} else {
			MoveState result = this.doGroundMovement();
			this.a.setMoveState(result);
		}
	}

	@Override
	protected MoveState doGroundMovement() {
		this.a.setGroundFriction(0.6F);
		this.a.setRotationRoll(this.correctRotation(this.a.getRotationRoll(), 0.0F, 6.0F));
		this.targetSpeed = this.a.getMoveSpeedStat();
		this.a.rotationPitch = this.correctRotation(this.a.rotationPitch, 50.0F, 4.0F);
		return super.doGroundMovement();
	}

	protected FlyState doFlying() {
		this.targetFlySpeed = this.speed;
		return this.fly();
	}

	protected FlyState fly() {
		this.a.setGroundFriction(1.0F);
		boolean isInLiquid = (this.a.isInWater()) || (this.a.isInLava());// (this.a.handleLavaMovement());
		double dX = this.posX - this.a.posX;
		double dZ = this.posZ - this.a.posZ;
		double dY = this.posY - this.a.posY;

		double dXZSq = dX * dX + dZ * dZ;
		double dXZ = Math.sqrt(dXZSq);
		double distanceSquared = dXZSq + dY * dY;

		if (distanceSquared > 0.04D) {
			int timeToTurn = 10;
			float gravity = this.a.getGravity();
			float liftConstant = gravity;
			double xAccel = 0.0D;
			double yAccel = 0.0D;
			double zAccel = 0.0D;
			double velX = this.a.motionX;
			double velY = this.a.motionY;
			double velZ = this.a.motionZ;
			double hSpeedSq = velX * velX + velZ * velZ;
			if (hSpeedSq == 0.0D)
				hSpeedSq = 1.0E-008D;
			double horizontalSpeed = Math.sqrt(hSpeedSq);
			double flySpeed = Math.sqrt(hSpeedSq + velY * velY);

			double desiredYVelocity = dY / timeToTurn;
			double dVelY = desiredYVelocity - (velY - gravity);

			float minFlightSpeed = 0.05F;
			if (flySpeed < minFlightSpeed) {
				float newYaw = (float) (Math.atan2(dZ, dX) * 180.0D / 3.141592653589793D - 90.0D);
				newYaw = this.correctRotation(this.a.rotationYaw, newYaw, this.a.getTurnRate());
				this.a.rotationYaw = newYaw;
				if (this.a.onGround) {
					return FlyState.GROUNDED;
				}
			} else {
				double liftForce = flySpeed / (this.a.getMaxPoweredFlightSpeed() * this.a.getLiftFactor())
						* liftConstant;
				double climbForce = liftForce * horizontalSpeed / (Math.abs(velY) + horizontalSpeed);
				double forwardForce = liftForce * Math.abs(velY) / (Math.abs(velY) + horizontalSpeed);
				double turnForce = liftForce;
				double climbAccel;
				if (dVelY < 0.0D) {
					double maxDiveForce = this.a.getMaxTurnForce() - gravity;
					climbAccel = -Math.min(Math.min(climbForce, maxDiveForce), -dVelY);
				} else {
					double maxClimbForce = this.a.getMaxTurnForce() + gravity;
					climbAccel = Math.min(Math.min(climbForce, maxClimbForce), dVelY);
				}

				float minBankForce = 0.01F;
				if (turnForce < minBankForce) {
					turnForce = minBankForce;
				}

				double desiredXZHeading = Math.atan2(dZ, dX) - 1.570796326794897D;
				double currXZHeading = Math.atan2(velZ, velX) - 1.570796326794897D;
				double dXZHeading = desiredXZHeading - currXZHeading;
				while (dXZHeading >= 3.141592653589793D)
					dXZHeading -= 6.283185307179586D;
				while (dXZHeading < -3.141592653589793D)
					dXZHeading += 6.283185307179586D;
				double bankForce = horizontalSpeed * dXZHeading / timeToTurn;
				double maxBankForce = Math.min(turnForce, this.a.getMaxTurnForce());
				if (bankForce > maxBankForce)
					bankForce = maxBankForce;
				else if (bankForce < -maxBankForce) {
					bankForce = -maxBankForce;
				}

				double bankXAccel = bankForce * -velZ / horizontalSpeed;
				double bankZAccel = bankForce * velX / horizontalSpeed;

				double totalForce = xAccel + yAccel + zAccel;

				double r = liftForce / totalForce;
				xAccel += bankXAccel;
				yAccel += climbAccel;
				zAccel += bankZAccel;
				velX += bankXAccel;
				velY += climbAccel;
				velZ += bankZAccel;

				double dYAccelGravity = yAccel - gravity;
				double middlePitch = 15.0D;
				double newPitch;
				if (velY - gravity < 0.0D) {
					double climbForceRatio = yAccel / climbForce;
					if (climbForceRatio > 1.0D)
						climbForceRatio = 1.0D;
					else if (climbForceRatio < -1.0D) {
						climbForceRatio = -1.0D;
					}
					double xzSpeed = Math.sqrt(velX * velX + velZ * velZ);
					double velPitch;
					if (xzSpeed > 0.0D)
						velPitch = Math.atan(velY / xzSpeed) / 3.141592653589793D * 180.0D;
					else {
						velPitch = -180.0D;
					}
					double pitchInfluence = (this.a.getMaxPoweredFlightSpeed() - Math.abs(velY))
							/ this.a.getMaxPoweredFlightSpeed();
					if (pitchInfluence < 0.0D) {
						pitchInfluence = 0.0D;
					}
					newPitch = velPitch + 15.0D * climbForceRatio * pitchInfluence;
				} else {
					double pitchLimit = this.a.getMaxPitch();
					double climbForceRatio = Math.min(yAccel / climbForce, 1.0D);
					newPitch = middlePitch + (pitchLimit - middlePitch) * climbForceRatio;
				}
				newPitch = this.correctRotation(this.a.rotationPitch, (float) newPitch, 1.5F);
				double newYaw = Math.atan2(velZ, velX) * 180.0D / 3.141592653589793D - 90.0D;
				newYaw = this.correctRotation(this.a.rotationYaw, (float) newYaw, this.a.getTurnRate());
				this.a.setPositionAndRotation(this.a.posX, this.a.posY, this.a.posZ, (float) newYaw, (float) newPitch);
				double newRoll = 60.0D * bankForce / turnForce;
				this.a.setRotationRoll(this.correctRotation(this.a.getRotationRoll(), (float) newRoll, 6.0F));
				double horizontalForce;
				if (velY > 0.0D) {
					horizontalForce = -climbAccel;
				} else {
					horizontalForce = forwardForce;
				}
				int xDirection = velX > 0.0D ? 1 : -1;
				int zDirection = velZ > 0.0D ? 1 : -1;
				double hComponentX = xDirection * velX / (xDirection * velX + zDirection * velZ);

				double xLiftAccel = xDirection * horizontalForce * hComponentX;
				double zLiftAccel = zDirection * horizontalForce * (1.0D - hComponentX);

				double loss = 0.4D;
				xLiftAccel += xDirection * -Math.abs(bankForce * loss) * hComponentX;
				zLiftAccel += zDirection * -Math.abs(bankForce * loss) * (1.0D - hComponentX);

				xAccel += xLiftAccel;
				zAccel += zLiftAccel;
			}

			if (flySpeed < this.targetFlySpeed) {
				this.a.setThrustEffort(0.6F);
				if (!this.a.isThrustOn()) {
					this.a.setThrustOn(true);
				}
				double desiredVThrustRatio = (dVelY - yAccel) / this.a.getThrust();
				Vec3d thrust = this.calcThrust(desiredVThrustRatio);
				xAccel += thrust.x;
				yAccel += thrust.y;
				zAccel += thrust.z;
			} else if (flySpeed > this.targetFlySpeed * 1.8D) {
				this.a.setThrustEffort(1.0F);
				if (!this.a.isThrustOn()) {
					this.a.setThrustOn(true);
				}
				double desiredVThrustRatio = (dVelY - yAccel) / (this.a.getThrust() * 10.0F);
				Vec3d thrust = this.calcThrust(desiredVThrustRatio);
				xAccel += -thrust.x;
				yAccel += thrust.y;
				zAccel += -thrust.z;
			} else if (this.a.isThrustOn()) {
				this.a.setThrustOn(false);
			}

			this.a.setFlightAccelerationVector((float) xAccel, (float) yAccel, (float) zAccel);
		}
		return FlyState.FLYING;
	}

	protected FlyState doTakeOff() {
		this.a.setGroundFriction(0.98F);
		this.a.setThrustOn(true);
		this.a.setThrustEffort(1.0F);
		this.targetSpeed = this.a.getMoveSpeedStat();

		MoveState result = this.doGroundMovement();
		if (result == MoveState.STANDING) {
			return FlyState.GROUNDED;
		}
		if (this.a.collidedHorizontally) {
			this.a.getJumpHelper().setJumping();
		}
		Vec3d thrust = this.calcThrust(0.0D);
		this.a.setFlightAccelerationVector((float) thrust.x, (float) thrust.y, (float) thrust.z);
		double speed = Math.sqrt(
				this.a.motionX * this.a.motionX + this.a.motionY * this.a.motionY + this.a.motionZ * this.a.motionZ);

		this.a.rotationPitch = this.correctRotation(this.a.rotationPitch, 40.0F, 4.0F);

		float gravity = this.a.getGravity();
		float liftConstant = gravity;
		double liftForce = speed / (this.a.getMaxPoweredFlightSpeed() * this.a.getLiftFactor()) * liftConstant;

		if (liftForce > gravity) {
			return FlyState.FLYING;
		}
		return FlyState.TAKEOFF;
	}

	protected FlyState doLanding() {
		this.a.setGroundFriction(0.3F);
		int x = MathHelper.floor(this.a.posX);
		int y = MathHelper.floor(this.a.posY);
		int z = MathHelper.floor(this.a.posZ);

		for (int i = 1; i < 5; i++) {
			if (this.a.world.getBlockState(new BlockPos(x, y - i, z)).getBlock() != Blocks.AIR)
				break;
			this.targetFlySpeed = (this.speed * (0.66F - (0.4F - (i - 1) * 0.133F)));
		}

		FlyState result = this.fly();
		this.a.setThrustOn(true);
		if (result == FlyState.FLYING) {
			double speed = Math.sqrt(this.a.motionX * this.a.motionX + this.a.motionY * this.a.motionY
					+ this.a.motionZ * this.a.motionZ);
			if (this.a.onGround) {
				if (speed < this.a.getLandingSpeedThreshold()) {
					return FlyState.GROUNDED;
				}

				this.a.setRotationRoll(this.correctRotation(this.a.getRotationRoll(), 40.0F, 6.0F));
				return FlyState.TOUCHDOWN;
			}
		}

		return FlyState.LANDING;
	}

	protected Vec3d calcThrust(double desiredVThrustRatio) {
		float thrust = this.a.getThrust();
		float rMin = this.a.getThrustComponentRatioMin();
		float rMax = this.a.getThrustComponentRatioMax();
		double vThrustRatio = desiredVThrustRatio;
		if (vThrustRatio > rMax)
			vThrustRatio = rMax;
		else if (vThrustRatio < rMin) {
			vThrustRatio = rMin;
		}
		double hThrust = (1.0D - vThrustRatio) * thrust;
		double vThrust = vThrustRatio * thrust;
		double xAccel = hThrust * -Math.sin(this.a.rotationYaw / 180.0F * 3.141592653589793D);
		double yAccel = vThrust;
		double zAccel = hThrust * Math.cos(this.a.rotationYaw / 180.0F * 3.141592653589793D);
		// Vec3 vec = this.a.world.getWorldVec3Pool().getVecFromPool(xAccel,
		// yAccel, zAccel);
		Vec3d vec = new Vec3d(xAccel, yAccel, zAccel);
		return vec;
	}
}