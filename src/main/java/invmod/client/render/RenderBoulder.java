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