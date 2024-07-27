// `^`^`^`
// ```java
// /**
//  * This class is responsible for rendering the custom entity EntityIMZombiePigman in Minecraft.
//  * It extends RenderBiped, utilizing the ModelZombie as its default model and applying various
//  * layers to enhance its appearance.
//  *
//  * Methods:
//  * - RenderIMZombiePigman(RenderManager): Constructor that sets up the renderer with a ModelZombie,
//  *   initializes the modelBiped, and adds held item and armor layers.
//  * - doRender(EntityIMZombiePigman, double, double, double, float, float): Handles the rendering logic
//  *   for the EntityIMZombiePigman, switching between big biped and normal models based on the entity's state.
//  * - doRenderBigBiped(EntityIMZombiePigman, double, double, double, float, float): Placeholder for rendering
//  *   the big biped version of the entity.
//  * - preRenderCallback(EntityIMZombiePigman, float): Applies a scaling transformation to the entity before rendering,
//  *   based on its scaleAmount.
//  * - getTexture(EntityIMZombiePigman): Returns the appropriate ResourceLocation for the entity's texture based on its
//  *   textureId.
//  * - getEntityTexture(EntityIMZombiePigman): Overrides the method from RenderBiped to provide the correct texture for
//  *   the entity.
//  *
//  * The class also defines two static ResourceLocation objects for the textures and contains protected fields for
//  * different models and layers that can be applied to the entity.
//  */
// ```
// `^`^`^`

package invmod.client.render;

import org.lwjgl.opengl.GL11;

import invmod.Reference;
import invmod.client.render.layer.LayerHeldItemBigBiped;
import invmod.client.render.model.ModelBigBiped;
import invmod.entity.monster.EntityIMZombiePigman;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;

public class RenderIMZombiePigman extends RenderBiped<EntityIMZombiePigman> {

	private static final ResourceLocation t_T1 = new ResourceLocation(Reference.MODID + ":textures/pigzombie64x32.png");
	private static final ResourceLocation t_T3 = new ResourceLocation(Reference.MODID + ":textures/zombiePigmanT3.png");

	protected ModelBiped modelBiped;
	protected ModelBigBiped modelBigBiped = new ModelBigBiped();

	protected LayerHeldItem layerHeldItem = new LayerHeldItem(this);
	protected LayerHeldItemBigBiped layerHeldItemBigBiped = new LayerHeldItemBigBiped(this);
	protected LayerBipedArmor layerBipedArmor = new LayerBipedArmor(this) {
		@Override
		protected void initArmor() {
			this.modelLeggings = new ModelZombie(0.5F, true);
			this.modelArmor = new ModelZombie(1.0F, true);
		}
	};

	public RenderIMZombiePigman(RenderManager renderManager) {
		super(renderManager, new ModelZombie(0.0F, true), 0.5F);
		this.modelBiped = (ModelBiped) this.mainModel;
		this.addLayer(this.layerHeldItem);
		this.addLayer(this.layerBipedArmor);
	}

	@Override
	public void doRender(EntityIMZombiePigman entity, double x, double y, double z, float entityYaw,
			float partialTicks) {
		try {
			this.layerRenderers.remove(this.layerHeldItem);// removeLayer(this.layerHeldItem);
			this.layerRenderers.remove(this.layerHeldItemBigBiped);// this.removeLayer(this.layerHeldItemBigBiped);
		} catch (Exception ex) {

		}
		if (entity.isBigRenderTempHack()) {
			this.addLayer(this.layerHeldItemBigBiped);
			this.mainModel = this.modelBigBiped;
			this.modelBigBiped.setSneaking(entity.isSneaking());
			this.doRenderBigBiped(entity, x, y, z, entityYaw, partialTicks);
		} else {
			this.addLayer(this.layerHeldItem);
			this.mainModel = this.modelBiped;
			super.doRender(entity, x, y, z, entityYaw, partialTicks);
		}

	}

	public void doRenderBigBiped(EntityIMZombiePigman entity, double x, double y, double z, float entityYaw,
			float partialTicks) {
	}

	@Override
	protected void preRenderCallback(EntityIMZombiePigman entity, float partialTickTime) {
		float f = entity.scaleAmount();
		GL11.glScalef(f, (2.0F + f) / 3.0F, f);
	}

	protected ResourceLocation getTexture(EntityIMZombiePigman entity) {
		switch (entity.getTextureId()) {
		case 0:
			return t_T1;
		case 2:
			return t_T3;
		default:
			return t_T1;
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityIMZombiePigman entity) {
		return this.getTexture((EntityIMZombiePigman) entity);
	}
}