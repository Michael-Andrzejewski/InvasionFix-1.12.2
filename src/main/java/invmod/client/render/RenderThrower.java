// `^`^`^`
// ```java
// /**
//  * This class is responsible for rendering the EntityIMThrower entities in the game. It extends the RenderLiving class, utilizing Minecraft's rendering system to display the thrower entities with their respective textures and models.
//  *
//  * Public Methods:
//  * - RenderThrower(RenderManager renderManager): Constructs the renderer with a default ModelThrower and a shadow size of 1.5F.
//  * - RenderThrower(RenderManager renderManager, ModelBase modelbase, float f): Overloaded constructor that allows for specifying a custom model and shadow size.
//  * - preRenderCallback(EntityIMThrower entity, float f): A callback method that is called before the entity is rendered, used to scale the model of the thrower entity.
//  * - getEntityTexture(EntityIMThrower entity): Determines the texture resource based on the entity's texture ID, returning the appropriate texture for rendering.
//  *
//  * The class defines two static final ResourceLocation objects, texture_T1 and texture_T2, which hold the resource paths to the texture files for the different types of thrower entities.
//  *
//  * The preRenderCallback method applies a GL scaling transformation to the model, making the thrower entity appear larger before it is rendered.
//  *
//  * The getEntityTexture method uses a switch statement to select the correct texture based on the entity's texture ID, defaulting to texture_T1 if no match is found.
//  */
// package invmod.client.render;
// 
// // ... (imports)
// 
// public class RenderThrower extends RenderLiving<EntityIMThrower> {
//     // ... (class implementation)
// }
// ```
// `^`^`^`

package invmod.client.render;

import org.lwjgl.opengl.GL11;

import invmod.Reference;
import invmod.client.render.model.ModelThrower;
import invmod.entity.monster.EntityIMThrower;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderThrower extends RenderLiving<EntityIMThrower> {
	private static final ResourceLocation texture_T1 = new ResourceLocation(
			Reference.MODID + ":textures/throwerT1.png");
	private static final ResourceLocation texture_T2 = new ResourceLocation(
			Reference.MODID + ":textures/throwerT2.png");

	public RenderThrower(RenderManager renderManager) {
		super(renderManager, new ModelThrower(), 1.5F);
	}

	public RenderThrower(RenderManager renderManager, ModelBase modelbase, float f) {
		super(renderManager, modelbase, f);
	}

	@Override
	protected void preRenderCallback(EntityIMThrower entity, float f) {
		GL11.glScalef(2.4F, 2.8F, 2.4F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityIMThrower entity) {
		switch (entity.getTextureId()) {
		case 1:
			return texture_T1;
		case 2:
			return texture_T2;

		}
		return texture_T1;
	}

}