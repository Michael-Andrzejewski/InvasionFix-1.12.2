package invmod.client.render.layer;

import invmod.Reference;
import invmod.client.render.RenderIMSkeleton;
import invmod.client.render.model.ModelIMSkeleton;
import invmod.entity.monster.EntityIMSkeleton;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;


//Copied from LayerSkeletonType
public class LayerSkeletonCloak implements LayerRenderer<EntityIMSkeleton>
{

	private static final ResourceLocation texture = new ResourceLocation(Reference.MODID + ":textures/skeleton_overlay.png");
	private final RenderIMSkeleton renderer;
	private final ModelIMSkeleton model;

	public LayerSkeletonCloak(RenderIMSkeleton renderer)
	{
		this.renderer = renderer;
		this.model = new ModelIMSkeleton(0.25f, true);
	}

	@Override
	public void doRenderLayer(EntityIMSkeleton entitylivingbaseIn,
		float limbSwing, float limbSwingAmount, float partialTicks,
		float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		this.model.setModelAttributes(this.renderer.getMainModel());
		this.model.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
		this.renderer.bindTexture(texture);
		this.model.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
	}

	@Override
	public boolean shouldCombineTextures()
	{
		return true;
	}

}
