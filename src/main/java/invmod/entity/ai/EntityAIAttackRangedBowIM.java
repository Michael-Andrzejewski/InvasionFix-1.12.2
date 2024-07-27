// `^`^`^`
// ```java
// /**
//  * This class represents an AI behavior for a custom skeleton entity (EntityIMSkeleton) in Minecraft that allows it to
//  * use a bow for ranged attacks. The AI handles the logic for the skeleton to attack its target with a bow, manage its
//  * movement while attacking, and adjust its strafing behavior to make it a more challenging opponent.
//  *
//  * Methods:
//  * - EntityAIAttackRangedBowIM: Constructor that initializes the AI with the skeleton entity, movement speed amplifier,
//  *   attack cooldown, and maximum attack distance.
//  * - setAttackCooldown: Sets the cooldown period between the skeleton's ranged attacks.
//  * - shouldExecute: Determines if the AI should start executing, checking if the skeleton has a target and a bow in hand.
//  * - isBowInMainhand: Helper method to check if the skeleton is holding a bow in its main hand.
//  * - shouldContinueExecuting: Checks if the AI should continue executing, based on the presence of a target and a bow.
//  * - startExecuting: Called when the AI begins execution, sets the skeleton to use its arms.
//  * - resetTask: Resets the AI task, stopping the arm swinging animation and resetting attack timers.
//  * - updateTask: Updates the AI task each tick, handling the logic for movement, strafing, and attacking behavior.
//  *
//  * The AI uses a combination of timers and distance checks to decide when to move towards the target, when to strafe,
//  * and when to attack. It also dynamically adjusts its strafing direction and whether to move forwards or backwards.
//  */
// ```
// `^`^`^`

package invmod.entity.ai;

import invmod.entity.monster.EntityIMSkeleton;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.util.EnumHand;

public class EntityAIAttackRangedBowIM extends EntityAIBase {

	private final EntityIMSkeleton entity;
	private final double moveSpeedAmp;
	private int attackCooldown;
	private final float maxAttackDistance;
	private int attackTime = -1;
	private int seeTime;
	private boolean strafingClockwise;
	private boolean strafingBackwards;
	private int strafingTime = -1;

	public EntityAIAttackRangedBowIM(EntityIMSkeleton skeleton, double speedAmplifier, int delay, float maxDistance) {
		this.entity = skeleton;
		this.moveSpeedAmp = speedAmplifier;
		this.attackCooldown = delay;
		this.maxAttackDistance = maxDistance * maxDistance;
		this.setMutexBits(3);
	}

	public void setAttackCooldown(int p_189428_1_) {
		this.attackCooldown = p_189428_1_;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		return this.entity.getAttackTarget() == null ? false : this.isBowInMainhand();
	}

	protected boolean isBowInMainhand() {
		return this.entity.getHeldItemMainhand() != null && this.entity.getHeldItemMainhand().getItem() == Items.BOW;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean shouldContinueExecuting() {
		return (this.shouldExecute() || !this.entity.getNavigator().noPath()) && this.isBowInMainhand();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		super.startExecuting();
		this.entity.setSwingingArms(true);
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		super.resetTask();
		this.entity.setSwingingArms(false);
		this.seeTime = 0;
		this.attackTime = -1;
		this.entity.resetActiveHand();
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask() {
		EntityLivingBase entitylivingbase = this.entity.getAttackTarget();

		if (entitylivingbase != null) {
			double d0 = this.entity.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY,
					entitylivingbase.posZ);
			boolean flag = this.entity.getEntitySenses().canSee(entitylivingbase);
			boolean flag1 = this.seeTime > 0;

			if (flag != flag1) {
				this.seeTime = 0;
			}

			if (flag) {
				++this.seeTime;
			} else {
				--this.seeTime;
			}

			if (d0 <= (double) this.maxAttackDistance && this.seeTime >= 20) {
				this.entity.getNavigator().clearPath();
				++this.strafingTime;
			} else {
				this.entity.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.moveSpeedAmp);
				this.strafingTime = -1;
			}

			if (this.strafingTime >= 20) {
				if ((double) this.entity.getRNG().nextFloat() < 0.3D) {
					this.strafingClockwise = !this.strafingClockwise;
				}

				if ((double) this.entity.getRNG().nextFloat() < 0.3D) {
					this.strafingBackwards = !this.strafingBackwards;
				}

				this.strafingTime = 0;
			}

			if (this.strafingTime > -1) {
				if (d0 > (double) (this.maxAttackDistance * 0.75F)) {
					this.strafingBackwards = false;
				} else if (d0 < (double) (this.maxAttackDistance * 0.25F)) {
					this.strafingBackwards = true;
				}

				this.entity.getMoveHelper().strafe(this.strafingBackwards ? -0.5F : 0.5F,
						this.strafingClockwise ? 0.5F : -0.5F);
				this.entity.faceEntity(entitylivingbase, 30.0F, 30.0F);
			} else {
				this.entity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
			}

			if (this.entity.isHandActive()) {
				if (!flag && this.seeTime < -60) {
					this.entity.resetActiveHand();
				} else if (flag) {
					int i = this.entity.getItemInUseMaxCount();

					if (i >= 20) {
						this.entity.resetActiveHand();
						this.entity.attackEntityWithRangedAttack(entitylivingbase, ItemBow.getArrowVelocity(i));
						this.attackTime = this.attackCooldown;
					}
				}
			} else if (--this.attackTime <= 0 && this.seeTime >= -60) {
				this.entity.setActiveHand(EnumHand.MAIN_HAND);
			}
		}
	}

}
