// `^`^`^`
// ```java
// /**
//  * This class is responsible for rendering the custom spider entities in the game.
//  * It extends the RenderLiving class, utilizing the ModelSpider model for rendering.
//  * 
//  * - RenderSpiderIM(RenderManager): Constructor that sets up the renderer with a specific model and shadow size. It also adds a layer to handle the rendering of spider eyes.
//  * 
//  * - getDeathMaxRotation(EntityIMSpider): Returns the maximum rotation that the spider's corpse should have upon death.
//  * 
//  * - preRenderCallback(EntityIMSpider, float): A callback method that is called before the spider is rendered. It adjusts the spider's shadow size and scales the model based on the spider's scale amount.
//  * 
//  * - getEntityTexture(EntityIMSpider): Determines the texture to use for the spider based on its texture ID. It can return different textures for different spider types, such as a normal spider, a jumping spider, or a mother spider.
//  * 
//  * The class also defines several ResourceLocation objects that point to the textures used for the spider's eyes and body. The commented-out method setSpiderEyeBrightness appears to handle the brightness of the spider's eyes during rendering, but it is currently not in use.
//  */
// package invmod.client.render;
// 
// // ... (imports)
// 
// public class RenderSpiderIM extends RenderLiving<EntityIMSpider> {
//     // ... (class implementation)
// }
// ```
// `^`^`^`

package invmod.client.render;

import org.lwjgl.opengl.GL11;

import invmod.Reference;
import invmod.client.render.layer.LayerIMSpiderEyes;
import invmod.entity.monster.EntityIMSpider;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderSpiderIM extends RenderLiving<EntityIMSpider> {
	private static final ResourceLocation t_eyes = new ResourceLocation("textures/entity/spider_eyes.png");
	private static final ResourceLocation t_spider = new ResourceLocation("textures/entity/spider/spider.png");
	private static final ResourceLocation t_jumping = new ResourceLocation(Reference.MODID + ":textures/spiderT2.png");
	private static final ResourceLocation t_mother = new ResourceLocation(Reference.MODID + ":textures/spiderT2b.png");

	public RenderSpiderIM(RenderManager renderManager) {
		super(renderManager, new ModelSpider(), 1.0F);
		this.addLayer(new LayerIMSpiderEyes(this));
		// setRenderPassModel(new ModelSpider());
	}

	@Override
	protected float getDeathMaxRotation(EntityIMSpider entityspider) {
		return 180.0F;
	}

	/*
	 * protected int setSpiderEyeBrightness(EntityIMSpider entityspider, int i,
	 * float f) { if (i != 0) { return -1; }
	 * 
	 * bindTexture(t_eyes); float f1 = 1.0F; GL11.glEnable(3042);
	 * GL11.glDisable(3008); GL11.glBlendFunc(1, 1);
	 * 
	 * if (entityspider.isInvisible()) { GL11.glDepthMask(false); } else {
	 * GL11.glDepthMask(true); }
	 * 
	 * char c0 = 61680; int j = c0 % 65536; int k = c0 / 65536;
	 * OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F,
	 * k / 1.0F); GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F); GL11.glColor4f(1.0F, 1.0F,
	 * 1.0F, f1); return 1; }
	 */

	@Override
	protected void preRenderCallback(EntityIMSpider entityliving, float f) {
		float f1 = entityliving.spiderScaleAmount();
		this.shadowSize = f1;
		GL11.glScalef(f1, f1, f1);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityIMSpider entity) {
		switch (entity.getTextureId()) {
		case 0:
			return t_spider;
		case 1:
			return t_jumping;
		case 2:
			return t_mother;
		}
		return t_spider;
	}
}