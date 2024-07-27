// `^`^`^`
// ```java
// /**
//  * This class is responsible for rendering the EntityIMBurrower entity in the game, which is a custom monster entity.
//  * It extends the Render class from Minecraft's rendering framework and is part of the invmod.client.render package.
//  *
//  * Public Methods:
//  * - RenderBurrower(RenderManager renderManager): Constructor that initializes the renderer with a RenderManager instance and sets up the model for the Burrower entity.
//  * - doRender(EntityIMBurrower entityBurrower, double x, double y, double z, float yaw, float partialTick): Handles the actual rendering of the Burrower entity. It calculates the interpolated positions and rotations for smooth animation and applies transformations to the model before rendering it.
//  * - getEntityTexture(EntityIMBurrower entity): Returns the ResourceLocation object that points to the texture used for the Burrower entity.
//  *
//  * The class uses OpenGL calls to manipulate the rendering state and applies transformations to render the entity correctly. It also handles the interpolation of the entity's segments for smooth movement between ticks.
//  */
// ```
// `^`^`^`

package invmod.client.render;

import org.lwjgl.opengl.GL11;

import invmod.Reference;
import invmod.client.render.model.ModelBurrower;
import invmod.entity.monster.EntityIMBurrower;
import invmod.util.PosRotate3D;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderBurrower extends Render<EntityIMBurrower> {
	private static final ResourceLocation texture = new ResourceLocation(Reference.MODID + ":textures/burrower.png");
	private ModelBurrower modelBurrower;

	public RenderBurrower(RenderManager renderManager) {
		super(renderManager);
		this.modelBurrower = new ModelBurrower(16);
	}

	@Override
	public void doRender(EntityIMBurrower entityBurrower, double x, double y, double z, float yaw, float partialTick) {
		PosRotate3D[] pos = entityBurrower.getSegments3D();
		PosRotate3D[] lastPos = entityBurrower.getSegments3DLastTick();
		PosRotate3D[] renderPos = new PosRotate3D[17];
		renderPos[0] = new PosRotate3D();
		renderPos[0].setPosX(x * -7.269999980926514D);
		renderPos[0].setPosY(y * -7.269999980926514D);
		renderPos[0].setPosZ(z * 7.269999980926514D);
		renderPos[0].setRotX(
				entityBurrower.getPrevRotX() + partialTick * (entityBurrower.getRotX() - entityBurrower.getPrevRotX()));
		renderPos[0].setRotY(
				entityBurrower.getPrevRotY() + partialTick * (entityBurrower.getRotY() - entityBurrower.getPrevRotY()));
		renderPos[0].setRotZ(
				entityBurrower.getPrevRotZ() + partialTick * (entityBurrower.getRotZ() - entityBurrower.getPrevRotZ()));

		for (int i = 0; i < 16; i++) {
			renderPos[(i + 1)] = new PosRotate3D();
			renderPos[(i + 1)].setPosX((lastPos[i].getPosX() + partialTick * (pos[i].getPosX() - lastPos[i].getPosX())
					- this.renderManager.viewerPosX) * -7.269999980926514D);
			renderPos[(i + 1)].setPosY((lastPos[i].getPosY() + partialTick * (pos[i].getPosY() - lastPos[i].getPosY())
					- this.renderManager.viewerPosY) * -7.269999980926514D);
			renderPos[(i + 1)].setPosZ((lastPos[i].getPosZ() + partialTick * (pos[i].getPosZ() - lastPos[i].getPosZ())
					- this.renderManager.viewerPosZ) * 7.269999980926514D);
			renderPos[(i + 1)].setRotX(lastPos[i].getRotX() + partialTick * (pos[i].getRotX() - lastPos[i].getRotX()));
			renderPos[(i + 1)].setRotY(lastPos[i].getRotY() + partialTick * (pos[i].getRotY() - lastPos[i].getRotY()));
			renderPos[(i + 1)].setRotZ(lastPos[i].getRotZ() + partialTick * (pos[i].getRotZ() - lastPos[i].getRotZ()));
		}

		GL11.glPushMatrix();
		GL11.glEnable(32826);
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		GL11.glScalef(2.2F, 2.2F, 2.2F);
		this.bindEntityTexture(entityBurrower);
		this.modelBurrower.render(entityBurrower, partialTick, renderPos, 0.0625F);
		GL11.glDisable(32826);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityIMBurrower entity) {
		return texture;
	}
}