// `^`^`^`
// ```java
// /**
//  * This class is responsible for rendering the EntityIMBoulder entity in the game, which is a custom boulder projectile.
//  * It extends the Render class from Minecraft's rendering system to provide the necessary rendering functionality.
//  *
//  * Public Methods:
//  * - RenderBoulder(RenderManager renderManager): Constructor that initializes the renderer with a RenderManager instance and sets up the model for the boulder.
//  * - doRender(EntityIMBoulder entityBoulder, double d, double d1, double d2, float f, float f1): Handles the actual rendering of the boulder entity. It positions the boulder in the world, scales it, binds the texture, and applies a rotation based on the boulder's flight time.
//  * - getEntityTexture(EntityIMBoulder entity): Returns the ResourceLocation object that points to the texture used for the boulder.
//  *
//  * The class uses OpenGL calls to manipulate the rendering environment, such as translating and scaling the model, and enabling/disabling certain OpenGL states.
//  * The texture for the boulder is defined as a static final ResourceLocation, which points to the relevant texture file within the mod's assets.
//  */
// ```
// `^`^`^`

package invmod.client.render;

import org.lwjgl.opengl.GL11;

import invmod.Reference;
import invmod.client.render.model.ModelBoulder;
import invmod.entity.projectile.EntityIMBoulder;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderBoulder extends Render<EntityIMBoulder> {
	private static final ResourceLocation texture = new ResourceLocation(Reference.MODID + ":textures/boulder.png");
	private ModelBoulder modelBoulder;

	public RenderBoulder(RenderManager renderManager) {
		super(renderManager);
		this.modelBoulder = new ModelBoulder();
	}

	@Override
	public void doRender(EntityIMBoulder entityBoulder, double d, double d1, double d2, float f, float f1) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d, (float) d1, (float) d2);
		GL11.glEnable(32826);
		GL11.glScalef(2.2F, 2.2F, 2.2F);
		this.bindEntityTexture(entityBoulder);
		float spin = entityBoulder.getFlightTime() % 20 / 20.0F;
		this.modelBoulder.render(entityBoulder, spin, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glDisable(32826);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityIMBoulder entity) {
		return texture;
	}
}