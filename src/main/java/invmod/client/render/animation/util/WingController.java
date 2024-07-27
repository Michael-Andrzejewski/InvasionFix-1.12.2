// `^`^`^`
// ```java
// /**
//  * This class, WingController, is designed to manage the wing animations of the EntityIMBird entity within the invmod game mod. It controls the wing flapping behavior based on the bird's movement and attack states.
//  *
//  * Public Methods:
//  * - WingController(EntityIMBird entity, AnimationState stateObject): Constructor that initializes the WingController with a specific bird entity and its animation state.
//  * - update(): This method updates the wing animation state based on the bird's current actions, such as attacking, flying, or gliding. It adjusts the flap effort dynamically and ensures the correct animation is played for the bird's current state.
//  *
//  * Private Methods:
//  * - ensureAnimation(AnimationState state, AnimationAction action, float animationSpeed, boolean pauseAfterAction): Ensures that the specified animation action is set with the correct speed and pause behavior. If the action is already set, it updates the speed and pause settings.
//  *
//  * The WingController class uses a sampling system to average the flap effort over time, providing a more natural wing movement. It also handles transitions between different wing animations, such as tucking, spreading, flapping, and gliding, based on the bird's movement state (grounded, running, flying) and whether it is attacking with its wings.
//  */
// ```
// `^`^`^`

package invmod.client.render.animation.util;

import invmod.client.render.animation.AnimationAction;
import invmod.client.render.animation.AnimationState;
import invmod.entity.MoveState;
import invmod.entity.monster.EntityIMBird;

public class WingController {
	private EntityIMBird theEntity;
	private AnimationState animationFlap;
	private int timeAttacking;
	private float flapEffort;
	private float[] flapEffortSamples;
	private int sampleIndex;

	public WingController(EntityIMBird entity, AnimationState stateObject) {
		this.theEntity = entity;
		this.animationFlap = stateObject;
		this.timeAttacking = 0;
		this.flapEffort = 1.0F;
		this.flapEffortSamples = new float[] { 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F };
		this.sampleIndex = 0;
	}

	public void update() {
		AnimationAction currAnimation = this.animationFlap.getCurrentAction();
		AnimationAction nextAnimation = this.animationFlap.getNextSetAction();
		boolean wingAttack = this.theEntity.isAttackingWithWings();
		if (!wingAttack)
			this.timeAttacking = 0;
		else {
			this.timeAttacking += 1;
		}
		if (this.theEntity.ticksExisted % 5 == 0) {
			if (++this.sampleIndex >= this.flapEffortSamples.length) {
				this.sampleIndex = 0;
			}
			float sample = this.theEntity.getThrustEffort();
			this.flapEffort -= this.flapEffortSamples[this.sampleIndex] / this.flapEffortSamples.length;
			this.flapEffort += sample / this.flapEffortSamples.length;
			this.flapEffortSamples[this.sampleIndex] = sample;
		}

		if (this.theEntity.getFlyState() != FlyState.GROUNDED) {
			if (currAnimation == AnimationAction.WINGTUCK) {
				this.ensureAnimation(this.animationFlap, AnimationAction.WINGSPREAD, 2.2F, true);
			} else if (this.theEntity.isThrustOn()) {
				this.ensureAnimation(this.animationFlap, AnimationAction.WINGFLAP, 2.0F * this.flapEffort, false);
			} else {
				this.ensureAnimation(this.animationFlap, AnimationAction.WINGGLIDE, 0.7F, false);
			}

		} else {
			boolean wingsActive = false;
			if (this.theEntity.getMoveState() == MoveState.RUNNING) {
				if (currAnimation == AnimationAction.WINGTUCK) {
					this.ensureAnimation(this.animationFlap, AnimationAction.WINGSPREAD, 2.2F, true);
				} else {
					this.ensureAnimation(this.animationFlap, AnimationAction.WINGFLAP, 1.0F, false);
					if ((!wingAttack) && (currAnimation == AnimationAction.WINGSPREAD)
							&& (this.animationFlap.getCurrentAnimationPercent() >= 0.65F)) {
						this.animationFlap.setPaused(true);
					}
				}
				wingsActive = true;
			}

			if (wingAttack) {
				float speed = (float) (1.0D / Math.min(this.timeAttacking / 40 * 0.6D + 0.4D, 1.0D));
				this.ensureAnimation(this.animationFlap, AnimationAction.WINGFLAP, speed, false);
				wingsActive = true;
			}

			if (!wingsActive) {
				this.ensureAnimation(this.animationFlap, AnimationAction.WINGTUCK, 1.8F, true);
			}
		}
		this.animationFlap.update();
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