// `^`^`^`
// ```java
// /**
//  * This abstract class, NavigatorParametric, extends the NavigatorIM class and is part of the invmod.entity.ai.navigator package. It provides a framework for parametric navigation for entities within the Invasion Mod, specifically for the EntityIMMob class.
//  *
//  * The class is designed to handle navigation updates, path following, and movement execution based on a parametric time value. It includes methods to update navigation, follow a path, and move the entity to a calculated position.
//  *
//  * Key methods:
//  * - onUpdateNavigation(int paramElapsed): Updates the navigation process by incrementing the total ticks, checking path status, following the path, and handling movement and path actions.
//  * - onUpdateNavigation(): Overridden method that calls onUpdateNavigation with a default parameter of 1.
//  * - doMovementTo(int param): Moves the entity to a position determined by the parametric time value and updates the time parameter based on the entity's proximity to the target position.
//  * - entityPositionAtParam(int paramInt): An abstract method that must be implemented to calculate the entity's position at a given parametric time value.
//  * - isReadyForNextNode(int paramInt): An abstract method that must be implemented to determine if the entity is ready to move to the next node in the path.
//  * - pathFollow(int param): Manages the transition to the next node in the path if the entity is ready, based on the parametric time value.
//  * - pathFollow(): Overridden method that calls pathFollow with a default parameter of 0.
//  *
//  * The class also maintains a minimum move tolerance squared (minMoveToleranceSq) to determine when the entity is close enough to the target position, and a time parameter (timeParam) to track the parametric time.
//  */
// package invmod.entity.ai.navigator;
// 
// // Class imports and package declaration omitted for brevity
// ```
// `^`^`^`

package invmod.entity.ai.navigator;

import invmod.entity.IPathSource;
import invmod.entity.monster.EntityIMMob;
import invmod.util.PosRotate3D;
import net.minecraft.util.math.BlockPos;

public abstract class NavigatorParametric extends NavigatorIM {

	protected double minMoveToleranceSq;
	protected int timeParam;

	public NavigatorParametric(EntityIMMob entity, IPathSource pathSource) {
		super(entity, pathSource);
		this.minMoveToleranceSq = 21.0D;
		this.timeParam = 0;
	}

	public void onUpdateNavigation(int paramElapsed) {
		this.totalTicks += 1;
		if ((this.noPath()) || (this.waitingForNotify)) {
			return;
		}
		if ((this.canNavigate()) && (this.nodeActionFinished)) {
			int pathIndex = this.path.getCurrentPathIndex();
			this.pathFollow(this.timeParam + paramElapsed);
			this.doMovementTo(this.timeParam);

			if (this.path.getCurrentPathIndex() != pathIndex) {
				this.ticksStuck = 0;
				if (this.activeNode.action != PathAction.NONE) {
					this.nodeActionFinished = false;
				}
			}
		}
		if (this.nodeActionFinished) {
			if (!this.isPositionClear(new BlockPos(this.activeNode.pos), this.theEntity)) {
				if (this.theEntity.onPathBlocked(this.path, this)) {
					this.setDoingTaskAndHold();
				} else {
					this.clearPath();
				}
			}
		} else {
			this.handlePathAction();
		}
	}

	@Override
	public void onUpdateNavigation() {
		this.onUpdateNavigation(1);
	}

	protected void doMovementTo(int param) {
		PosRotate3D movePos = this.entityPositionAtParam(param);
		this.theEntity.setVelocity(movePos.getPosX(), movePos.getPosY(), movePos.getPosZ());

		if (Math.abs(this.theEntity.getDistanceSq(movePos.getPosX(), movePos.getPosY(),
				movePos.getPosZ())) < this.minMoveToleranceSq) {
			this.timeParam = param;
			this.ticksStuck -= 1;
		} else {
			this.ticksStuck += 1;
		}
	}

	protected abstract PosRotate3D entityPositionAtParam(int paramInt);

	protected abstract boolean isReadyForNextNode(int paramInt);

	protected void pathFollow(int param) {
		int nextIndex = this.path.getCurrentPathIndex() + 1;
		if (this.isReadyForNextNode(param)) {
			if (nextIndex < this.path.getCurrentPathLength()) {
				this.timeParam = 0;
				this.path.setCurrentPathIndex(nextIndex);
				this.activeNode = this.path.getPathPointFromIndex(this.path.getCurrentPathIndex());
			}
		} else {
			this.timeParam = param;
		}
	}

	@Override
	protected void pathFollow() {
		this.pathFollow(0);
	}

}