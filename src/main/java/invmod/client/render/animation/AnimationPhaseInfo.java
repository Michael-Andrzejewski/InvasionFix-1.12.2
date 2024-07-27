// `^`^`^`
// ```java
// /**
//  * This class represents the information for a specific phase of an animation within a rendering system.
//  * It is designed to handle the timing and transitions between different animation actions.
//  *
//  * Public Methods:
//  * - AnimationPhaseInfo(AnimationAction, float, float, Transition): Constructs an instance with specified action, time range, and default transition.
//  * - AnimationPhaseInfo(AnimationAction, float, float, Transition, Map<AnimationAction, Transition>): Constructs an instance with specified action, time range, default transition, and a map of transitions.
//  * - getAction(): Returns the animation action associated with this phase.
//  * - getTimeBegin(): Returns the starting time of the animation phase.
//  * - getTimeEnd(): Returns the ending time of the animation phase.
//  * - hasTransition(AnimationAction): Checks if there is a transition defined for a specific action.
//  * - getTransition(AnimationAction): Retrieves the transition associated with a given action.
//  * - getDefaultTransition(): Returns the default transition for the animation phase.
//  *
//  * The class uses a map to store transitions between different animation actions, allowing for flexible animation behavior.
//  */
// package invmod.client.render.animation;
// 
// // ... (rest of the code)
// ```
// `^`^`^`

package invmod.client.render.animation;

import java.util.HashMap;
import java.util.Map;

public class AnimationPhaseInfo {
	private AnimationAction action;
	private float timeBegin;
	private float timeEnd;
	private Map<AnimationAction, Transition> transitions;
	private Transition defaultTransition;

	public AnimationPhaseInfo(AnimationAction action, float timeBegin, float timeEnd, Transition defaultTransition) {
		this(action, timeBegin, timeEnd, defaultTransition, new HashMap(1));
		this.transitions.put(defaultTransition.getNewAction(), defaultTransition);
	}

	public AnimationPhaseInfo(AnimationAction action, float timeBegin, float timeEnd, Transition defaultTransition,
			Map<AnimationAction, Transition> transitions) {
		this.action = action;
		this.timeBegin = timeBegin;
		this.timeEnd = timeEnd;
		this.defaultTransition = defaultTransition;
		this.transitions = transitions;
	}

	public AnimationAction getAction() {
		return this.action;
	}

	public float getTimeBegin() {
		return this.timeBegin;
	}

	public float getTimeEnd() {
		return this.timeEnd;
	}

	public boolean hasTransition(AnimationAction newAction) {
		return this.transitions.containsKey(newAction);
	}

	public Transition getTransition(AnimationAction newAction) {
		return this.transitions.get(newAction);
	}

	public Transition getDefaultTransition() {
		return this.defaultTransition;
	}
}