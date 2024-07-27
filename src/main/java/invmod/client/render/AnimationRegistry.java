// `^`^`^`
// ```java
// /**
//  * AnimationRegistry is a singleton class responsible for managing and storing animations within the invmod client rendering system. It provides a centralized registry for animations, allowing for easy retrieval and management of animation data throughout the application.
//  *
//  * Methods:
//  * - AnimationRegistry(): A private constructor that initializes the animation registry with a default empty animation.
//  * - registerAnimation(String name, Animation animation): Registers a new animation with a unique name into the registry. If the name already exists, it logs a fatal error.
//  * - getAnimation(String name): Retrieves an animation by its name. If the animation does not exist, it logs a fatal error and returns a default empty animation.
//  * - instance(): Provides access to the singleton instance of the AnimationRegistry.
//  *
//  * The class uses a HashMap to store animations keyed by their names for quick access. It also defines an empty animation as a fallback to prevent null pointer exceptions when requesting non-existent animations. The class ensures that each animation has a unique name and provides error logging through ModLogger in case of duplicate names or missing animations.
//  */
// package invmod.client.render;
// 
// // ... (rest of the imports and class code)
// ```
// `^`^`^`

package invmod.client.render;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import invmod.client.render.animation.Animation;
import invmod.client.render.animation.AnimationAction;
import invmod.client.render.animation.AnimationPhaseInfo;
import invmod.client.render.animation.BonesWings;
import invmod.client.render.animation.Transition;
import invmod.util.ModLogger;

public class AnimationRegistry {
	private static final AnimationRegistry instance = new AnimationRegistry();
	private Map<String, Animation> animationMap;
	private Animation emptyAnim;

	private AnimationRegistry() {
		this.animationMap = new HashMap(4);
		EnumMap allKeyFramesWings = new EnumMap(BonesWings.class);
		List animationPhases = new ArrayList(1);
		animationPhases.add(new AnimationPhaseInfo(AnimationAction.STAND, 0.0F, 1.0F,
				new Transition(AnimationAction.STAND, 1.0F, 0.0F)));
		this.emptyAnim = new Animation(BonesWings.class, 1.0F, 1.0F, allKeyFramesWings, animationPhases);
	}

	public void registerAnimation(String name, Animation animation) {
		if (!this.animationMap.containsKey(name)) {
			this.animationMap.put(name, animation);
			return;
		}
		ModLogger.logFatal("Register animation: Name \"" + name + "\" already assigned");
	}

	public Animation getAnimation(String name) {
		if (this.animationMap.containsKey(name)) {
			return this.animationMap.get(name);
		}

		ModLogger.logFatal("Tried to use animation \"" + name + "\" but it doesn't exist");
		return this.emptyAnim;
	}

	public static AnimationRegistry instance() {
		return instance;
	}
}