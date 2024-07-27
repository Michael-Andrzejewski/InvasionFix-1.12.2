// `^`^`^`
// ```java
// /**
//  * ModelBurrower is a custom model class extending ModelBase designed for rendering a burrower entity in Minecraft.
//  * The model consists of a head and a variable number of body segments, allowing for dynamic size adjustments.
//  *
//  * Constructor:
//  * - ModelBurrower(int numberOfSegments): Initializes the model with a specified number of body segments. It sets up
//  *   the texture dimensions, creates the head model, and iteratively creates each body segment with alternating sizes.
//  *
//  * Methods:
//  * - render(Entity entity, float partialTick, PosRotate3D[] pos, float modelScale): Renders the model by setting the
//  *   position and rotation for the head and each body segment based on the provided PosRotate3D array. It scales the
//  *   model according to the given modelScale. The method ensures that the model is rendered with the correct orientation
//  *   and position in the game world.
//  *
//  * - setRotation(ModelRenderer model, float x, float y, float z): A private helper method that sets the rotation angles
//  *   for a given ModelRenderer instance. This method is used internally to apply the rotation to the head and body
//  *   segments during rendering.
//  *
//  * The class utilizes the ModelRenderer class from the net.minecraft.client.model package to define the shape and
//  * rotation of the model's components. The model is designed to be used within the Minecraft modding context, where
//  * custom entity models can be created and rendered.
//  */
// ```
// `^`^`^`

package invmod.client.render.model;

import invmod.util.PosRotate3D;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBurrower extends ModelBase {
	ModelRenderer head;
	ModelRenderer[] segments;

	public ModelBurrower(int numberOfSegments) {
		this.textureWidth = 64;
		this.textureHeight = 32;

		this.head = new ModelRenderer(this, 0, 0);
		this.head.addBox(-1.0F, -3.0F, -3.0F, 2, 6, 6);
		this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.head.setTextureSize(64, 32);
		this.head.mirror = true;
		this.setRotation(this.head, 0.0F, 0.0F, 0.0F);
		this.segments = new ModelRenderer[numberOfSegments];
		for (int i = 0; i < numberOfSegments; i++) {
			this.segments[i] = new ModelRenderer(this, 0, 0);

			if (i % 2 == 0)
				this.segments[i].addBox(-0.5F, -3.5F, -3.5F, 2, 7, 7);
			else {
				this.segments[i].addBox(-0.5F, -2.5F, -2.5F, 2, 5, 5);
			}
			this.segments[i].setRotationPoint(-4.0F, 0.0F, 0.0F);
			this.segments[i].setTextureSize(64, 32);
			this.segments[i].mirror = true;
			this.setRotation(this.segments[i], 0.0F, 0.0F, 0.0F);
		}
	}

	public void render(Entity entity, float partialTick, PosRotate3D[] pos, float modelScale) {
		super.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, modelScale);

		this.head.setRotationPoint((float) pos[0].getPosX(), (float) pos[0].getPosY(), (float) pos[0].getPosZ());
		this.setRotation(this.head, pos[0].getRotX(), pos[0].getRotY(), pos[0].getRotZ());
		for (int i = 0; i < this.segments.length; i++) {
			this.segments[i].setRotationPoint((float) pos[(i + 1)].getPosX(), (float) pos[(i + 1)].getPosY(),
					(float) pos[(i + 1)].getPosZ());
			this.setRotation(this.segments[i], pos[(i + 1)].getRotX(), pos[(i + 1)].getRotY(), pos[(i + 1)].getRotZ());
			this.segments[i].render(modelScale);
		}
		this.head.render(modelScale);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}