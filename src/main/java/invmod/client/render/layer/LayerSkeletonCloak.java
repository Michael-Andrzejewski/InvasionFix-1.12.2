// `^`^`^`
// ```java
// /**
//  * This class represents a custom rendering layer for the EntityIMSkeleton, which is a type of skeleton entity in the game.
//  * It is responsible for adding an additional cloak layer with a specific texture to the skeleton's appearance.
//  *
//  * Class: LayerSkeletonCloak
//  * Package: invmod.client.render.layer
//  * Extends: None
//  * Implements: LayerRenderer<EntityIMSkeleton>
//  *
//  * Methods:
//  * - LayerSkeletonCloak(RenderIMSkeleton renderer): Constructor that initializes the renderer and the model with a specific scale and overlay flag.
//  * - doRenderLayer(EntityIMSkeleton entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale): 
//  *   Renders the cloak layer on the skeleton using the provided model attributes and animation settings. It binds the custom texture before rendering.
//  * - shouldCombineTextures(): Indicates that the cloak texture should be combined with the default entity texture.
//  *
//  * The class utilizes a static texture resource located at 'textures/skeleton_overlay.png' within the mod's assets, defined by the mod's ID.
//  * The cloak's appearance is defined by the ModelIMSkeleton, which is adjusted to the skeleton's current animations and rendered onto the entity.
//  */
// ```
// `^`^`^`

package invmod.client.render.layer;

import invmod.Reference;
import invmod.client.render.RenderIMSkeleton;
import invmod.client.render.model.ModelIMSkeleton;
import invmod.entity.monster.EntityIMSkeleton;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

//Copied from LayerSkeletonType
public class LayerSkeletonCloak implements LayerRenderer<EntityIMSkeleton> {

	private static final ResourceLocation texture = new ResourceLocation(
			Reference.MODID + ":textures/skeleton_overlay.png");
	private final RenderIMSkeleton renderer;
	private final ModelIMSkeleton model;

	public LayerSkeletonCloak(RenderIMSkeleton renderer) {
		this.renderer = renderer;
		this.model = new ModelIMSkeleton(0.25f, true);
	}

	@Override
	public void doRenderLayer(EntityIMSkeleton entitylivingbaseIn, float limbSwing, float limbSwingAmount,
			float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		this.model.setModelAttributes(this.renderer.getMainModel());
		this.model.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
		this.renderer.bindTexture(texture);
		this.model.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
	}

	@Override
	public boolean shouldCombineTextures() {
		return true;
	}

}
