// `^`^`^`
// ```java
// /**
//  * This class, EntityAIBirdFight, extends the EntityAIMeleeFight and is designed to manage the melee combat behavior of a bird-like entity in a Minecraft mod. It controls how the entity engages in combat, retreats, and performs special attacks such as wing buffet attacks.
//  *
//  * Methods:
//  * - EntityAIBirdFight: Constructor initializing the bird entity, target class, attack delay, and retreat health loss percentage.
//  * - updateTask: Updates the attack task, setting whether the entity is attacking with wings based on proximity to the target.
//  * - resetTask: Resets the attack task and stops the entity from attacking with wings.
//  * - updatePath: Updates the navigation path to the target, adjusting movement type based on distance.
//  * - updateDisengage: Determines if the entity should disengage from melee combat based on certain conditions.
//  * - attackEntity: Performs a melee attack and potentially a wing buffet attack if the entity wants to retreat.
//  * - isInStartMeleeRange: Checks if the target is within a specific range to start a melee attack.
//  * - doWingBuffetAttack: Executes a wing buffet attack, applying knockback and a sound effect to the target.
//  *
//  * The class leverages the entity's AI to create a dynamic combat experience, allowing the bird entity to engage in melee combat, retreat when necessary, and use special attacks to create a more challenging and realistic behavior pattern.
//  */
// ```
// `^`^`^`

package invmod.entity.ai;

import invmod.entity.Goal;
import invmod.entity.INavigationFlying;
import invmod.entity.ai.navigator.Path;
import invmod.entity.monster.EntityIMBird;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.MathHelper;

public class EntityAIBirdFight<T extends EntityLivingBase> extends EntityAIMeleeFight<T> {
	private EntityIMBird theEntity;
	private boolean wantsToRetreat;
	private boolean buffetedTarget;

	public EntityAIBirdFight(EntityIMBird entity, Class<? extends T> targetClass, int attackDelay,
			float retreatHealthLossPercent) {
		super(entity, targetClass, attackDelay, retreatHealthLossPercent);
		this.theEntity = entity;
		this.wantsToRetreat = false;
		this.buffetedTarget = false;
	}

	@Override
	public void updateTask() {
		if (this.getAttackTime() == 0) {
			this.theEntity.setAttackingWithWings(this.isInStartMeleeRange());
		}
		super.updateTask();
	}

	@Override
	public void resetTask() {
		this.theEntity.setAttackingWithWings(false);
		super.resetTask();
	}

	@Override
	public void updatePath() {
		INavigationFlying nav = this.theEntity.getNavigatorNew();
		Entity target = this.theEntity.getAttackTarget();
		if (target != nav.getTargetEntity()) {
			nav.clearPath();
			nav.setMovementType(INavigationFlying.MoveType.PREFER_WALKING);
			Path path = nav.getPathToEntity(target, 0.0F);
			if ((path != null) && (path.getCurrentPathLength() > 1.6D * this.theEntity.getDistance(target))) {
				nav.setMovementType(INavigationFlying.MoveType.MIXED);
			}
			nav.autoPathToEntity(target);
		}
	}

	@Override
	protected void updateDisengage() {
		if (!this.wantsToRetreat) {
			if (this.shouldLeaveMelee())
				this.wantsToRetreat = true;
		} else if ((this.buffetedTarget) && (this.theEntity.getAIGoal() == Goal.MELEE_TARGET)) {
			this.theEntity.transitionAIGoal(Goal.LEAVE_MELEE);
		}
	}

	@Override
	protected void attackEntity(EntityLivingBase target) {
		this.theEntity.doMeleeSound();
		super.attackEntity(target);
		if (this.wantsToRetreat) {
			this.doWingBuffetAttack(target);
			this.buffetedTarget = true;
		}
	}

	protected boolean isInStartMeleeRange() {
		EntityLivingBase target = this.theEntity.getAttackTarget();
		if (target == null) {
			return false;
		}
		double d = this.theEntity.width + this.theEntity.getAttackRange() + 3.0D;
		return this.theEntity.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ) < d * d;
	}

	protected void doWingBuffetAttack(EntityLivingBase target) {
		int knockback = 2;
		target.addVelocity(-MathHelper.sin(this.theEntity.rotationYaw * 3.141593F / 180.0F) * knockback * 0.5F, 0.4D,
				MathHelper.cos(this.theEntity.rotationYaw * 3.141593F / 180.0F) * knockback * 0.5F);
		target.playSound(SoundEvents.ENTITY_GENERIC_BIG_FALL, 1f, 1f);
		// target.world.playSoundAtEntity(target, "damage.fallbig", 1.0F, 1.0F);
	}
}