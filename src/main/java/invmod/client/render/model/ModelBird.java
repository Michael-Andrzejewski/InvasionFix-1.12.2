// `^`^`^`
// ```java
// /**
//  * ModelBird is a custom 3D model class for rendering a bird entity in Minecraft.
//  * It extends ModelBase, a standard class for creating entity models in Minecraft.
//  * The model includes various body parts such as body, wings, head, beak, tail, and legs,
//  * each represented by a ModelRenderer object. These parts are initialized with their
//  * respective textures and geometries in the constructor.
//  *
//  * The constructor also initializes an animation for wing flapping using the ModelAnimator class,
//  * which is a custom animation handler. It defines keyframes for the inner and outer parts of the wings
//  * to simulate a bird's flight, and these animations are mirrored for the left side.
//  *
//  * The render method overrides the ModelBase render method to draw the bird model with the
//  * provided entity and animation parameters.
//  *
//  * The setFlyingAnimations method is used to update the wing flapping and leg movement animations
//  * based on the current progress of each animation. This allows the bird's wings and legs to move
//  * in a realistic manner when it is flying.
//  *
//  * Note: The code snippet provided is incomplete and does not include the full implementation of the
//  * setFlyingAnimations method or other potential methods and functionalities of the ModelBird class.
//  */
// ```
// ```java
// /**
//  * This code is part of a model class for animating an entity in a Minecraft-like environment. It defines the rotation
//  * angles for various parts of the entity's body to simulate movement and posture. The code snippet includes methods
//  * for setting the rotation angles of body parts during animation cycles.
//  *
//  * - The first section sets the initial rotation angles for the body and limbs based on the roll and legSweepProgress
//  *   variables, which likely represent the entity's current movement state.
//  * - The body's Y rotation point is animated to simulate a flapping or bobbing motion using the flapProgress variable.
//  * - Additional adjustments to the thigh and tail rotations are made based on the flapProgress, adding to the realism
//  *   of the movement.
//  * - The setRotation method is a utility function that directly sets the rotation angles for a given ModelRenderer
//  *   instance.
//  * - The setRotationAngles method overrides a superclass method to define custom behavior for the entity's body rotation
//  *   based on the entity's pitch, which is the up/down angle of the entity's movement or look direction.
//  *
//  * Overall, this code is responsible for the procedural animation of an entity's body parts, making the entity's
//  * movements appear more lifelike and dynamic in the game world.
//  */
// ```
// `^`^`^`

package invmod.client.render.model;

import java.util.ArrayList;
import java.util.List;

import invmod.client.render.animation.InterpType;
import invmod.client.render.animation.KeyFrame;
import invmod.client.render.animation.ModelAnimator;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelBird extends ModelBase {
	private ModelAnimator animationWingFlap;
	private ModelRenderer body;
	private ModelRenderer rightwing1;
	private ModelRenderer leftwing1;
	private ModelRenderer head;
	private ModelRenderer beak;
	private ModelRenderer leftwing2;
	private ModelRenderer rightwing2;
	private ModelRenderer tail;
	private ModelRenderer legR;
	private ModelRenderer ltoeR;
	private ModelRenderer btoeR;
	private ModelRenderer rtoeR;
	private ModelRenderer thighR;
	private ModelRenderer legL;
	private ModelRenderer ltoeL;
	private ModelRenderer btoeL;
	private ModelRenderer rtoeL;
	private ModelRenderer thighL;

	public ModelBird() {
		this.textureWidth = 64;
		this.textureHeight = 32;

		this.body = new ModelRenderer(this, 24, 0);
		this.body.addBox(-3.5F, 0.0F, -3.5F, 7, 12, 7);
		this.body.setRotationPoint(3.5F, 7.0F, 3.5F);
		this.body.setTextureSize(64, 32);
		this.body.mirror = true;
		this.setRotation(this.body, 0.0F, 0.0F, 0.0F);
		this.rightwing1 = new ModelRenderer(this, 0, 22);
		this.rightwing1.addBox(-7.0F, -1.0F, -1.0F, 7, 9, 1);
		this.rightwing1.setRotationPoint(-3.5F, 2.0F, 3.5F);
		this.rightwing1.setTextureSize(64, 32);
		this.rightwing1.mirror = false;
		this.setRotation(this.rightwing1, 0.0F, 0.0F, 0.0F);
		this.rightwing2 = new ModelRenderer(this, 16, 24);
		this.rightwing2.addBox(-14.0F, -1.0F, -0.5F, 14, 7, 1);
		this.rightwing2.setRotationPoint(-7.0F, 0.0F, -0.5F);
		this.rightwing2.setTextureSize(64, 32);
		this.rightwing2.mirror = false;
		this.setRotation(this.rightwing2, 0.0F, 0.0F, 0.0F);
		this.rightwing1.addChild(this.rightwing2);
		this.leftwing1 = new ModelRenderer(this, 0, 22);
		this.leftwing1.addBox(0.0F, -1.0F, -1.0F, 7, 9, 1);
		this.leftwing1.setRotationPoint(3.5F, 2.0F, 3.5F);
		this.leftwing1.setTextureSize(64, 32);
		this.leftwing1.mirror = true;
		this.setRotation(this.leftwing1, 0.0F, 0.0F, 0.0F);
		this.leftwing2 = new ModelRenderer(this, 16, 24);
		this.leftwing2.addBox(0.0F, -1.0F, -0.5F, 14, 7, 1);
		this.leftwing2.setRotationPoint(7.0F, 0.0F, -0.5F);
		this.leftwing2.setTextureSize(64, 32);
		this.leftwing2.mirror = true;
		this.setRotation(this.leftwing2, 0.0F, 0.0F, 0.0F);
		this.leftwing1.addChild(this.leftwing2);
		this.head = new ModelRenderer(this, 2, 0);
		this.head.addBox(-2.5F, -5.0F, -4.0F, 5, 6, 6);
		this.head.setRotationPoint(0.0F, 0.5F, 1.5F);
		this.head.setTextureSize(64, 32);
		this.head.mirror = true;
		this.setRotation(this.head, 0.0F, 0.0F, 0.0F);
		this.beak = new ModelRenderer(this, 19, 0);
		this.beak.addBox(-0.5F, 0.0F, -2.0F, 1, 2, 2);
		this.beak.setRotationPoint(0.0F, -3.0F, -4.0F);
		this.beak.setTextureSize(64, 32);
		this.beak.mirror = true;
		this.setRotation(this.beak, 0.0F, 0.0F, 0.0F);
		this.head.addChild(this.beak);
		this.tail = new ModelRenderer(this, 0, 12);
		this.tail.addBox(-3.0F, 0.0F, 0.0F, 5, 9, 1);
		this.tail.setRotationPoint(0.5F, 12.0F, 2.5F);
		this.tail.setTextureSize(64, 32);
		this.tail.mirror = true;
		this.setRotation(this.tail, 0.446143F, 0.0F, 0.0F);
		this.legR = new ModelRenderer(this, 13, 12);
		this.legR.addBox(-0.5F, 0.0F, -0.5F, 1, 5, 1);
		this.legR.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.legR.setTextureSize(64, 32);
		this.legR.mirror = false;
		this.setRotation(this.legR, 0.0F, 0.0F, 0.0F);
		this.ltoeR = new ModelRenderer(this, 0, 0);
		this.ltoeR.addBox(0.0F, 0.0F, -2.0F, 1, 1, 2);
		this.ltoeR.setRotationPoint(0.2F, 4.0F, 0.0F);
		this.ltoeR.setTextureSize(64, 32);
		this.ltoeR.mirror = false;
		this.setRotation(this.ltoeR, 0.0F, -0.1396263F, 0.0F);
		this.legR.addChild(this.ltoeR);
		this.btoeR = new ModelRenderer(this, 0, 0);
		this.btoeR.addBox(-0.5F, 0.0F, 0.0F, 1, 1, 2);
		this.btoeR.setRotationPoint(0.0F, 4.0F, 0.0F);
		this.btoeR.setTextureSize(64, 32);
		this.btoeR.mirror = false;
		this.setRotation(this.btoeR, -0.349066F, 0.0F, 0.0F);
		this.legR.addChild(this.btoeR);
		this.rtoeR = new ModelRenderer(this, 0, 0);
		this.rtoeR.addBox(-1.0F, 0.0F, -2.0F, 1, 1, 2);
		this.rtoeR.setRotationPoint(-0.2F, 4.0F, 0.0F);
		this.rtoeR.setTextureSize(64, 32);
		this.rtoeR.mirror = false;
		this.setRotation(this.rtoeR, 0.0F, 0.1396263F, 0.0F);
		this.legR.addChild(this.rtoeR);
		this.thighR = new ModelRenderer(this, 13, 18);
		this.thighR.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2);
		this.thighR.setRotationPoint(-1.5F, 12.0F, -1.0F);
		this.thighR.setTextureSize(64, 32);
		this.thighR.mirror = false;
		this.setRotation(this.thighR, 0.0F, 0.0F, 0.0F);
		this.thighR.addChild(this.legR);
		this.legL = new ModelRenderer(this, 13, 12);
		this.legL.addBox(-0.5F, 0.0F, -0.5F, 1, 5, 1);
		this.legL.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.legL.setTextureSize(64, 32);
		this.legL.mirror = true;
		this.setRotation(this.legL, 0.0F, 0.0F, 0.0F);
		this.ltoeL = new ModelRenderer(this, 0, 0);
		this.ltoeL.addBox(0.0F, 0.0F, -2.0F, 1, 1, 2);
		this.ltoeL.setRotationPoint(0.2F, 4.0F, 0.0F);
		this.ltoeL.setTextureSize(64, 32);
		this.ltoeL.mirror = true;
		this.setRotation(this.ltoeL, 0.0F, -0.1396263F, 0.0F);
		this.legL.addChild(this.ltoeL);
		this.btoeL = new ModelRenderer(this, 0, 0);
		this.btoeL.addBox(-0.5F, 0.0F, 0.0F, 1, 1, 2);
		this.btoeL.setRotationPoint(0.0F, 4.0F, 0.0F);
		this.btoeL.setTextureSize(64, 32);
		this.btoeL.mirror = true;
		this.setRotation(this.btoeL, -0.349066F, 0.0F, 0.0F);
		this.legL.addChild(this.btoeL);
		this.rtoeL = new ModelRenderer(this, 0, 0);
		this.rtoeL.addBox(-1.0F, 0.0F, -2.0F, 1, 1, 2);
		this.rtoeL.setRotationPoint(-0.2F, 4.0F, 0.0F);
		this.rtoeL.setTextureSize(64, 32);
		this.rtoeL.mirror = true;
		this.setRotation(this.rtoeL, 0.0F, 0.1396263F, 0.0F);
		this.legL.addChild(this.rtoeL);
		this.thighL = new ModelRenderer(this, 13, 18);
		this.thighL.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2);
		this.thighL.setRotationPoint(1.5F, 12.0F, -1.0F);
		this.thighL.setTextureSize(64, 32);
		this.thighL.mirror = true;
		this.setRotation(this.thighL, 0.0F, 0.0F, 0.0F);
		this.thighL.addChild(this.legL);
		this.body.addChild(this.thighL);
		this.body.addChild(this.thighR);
		this.body.addChild(this.head);
		this.body.addChild(this.tail);
		this.body.addChild(this.rightwing1);
		this.body.addChild(this.leftwing1);

		this.animationWingFlap = new ModelAnimator();

		float frameUnit = 0.01666667F;
		List innerWingFrames = new ArrayList(12);
		innerWingFrames.add(new KeyFrame(0.0F, 2.0F, -43.5F, 0.0F, InterpType.LINEAR));
		innerWingFrames.add(new KeyFrame(5.0F * frameUnit, 4.0F, -38.0F, 0.0F, InterpType.LINEAR));
		innerWingFrames.add(new KeyFrame(10.0F * frameUnit, 5.5F, -27.5F, 0.0F, InterpType.LINEAR));
		innerWingFrames.add(new KeyFrame(15.0F * frameUnit, 5.5F, -7.0F, 0.0F, InterpType.LINEAR));
		innerWingFrames.add(new KeyFrame(20.0F * frameUnit, 5.5F, 15.0F, 0.0F, InterpType.LINEAR));
		innerWingFrames.add(new KeyFrame(25.0F * frameUnit, 4.5F, 30.0F, 0.0F, InterpType.LINEAR));
		innerWingFrames.add(new KeyFrame(30.0F * frameUnit, 2.0F, 38.0F, 9.0F, InterpType.LINEAR));
		innerWingFrames.add(new KeyFrame(35.0F * frameUnit, 1.0F, 20.0F, 0.0F, InterpType.LINEAR));
		innerWingFrames.add(new KeyFrame(40.0F * frameUnit, 1.0F, 3.5F, 0.0F, InterpType.LINEAR));
		innerWingFrames.add(new KeyFrame(45.0F * frameUnit, 1.0F, -19.0F, 0.0F, InterpType.LINEAR));
		innerWingFrames.add(new KeyFrame(50.0F * frameUnit, -3.0F, -38.0F, 0.0F, InterpType.LINEAR));
		innerWingFrames.add(new KeyFrame(55.0F * frameUnit, -1.0F, -48.0F, 0.0F, InterpType.LINEAR));
		KeyFrame.toRadians(innerWingFrames);
		this.animationWingFlap.addPart(this.rightwing1, innerWingFrames);
		List copy = KeyFrame.cloneFrames(innerWingFrames);
		KeyFrame.mirrorFramesX(copy);
		this.animationWingFlap.addPart(this.leftwing1, copy);

		List outerWingFrames = new ArrayList(13);
		outerWingFrames.add(new KeyFrame(0.0F, 2.0F, 34.5F, 0.0F, InterpType.LINEAR));
		outerWingFrames.add(new KeyFrame(5.0F * frameUnit, 5.0F, 13.0F, -7.0F, InterpType.LINEAR));
		outerWingFrames.add(new KeyFrame(10.0F * frameUnit, 7.0F, 8.5F, -10.0F, InterpType.LINEAR));
		outerWingFrames.add(new KeyFrame(15.0F * frameUnit, 7.5F, -2.5F, -10.0F, InterpType.LINEAR));
		outerWingFrames.add(new KeyFrame(25.0F * frameUnit, 5.0F, 7.0F, -10.0F, InterpType.LINEAR));
		outerWingFrames.add(new KeyFrame(30.0F * frameUnit, 2.0F, 15.0F, 0.0F, InterpType.LINEAR));
		outerWingFrames.add(new KeyFrame(35.0F * frameUnit, -3.0F, 37.0F, 12.0F, InterpType.LINEAR));
		outerWingFrames.add(new KeyFrame(40.0F * frameUnit, -9.0F, 56.0F, 27.0F, InterpType.LINEAR));
		outerWingFrames.add(new KeyFrame(45.0F * frameUnit, -13.0F, 68.0F, 28.0F, InterpType.LINEAR));
		outerWingFrames.add(new KeyFrame(50.0F * frameUnit, -13.5F, 70.0F, 31.5F, InterpType.LINEAR));
		outerWingFrames.add(new KeyFrame(53.0F * frameUnit, -9.0F, 71.0F, 31.0F, InterpType.LINEAR));
		outerWingFrames.add(new KeyFrame(55.0F * frameUnit, -3.5F, 65.5F, 22.0F, InterpType.LINEAR));
		outerWingFrames.add(new KeyFrame(58.0F * frameUnit, 0.0F, 52.0F, 8.0F, InterpType.LINEAR));
		KeyFrame.toRadians(outerWingFrames);
		this.animationWingFlap.addPart(this.rightwing2, outerWingFrames);
		List copy2 = KeyFrame.cloneFrames(outerWingFrames);
		KeyFrame.mirrorFramesX(copy2);
		this.animationWingFlap.addPart(this.leftwing2, copy2);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		this.body.render(f5);
	}

	public void setFlyingAnimations(float flapProgress, float legSweepProgress, float roll) {
		this.animationWingFlap.updateAnimation(flapProgress);

		this.body.rotateAngleY = (this.body.rotateAngleX = 0.0F);
		this.body.rotateAngleZ = (-roll / 180.0F * 3.141593F);

		this.thighR.rotateAngleX = (0.08726647F * legSweepProgress);
		this.thighL.rotateAngleX = (0.08726647F * legSweepProgress);
		this.legR.rotateAngleX = (0.08726647F * legSweepProgress);
		this.legL.rotateAngleX = (0.08726647F * legSweepProgress);
		this.ltoeR.rotateAngleX = (0.8726647F * legSweepProgress);
		this.rtoeR.rotateAngleX = (0.8726647F * legSweepProgress);
		this.btoeR.rotateAngleX = (-0.3490659F + 0.0F * legSweepProgress);
		this.ltoeL.rotateAngleX = (0.8726647F * legSweepProgress);
		this.rtoeL.rotateAngleX = (0.8726647F * legSweepProgress);
		this.btoeL.rotateAngleX = (-0.3490659F + 0.0F * legSweepProgress);

		this.body.rotationPointY = (7.0F + MathHelper.cos(flapProgress * 3.141593F * 2.0F) * 1.4F);
		ModelRenderer tmp190_187 = this.thighR;
		tmp190_187.rotateAngleX = ((float) (tmp190_187.rotateAngleX
				+ MathHelper.cos(flapProgress * 3.141593F * 2.0F) * 0.08726646324990228D));
		ModelRenderer tmp218_215 = this.thighL;
		tmp218_215.rotateAngleX = ((float) (tmp218_215.rotateAngleX
				+ MathHelper.cos(flapProgress * 3.141593F * 2.0F) * 0.08726646324990228D));
		this.tail.rotateAngleX = ((float) (0.2617993956013792D
				+ MathHelper.cos(flapProgress * 3.141593F * 2.0F) * 0.03490658588512815D));
		this.head.rotateAngleX = ((float) (-0.3141592700403172D
				- MathHelper.cos(flapProgress * 3.141593F * 2.0F) * 0.03490658588512815D));
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float limbPeriod, float limbMaxMovement, float ticksExisted, float headYaw,
			float entityPitch, float unitScale, Entity entity) {
		super.setRotationAngles(limbPeriod, limbMaxMovement, ticksExisted, headYaw, entityPitch, unitScale, entity);
		this.body.rotateAngleX = (1.570796F - entityPitch / 180.0F * 3.141593F);
	}
}