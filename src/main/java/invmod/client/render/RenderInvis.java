package invmod.client.render;

import invmod.entity.EntityIMSpawnProxy;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;


public class RenderInvis extends Render<EntityIMSpawnProxy>
{
	public RenderInvis(RenderManager renderManager)
	{
		super(renderManager);
	}

	@Override
	public void doRender(EntityIMSpawnProxy entity, double d, double d1, double d2, float f, float f1)
	{
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityIMSpawnProxy entity)
	{
		return null;
	}
}