// `^`^`^`
// ```java
// /**
//  * This abstract class extends RenderLiving to provide rendering capabilities for custom mob entities of type EntityIMMob.
//  * It is designed to be subclassed for specific EntityIMMob rendering implementations in the Minecraft mod 'invmod'.
//  *
//  * Public Methods:
//  * - RenderIMMob(RenderManager, ModelBase, float): Constructs the renderer with a specified RenderManager, model, and shadow size.
//  * - doRenderLiving(T, double, double, double, float, float): Renders the living entity with position, yaw, and partial tick parameters.
//  *   It also checks if the entity should display a label and renders it if necessary.
//  *
//  * The class is generic, with the type parameter T extending EntityIMMob, ensuring that it can be used for any custom mob entity that is a subclass of EntityIMMob.
//  * The commented-out code suggests there was an intention to render multiple labels, potentially stacked vertically, but this feature is currently not implemented.
//  */
// package invmod.client.render;
// 
// import invmod.entity.monster.EntityIMMob;
// import net.minecraft.client.model.ModelBase;
// import net.minecraft.client.renderer.entity.RenderLiving;
// import net.minecraft.client.renderer.entity.RenderManager;
// 
// public abstract class RenderIMMob<T extends EntityIMMob> extends RenderLiving<T> {
//     // Class implementation...
// }
// ```
// `^`^`^`

package invmod.client.render;

import invmod.entity.monster.EntityIMMob;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;

public abstract class RenderIMMob<T extends EntityIMMob> extends RenderLiving<T> {
	public RenderIMMob(RenderManager renderManager, ModelBase model, float shadowWidth) {
		super(renderManager, model, shadowWidth);
	}

	public void doRenderLiving(T entity, double renderX, double renderY, double renderZ, float interpYaw,
			float parTick) {
		super.doRender(entity, renderX, renderY, renderZ, interpYaw, parTick);
		if (entity.shouldRenderLabel()) {
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