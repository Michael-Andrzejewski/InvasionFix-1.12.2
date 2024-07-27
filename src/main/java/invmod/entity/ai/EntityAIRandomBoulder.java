// `^`^`^`
// ```java
// /**
//  * This class represents an AI behavior for the EntityIMThrower, a custom entity in the mod 'invmod'.
//  * The AI allows the entity to randomly throw boulders at a target, typically the TileEntityNexus, at random intervals.
//  *
//  * Constructor:
//  * - EntityAIRandomBoulder(EntityIMThrower entity, int ammo): Initializes the AI with the specified entity and a set amount of ammunition (boulders).
//  *
//  * Methods:
//  * - shouldExecute(): Determines if the AI should start executing. It checks if the entity is linked to a Nexus, has ammo, and is capable of throwing. It also uses a timer to introduce a delay between throws.
//  * - isInterruptible(): Returns false indicating that this AI task should not be interrupted by other tasks.
//  * - startExecuting(): Decrements the ammo count, resets the timer, and calculates a random position near the Nexus to throw a boulder at. It then commands the entity to throw the boulder at the calculated position.
//  *
//  * The AI task is non-interruptible and operates on a timer, ensuring that the entity throws boulders at random intervals, provided it has ammunition and a target Nexus.
//  */
// package invmod.entity.ai;
// 
// // ... (rest of the code)
// ```
// `^`^`^`

package invmod.entity.ai;

import invmod.entity.monster.EntityIMThrower;
import invmod.tileentity.TileEntityNexus;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIRandomBoulder extends EntityAIBase {

	private final EntityIMThrower theEntity;
	private int randomAmmo;
	private int timer;

	public EntityAIRandomBoulder(EntityIMThrower entity, int ammo) {
		this.theEntity = entity;
		this.randomAmmo = ammo;
		this.timer = 180;
	}

	@Override
	public boolean shouldExecute() {
		if ((this.theEntity.getNexus() != null) && (this.randomAmmo > 0) && (this.theEntity.canThrow())) {
			if (--this.timer <= 0)
				return true;
		}
		return false;
	}

	@Override
	public boolean isInterruptible() {
		return false;
	}

	@Override
	public void startExecuting() {
		this.randomAmmo -= 1;
		this.timer = 240;
		TileEntityNexus nexus = this.theEntity.getNexus();
		int d = (int) (this.theEntity.findDistanceToNexus() * 0.37D);
		if (d == 0)
			d = 1;
		double d0 = nexus.getPos().getX() - d + this.theEntity.getRNG().nextInt(2 * d);
		double d1 = nexus.getPos().getY() - 5 + this.theEntity.getRNG().nextInt(10);
		double d2 = nexus.getPos().getZ() - d + this.theEntity.getRNG().nextInt(2 * d);
		this.theEntity.throwBoulder(d0, d1, d2);
	}

}