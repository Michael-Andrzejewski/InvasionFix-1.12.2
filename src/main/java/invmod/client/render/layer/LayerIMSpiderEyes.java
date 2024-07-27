// `^`^`^`
// ```java
// /**
//  * This class represents a custom layer renderer for the eyes of the EntityIMSpider in a Minecraft mod.
//  * It is responsible for rendering the glowing eyes texture on spiders.
//  *
//  * Class: LayerIMSpiderEyes
//  * Package: invmod.client.render.layer
//  * Extends: None
//  * Implements: LayerRenderer<T>
//  *
//  * Methods:
//  * - LayerIMSpiderEyes(RenderSpiderIM renderer): Constructor that takes a RenderSpiderIM instance and sets it as the spider renderer.
//  * - doRenderLayer(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale):
//  *   Renders the spider eyes layer. It sets up blending and lighting effects to create the glowing eyes effect on the spider entity.
//  * - shouldCombineTextures(): Returns false indicating that the spider eyes texture should not be combined with the base texture of the spider.
//  *
//  * The class uses OpenGL calls to achieve the desired visual effects, such as enabling and disabling blend modes, setting the depth mask, and managing lightmap texture coordinates.
//  * The actual rendering of the eyes is done by binding the spider eyes texture and then rendering the spider model with the appropriate transformations and lighting.
//  */
// ```
// 
// `^`^`^`

package invmod.client.render.layer;

import invmod.client.render.RenderSpiderIM;
import invmod.entity.monster.EntityIMSpider;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class LayerIMSpiderEyes<T extends EntityIMSpider> implements LayerRenderer<T> {

	private static final ResourceLocation t_eyes = new ResourceLocation("textures/entity/spider_eyes.png");
	private final RenderSpiderIM spiderRenderer;

	public LayerIMSpiderEyes(RenderSpiderIM renderer) {
		this.spiderRenderer = renderer;
	}

	// Copied from LayerSpiderEyes
	@Override
	public void doRenderLayer(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		this.spiderRenderer.bindTexture(t_eyes);
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);

		GlStateManager.depthMask(!entitylivingbaseIn.isInvisible());

		int i = 61680;
		int j = i % 65536;
		int k = i / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.spiderRenderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks,
				netHeadYaw, headPitch, scale);
		i = entitylivingbaseIn.getBrightnessForRender();
		j = i % 65536;
		k = i / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
		this.spiderRenderer.setLightmap(entitylivingbaseIn);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
