package invmod.client.render;

import invmod.Reference;
import invmod.entity.monster.EntityIMPigEngy;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;


public class RenderPigEngy extends RenderBiped<EntityIMPigEngy>
{

	private static final ResourceLocation t1 = new ResourceLocation(Reference.MODID + ":textures/pigengT1.png");
	private static final ResourceLocation t2 = new ResourceLocation(Reference.MODID + ":textures/pigengT2.png");

	public RenderPigEngy(RenderManager renderManager)
	{
		this(renderManager, new ModelBiped()
		{
			@Override
			public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
			{
				super.setRotationAngles(limbSwing / 3f, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
			}
		}, 0.5F, 1f);
		this.addLayer(new LayerHeldItem(this));
	}

	public RenderPigEngy(RenderManager renderManager, ModelBiped model, float shadowSize)
	{
		this(renderManager, model, shadowSize, 1f);
	}

	public RenderPigEngy(RenderManager renderManager, ModelBiped model, float shadowSize, float par3)
	{
		super(renderManager, model, shadowSize, par3);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityIMPigEngy entity)
	{
		switch (entity.getTier())
		{
			case 1:
				return t1;
			case 2:
				return t2;
			default:
				return t1;
		}
	}
}