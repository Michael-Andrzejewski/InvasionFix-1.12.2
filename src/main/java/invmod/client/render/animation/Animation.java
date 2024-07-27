// `^`^`^`
// ```java
// /**
//  * This class represents a generic animation system for animating various skeleton parts of an entity.
//  * It is designed to handle animations for any enum type, allowing for flexible and reusable animation code.
//  *
//  * Public Methods:
//  * - Animation: Constructor initializing the animation with the skeleton type, animation period, base speed, keyframes, and animation phases.
//  * - getAnimationPeriod: Returns the duration of the animation cycle.
//  * - getBaseSpeed: Returns the base speed of the animation.
//  * - getAnimationPhases: Provides an unmodifiable list of the animation phases associated with the animation.
//  * - getSkeletonType: Returns the class type of the skeleton that this animation is associated with.
//  * - getKeyFramesFor: Retrieves an unmodifiable list of keyframes for a specific part of the skeleton, if available.
//  *
//  * The animation system uses a map of keyframes for each part of the skeleton, allowing for complex animations that can be defined per skeleton part.
//  * The animation phases provide additional information about different stages of the animation, which can be used to control the flow and timing of the animation.
//  * The use of generics ensures that this animation system can be adapted to any type of skeleton structure defined by an enum, making it versatile for various animation needs.
//  */
// package invmod.client.render.animation;
// 
// // ... (rest of the imports and class code)
// ```
// `^`^`^`

package invmod.client.render.animation;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

public class Animation<T extends Enum<T>> {
	private float animationPeriod;
	private float baseSpeed;
	private Class<T> skeletonType;
	private EnumMap<T, List<KeyFrame>> allKeyFrames;
	private List<AnimationPhaseInfo> animationPhases;

	public Animation(Class<T> skeletonType, float animationPeriod, float baseTime,
			EnumMap<T, List<KeyFrame>> allKeyFrames, List<AnimationPhaseInfo> animationPhases) {
		this.animationPeriod = animationPeriod;
		this.baseSpeed = baseTime;
		this.skeletonType = skeletonType;
		this.allKeyFrames = allKeyFrames;
		this.animationPhases = animationPhases;
	}

	public float getAnimationPeriod() {
		return this.animationPeriod;
	}

	public float getBaseSpeed() {
		return this.baseSpeed;
	}

	public List<AnimationPhaseInfo> getAnimationPhases() {
		return Collections.unmodifiableList(this.animationPhases);
	}

	public Class<T> getSkeletonType() {
		return this.skeletonType;
	}

	public List<KeyFrame> getKeyFramesFor(T skeletonPart) {
		if (this.allKeyFrames.containsKey(skeletonPart)) {
			return Collections.unmodifiableList(this.allKeyFrames.get(skeletonPart));
		}
		return null;
	}
}