package invmod.client.render;

import org.lwjgl.opengl.GL11;
import invmod.Reference;
import invmod.client.render.model.ModelImp;
import invmod.entity.monster.EntityIMImp;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;


public class RenderImp extends RenderLiving<EntityIMImp>
{
	private static final ResourceLocation texture = new ResourceLocation(Reference.MODID + ":textures/imp.png");

	public RenderImp(RenderManager renderManager)
	{
		super(renderManager, new ModelImp(), 0.3F);
	}

	public RenderImp(RenderManager renderManager, ModelBase modelbase, float f)
	{
		super(renderManager, modelbase, f);
	}

	@Override
	protected void preRenderCallback(EntityIMImp entity, float f)
	{
		GL11.glScalef(1.0F, 1.0F, 1.0F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityIMImp entity)
	{
		return texture;
	}
}