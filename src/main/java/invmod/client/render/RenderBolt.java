// `^`^`^`
// ```java
// /**
//  * This class is responsible for rendering the EntityIMBolt entity in Minecraft.
//  * EntityIMBolt is a custom projectile entity, and this class extends the base Render class
//  * to provide the necessary rendering functionality.
//  *
//  * Public Methods:
//  * - RenderBolt(RenderManager renderManager): Constructor that initializes the renderer with the render manager.
//  * - doRender(EntityIMBolt entityBolt, double d, double d1, double d2, float f, float f1): Handles the actual rendering of the EntityIMBolt. It sets up the OpenGL state, applies transformations to position and orient the bolt, scales it, and then calls renderFromVertices to draw the bolt.
//  * - renderFromVertices(EntityIMBolt entityBolt, Tessellator tessellator): Renders the bolt using vertex data. It disables textures and lighting, enables blending, and sets the blend function. It then iterates over the vertices of the bolt, setting up the geometry and color before drawing the vertices using the Tessellator.
//  * - getEntityTexture(EntityIMBolt entity): Returns null as this entity does not use a texture.
//  *
//  * The rendering process involves OpenGL calls for translating, rotating, and scaling the bolt entity, as well as setting up the rendering state for a semi-transparent effect. The actual geometry is defined by the vertices provided by the EntityIMBolt instance.
//  */
// package invmod.client.render;
// 
// // ... (imports)
// 
// public class RenderBolt extends Render<EntityIMBolt> {
//     // ... (class code)
// }
// ```
// `^`^`^`

package invmod.client.render;

import org.lwjgl.opengl.GL11;

import invmod.entity.projectile.EntityIMBolt;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;

public class RenderBolt extends Render<EntityIMBolt> {

	public RenderBolt(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntityIMBolt entityBolt, double d, double d1, double d2, float f, float f1) {
		Tessellator tessellator = Tessellator.getInstance();
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d, (float) d1, (float) d2);
		GL11.glRotatef(entityBolt.getYaw(), 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(entityBolt.getPitch(), 0.0F, 0.0F, 1.0F);
		float scale = 0.0625F;
		GL11.glScalef(scale, scale, scale);
		this.renderFromVertices(entityBolt, tessellator);
		GL11.glPopMatrix();
	}

	public void renderFromVertices(EntityIMBolt entityBolt, Tessellator tessellator) {
		BufferBuilder buffer = tessellator.getBuffer();
		double[][] vertices = entityBolt.getVertices();
		if (vertices == null) {
			return;
		}
		GL11.glDisable(3553);
		GL11.glDisable(2896);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 1);

		double[] xCoords = vertices[0];
		double[] yCoords = vertices[1];
		double[] zCoords = vertices[2];

		double drawWidth = -0.1D;
		for (int pass = 0; pass < 4; pass++) {
			drawWidth += 0.32D;
			for (int i = 1; i < yCoords.length; i++) {
				// tessellator.getWorldRenderer().startDrawing(5);
				// tessellator.getWorldRenderer()./*setColorRGBA_F*/func_178960_a(0.5F, 0.5F,
				// 0.65F, 0.6F);
				buffer.begin(5, new VertexFormat());
				buffer.color(0.5f, 0.5f, 0.65f, 0.6f);

				for (int j = 0; j < 5; j++) {
					double xOffset = 0.5D - drawWidth;
					double zOffset = 0.5D - drawWidth;
					if ((j == 1) || (j == 2)) {
						xOffset += drawWidth * 2.0D;
					}
					if ((j == 2) || (j == 3)) {
						zOffset += drawWidth * 2.0D;
					}
					// tessellator.getWorldRenderer().addVertex(xCoords[(i - 1)] + xOffset,
					// yCoords[(i - 1)] * 16.0D, zCoords[(i - 1)] + zOffset);
					// tessellator.getWorldRenderer().addVertex(xCoords[i] + xOffset, yCoords[i] *
					// 16.0D, zCoords[i] + zOffset);
					buffer.addVertexData(new int[] { (int) (xCoords[(i - 1)] + xOffset),
							(int) (yCoords[(i - 1)] * 16.0D), (int) (zCoords[(i - 1)] + zOffset) });
					buffer.addVertexData(new int[] { (int) (xCoords[i] + xOffset), (int) (yCoords[i] * 16.0D),
							(int) (zCoords[i] + zOffset) });
				}
				tessellator.draw();
			}
		}
		GL11.glDisable(3042);
		GL11.glEnable(2896);
		GL11.glEnable(3553);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityIMBolt entity) {
		return null;
	}
}