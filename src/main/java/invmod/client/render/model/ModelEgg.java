// `^`^`^`
// ```java
// /**
//  * ModelEgg: A custom 3D model for an egg entity in Minecraft.
//  *
//  * This class extends ModelBase and is used to define the shape and structure of an egg entity's model
//  * for rendering in Minecraft. It includes a series of ModelRenderer instances that represent different
//  * parts of the egg model.
//  *
//  * Constructor:
//  * - ModelEgg(): Initializes the model by setting the texture dimensions and creating the ModelRenderer
//  *   components with their respective texture coordinates, dimensions, and initial rotations. Each part
//  *   of the egg is created using the addBox method, and the setRotation method is used to define the
//  *   initial rotation for each part.
//  *
//  * Methods:
//  * - render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5): Renders the egg
//  *   model by calling the render method on each ModelRenderer instance. It also sets the rotation angles
//  *   for the model based on the entity's current state.
//  * - setRotation(ModelRenderer model, float x, float y, float z): Helper method to set the rotation
//  *   angles for a ModelRenderer instance.
//  * - setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity): Called
//  *   to update the model's rotation angles before rendering, typically used for animations or to react
//  *   to entity state changes.
//  *
//  * The model is composed of multiple layers and sides, each represented by a ModelRenderer instance,
//  * such as base, top, l1s1-l1s4 (layer 1 sides), l2s1-l2s4 (layer 2 sides), l3s1-l3s4 (layer 3 sides),
//  * and l4s1-l4s4 (layer 4 sides). The naming convention lXsY refers to layer X side Y.
//  */
// ```
// `^`^`^`

package invmod.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelEgg extends ModelBase {
	ModelRenderer base;
	ModelRenderer l3s4;
	ModelRenderer l3s2;
	ModelRenderer l3s3;
	ModelRenderer l3s1;
	ModelRenderer top;
	ModelRenderer l4s1;
	ModelRenderer l4s4;
	ModelRenderer l2s3;
	ModelRenderer l2s4;
	ModelRenderer l2s1;
	ModelRenderer l4s2;
	ModelRenderer l4s3;
	ModelRenderer l2s2;
	ModelRenderer l1s4;
	ModelRenderer l1s1;
	ModelRenderer l1s2;
	ModelRenderer l1s3;

	public ModelEgg() {
		this.textureWidth = 64;
		this.textureHeight = 32;

		this.top = new ModelRenderer(this, 0, 8);
		this.top.addBox(2F, -11F, 2F, 5, 1, 5);
		this.top.setRotationPoint(0F, 0F, 0F);
		this.top.setTextureSize(64, 32);
		this.top.mirror = true;
		this.setRotation(this.top, 0F, 0F, 0F);
		this.l4s4 = new ModelRenderer(this, 28, 23);
		this.l4s4.addBox(1F, -10F, 2F, 1, 3, 6);
		this.l4s4.setRotationPoint(0F, 0F, 0F);
		this.l4s4.setTextureSize(64, 32);
		this.l4s4.mirror = true;
		this.setRotation(this.l4s4, 0F, 0F, 0F);
		this.l4s3 = new ModelRenderer(this, 0, 24);
		this.l4s3.addBox(1F, -10F, 1F, 6, 3, 1);
		this.l4s3.setRotationPoint(0F, 0F, 0F);
		this.l4s3.setTextureSize(64, 32);
		this.l4s3.mirror = true;
		this.setRotation(this.l4s3, 0F, 0F, 0F);
		this.l4s2 = new ModelRenderer(this, 28, 23);
		this.l4s2.addBox(7F, -10F, 1F, 1, 3, 6);
		this.l4s2.setRotationPoint(0F, 0F, 0F);
		this.l4s2.setTextureSize(64, 32);
		this.l4s2.mirror = true;
		this.setRotation(this.l4s2, 0F, 0F, 0F);
		this.l4s1 = new ModelRenderer(this, 0, 24);
		this.l4s1.addBox(2F, -10F, 7F, 6, 3, 1);
		this.l4s1.setRotationPoint(0F, 0F, 0F);
		this.l4s1.setTextureSize(64, 32);
		this.l4s1.mirror = true;
		this.setRotation(this.l4s1, 0F, 0F, 0F);
		this.l3s4 = new ModelRenderer(this, 10, 22);
		this.l3s4.addBox(0F, -7F, 1F, 1, 2, 8);
		this.l3s4.setRotationPoint(0F, 0F, 0F);
		this.l3s4.setTextureSize(64, 32);
		this.l3s4.mirror = true;
		this.setRotation(this.l3s4, 0F, 0F, 0F);
		this.l3s3 = new ModelRenderer(this, 0, 21);
		this.l3s3.addBox(0F, -7F, 0F, 8, 2, 1);
		this.l3s3.setRotationPoint(0F, 0F, 0F);
		this.l3s3.setTextureSize(64, 32);
		this.l3s3.mirror = true;
		this.setRotation(this.l3s3, 0F, 0F, 0F);
		this.l3s2 = new ModelRenderer(this, 10, 22);
		this.l3s2.addBox(8F, -7F, 0F, 1, 2, 8);
		this.l3s2.setRotationPoint(0F, 0F, 0F);
		this.l3s2.setTextureSize(64, 32);
		this.l3s2.mirror = true;
		this.setRotation(this.l3s2, 0F, 0F, 0F);
		this.l3s1 = new ModelRenderer(this, 0, 21);
		this.l3s1.addBox(1F, -7F, 8F, 8, 2, 1);
		this.l3s1.setRotationPoint(0F, 0F, 0F);
		this.l3s1.setTextureSize(64, 32);
		this.l3s1.mirror = true;
		this.setRotation(this.l3s1, 0F, 0F, 0F);
		this.l2s4 = new ModelRenderer(this, 20, 10);
		this.l2s4.addBox(-1F, -5F, 0F, 1, 4, 9);
		this.l2s4.setRotationPoint(0F, 0F, 0F);
		this.l2s4.setTextureSize(64, 32);
		this.l2s4.mirror = true;
		this.setRotation(this.l2s4, 0F, 0F, 0F);
		this.l2s3 = new ModelRenderer(this, 0, 16);
		this.l2s3.addBox(0F, -5F, -1F, 9, 4, 1);
		this.l2s3.setRotationPoint(0F, 0F, 0F);
		this.l2s3.setTextureSize(64, 32);
		this.l2s3.mirror = true;
		this.setRotation(this.l2s3, 0F, 0F, 0F);
		this.l2s2 = new ModelRenderer(this, 20, 10);
		this.l2s2.addBox(9F, -5F, 0F, 1, 4, 9);
		this.l2s2.setRotationPoint(0F, 0F, 0F);
		this.l2s2.setTextureSize(64, 32);
		this.l2s2.mirror = true;
		this.setRotation(this.l2s2, 0F, 0F, 0F);
		this.l2s1 = new ModelRenderer(this, 0, 16);
		this.l2s1.addBox(0F, -5F, 9F, 9, 4, 1);
		this.l2s1.setRotationPoint(0F, 0F, 0F);
		this.l2s1.setTextureSize(64, 32);
		this.l2s1.mirror = true;
		this.setRotation(this.l2s1, 0F, 0F, 0F);
		this.l1s4 = new ModelRenderer(this, 28, 0);
		this.l1s4.addBox(0F, -1F, 1F, 1, 1, 8);
		this.l1s4.setRotationPoint(0F, 0F, 0F);
		this.l1s4.setTextureSize(64, 32);
		this.l1s4.mirror = true;
		this.setRotation(this.l1s4, 0F, 0F, 0F);
		this.l1s3 = new ModelRenderer(this, 0, 14);
		this.l1s3.addBox(0F, -1F, 0F, 8, 1, 1);
		this.l1s3.setRotationPoint(0F, 0F, 0F);
		this.l1s3.setTextureSize(64, 32);
		this.l1s3.mirror = true;
		this.setRotation(this.l1s3, 0F, 0F, 0F);
		this.l1s2 = new ModelRenderer(this, 28, 0);
		this.l1s2.addBox(8F, -1F, 0F, 1, 1, 8);
		this.l1s2.setRotationPoint(0F, 0F, 0F);
		this.l1s2.setTextureSize(64, 32);
		this.l1s2.mirror = true;
		this.setRotation(this.l1s2, 0F, 0F, 0F);
		this.l1s1 = new ModelRenderer(this, 0, 14);
		this.l1s1.addBox(0F, 0F, 0F, 8, 1, 1);
		this.l1s1.setRotationPoint(1F, -1F, 8F);
		this.l1s1.setTextureSize(64, 32);
		this.l1s1.mirror = true;
		this.setRotation(this.l1s1, 0F, 0F, 0F);
		this.base = new ModelRenderer(this, 0, 0);
		this.base.addBox(1F, 0F, 1F, 7, 1, 7);
		this.base.setRotationPoint(0F, 0F, 0F);
		this.base.setTextureSize(64, 32);
		this.base.mirror = true;
		this.setRotation(this.base, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		this.base.render(f5);
		this.l3s4.render(f5);
		this.l3s2.render(f5);
		this.l3s3.render(f5);
		this.l3s1.render(f5);
		this.top.render(f5);
		this.l4s1.render(f5);
		this.l4s4.render(f5);
		this.l2s3.render(f5);
		this.l2s4.render(f5);
		this.l2s1.render(f5);
		this.l4s2.render(f5);
		this.l4s3.render(f5);
		this.l2s2.render(f5);
		this.l1s4.render(f5);
		this.l1s1.render(f5);
		this.l1s2.render(f5);
		this.l1s3.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}
}