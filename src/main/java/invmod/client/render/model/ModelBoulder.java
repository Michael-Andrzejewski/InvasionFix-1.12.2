// `^`^`^`
// ```java
// /**
//  * ModelBoulder is a custom model class for rendering a boulder entity in Minecraft.
//  * It extends the ModelBase class from Minecraft's net.minecraft.client.model package.
//  *
//  * Public Methods:
//  * - ModelBoulder(): Constructor that initializes the boulder model. It defines the shape
//  *   of the boulder using a ModelRenderer, setting its dimensions and initial rotation angles.
//  *
//  * - render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5): 
//  *   This method is called to render the boulder model. It takes in the entity being rendered
//  *   along with various parameters for animation and positioning. It sets the rotation angles
//  *   for the model and then renders the boulder.
//  *
//  * - setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity): 
//  *   This method is used to update the rotation angles of the boulder model based on the input
//  *   parameters. This allows the boulder to have dynamic rotations when rendered.
//  *
//  * The class defines a single ModelRenderer instance, 'boulder', which represents the boulder itself.
//  * The constructor sets up the boulder's size and rotation, while the render method is responsible
//  * for drawing the boulder on the screen with the correct orientation. The setRotationAngles method
//  * allows for the boulder's rotation to be updated, which can be used to animate the boulder or
//  * respond to in-game physics or interactions.
//  */
// ```
// `^`^`^`

package invmod.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBoulder extends ModelBase {
	ModelRenderer boulder;

	public ModelBoulder() {
		this.boulder = new ModelRenderer(this, 0, 0);
		this.boulder.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
		this.boulder.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.boulder.rotateAngleX = 0.0F;
		this.boulder.rotateAngleY = 0.0F;
		this.boulder.rotateAngleZ = 0.0F;
		this.boulder.mirror = false;
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		this.boulder.render(f5);
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		this.boulder.rotateAngleX = f;
		this.boulder.rotateAngleY = f1;
		this.boulder.rotateAngleZ = f2;
	}
}