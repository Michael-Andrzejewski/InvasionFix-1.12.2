// `^`^`^`
// ```java
// /**
//  * This class is responsible for rendering the EntityIMEgg entity within the game. It extends the Render class from Minecraft's rendering framework and is designed to handle the visual representation of the EntityIMEgg, which is assumed to be a custom entity in the game, possibly representing some form of egg, such as a spider egg, as suggested by the texture file name.
// 
//  * Class: RenderEgg
//  * Package: invmod.client.render
//  * Extends: Render<EntityIMEgg>
//  * Dependencies: LWJGL (OpenGL), Minecraft rendering classes, invmod custom classes
// 
//  * Methods:
//  * - RenderEgg(RenderManager renderManager): Constructor that initializes the renderer with a RenderManager instance and sets up the model for the egg entity.
//  * - doRender(EntityIMEgg entityEgg, double d, double d1, double d2, float f, float f1): Handles the actual rendering of the egg entity. It positions the entity in the world, enables necessary OpenGL states, binds the entity's texture, renders the model, and then restores the OpenGL state.
//  * - getEntityTexture(EntityIMEgg entity): Returns the ResourceLocation object that points to the texture to be used for the egg entity.
// 
//  * The texture for the egg entity is defined as a static final ResourceLocation, which points to a specific image file within the mod's assets, indicating that this texture is constant and does not change during runtime.
// 
//  * Usage: This class is used by the game's rendering engine to draw the EntityIMEgg on the screen when it is present in the game world.
//  */
// ```
// `^`^`^`

package invmod.client.render;

import org.lwjgl.opengl.GL11;

import invmod.Reference;
import invmod.client.render.model.ModelEgg;
import invmod.entity.block.EntityIMEgg;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderEgg extends Render<EntityIMEgg> {
	private static final ResourceLocation texture = new ResourceLocation(Reference.MODID + ":textures/spideregg.png");
	private ModelEgg modelEgg;

	public RenderEgg(RenderManager renderManager) {
		super(renderManager);
		this.modelEgg = new ModelEgg();
	}

	@Override
	public void doRender(EntityIMEgg entityEgg, double d, double d1, double d2, float f, float f1) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d, (float) d1, (float) d2);
		GL11.glEnable(32826);
		GL11.glScalef(-1.0F, -1.0F, 1.0F);

		this.bindEntityTexture(entityEgg);
		this.modelEgg.render(entityEgg, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glDisable(32826);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityIMEgg entity) {
		return texture;
	}
}