// `^`^`^`
// ```java
// /**
//  * This class is a custom renderer for the EntityIMImp entity, which is a type of monster in the game.
//  * It extends the RenderLiving class from Minecraft's rendering framework and is responsible for
//  * rendering the Imp entity with the appropriate model and texture.
//  *
//  * Public Methods:
//  * - RenderImp(RenderManager renderManager): Constructs the renderer with a predefined model (ModelImp) and shadow size.
//  * - RenderImp(RenderManager renderManager, ModelBase modelbase, float f): Overloaded constructor that allows for specifying a custom model and shadow size.
//  * - preRenderCallback(EntityIMImp entity, float f): A callback method that is called before the entity is rendered. It is used to apply any transformations to the model. Currently, it sets the scale of the Imp to 1.0 in all dimensions.
//  * - getEntityTexture(EntityIMImp entity): Returns the ResourceLocation of the texture to be bound to the model before rendering. It points to the texture file for the Imp entity.
//  *
//  * The texture for the Imp entity is defined as a static final ResourceLocation, which points to the appropriate texture file within the mod's assets.
//  */
// package invmod.client.render;
// 
// // Import statements...
// 
// public class RenderImp extends RenderLiving<EntityIMImp> {
//     // Class implementation...
// }
// ```
// `^`^`^`

package invmod.client.render;

import org.lwjgl.opengl.GL11;

import invmod.Reference;
import invmod.client.render.model.ModelImp;
import invmod.entity.monster.EntityIMImp;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderImp extends RenderLiving<EntityIMImp> {
	private static final ResourceLocation texture = new ResourceLocation(Reference.MODID + ":textures/imp.png");

	public RenderImp(RenderManager renderManager) {
		super(renderManager, new ModelImp(), 0.3F);
	}

	public RenderImp(RenderManager renderManager, ModelBase modelbase, float f) {
		super(renderManager, modelbase, f);
	}

	@Override
	protected void preRenderCallback(EntityIMImp entity, float f) {
		GL11.glScalef(1.0F, 1.0F, 1.0F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityIMImp entity) {
		return texture;
	}
}