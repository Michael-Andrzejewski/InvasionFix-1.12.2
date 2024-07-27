// `^`^`^`
// ```java
// /**
//  * This class defines the pouncing behavior for the EntityIMSpider in a Minecraft mod.
//  * The EntityAIPounce class extends EntityAIBase to create a custom AI task that allows
//  * the spider entity to pounce on its target.
//  *
//  * Constructor:
//  * - EntityAIPounce(EntityIMSpider entity, float minPower, float maxPower, int cooldown):
//  *   Initializes the AI with the spider entity, minimum and maximum pounce power, and cooldown period.
//  *
//  * Methods:
//  * - shouldExecute(): Determines if the AI task should start, checking if the pounce cooldown has elapsed,
//  *   if there is a target in sight, and if the spider is on the ground.
//  *
//  * - shouldContinueExecuting(): Checks if the spider should continue pouncing.
//  *
//  * - startExecuting(): Initiates the pounce if the target is in a valid position, setting the spider as
//  *   airborne and halting its navigation for a tick.
//  *
//  * - updateTask(): Updates the task each tick, halting navigation and checking if the spider has landed
//  *   to reset the pounce timer and clear the navigation path.
//  *
//  * - pounce(double x, double y, double z): Calculates the trajectory for the pounce based on the target's
//  *   position and the spider's current position and gravity. It ensures the pounce power is within the
//  *   specified range and sets the spider's motion accordingly.
//  *
//  * The AI task is designed to add a dynamic and challenging behavior to the EntityIMSpider, making it
//  * more engaging in combat scenarios.
//  */
// ```
// `^`^`^`

package invmod.entity.ai;

import invmod.entity.monster.EntityIMSpider;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.MathHelper;

public class EntityAIPounce extends EntityAIBase {

	private EntityIMSpider theEntity;
	private boolean isPouncing;
	private int pounceTimer;
	private int cooldown;
	private float minPower;
	private float maxPower;

	public EntityAIPounce(EntityIMSpider entity, float minPower, float maxPower, int cooldown) {
		this.theEntity = entity;
		this.isPouncing = false;
		this.minPower = minPower;
		this.maxPower = maxPower;
		this.cooldown = cooldown;
	}

	@Override
	public boolean shouldExecute() {
		EntityLivingBase target = this.theEntity.getAttackTarget();
		if ((--this.pounceTimer <= 0) && (target != null) && (this.theEntity.canEntityBeSeen(target))
				&& (this.theEntity.onGround)) {
			return true;
		}

		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.isPouncing;
	}

	@Override
	public void startExecuting() {
		EntityLivingBase target = this.theEntity.getAttackTarget();
		if (this.pounce(target.posX, target.posY, target.posZ)) {
			this.theEntity.setAirborneTime(0);
			this.isPouncing = true;
			this.theEntity.getNavigatorNew().haltForTick();
		} else {
			this.isPouncing = false;
		}
	}

	@Override
	public void updateTask() {
		this.theEntity.getNavigatorNew().haltForTick();
		int airborneTime = this.theEntity.getAirborneTime();
		if ((airborneTime > 20) && (this.theEntity.onGround)) {
			this.isPouncing = false;
			this.pounceTimer = this.cooldown;
			this.theEntity.setAirborneTime(0);
			this.theEntity.getNavigatorNew().clearPath();
		} else {
			this.theEntity.setAirborneTime(airborneTime + 1);
		}
	}

	protected boolean pounce(double x, double y, double z) {
		double dX = x - this.theEntity.posX;
		double dY = y - this.theEntity.posY;
		double dZ = z - this.theEntity.posZ;
		double dXZ = MathHelper.sqrt(dX * dX + dZ * dZ);
		double a = Math.atan(dY / dXZ);
		if ((a > -0.7853981633974483D) && (a < 0.7853981633974483D)) {
			double rratio = (1.0D - Math.tan(a)) * (1.0D / Math.cos(a));
			double r = dXZ / rratio;
			double v = 1.0D / Math.sqrt(1.0F / this.theEntity.getGravity() / r);
			if ((v > this.minPower) && (v < this.maxPower)) {
				double distance = MathHelper.sqrt(2.0D * (dXZ * dXZ));
				this.theEntity.motionX = (v * dX / distance);
				this.theEntity.motionY = (v * dXZ / distance);
				this.theEntity.motionZ = (v * dZ / distance);
				return true;
			}
		}
		return false;
	}
}