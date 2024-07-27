// `^`^`^`
// ```java
// /**
//  * This class extends the EntityLookHelper to provide custom look behavior for EntityIMMob entities.
//  * It allows the entity to set its look position based on another entity or a specific location in the world,
//  * and updates the entity's head rotation to face that position.
//  *
//  * Methods:
//  * - IMLookHelper(EntityIMMob entity): Constructor that sets the entity for which the look behavior is being customized.
//  * - setLookPositionWithEntity(Entity target, float deltaYaw, float deltaPitch): Sets the look position based on the target entity's position, adjusting for eye height.
//  * - setLookPosition(double x, double y, double z, float deltaYaw, float deltaPitch): Sets the look position based on specific world coordinates.
//  * - onUpdateLook(): Updates the entity's head rotation to face the look position if a new position has been set.
//  * - updateRotation(float currentAngle, float targetAngle, float maxIncrease): Helper method that adjusts the current angle towards the target angle without exceeding the max increase.
//  *
//  * The class maintains internal state to track whether the look position needs to be updated and performs calculations to smoothly transition the entity's head rotation.
//  */
// ```
// `^`^`^`

package invmod.client.render.animation.util;

import invmod.entity.monster.EntityIMMob;
import invmod.util.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.util.math.MathHelper;

public class IMLookHelper extends EntityLookHelper {
	private final EntityIMMob theEntity;
	private float b;
	private float c;
	private boolean d = false;
	private double e;
	private double f;
	private double g;

	public IMLookHelper(EntityIMMob entity) {
		super(entity);
		this.theEntity = entity;
	}

	@Override
	public void setLookPositionWithEntity(Entity par1Entity, float par2, float par3) {
		this.e = par1Entity.posX;

		if ((par1Entity instanceof EntityLiving)) {
			this.f = (par1Entity.posY + par1Entity.getEyeHeight());
		} else {
			this.f = ((par1Entity.getEntityBoundingBox().minY + par1Entity.getEntityBoundingBox().maxY) / 2.0D);
		}

		this.g = par1Entity.posZ;
		this.b = par2;
		this.c = par3;
		this.d = true;
	}

	@Override
	public void setLookPosition(double par1, double par3, double par5, float par7, float par8) {
		this.e = par1;
		this.f = par3;
		this.g = par5;
		this.b = par7;
		this.c = par8;
		this.d = true;
	}

	@Override
	public void onUpdateLook() {
		if (this.d) {
			this.d = false;
			double d0 = this.e - this.theEntity.posX;
			double d1 = this.f - (this.theEntity.posY + this.theEntity.getEyeHeight());
			double d2 = this.g - this.theEntity.posZ;
			double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
			float yaw = (float) MathUtil.boundAngle180Deg(this.theEntity.rotationYaw);
			float pitch = (float) MathUtil.boundAngle180Deg(this.theEntity.rotationPitch);
			float yawHeadOffset = (float) (Math.atan2(d2, d0) * 180.0D / 3.141592653589793D) - 90.0F - yaw;
			float pitchHeadOffset = (float) (Math.atan2(d1, d3) * 180.0D / 3.141592653589793D + 40.0D - pitch);
			float f2 = (float) MathUtil.boundAngle180Deg(yawHeadOffset);
			float yawFinal;
			if ((f2 > 100.0F) || (f2 < -100.0F))
				yawFinal = 0.0F;
			else {
				yawFinal = f2 / 6.0F;
			}

			this.theEntity.setRotationPitchHead(
					this.updateRotation(this.theEntity.getRotationPitchHead(), pitchHeadOffset, this.c));
			this.theEntity
					.setRotationYawHeadIM(this.updateRotation(this.theEntity.getRotationYawHeadIM(), yawFinal, this.b));
		}
	}

	private float updateRotation(float par1, float par2, float par3) {
		// float f3 = MathHelper.wrapAngleTo180_float(par2 - par1);
		float f3 = MathHelper.wrapDegrees(par2 - par1);

		if (f3 > par3) {
			f3 = par3;
		}

		if (f3 < -par3) {
			f3 = -par3;
		}

		return par1 + f3;
	}
}