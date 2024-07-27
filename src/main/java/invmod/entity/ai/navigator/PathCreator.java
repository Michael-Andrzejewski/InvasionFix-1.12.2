// `^`^`^`
// ```java
// /**
//  * The PathCreator class is part of the invmod.entity.ai.navigator package and is responsible for generating paths for entities within a Minecraft mod. It implements the IPathSource interface, providing a framework for pathfinding capabilities for entities that can navigate the game world.
//  *
//  * Constructors:
//  * - PathCreator(): Initializes the object with default search depth and quick fail depth values.
//  * - PathCreator(int searchDepth, int quickFailDepth): Allows customization of search depth and quick fail depth parameters.
//  *
//  * Methods:
//  * - getSearchDepth(): Returns the current search depth used for pathfinding.
//  * - getQuickFailDepth(): Returns the current quick fail depth to determine when to abort a path search early.
//  * - setSearchDepth(int depth): Sets the search depth to the specified value.
//  * - setQuickFailDepth(int depth): Sets the quick fail depth to the specified value.
//  * - createPath(IPathfindable entity, Vec3d pos0, Vec3d pos1, float targetRadius, float maxSearchRange, IBlockAccess terrainMap): Creates a path from one vector position to another for a pathfindable entity.
//  * - createPath(EntityIMLiving entity, Entity target, float targetRadius, float maxSearchRange, IBlockAccess terrainMap): Creates a path for an entity to a target entity.
//  * - createPath(EntityIMLiving entity, Vec3d vec, float targetRadius, float maxSearchRange, IBlockAccess terrainMap): Creates a path for an entity to a vector position.
//  * - createPath(IPathResult observer, IPathfindable entity, BlockPos pos0, BlockPos pos1, float maxSearchRange, IBlockAccess terrainMap): Placeholder method for future implementation.
//  * - createPath(IPathResult observer, EntityIMLiving entity, Entity target, float maxSearchRange, IBlockAccess terrainMap): Placeholder method for future implementation.
//  * - createPath(IPathResult observer, EntityIMLiving entity, BlockPos pos, float maxSearchRange, IBlockAccess terrainMap): Placeholder method for future implementation.
//  * - canPathfindNice(IPathSource.PathPriority priority, float maxSearchRange, int searchDepth, int quickFailDepth): Determines if nice pathfinding can be performed based on given parameters.
//  *
//  * The class also maintains an internal array to track the time taken for pathfinding operations and provides methods to adjust pathfinding parameters dynamically.
//  */
// ```
// `^`^`^`

package invmod.entity.ai.navigator;

import invmod.IPathfindable;
import invmod.entity.EntityIMLiving;
import invmod.entity.IPathResult;
import invmod.entity.IPathSource;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;

public class PathCreator implements IPathSource {

	private int searchDepth;
	private int quickFailDepth;
	private int[] nanosUsed;
	private int index;

	public PathCreator() {
		this(200, 50);
	}

	public PathCreator(int searchDepth, int quickFailDepth) {
		this.searchDepth = searchDepth;
		this.quickFailDepth = quickFailDepth;
		this.nanosUsed = new int[6];
		this.index = 0;
	}

	@Override
	public int getSearchDepth() {
		return this.searchDepth;
	}

	@Override
	public int getQuickFailDepth() {
		return this.quickFailDepth;
	}

	@Override
	public void setSearchDepth(int depth) {
		this.searchDepth = depth;
	}

	@Override
	public void setQuickFailDepth(int depth) {
		this.quickFailDepth = depth;
	}

	@Override
	public Path createPath(IPathfindable entity, Vec3d pos0, Vec3d pos1, float targetRadius, float maxSearchRange,
			IBlockAccess terrainMap) {
		long time = System.nanoTime();
		Path path = PathfinderIM.createPath(entity, pos0, pos1, targetRadius, maxSearchRange, terrainMap,
				this.searchDepth, this.quickFailDepth);
		int elapsed = (int) (System.nanoTime() - time);
		this.nanosUsed[this.index] = elapsed;
		if (++this.index >= this.nanosUsed.length) {
			this.index = 0;
		}
		return path;
	}

	@Override
	public Path createPath(EntityIMLiving entity, Entity target, float targetRadius, float maxSearchRange,
			IBlockAccess terrainMap) {
		Vec3d vec = new Vec3d(target.posX + 0.5D - entity.width / 2.0F, target.posY,
				target.posZ + 0.5D - entity.width / 2.0F);
		return this.createPath(entity, vec, targetRadius, maxSearchRange, terrainMap);
	}

	@Override
	public Path createPath(EntityIMLiving entity, Vec3d vec, float targetRadius, float maxSearchRange,
			IBlockAccess terrainMap) {
		BlockPos size = entity.getCollideSize();
		double startZ;
		double startX;
		double startY = entity.getEntityBoundingBox().minY;
		if ((size.getX() <= 1) && (size.getZ() <= 1)) {
			startX = entity.getPosition().getX();
			startZ = entity.getPosition().getZ();
		} else {
			startX = entity.getEntityBoundingBox().minX;
			startZ = entity.getEntityBoundingBox().minZ;
		}
		return this.createPath(entity, new Vec3d(startX, startY, startZ),
				vec.addVector(0.5d - entity.width / 2.0F, 0d, 0.5d - entity.width / 2d), targetRadius, maxSearchRange,
				terrainMap);
	}

	@Override
	public void createPath(IPathResult observer, IPathfindable entity, BlockPos pos0, BlockPos pos1,
			float maxSearchRange, IBlockAccess terrainMap) {
	}

	@Override
	public void createPath(IPathResult observer, EntityIMLiving entity, Entity target, float maxSearchRange,
			IBlockAccess terrainMap) {
	}

	@Override
	public void createPath(IPathResult observer, EntityIMLiving entity, BlockPos pos, float maxSearchRange,
			IBlockAccess terrainMap) {
	}

	@Override
	public boolean canPathfindNice(IPathSource.PathPriority priority, float maxSearchRange, int searchDepth,
			int quickFailDepth) {
		return true;
	}
}