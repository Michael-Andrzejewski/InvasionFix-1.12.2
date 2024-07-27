// `^`^`^`
// ```java
// /**
//  * This class is a custom renderer for the EntityIMSpawnProxy entity within the invmod mod, which is likely a Minecraft mod.
//  * The class extends the generic Render class from Minecraft's rendering system, specifically tailored to handle the rendering of EntityIMSpawnProxy entities.
//  *
//  * Public Methods:
//  * - RenderInvis(RenderManager renderManager): Constructor that takes a RenderManager instance and passes it to the superclass constructor.
//  * - doRender(EntityIMSpawnProxy entity, double d, double d1, double d2, float f, float f1): Overridden method from the Render class. It is meant to render the entity at the specified location and with the given rotation angles. However, the method is empty, indicating that the EntityIMSpawnProxy might be invisible or requires no specific rendering code.
//  * - getEntityTexture(EntityIMSpawnProxy entity): Overridden method that is supposed to return the ResourceLocation of the texture used for the entity. It returns null, which is consistent with an invisible entity that does not require a texture.
//  *
//  * The overall purpose of this code is to define a custom renderer for an entity that does not render anything visually in the game (presumably an invisible entity). The methods are placeholders and adhere to the required structure of a Minecraft entity renderer, but they do not implement any rendering logic.
//  */
// package invmod.client.render;
// 
// // Import statements...
// 
// public class RenderInvis extends Render<EntityIMSpawnProxy> {
//     // Constructor and method implementations...
// }
// ```
// `^`^`^`

package invmod.client.render;

import invmod.entity.EntityIMSpawnProxy;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderInvis extends Render<EntityIMSpawnProxy> {
	public RenderInvis(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntityIMSpawnProxy entity, double d, double d1, double d2, float f, float f1) {
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityIMSpawnProxy entity) {
		return null;
	}
}