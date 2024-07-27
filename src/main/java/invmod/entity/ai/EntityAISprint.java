// `^`^`^`
// ```java
// /**
//  * This class represents an AI behavior for sprinting entities in a Minecraft mod.
//  * It is designed to be used with entities that extend the EntityIMMob class.
//  *
//  * Methods:
//  * - EntityAISprint(EntityIMMob entity): Constructor that initializes the AI with the given entity.
//  * - shouldExecute(): Determines if the AI should start executing, based on visibility of the target or if already sprinting.
//  * - startExecuting(): Sets up the AI for execution, initializing timers and flags.
//  * - updateTask(): Updates the AI behavior each tick, managing sprinting logic and transitions between states.
//  * - startSprint(): Prepares the entity to start sprinting towards its target if conditions are met.
//  * - sprint(): Activates the sprint, increasing the entity's speed and setting sprinting state.
//  * - endSprint(): Deactivates the sprint, resetting the entity's speed and turning rate to normal.
//  * - crash(): Handles the entity's behavior when it crashes, applying stun and damage, and ending the sprint.
//  *
//  * The AI manages a sprinting state, including wind-up before sprinting, maintaining the sprint, and handling what happens
//  * if the entity loses its target or crashes. It uses timers to manage state transitions and checks angles and distances
//  * to determine if the entity should continue sprinting or not.
//  */
// package invmod.entity.ai;
// 
// // ... (rest of the code)
// ```
// `^`^`^`

package invmod.entity.ai;

import invmod.entity.monster.EntityIMMob;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;

public class EntityAISprint extends EntityAIBase {

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

	public EntityAISprint(EntityIMMob entity) {
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
			double dAngle = (Math.atan2(dZ, dX) * 180.0D / 3.141592653589793D - 90.0D - this.theEntity.rotationYaw)
					% 360.0D;
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