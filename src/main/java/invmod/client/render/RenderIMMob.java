package invmod.client.render;

import invmod.entity.monster.EntityIMMob;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;


public abstract class RenderIMMob<T extends EntityIMMob> extends RenderLiving<T>
{
	public RenderIMMob(RenderManager renderManager, ModelBase model, float shadowWidth)
	{
		super(renderManager, model, shadowWidth);
	}

	public void doRenderLiving(T entity, double renderX, double renderY, double renderZ, float interpYaw, float parTick)
	{
		super.doRender(entity, renderX, renderY, renderZ, interpYaw, parTick);
		if (entity.shouldRenderLabel())
		{
			String s = entity.getRenderLabel();
			// was this something important?
			// String[] labels = s.split("\n");
			// for (int i = 0; i < labels.length; i++)
			// {
			// renderLivingLabel(entity, labels[i], renderX, renderY +
			// (labels.length - 1 - i) * 0.22D, renderZ, 32);
			// }
		}
	}
}