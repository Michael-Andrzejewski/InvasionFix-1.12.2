// `^`^`^`
// ```java
// /**
//  * This class, ModelAnimator, is designed to animate model parts in a Minecraft mod using keyframe-based animation.
//  * It allows for the interpolation of model part rotations and positions over time to create smooth animations.
//  *
//  * Public Methods:
//  * - ModelAnimator(): Constructs an animator with a default animation period.
//  * - ModelAnimator(float animationPeriod): Constructs an animator with a specified animation period.
//  * - ModelAnimator(Map<T, ModelRenderer> modelParts, Animation<T> animation): Constructs an animator using a map of model parts and a predefined animation.
//  * - addPart(ModelRenderer part, List<KeyFrame> keyFrames): Adds a new part to be animated with its corresponding keyframes.
//  * - clearParts(): Clears all parts from the animator, effectively resetting it.
//  * - updateAnimation(float newTime): Updates the animation of all parts to the new time, interpolating between keyframes as necessary.
//  *
//  * Private Methods:
//  * - interpolate(KeyFrame prevFrame, KeyFrame nextFrame, float time, ModelRenderer part): Interpolates between two keyframes for a model part at a given time.
//  * - validate(List<KeyFrame> keyFrames): Validates a list of keyframes to ensure they are in the correct order and have the correct timing for animation.
//  *
//  * The class relies on a list of triplets, each containing a ModelRenderer, an integer index, and a list of KeyFrames. The ModelRenderer represents a part of the model to be animated, the integer index tracks the current keyframe, and the list of KeyFrames defines the animation sequence for that part.
//  *
//  * The updateAnimation method is the core of the class, where it calculates the new state of each part based on the current time and the keyframes. The interpolate method is used to perform the actual interpolation between keyframes.
//  */
// ```
// `^`^`^`

package invmod.client.render.animation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import invmod.util.Triplet;
import net.minecraft.client.model.ModelRenderer;

public class ModelAnimator<T extends Enum<T>> {
	private List<Triplet<ModelRenderer, Integer, List<KeyFrame>>> parts;
	private float animationPeriod;

	public ModelAnimator() {
		this(1.0F);
	}

	public ModelAnimator(float animationPeriod) {
		this.animationPeriod = animationPeriod;
		this.parts = new ArrayList(1);
	}

	public ModelAnimator(Map<T, ModelRenderer> modelParts, Animation<T> animation) {
		this.animationPeriod = animation.getAnimationPeriod();
		this.parts = new ArrayList(((Enum[]) animation.getSkeletonType().getEnumConstants()).length);
		for (Map.Entry entry : modelParts.entrySet()) {
			List keyFrames = animation.getKeyFramesFor((T) entry.getKey());
			if (keyFrames != null) {
				this.parts.add(new Triplet(entry.getValue(), Integer.valueOf(0), keyFrames));
			}
		}
	}

	public void addPart(ModelRenderer part, List<KeyFrame> keyFrames) {
		if (this.validate(keyFrames)) {
			this.parts.add(new Triplet(part, Integer.valueOf(0), keyFrames));
		}
	}

	public void clearParts() {
		this.parts.clear();
	}

	public void updateAnimation(float newTime) {
		for (Triplet entry : this.parts) {
			int prevIndex = ((Integer) entry.getVal2()).intValue();
			List keyFrames = (List) entry.getVal3();
			KeyFrame prevFrame = (KeyFrame) keyFrames.get(prevIndex++);
			KeyFrame nextFrame = null;

			if (prevFrame.getTime() <= newTime) {
				for (; prevIndex < keyFrames.size(); prevIndex++) {
					KeyFrame keyFrame = (KeyFrame) keyFrames.get(prevIndex);
					if (newTime < keyFrame.getTime()) {
						nextFrame = keyFrame;
						prevIndex--;
						break;
					}

					prevFrame = keyFrame;
				}

				if (prevIndex >= keyFrames.size()) {
					prevIndex = keyFrames.size() - 1;
					nextFrame = (KeyFrame) keyFrames.get(0);
				}
			} else {
				for (prevIndex = 0; prevIndex < keyFrames.size(); prevIndex++) {
					KeyFrame keyFrame = (KeyFrame) keyFrames.get(prevIndex);
					if (newTime < keyFrame.getTime()) {
						nextFrame = keyFrame;
						prevIndex--;
						prevFrame = (KeyFrame) keyFrames.get(prevIndex);
						break;
					}
				}
			}
			entry.setVal2(Integer.valueOf(prevIndex));
			this.interpolate(prevFrame, nextFrame, newTime, (ModelRenderer) entry.getVal1());
		}
	}

	private void interpolate(KeyFrame prevFrame, KeyFrame nextFrame, float time, ModelRenderer part) {
		if (prevFrame.getInterpType() == InterpType.LINEAR) {
			float dtPrev = time - prevFrame.getTime();
			float dtFrame = nextFrame.getTime() - prevFrame.getTime();
			if (dtFrame < 0.0F) {
				dtFrame += this.animationPeriod;
			}

			float r = dtPrev / dtFrame;
			part.rotateAngleX = (prevFrame.getRotX() + r * (nextFrame.getRotX() - prevFrame.getRotX()));
			part.rotateAngleY = (prevFrame.getRotY() + r * (nextFrame.getRotY() - prevFrame.getRotY()));
			part.rotateAngleZ = (prevFrame.getRotZ() + r * (nextFrame.getRotZ() - prevFrame.getRotZ()));

			if (prevFrame.hasPos()) {
				if (nextFrame.hasPos()) {
					part.rotationPointX = (prevFrame.getPosX() + r * (nextFrame.getPosX() - prevFrame.getPosX()));
					part.rotationPointY = (prevFrame.getPosY() + r * (nextFrame.getPosY() - prevFrame.getPosY()));
					part.rotationPointZ = (prevFrame.getPosZ() + r * (nextFrame.getPosZ() - prevFrame.getPosZ()));
				} else {
					part.rotationPointX = prevFrame.getPosX();
					part.rotationPointY = prevFrame.getPosY();
					part.rotationPointZ = prevFrame.getPosZ();
				}
			}
		}
	}

	private boolean validate(List<KeyFrame> keyFrames) {
		if (keyFrames.size() < 2) {
			return false;
		}
		if (keyFrames.get(0).getTime() != 0.0F) {
			return false;
		}
		int prevTime = 0;
		for (int i = 1; i < keyFrames.size(); i++) {
			if (keyFrames.get(i).getTime() <= prevTime) {
				return false;
			}
		}
		return true;
	}
}