// `^`^`^`
// ```java
// /**
//  * ModelBat is a custom model class for rendering a bat entity in Minecraft.
//  * It extends the ModelBase class and defines the bat's body parts and their animations.
//  *
//  * Public Methods:
//  * - ModelBat(): Constructor that initializes the bat model by creating and positioning the various body parts.
//  * - getBatSize(): Returns an integer representing the size of the bat model.
//  * - render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7): 
//  *   Handles the rendering of the bat model by setting the rotation angles and rendering each part.
//  *
//  * The model includes the following body parts:
//  * - batHead: The head of the bat, with additional child models for ears or other head features.
//  * - batBody: The main body of the bat, including the torso and parts of the wings.
//  * - batRightWing: The right wing of the bat.
//  * - batLeftWing: The left wing of the bat.
//  * - batOuterRightWing: The outer section of the right wing.
//  * - batOuterLeftWing: The outer section of the left wing.
//  *
//  * The constructor sets up the model's texture dimensions and defines the shapes and positions of the body parts.
//  * The render method applies rotation transformations to the model parts to simulate flapping wings and head movement,
//  * and then renders the head and body.
//  */
// ```
// `^`^`^`

package invmod.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBat extends ModelBase {

	private ModelRenderer batHead;
	private ModelRenderer batBody;
	private ModelRenderer batRightWing;
	private ModelRenderer batLeftWing;
	private ModelRenderer batOuterRightWing;
	private ModelRenderer batOuterLeftWing;

	public ModelBat() {
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.batHead = new ModelRenderer(this, 0, 0);
		this.batHead.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6);
		ModelRenderer modelrenderer = new ModelRenderer(this, 24, 0);
		modelrenderer.addBox(-4.0F, -6.0F, -2.0F, 3, 4, 1);
		this.batHead.addChild(modelrenderer);
		ModelRenderer modelrenderer1 = new ModelRenderer(this, 24, 0);
		modelrenderer1.mirror = true;
		modelrenderer1.addBox(1.0F, -6.0F, -2.0F, 3, 4, 1);
		this.batHead.addChild(modelrenderer1);
		this.batBody = new ModelRenderer(this, 0, 16);
		this.batBody.addBox(-3.0F, 4.0F, -3.0F, 6, 12, 6);
		this.batBody.setTextureOffset(0, 34).addBox(-5.0F, 16.0F, 0.0F, 10, 6, 1);
		this.batRightWing = new ModelRenderer(this, 42, 0);
		this.batRightWing.addBox(-12.0F, 1.0F, 1.5F, 10, 16, 1);
		this.batOuterRightWing = new ModelRenderer(this, 24, 16);
		this.batOuterRightWing.setRotationPoint(-12.0F, 1.0F, 1.5F);
		this.batOuterRightWing.addBox(-8.0F, 1.0F, 0.0F, 8, 12, 1);
		this.batLeftWing = new ModelRenderer(this, 42, 0);
		this.batLeftWing.mirror = true;
		this.batLeftWing.addBox(2.0F, 1.0F, 1.5F, 10, 16, 1);
		this.batOuterLeftWing = new ModelRenderer(this, 24, 16);
		this.batOuterLeftWing.mirror = true;
		this.batOuterLeftWing.setRotationPoint(12.0F, 1.0F, 1.5F);
		this.batOuterLeftWing.addBox(0.0F, 1.0F, 0.0F, 8, 12, 1);
		this.batBody.addChild(this.batRightWing);
		this.batBody.addChild(this.batLeftWing);
		this.batRightWing.addChild(this.batOuterRightWing);
		this.batLeftWing.addChild(this.batOuterLeftWing);
	}

	public int getBatSize() {
		return 36;
	}

	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		this.batHead.rotateAngleX = (par6 / 57.295776F);
		this.batHead.rotateAngleY = (par5 / 57.295776F);
		this.batHead.rotateAngleZ = 0.0F;
		this.batHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.batRightWing.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.batLeftWing.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.batBody.rotateAngleX = (0.7853982F + MathHelper.cos(par4 * 0.1F) * 0.15F);
		this.batBody.rotateAngleY = 0.0F;
		this.batRightWing.rotateAngleY = (MathHelper.cos(par4 * 1.3F) * 3.141593F * 0.25F);
		this.batLeftWing.rotateAngleY = (-this.batRightWing.rotateAngleY);
		this.batOuterRightWing.rotateAngleY = (this.batRightWing.rotateAngleY * 0.5F);
		this.batOuterLeftWing.rotateAngleY = (-this.batRightWing.rotateAngleY * 0.5F);

		this.batHead.render(par7);
		this.batBody.render(par7);
	}
}