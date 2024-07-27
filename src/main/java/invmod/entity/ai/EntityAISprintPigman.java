// `^`^`^`
// ```java
// /**
//  * This class defines the AI behavior for a sprinting attack used by EntityIMZombiePigman entities in the game Minecraft.
//  * It extends the EntityAIBase class from Minecraft's AI system and manages the sprinting attack cycle, including wind-up,
//  * sprinting, and crashing if the attack fails.
//  *
//  * Methods:
//  * - EntityAISprintPigman(EntityIMMob entity): Constructor that initializes the AI with the given entity.
//  * - shouldExecute(): Determines if the AI should start executing, based on visibility of the target or if it's already sprinting.
//  * - startExecuting(): Prepares the AI to start executing the sprint attack.
//  * - updateTask(): Updates the task each tick, managing the sprinting behavior and transitions between states.
//  * - startSprint(): Initiates the sprinting wind-up phase if the target is in a suitable position.
//  * - sprint(): Transitions the entity into the sprinting state, increasing its speed and setting it to sprint.
//  * - endSprint(): Ends the sprinting attack, resetting the entity's speed and sprinting state.
//  * - crash(): Called when the entity crashes during a sprint, dealing damage to itself and playing a sound effect.
//  *
//  * The AI uses a timer to manage the sprinting attack's duration and cooldown, and tracks the last known position of the target
//  * to determine if the entity has stopped moving. It also adjusts the entity's turn rate and move speed during the sprint.
//  */
// ```
// `^`^`^`

package invmod.entity.ai;

import invmod.entity.monster.EntityIMMob;
import invmod.entity.monster.EntityIMZombiePigman;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;

public class EntityAISprintPigman extends EntityAIBase {

	private EntityIMMob theEntity;
	private int updateTimer;
	private int timer;
	private boolean isExecuting;
	private boolean isSprinting;
	private boolean isInWindup;
	private int missingTarget;
	private double lastX;
	private double lastY;
	private double lastZ;

	public EntityAISprintPigman(EntityIMMob entity) {
		this.theEntity = entity;
		this.updateTimer = 0;
		this.timer = 0;
		this.isExecuting = true;
		this.isSprinting = false;
		this.isInWindup = false;
		this.missingTarget = 0;
	}

	@Override
	public boolean shouldExecute() {
		if (--this.updateTimer <= 0) {
			this.updateTimer = 20;
			if (((this.theEntity.getAttackTarget() != null)
					&& (this.theEntity.canEntityBeSeen(this.theEntity.getAttackTarget()))) || (this.isSprinting)) {
				return true;
			}

			this.isExecuting = false;
			return false;
		}

		return this.isExecuting;
	}

	@Override
	public void startExecuting() {
		this.isExecuting = true;
		this.timer = 60;
	}

	@Override
	public void updateTask() {
		if (this.isSprinting) {
			Entity target = this.theEntity.getAttackTarget();
			if ((!this.theEntity.isSprinting()) || (target == null)
					|| ((this.missingTarget > 0) && (++this.missingTarget > 20))) {
				this.endSprint();
				return;
			}

			double dX = target.posX - this.theEntity.posX;
			double dZ = target.posZ - this.theEntity.posZ;
			double dAngle = (Math.atan2(dZ, dX) * 180.0D / Math.PI - 90.0D - this.theEntity.rotationYaw) % 360.0D;
			if (dAngle > 60.0D) {
				this.theEntity.setTurnRate(2.0F);
				this.missingTarget = 1;
			}

			if (this.theEntity.getDistanceSq(this.lastX, this.lastY, this.lastZ) < 0.0009D) {
				this.crash();
				return;
			}

			this.lastX = this.theEntity.posX;
			this.lastY = this.theEntity.posY;
			this.lastZ = this.theEntity.posZ;
		}

		if (--this.timer <= 0) {
			if (!this.isInWindup) {
				if (!this.isSprinting) {
					this.startSprint();
				} else {
					this.endSprint();
				}
			} else {
				this.sprint();
			}
		}
	}

	protected void startSprint() {
		EntityIMZombiePigman pigman = (EntityIMZombiePigman) this.theEntity;
		pigman.updateAnimation(true);
		Entity target = this.theEntity.getAttackTarget();
		if ((target == null) || (target.getEntityBoundingBox().minY - this.theEntity.posY >= 1.0D)) {
			return;
		}
		double dX = target.posX - this.theEntity.posX;
		double dZ = target.posZ - this.theEntity.posZ;
		double dAngle = (Math.atan2(dZ, dX) * 180.0D / 3.141592653589793D - 90.0D - this.theEntity.rotationYaw)
				% 360.0D;
		if (dAngle < 10.0D) {
			this.isInWindup = true;
			this.timer = 20;

			this.theEntity.setMoveSpeedStat(0.0F);
		} else {
			this.timer = 10;
		}
	}

	protected void sprint() {
		this.isInWindup = false;
		this.isSprinting = true;
		this.missingTarget = 0;
		this.timer = 35;

		this.theEntity.resetMoveSpeed();
		this.theEntity.setMoveSpeedStat(this.theEntity.getMoveSpeedStat() * 2.3F);
		this.theEntity.setSprinting(true);
		this.theEntity.setTurnRate(4.9F);
		// this.theEntity.attackTime = 0;
	}

	protected void endSprint() {
		EntityIMZombiePigman pigman = (EntityIMZombiePigman) this.theEntity;
		pigman.updateAnimation(true);
		this.isSprinting = false;
		this.timer = 180;
		this.theEntity.resetMoveSpeed();
		this.theEntity.setTurnRate(30.0F);
		this.theEntity.setSprinting(false);
	}

	protected void crash() {
		this.theEntity.stunEntity(40);
		this.theEntity.attackEntityFrom(DamageSource.GENERIC, 5.0F);
		// this.theEntity.world.playSoundAtEntity(this.theEntity, "random.explode",
		// 1.0F, 0.6F);
		this.theEntity.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1f, 0.6f);
		this.endSprint();
	}
}