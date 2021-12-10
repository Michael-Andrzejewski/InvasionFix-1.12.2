package invmod.client.render;

import org.lwjgl.opengl.GL11;

import invmod.Reference;
import invmod.client.render.model.ModelThrower;
import invmod.entity.monster.EntityIMThrower;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderThrower extends RenderLiving<EntityIMThrower> {
	private static final ResourceLocation texture_T1 = new ResourceLocation(
			Reference.MODID + ":textures/throwerT1.png");
	private static final ResourceLocation texture_T2 = new ResourceLocation(
			Reference.MODID + ":textures/throwerT2.png");

	public RenderThrower(RenderManager renderManager) {
		super(renderManager, new ModelThrower(), 1.5F);
	}

	public RenderThrower(RenderManager renderManager, ModelBase modelbase, float f) {
		super(renderManager, modelbase, f);
	}

	@Override
	protected void preRenderCallback(EntityIMThrower entity, float f) {
		GL11.glScalef(2.4F, 2.8F, 2.4F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityIMThrower entity) {
		switch (entity.getTextureId()) {
		case 1:
			return texture_T1;
		case 2:
			return texture_T2;

		}
		return texture_T1;
	}

}