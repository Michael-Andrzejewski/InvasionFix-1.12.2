// `^`^`^`
// ```java
// /**
//  * This class defines the AI behavior for a charging attack used by certain entities in Minecraft.
//  * It extends the EntityAIMoveToEntity class, specifying the behavior for entities to charge towards a target.
//  *
//  * Constructor:
//  * - EntityAICharge(EntityIMMob entity, Class<? extends T> targetClass, float f):
//  *   Initializes the AI with the charging entity, the target class, and the speed of the charge.
//  *
//  * Methods:
//  * - shouldExecute():
//  *   Determines if the AI should start executing, checking conditions such as distance to target,
//  *   whether the entity is on the ground, and if a charge position can be found.
//  *
//  * - startExecuting():
//  *   Prepares the entity for charging by setting a windup time before the actual charge.
//  *
//  * - shouldContinueExecuting():
//  *   Checks if the entity should continue executing the charge, based on windup time and run time.
//  *
//  * - updateTask():
//  *   Updates the task each tick, handling the windup, movement towards the target, and attacking the target
//  *   when in range.
//  *
//  * - resetTask():
//  *   Resets the task after execution or if it's interrupted, resetting windup, target, and attack status.
//  *
//  * - findChargePoint(Entity attacker, Entity target, double overshoot):
//  *   Calculates the point to charge towards, overshooting the target to ensure contact.
//  *
//  * This AI is typically used for entities that have a charging attack, such as the EntityIMZombiePigman,
//  * allowing them to aggressively move towards and attack their target.
//  */
// ```
// `^`^`^`

package invmod.entity.ai;

import invmod.entity.monster.EntityIMMob;
import invmod.entity.monster.EntityIMZombiePigman;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class EntityAICharge<T extends EntityLivingBase> extends EntityAIMoveToEntity<T> {
	protected EntityCreature charger;
	protected EntityLivingBase chargeTarget;
	protected double chargeX;
	protected double chargeY;
	protected double chargeZ;
	protected float speed;
	protected int windup;
	protected boolean hasAttacked;
	protected int chargeDelay;
	protected int runTime;

	public EntityAICharge(EntityIMMob entity, Class<? extends T> targetClass, float f) {
		super(entity, targetClass);
		this.charger = entity;
		this.speed = f;
		this.windup = 0;
		this.hasAttacked = false;
		this.chargeDelay = 100;
		this.runTime = 15;
	}

	@Override
	public boolean shouldExecute() {

		if (this.chargeDelay > 0) {
			this.chargeDelay--;
			return false;
		}

		this.chargeTarget = this.charger.getAttackTarget();
		if (this.chargeTarget == null) {
			return false;

		}
		double distance = Math.sqrt(this.charger.getDistanceSq(this.chargeTarget));
		if ((distance < 5.0D) || (distance > 20.0D)) {
			return false;
		}
		if (!this.charger.onGround) {
			return false;
		}
		Vec3d chargePos = this.findChargePoint(this.charger, this.chargeTarget, 6.0D);
		if (chargePos == null) {
			return false;
		}

		this.chargeX = chargePos.x;
		this.chargeY = chargePos.y;
		this.chargeZ = chargePos.z;

		return this.charger.getRNG().nextInt(1) == 0;
	}

	@Override
	public void startExecuting() {

		this.windup = (15 + this.charger.getRNG().nextInt(25));
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (this.windup == 0 && this.runTime > 0) {
			this.runTime--;
		}

		return (this.windup > 0) || (this.runTime > 0);
	}

	@Override
	public void updateTask() {
		this.charger.getLookHelper().setLookPosition(this.chargeX, this.chargeY - 1.0D, this.chargeZ, 10.0F,
				this.charger.getVerticalFaceSpeed());
		if (this.windup > 0) {
			if (--this.windup == 0) {
				this.charger.getNavigator().tryMoveToXYZ(this.chargeX, this.chargeY, this.chargeZ, this.speed);
			} else {
				EntityCreature tmp90_87 = this.charger;
				tmp90_87.limbSwingAmount = ((float) (tmp90_87.limbSwingAmount + 0.8D));
				if ((this.charger instanceof EntityIMZombiePigman)) {
					((EntityIMZombiePigman) this.charger).setCharging(true);
				}
			}
		}
		double var1 = this.charger.width * 2.1F * this.charger.width * 2.1F;
		if (this.charger.getDistanceSq(this.chargeTarget.posX, this.chargeTarget.getEntityBoundingBox().minY,
				this.chargeTarget.posZ) <= var1) {
			if (!this.hasAttacked) {
				this.hasAttacked = true;
				this.charger.attackEntityAsMob(this.chargeTarget);
			}
		}
	}

	@Override
	public void resetTask() {
		this.windup = 0;
		this.chargeTarget = null;
		this.hasAttacked = false;
		this.chargeDelay = 100;
		this.runTime = 15;
		if ((this.charger instanceof EntityIMZombiePigman)) {
			((EntityIMZombiePigman) this.charger).setCharging(false);
		}
	}

	protected Vec3d findChargePoint(Entity attacker, Entity target, double overshoot) {
		double vecx = target.posX - attacker.posX;
		double vecz = target.posZ - attacker.posZ;
		float rangle = (float) Math.atan2(vecz, vecx);

		double distance = MathHelper.sqrt(vecx * vecx + vecz * vecz);

		double dx = MathHelper.cos(rangle) * (distance + overshoot);
		double dz = MathHelper.sin(rangle) * (distance + overshoot);

		return new Vec3d((attacker.posX + dx), target.posY, (attacker.posZ + dz));
	}
}
