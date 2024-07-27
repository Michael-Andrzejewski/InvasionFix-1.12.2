// `^`^`^`
// ```java
// /**
//  * This class extends the RenderWolf class to provide custom rendering for a wolf entity in Minecraft.
//  * It is part of the invmod client rendering system and utilizes OpenGL for rendering adjustments.
//  *
//  * Constructor:
//  * - RenderIMWolf(RenderManager renderManager): Initializes the renderer with the specified RenderManager.
//  *
//  * Methods:
//  * - preRenderCallback(EntityWolf par1EntityLiving, float par2): A callback method that is called before the wolf entity is rendered.
//  *   It applies a scaling transformation to the wolf model to make it appear larger in the game.
//  *
//  * - getEntityTexture(EntityWolf entity): Overrides the method to provide a custom texture for the wolf entity.
//  *   It returns a ResourceLocation object pointing to the texture file defined by the mod's resource identifier.
//  *
//  * The class uses a static final ResourceLocation to store the path to the custom wolf texture.
//  * The texture is expected to be located in the 'textures' directory within the mod's assets folder.
//  */
// ```
// `^`^`^`

package invmod.client.render;

import org.lwjgl.opengl.GL11;

import invmod.Reference;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.ResourceLocation;

public class RenderIMWolf extends RenderWolf {
	public RenderIMWolf(RenderManager renderManager) {
		// super(renderManager, new ModelWolf(), 1.0F);
		super(renderManager);
	}

	private static final ResourceLocation wolf = new ResourceLocation(
			Reference.MODID + ":textures/wolf_tame_nexus.png");

	@Override
	protected void preRenderCallback(EntityWolf par1EntityLiving, float par2) {
		float f = 1.3F;
		GL11.glScalef(f, (2.0F + f) / 3.0F, f);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityWolf entity) {
		return wolf;
	}
}