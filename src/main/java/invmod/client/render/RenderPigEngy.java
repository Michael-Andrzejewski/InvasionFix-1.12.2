// `^`^`^`
// ```java
// /**
//  * This class is responsible for rendering the EntityIMPigEngy entities in the game. It extends the RenderBiped class,
//  * which is a part of Minecraft's rendering system for bipedal entities. The RenderPigEngy class provides custom textures
//  * and model adjustments for the EntityIMPigEngy, which is a custom entity added by the mod.
//  *
//  * - RenderPigEngy(RenderManager renderManager):
//  *   Constructor that initializes the renderer with a modified ModelBiped to alter the limb swing angles and adds a layer
//  *   that allows the entity to hold items.
//  *
//  * - RenderPigEngy(RenderManager renderManager, ModelBiped model, float shadowSize):
//  *   An overloaded constructor that allows for specifying a custom model and shadow size for the entity.
//  *
//  * - RenderPigEngy(RenderManager renderManager, ModelBiped model, float shadowSize, float par3):
//  *   An additional overloaded constructor that provides further customization of the renderer, including an unused parameter
//  *   'par3' which could be intended for future use or additional rendering properties.
//  *
//  * - getEntityTexture(EntityIMPigEngy entity):
//  *   This method is overridden to provide the correct texture for the EntityIMPigEngy based on its tier. It supports two
//  *   tiers, each with its own texture, defaulting to the first tier texture if an unknown tier is encountered.
//  *
//  * The class utilizes two static final ResourceLocation objects, 't1' and 't2', which hold the paths to the texture files
//  * for the two tiers of the EntityIMPigEngy. These textures are defined within the mod's assets directory and are referenced
//  * using the mod's unique identifier.
//  */
// ```
// `^`^`^`

package invmod.client.render;

import invmod.Reference;
import invmod.entity.monster.EntityIMPigEngy;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderPigEngy extends RenderBiped<EntityIMPigEngy> {

	private static final ResourceLocation t1 = new ResourceLocation(Reference.MODID + ":textures/pigengT1.png");
	private static final ResourceLocation t2 = new ResourceLocation(Reference.MODID + ":textures/pigengT2.png");

	public RenderPigEngy(RenderManager renderManager) {
		this(renderManager, new ModelBiped() {
			@Override
			public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
					float headPitch, float scaleFactor, Entity entityIn) {
				super.setRotationAngles(limbSwing / 3f, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor,
						entityIn);
			}
		}, 0.5F, 1f);
		this.addLayer(new LayerHeldItem(this));
	}

	public RenderPigEngy(RenderManager renderManager, ModelBiped model, float shadowSize) {
		this(renderManager, model, shadowSize, 1f);
	}

	public RenderPigEngy(RenderManager renderManager, ModelBiped model, float shadowSize, float par3) {
		super(renderManager, model, shadowSize);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityIMPigEngy entity) {
		switch (entity.getTier()) {
		case 1:
			return t1;
		case 2:
			return t2;
		default:
			return t1;
		}
	}
}