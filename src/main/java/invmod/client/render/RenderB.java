// `^`^`^`
// ```java
// /**
//  * This class is responsible for rendering the EntityIMBird entity within the game, providing a visual representation of the bird entity using OpenGL. It extends the RenderLiving class, which is a part of Minecraft's rendering system for entities.
//  *
//  * - RenderB(RenderManager renderManager):
//  *   Constructor that initializes the renderer with a specific model (ModelBird) and shadow size.
//  *
//  * - doRender(EntityIMBird entityBird, double renderX, double renderY, double renderZ, float interpYaw, float partialTick):
//  *   Handles the rendering of the EntityIMBird. It checks if the bird has a debug flag for flying and renders navigation vectors if necessary. It also sets the wing and leg animations based on the bird's current state before delegating to the superclass for actual rendering.
//  *
//  * - renderNavigationVector(EntityIMBird entityBird, double entityRenderOffsetX, double entityRenderOffsetY, double entityRenderOffsetZ):
//  *   Renders a navigation vector line from the bird to its target, which is useful for debugging purposes. It uses OpenGL calls to draw the line with appropriate blending and coloring.
//  *
//  * - getEntityTexture(EntityIMBird entity):
//  *   Returns the ResourceLocation object that points to the texture file for the EntityIMBird.
//  *
//  * The class also includes a static final field 'texture' that holds the ResourceLocation of the bird's texture, which is used in the getEntityTexture method.
//  *
//  * Note: The commented out override method 'doRender' seems to be a legacy or alternative render method that is not currently in use.
//  */
// ```
// `^`^`^`

package invmod.client.render;

import org.lwjgl.opengl.GL11;

import invmod.Reference;
import invmod.client.render.model.ModelBird;
import invmod.entity.monster.EntityIMBird;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

public class RenderB extends RenderLiving<EntityIMBird> {
	private ModelBird modelBird;
	private static final ResourceLocation texture = new ResourceLocation(Reference.MODID + ":textures/bird_tx1.png");

	public RenderB(RenderManager renderManager) {
		super(renderManager, new ModelBird(), 0.4F);
		this.modelBird = ((ModelBird) this.mainModel);
	}

	@Override
	public void doRender(EntityIMBird entityBird, double renderX, double renderY, double renderZ, float interpYaw,
			float partialTick) {
		if (entityBird.hasFlyingDebug()) {
			this.renderNavigationVector(entityBird, renderX, renderY, renderZ);
		}

		float flapProgress = entityBird.getWingAnimationState().getCurrentAnimationTimeInterp(partialTick);
		this.modelBird.setFlyingAnimations(flapProgress, entityBird.getLegSweepProgress(),
				entityBird.getRotationRoll());
		super.doRender(entityBird, renderX, renderY, renderZ, interpYaw, partialTick);
	}

	/*
	 * @Override public void doRender(T entity, double d, double d1, double d2,
	 * float f, float f1) { renderBz((EntityIMBird) entity, d, d1, d2, f, f1); }
	 */

	private void renderNavigationVector(EntityIMBird entityBird, double entityRenderOffsetX, double entityRenderOffsetY,
			double entityRenderOffsetZ) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		GL11.glPushMatrix();

		GL11.glDisable(3553);
		GL11.glDisable(2896);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 1);

		Vec3d target = entityBird.getFlyTarget();
		double drawWidth = 0.1D;

		// tessellator.getWorldRenderer().startDrawing(5);
		// tessellator.getWorldRenderer()./*setColorRGBA_F*/func_178960_a(1.0F, 0.0F,
		// 0.0F, 1.0F);
		buffer.begin(5, new VertexFormat());
		buffer.color(1f, 0f, 0f, 1f);
		for (int j = 0; j < 5; j++) {
			double xOffset = drawWidth;
			double zOffset = drawWidth;
			if ((j == 1) || (j == 2)) {
				xOffset += drawWidth * 2.0D;
			}
			if ((j == 2) || (j == 3)) {
				zOffset += drawWidth * 2.0D;
			}
			buffer.addVertexData(new int[] { (int) (entityRenderOffsetX - entityBird.width / 2.0F + xOffset),
					(int) (entityRenderOffsetY + entityBird.height / 2.0F),
					(int) (entityRenderOffsetZ - entityBird.width / 2.0F + zOffset) });
			buffer.addVertexData(new int[] { (int) (target.x + xOffset - this.renderManager.viewerPosX),
					(int) (target.y - this.renderManager.viewerPosY),
					(int) (target.z + zOffset - this.renderManager.viewerPosZ) });
		}
		tessellator.draw();

		GL11.glDisable(3042);
		GL11.glEnable(2896);
		GL11.glEnable(3553);

		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityIMBird entity) {
		return texture;
	}
}