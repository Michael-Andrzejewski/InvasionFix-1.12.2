// `^`^`^`
// ```java
// /**
//  * This class, LegController, is part of the invmod.client.render.animation.util package and is responsible for managing the leg animations of the EntityIMBird entity in a game or simulation. It uses the AnimationState object to control the transition and speed of animations based on the movement state of the entity.
// 
//  * Constructor:
//  * - LegController(EntityIMBird entity, AnimationState stateObject): Initializes the controller with the specified bird entity and animation state object, setting default values for the animation run, time attacking, flap effort, and flap effort samples.
// 
//  * Methods:
//  * - update(): Evaluates the current movement state of the bird entity (running, standing, or flying) and adjusts the animation accordingly. It calculates the speed of the entity and ensures the correct animation action is set, transitioning between actions like standing, running, and various attack phases.
//  * - ensureAnimation(AnimationState state, AnimationAction action, float animationSpeed, boolean pauseAfterAction): Ensures that the specified animation action is set with the given speed and pause behavior. If the action is already set, it updates the speed and pause state.
// 
//  * The class maintains internal state such as the time spent attacking and an array of flap effort samples to potentially influence the animation, although these are not directly manipulated in the provided code snippet. The update method is the core of the class, integrating entity movement data to provide a seamless animation experience.
//  */
// ```
// `^`^`^`

package invmod.client.render.animation.util;

import invmod.client.render.animation.AnimationAction;
import invmod.client.render.animation.AnimationState;
import invmod.entity.MoveState;
import invmod.entity.monster.EntityIMBird;

public class LegController {
	private EntityIMBird theEntity;
	private AnimationState animationRun;
	private int timeAttacking;
	private float flapEffort;
	private float[] flapEffortSamples;
	private int sampleIndex;

	public LegController(EntityIMBird entity, AnimationState stateObject) {
		this.theEntity = entity;
		this.animationRun = stateObject;
		this.timeAttacking = 0;
		this.flapEffort = 1.0F;
		this.flapEffortSamples = new float[] { 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F };
		this.sampleIndex = 0;
	}

	public void update() {
		AnimationAction currAnimation = this.animationRun.getCurrentAction();
		if (this.theEntity.getMoveState() == MoveState.RUNNING) {
			double dX = this.theEntity.posX - this.theEntity.lastTickPosX;
			double dZ = this.theEntity.posZ - this.theEntity.lastTickPosZ;
			double dist = Math.sqrt(dX * dX + dZ * dZ);
			float speed = 0.2F + (float) dist * 1.3F;

			if (this.animationRun.getNextSetAction() != AnimationAction.RUN) {
				if (dist >= 1.E-005D) {
					if (currAnimation == AnimationAction.STAND) {
						this.ensureAnimation(this.animationRun, AnimationAction.STAND_TO_RUN, speed, false);
					} else if (currAnimation == AnimationAction.STAND_TO_RUN) {
						this.ensureAnimation(this.animationRun, AnimationAction.RUN, speed, false);
					} else {
						this.ensureAnimation(this.animationRun, AnimationAction.STAND, 1.0F, true);
					}
				}
			} else {
				this.animationRun.setAnimationSpeed(speed);
				if (dist < 1.E-005D) {
					this.ensureAnimation(this.animationRun, AnimationAction.STAND, 0.2F, true);
				}
			}
		} else if (this.theEntity.getMoveState() == MoveState.STANDING) {
			this.ensureAnimation(this.animationRun, AnimationAction.STAND, 1.0F, true);
		} else if (this.theEntity.getMoveState() == MoveState.FLYING) {
			if (this.theEntity.getClawsForward()) {
				if (currAnimation == AnimationAction.STAND) {
					this.ensureAnimation(this.animationRun, AnimationAction.LEGS_CLAW_ATTACK_P1, 1.5F, true);
				} else if (this.animationRun.getNextSetAction() != AnimationAction.LEGS_CLAW_ATTACK_P1) {
					this.ensureAnimation(this.animationRun, AnimationAction.STAND, 1.5F, true);
				}
			} else if (((this.theEntity.getFlyState() == FlyState.FLYING)
					|| (this.theEntity.getFlyState() == FlyState.LANDING))
					&& (currAnimation != AnimationAction.LEGS_RETRACT)) {
				if (currAnimation == AnimationAction.STAND) {
					this.ensureAnimation(this.animationRun, AnimationAction.LEGS_RETRACT, 1.0F, true);
				} else if (currAnimation == AnimationAction.LEGS_CLAW_ATTACK_P1) {
					this.ensureAnimation(this.animationRun, AnimationAction.LEGS_CLAW_ATTACK_P2, 1.0F, true);
				} else {
					this.ensureAnimation(this.animationRun, AnimationAction.STAND, 1.0F, true);
				}
			}
		}

		this.animationRun.update();
	}

	private void ensureAnimation(AnimationState state, AnimationAction action, float animationSpeed,
			boolean pauseAfterAction) {
		if (state.getNextSetAction() != action) {
			state.setNewAction(action, animationSpeed, pauseAfterAction);
		} else {
			state.setAnimationSpeed(animationSpeed);
			state.setPauseAfterSetAction(pauseAfterAction);
			state.setPaused(false);
		}
	}
}