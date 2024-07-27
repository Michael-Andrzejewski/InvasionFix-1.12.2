// `^`^`^`
// ```java
// /**
//  * This class defines the AI behavior for an in-game entity to navigate towards a Nexus point.
//  * The entity will attempt to find a path to the Nexus and move towards it, with mechanisms to handle pathfinding failures.
//  *
//  * Methods:
//  * - EntityAIGoToNexus(EntityIMMob entity): Constructor that initializes the AI with the given entity.
//  * - shouldExecute(): Checks if the AI should start executing, based on the entity's current goal and the presence of a Nexus.
//  * - startExecuting(): Called when the AI begins execution; it sets the initial path to the Nexus.
//  * - updateTask(): Regularly called to update the task; it handles path recalculation and wandering behavior if pathfinding fails.
//  * - setPathToNexus(): Attempts to set a path to the Nexus; adjusts pathfinding based on distance and recalculates the path if necessary.
//  * - pathTooShort(): Checks if the current path is too short, indicating the entity is close to the Nexus.
//  * - wanderToNexus(): Causes the entity to wander towards the Nexus when direct pathfinding fails.
//  *
//  * The AI uses a combination of direct pathfinding and wandering to ensure the entity can reach the Nexus, even in complex environments.
//  */
// ```
// 
// `^`^`^`

package invmod.entity.ai;

//NOOB HAUS:Done

import invmod.entity.Goal;
import invmod.entity.ai.navigator.Path;
import invmod.entity.monster.EntityIMMob;
import invmod.tileentity.TileEntityNexus;
import invmod.util.Distance;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIGoToNexus extends EntityAIBase {
	private EntityIMMob theEntity;
	private BlockPos lastPathRequestPos;
	private int pathRequestTimer;
	private int pathFailedCount;

	public EntityAIGoToNexus(EntityIMMob entity) {
		this.theEntity = entity;
		this.lastPathRequestPos = new BlockPos(0, -128, 0);
		this.pathRequestTimer = 0;
		this.pathFailedCount = 0;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		return this.theEntity.getAIGoal() == Goal.BREAK_NEXUS && this.theEntity.getNexus() != null;
	}

	@Override
	public void startExecuting() {
		this.setPathToNexus();
	}

	@Override
	public void updateTask() {
		if (this.pathFailedCount > 1)
			this.wanderToNexus();
		if ((this.theEntity.getNavigatorNew().noPath()) || (this.theEntity.getNavigatorNew().getStuckTime() > 40))
			this.setPathToNexus();
	}

	private void setPathToNexus() {
		TileEntityNexus nexus = this.theEntity.getNexus();
		if ((nexus != null) && (this.pathRequestTimer-- <= 0)) {
			boolean pathSet = false;
			double distance = this.theEntity.findDistanceToNexus();
			double x = nexus.getPos().getX() + 0.5d;
			double y = nexus.getPos().getY() + 1d;
			double z = nexus.getPos().getZ() + 0.5d;
			if (distance > 2000.0D) {
				pathSet = this.theEntity.getNavigatorNew().tryMoveTowardsXZ(x, z, 1, 6, 4,
						this.theEntity.getMoveSpeedStat());
			} else if (distance > 1.5D) {
				pathSet = this.theEntity.getNavigatorNew().tryMoveToXYZ(x, y, z, 1.0F,
						this.theEntity.getMoveSpeedStat());
				// if(!pathSet) pathSet = this.theEntity.getNavigator().tryMoveToXYZ(x, y, z,
				// 1.0F);
			}

			if ((!pathSet) || ((this.theEntity.getNavigatorNew().getLastPathDistanceToTarget() > 3.0F)
					&& (Distance.distanceBetween(this.lastPathRequestPos, this.theEntity.getPosition()) < 3.5D))) {
				this.pathFailedCount += 1;
				this.pathRequestTimer = (40 * this.pathFailedCount + this.theEntity.world.rand.nextInt(10));
			} else {
				this.pathFailedCount = 0;
				this.pathRequestTimer = 20;
			}

			this.lastPathRequestPos = this.theEntity.getPosition();
		}
	}

	private boolean pathTooShort() {
		Path path = this.theEntity.getNavigatorNew().getPath();
		if (path != null) {
			Vec3d vec = path.getFinalPathPoint().pos;
			return this.theEntity.getDistanceSq(vec.x, vec.y, vec.z) < 4.0D;
		}
		return true;
	}

	protected void wanderToNexus() {
		TileEntityNexus nexus = this.theEntity.getNexus();
		this.theEntity.getMoveHelper().setMoveTo(nexus.getPos().add(0.5d, 0d, 0.5d), this.theEntity.getMoveSpeedStat());
	}
}