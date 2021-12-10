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