// `^`^`^`
// ```java
// /**
//  * This class represents a transition within an animation system. It is designed to manage the change from one animation action to another, capturing the details of the transition such as the source time, destination time, and the new action to transition to.
//  *
//  * Class: Transition
//  * Package: invmod.client.render.animation
//  *
//  * Public Methods:
//  * - Transition(AnimationAction newAction, float sourceTime, float destTime): Constructs a Transition object with the specified new action to transition to, the source time from which the transition starts, and the destination time at which the transition ends.
//  * - getNewAction(): Returns the AnimationAction that this transition is moving to.
//  * - getSourceTime(): Returns the source time as a float, indicating the start time of the transition within the current animation.
//  * - getDestTime(): Returns the destination time as a float, indicating the end time of the transition within the target animation.
//  *
//  * The Transition class is a fundamental part of the animation system, allowing for smooth changes between different animation states. It encapsulates the necessary information to interpolate between two points in an animation sequence, ensuring a seamless visual experience.
//  */
// ```
// `^`^`^`

package invmod.client.render.animation;

public class Transition {
	private AnimationAction newAction;
	private float sourceTime;
	private float destTime;

	public Transition(AnimationAction newAction, float sourceTime, float destTime) {
		this.newAction = newAction;
		this.sourceTime = sourceTime;
		this.destTime = destTime;
	}

	public AnimationAction getNewAction() {
		return this.newAction;
	}

	public float getSourceTime() {
		return this.sourceTime;
	}

	public float getDestTime() {
		return this.destTime;
	}
}