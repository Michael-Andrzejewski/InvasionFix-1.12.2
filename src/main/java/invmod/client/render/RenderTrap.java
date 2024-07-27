// `^`^`^`
// ```java
// /**
//  * This class is responsible for rendering the trap entity in the game. It extends the Render class from Minecraft's rendering framework and is specific to the EntityIMTrap entity.
//  *
//  * Public Methods:
//  * - RenderTrap(RenderManager renderManager): Constructs the renderer with a default ModelTrap.
//  * - RenderTrap(RenderManager renderManager, ModelTrap model): Constructs the renderer with a specified ModelTrap.
//  * - doRender(EntityIMTrap entityTrap, double d, double d1, double d2, float f, float f1): Handles the actual rendering of the trap entity. It sets up the OpenGL state, binds the entity's texture, and then renders the model at the appropriate location, orientation, and scale.
//  * - getEntityTexture(EntityIMTrap entity): Returns the ResourceLocation object that points to the texture used for the trap entity.
//  *
//  * The class uses OpenGL calls to manipulate the rendering state and position the model in the world. It also uses a static ResourceLocation to reference the texture used for the trap's appearance.
//  */
// ```
// `^`^`^`

package invmod.client.render;

import org.lwjgl.opengl.GL11;

import invmod.Reference;
import invmod.client.render.model.ModelTrap;
import invmod.entity.block.trap.EntityIMTrap;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderTrap extends Render<EntityIMTrap> {
	private static final ResourceLocation texture = new ResourceLocation(Reference.MODID + ":textures/trap.png");
	private ModelTrap modelTrap;

	public RenderTrap(RenderManager renderManager) {
		this(renderManager, new ModelTrap());
	}

	public RenderTrap(RenderManager renderManager, ModelTrap model) {
		super(renderManager);
		this.modelTrap = model;
	}

	@Override
	public void doRender(EntityIMTrap entityTrap, double d, double d1, double d2, float f, float f1) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d, (float) d1, (float) d2);

		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		GL11.glEnable(32826);
		GL11.glScalef(1.3F, 1.3F, 1.3F);
		this.bindEntityTexture(entityTrap);
		this.modelTrap.render(entityTrap, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, entityTrap.isEmpty(),
				entityTrap.getTrapType());
		GL11.glDisable(32826);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityIMTrap entity) {
		return texture;
	}
}