// `^`^`^`
// ```java
// /**
//  * This class, MouthController, is designed to manage the mouth animations of an entity within a game or simulation environment. It is part of the invmod.client.render.animation.util package and is used to control the opening and closing of an entity's mouth, specifically for the EntityIMBird class, which is a type of EntityIMLiving.
// 
//  * The MouthController class contains the following methods:
// 
//  * MouthController(EntityIMBird entity, AnimationState stateObject): Constructor that initializes the controller with a specific bird entity and its corresponding animation state.
// 
//  * update(): This method is called each update cycle and is responsible for decrementing the mouth open time if the mouth is open, ensuring the correct mouth animation is played (open or close), and updating the animation state.
// 
//  * setMouthState(int timeOpen): Sets the duration for which the mouth should remain open.
// 
//  * ensureAnimation(AnimationState state, AnimationAction action, float animationSpeed, boolean pauseAfterAction): A private helper method that ensures the correct animation action is set with the specified speed and whether the animation should pause after completing the action.
// 
//  * The class relies on an AnimationState object to track and update the current state of the mouth animation and uses an integer to track the remaining time the mouth should stay open. The update method is the core of the class, managing the transition between mouth open and mouth close animations based on the mouthOpenTime value.
//  */
// ```
// `^`^`^`

package invmod.client.render.animation.util;

import invmod.client.render.animation.AnimationAction;
import invmod.client.render.animation.AnimationState;
import invmod.entity.EntityIMLiving;
import invmod.entity.monster.EntityIMBird;

public class MouthController {
	private EntityIMLiving theEntity;
	private AnimationState mouthState;
	private int mouthOpenTime;

	public MouthController(EntityIMBird entity, AnimationState stateObject) {
		this.theEntity = entity;
		this.mouthState = stateObject;
		this.mouthOpenTime = 0;
	}

	public void update() {
		if (this.mouthOpenTime > 0) {
			this.mouthOpenTime -= 1;
			this.ensureAnimation(this.mouthState, AnimationAction.MOUTH_OPEN, 1.0F, true);
		} else {
			this.ensureAnimation(this.mouthState, AnimationAction.MOUTH_CLOSE, 1.0F, true);
		}
		this.mouthState.update();
	}

	public void setMouthState(int timeOpen) {
		this.mouthOpenTime = timeOpen;
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