// `^`^`^`
// ```java
// /**
//  * This class defines the AI behavior for an EntityIMThrower to attack entities in Minecraft.
//  * It extends the EntityAIKillEntity class, specifying the attack strategy for the thrower entity.
//  *
//  * Methods:
//  * - EntityAIThrowerKillEntity: Constructor initializing the AI with the thrower entity, target class, attack delay, throw range, and launch speed.
//  * - attackEntity: Executes the attack on the target entity. If in melee mode, it performs a melee attack. Otherwise, it calculates a random position near the target to throw boulders or TNT, depending on the entity's tier.
//  * - canAttackEntity: Determines whether the thrower can attack the target. It checks if melee attack is possible, if the entity can throw projectiles, and if the target is within a calculable range based on the launch speed.
//  *
//  * The AI uses a random number generator to vary the attack pattern and keeps track of the attack range and launch speed to decide on the attack method. The maximum number of boulders thrown is limited to three.
//  */
// package invmod.entity.ai;
// 
// // ... (rest of the imports and class code)
// ```
// This summary provides an overview of the class's purpose, the methods it contains, and the logic behind the attack decisions for the EntityIMThrower in the game.
// `^`^`^`

package invmod.entity.ai;

import java.util.Random;

import invmod.entity.monster.EntityIMThrower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class EntityAIThrowerKillEntity<T extends EntityLivingBase> extends EntityAIKillEntity<T> {

	private boolean melee;
	private float attackRangeSq;
	private float launchSpeed;
	private final EntityIMThrower theEntity;
	private Random rand;
	private int maxBoulderAmount;

	public EntityAIThrowerKillEntity(EntityIMThrower entity, Class<? extends T> targetClass, int attackDelay,
			float throwRange, float launchSpeed) {
		super(entity, targetClass, attackDelay);
		this.rand = new Random();
		this.attackRangeSq = (throwRange * throwRange);
		this.launchSpeed = launchSpeed;
		this.theEntity = entity;
		this.maxBoulderAmount = 3;
	}

	@Override
	protected void attackEntity(Entity target) {
		if (this.melee) {
			this.setAttackTime(this.getAttackDelay());
			super.attackEntity(target);
		} else {
			this.setAttackTime(this.getAttackDelay() * 2);
			int distance = Math.round((float) this.theEntity.getDistance(target.posX, target.posY, target.posZ));
			int missDistance = Math.round((float) Math.ceil(distance / 10));

			for (int i = 1; i <= this.rand.nextInt(this.maxBoulderAmount); i++) {
				double x = (target.posX - missDistance) + this.rand.nextInt((missDistance + 1) * 2);
				double y = (target.posY - missDistance + 1) + this.rand.nextInt((missDistance + 1) * 2);
				double z = (target.posZ - missDistance) + this.rand.nextInt((missDistance + 1) * 2);

				if (this.theEntity.getTier() == 1) {
					this.theEntity.throwBoulder(x, y, z);
				} else {
					this.theEntity.throwTNT(x, y, z);
				}
			}
		}
	}

	@Override
	protected boolean canAttackEntity(Entity target) {
		this.melee = super.canAttackEntity(target);
		if (this.melee) {
			return true;
		}
		if ((!this.theEntity.canThrow())) {
			return false;
		}

		double dX = this.theEntity.posX - target.posX;
		double dZ = this.theEntity.posZ - target.posZ;
		double dXY = MathHelper.sqrt(dX * dX + dZ * dZ);
		return (this.getAttackTime() <= 0) && (this.theEntity.getEntitySenses().canSee(target))
				&& (0.025D * dXY / (this.launchSpeed * this.launchSpeed) <= 1.0D);
	}
}