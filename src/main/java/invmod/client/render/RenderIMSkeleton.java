// `^`^`^`
// ```java
// /**
//  * This class is responsible for rendering the EntityIMSkeleton, a custom skeleton entity in the game.
//  * It extends the RenderBiped class, utilizing Minecraft's rendering system to display the entity with its unique model and textures.
//  *
//  * Public Methods:
//  * - RenderIMSkeleton(RenderManager renderManager): Constructs the renderer with a default model and shadow size.
//  * - RenderIMSkeleton(RenderManager renderManager, ModelBiped model, float shadowSize): Constructs the renderer with a specified model and shadow size.
//  * - RenderIMSkeleton(RenderManager renderManager, ModelBiped model, float shadowSize, float scale): Constructs the renderer with a specified model, shadow size, and scale.
//  *   It also initializes additional layers for the entity, such as held items, armor, and a cloak.
//  * - getEntityTexture(EntityIMSkeleton entity): Returns the ResourceLocation of the entity's texture.
//  *
//  * The constructor initializes the rendering layers for the EntityIMSkeleton, including held items, armor with custom models, and a cloak layer.
//  * The getEntityTexture method provides the specific texture used for the EntityIMSkeleton, which is defined as a static final ResourceLocation.
//  */
// ```
// `^`^`^`

package invmod.client.render;

import invmod.Reference;
import invmod.client.render.layer.LayerSkeletonCloak;
import invmod.client.render.model.ModelIMSkeleton;
import invmod.entity.monster.EntityIMSkeleton;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;

public class RenderIMSkeleton extends RenderBiped<EntityIMSkeleton> {

	private static final ResourceLocation texture = new ResourceLocation(Reference.MODID + ":textures/skeleton.png");

	public RenderIMSkeleton(RenderManager renderManager) {
		this(renderManager, new ModelIMSkeleton(), 0.5f, 1f);
	}

	public RenderIMSkeleton(RenderManager renderManager, ModelBiped model, float shadowSize) {
		this(renderManager, model, shadowSize, 1f);
	}

	public RenderIMSkeleton(RenderManager renderManager, ModelBiped model, float shadowSize, float scale) {
		// super(rendermanagerIn, new ModelBiped(), 0.5F, new
		// ResourceLocation(Reference.MODID,
		// "textures/entity/entity_mob_cqrdwarf.png"));
		super(renderManager, model, shadowSize);
		this.addLayer(new LayerHeldItem(this));
		this.addLayer(new LayerBipedArmor(this) {
			@Override
			protected void initArmor() {
				this.modelLeggings = new ModelIMSkeleton(0.5f, true);
				this.modelArmor = new ModelIMSkeleton(1f, true);
			}
		});
		this.addLayer(new LayerSkeletonCloak(this));
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityIMSkeleton entity) {
		return texture;
	}
}