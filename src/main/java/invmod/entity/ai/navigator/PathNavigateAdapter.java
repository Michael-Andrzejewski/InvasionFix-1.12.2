// `^`^`^`
// ```java
// /**
//  * This class, PathNavigateAdapter, extends PathNavigateGround and serves as a custom navigation adapter for entities in Minecraft.
//  * It is designed to work with a custom NavigatorIM instance, which provides specific navigation logic for entities.
//  *
//  * Methods:
//  * - PathNavigateAdapter(NavigatorIM navigator): Constructor that initializes the adapter with a NavigatorIM instance.
//  * - onUpdateNavigation(): Delegates the update navigation call to the NavigatorIM instance.
//  * - noPath(): Checks if the navigator currently has no path.
//  * - clearPath(): Clears the current path of the navigator.
//  * - setSpeed(double speed): Sets the movement speed of the navigator.
//  * - tryMoveToXYZ(double x, double y, double z, double movespeed): Attempts to move the entity to a specified XYZ location.
//  * - tryMoveToEntityLiving(Entity entity, double movespeed): Attempts to move the entity towards another living entity.
//  * - setPath(invmod.entity.ai.navigator.Path entity, float movespeed): Sets a custom path for the navigator to follow.
//  * - setPath(net.minecraft.pathfinding.Path entity, double movespeed): Overridden to do nothing (returns false).
//  * - getPathToEntityLiving(Entity entity): Overridden to return null, as it is not implemented.
//  * - getPath(): Overridden to return null, as it is not implemented.
//  * - getPathFinder(): Initializes and returns a new PathFinder with a WalkNodeProcessor configured to handle door interaction.
//  * - getEntityPosition(): Overridden to return null, as it is not implemented.
//  * - canNavigate(): Overridden to always return false, indicating navigation is not possible.
//  * - isDirectPathBetweenPoints(Vec3d p1, Vec3d p2, int p3, int p4, int p5): Overridden to always return false.
//  * - setBreakDoors(boolean thing): Configures the WalkNodeProcessor to handle door breaking and entering.
//  *
//  * Note: Several methods are overridden from the parent class but are not implemented, returning null or false.
//  */
// ```
// 
// This summary provides an overview of the `PathNavigateAdapter` class, its constructor, and each method's purpose within the class. It also notes where methods are overridden without implementation, indicating areas that may require further development or are intentionally left unimplemented.
// `^`^`^`

package invmod.entity.ai.navigator;

import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.Vec3d;

public class PathNavigateAdapter extends PathNavigateGround {

	private NavigatorIM navigator;
	protected WalkNodeProcessor walkNodeProcessor;

	public PathNavigateAdapter(NavigatorIM navigator) {
		super(navigator.getEntity(), navigator.getEntity().world);
		this.navigator = navigator;
		if (this.nodeProcessor == null)
			this.nodeProcessor = this.walkNodeProcessor;
	}

	@Override
	public void onUpdateNavigation() {
		this.navigator.onUpdateNavigation();
	}

	@Override
	public boolean noPath() {
		return this.navigator.noPath();
	}

	@Override
	public void clearPath() {
		this.navigator.clearPath();
	}

	@Override
	public void setSpeed(double speed) {
		this.navigator.setSpeed((float) speed);
	}

	@Override
	public boolean tryMoveToXYZ(double x, double y, double z, double movespeed) {
		return this.navigator.tryMoveToXYZ(x, y, z, 0.0F, (float) movespeed);
	}

	@Override
	public boolean tryMoveToEntityLiving(Entity entity, double movespeed) {
		return this.navigator.tryMoveToEntity(entity, 0.0F, (float) movespeed);
	}

	public boolean setPath(invmod.entity.ai.navigator.Path entity, float movespeed) {
		return this.navigator.setPath(entity, movespeed);
	}

	@Override
	public boolean setPath(net.minecraft.pathfinding.Path entity, double movespeed) {
		return false;
	}

	// @Override
	// public PathEntity getPathToXYZ(double x, double y, double z) {
	// return null;
	// }
	//
	// @Override
	// public void setAvoidsWater(boolean avoidsWater) {
	// }
	//
	// @Override
	// public boolean getAvoidsWater() {
	// return false;
	// }
	//
	// @Override
	// public void setBreakDoors(boolean breakDoors) {
	// }
	//
	// @Override
	// public void setEnterDoors(boolean enterDoors) {
	// }
	//
	// @Override
	// public boolean getCanBreakDoors() {
	// return false;
	// }
	//
	// @Override
	// public void setAvoidSun(boolean shouldAvoidSun) {
	// }
	//
	// @Override
	// public void setCanSwim(boolean shouldBeAbleToSwim) {
	// }

	@Override
	public net.minecraft.pathfinding.Path getPathToEntityLiving(Entity entity) {
		return null;
	}

	@Override
	public net.minecraft.pathfinding.Path getPath() {
		return null;
	}

	@Override
	protected PathFinder getPathFinder() {
		this.walkNodeProcessor = new WalkNodeProcessor();
		// was canBreakDoors
		this.walkNodeProcessor.setCanOpenDoors(true);
		this.walkNodeProcessor.setCanEnterDoors(true); // .func_176175_a(true); //TODO: Unsure.
		return new PathFinder(this.walkNodeProcessor);
	}

	@Override
	protected Vec3d getEntityPosition() {
		return null;
	}

	@Override
	protected boolean canNavigate() {
		return false;
	}

	@Override
	protected boolean isDirectPathBetweenPoints(Vec3d p_75493_1_, Vec3d p_75493_2_, int p_75493_3_, int p_75493_4_,
			int p_75493_5_) {
		return false;
	}

	/*
	 * @Override public void func_179693_d(boolean thing) {
	 * this.walkNodeProcessor.func_176178_d(thing); }
	 */

	// TODO: Unsure.
	@Override
	public void setBreakDoors(boolean thing) {
		// was canBreakDoors
		this.walkNodeProcessor.setCanOpenDoors(true);
		this.walkNodeProcessor.setCanEnterDoors(thing);
	}
}