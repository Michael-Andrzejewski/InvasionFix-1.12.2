// `^`^`^`
// ```java
// /**
//  * PathfinderIM is a pathfinding class designed for use within the Minecraft mod 'invmod'. It provides functionality
//  * to calculate paths for entities that implement the IPathfindable interface. The class utilizes A* search algorithm
//  * principles to find the shortest path between two points in the game world, considering various movement capabilities
//  * and obstacles.
//  *
//  * Methods:
//  * - createPath: A static synchronized method that serves as an entry point to create a path from one point to another.
//  * - PathfinderIM (constructor): Initializes the pathfinder with default values and data structures.
//  * - createEntityPathTo: Sets up the pathfinding process, initializes the start and end nodes, and calls addToPath to
//  *   begin the search.
//  * - addToPath: The core method that iterates through potential path nodes, evaluating and expanding the path until
//  *   the destination is reached or the search is aborted.
//  * - addNode: Adds a new node to the path options if it is within the search range and not already the first node.
//  * - estimateDistance: Estimates the distance between two nodes as part of the heuristic used in pathfinding.
//  * - openPoint (overloaded): Creates or retrieves a PathNode at a given position, with an optional PathAction.
//  * - findPathOptions: Populates the pathOptions array with possible next steps from the current node.
//  * - createEntityPath: Constructs the final Path object from the calculated nodes leading to the destination.
//  *
//  * The class also maintains several private members for internal state management, such as the world map, path nodes,
//  * and various parameters for controlling the search behavior and limits.
//  */
// ```
// `^`^`^`

package invmod.entity.ai.navigator;

import invmod.IPathfindable;
import invmod.util.ModLogger;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;

public class PathfinderIM {

	private static PathfinderIM pathfinder = new PathfinderIM();
	private IBlockAccess worldMap;
	private NodeContainer path;
	private IntHashMap<PathNode> pointMap;
	private PathNode[] pathOptions;
	private PathNode finalTarget;
	private float targetRadius;
	private int pathsIndex;
	private float searchRange;
	private int nodeLimit;
	private int nodesOpened;

	public static synchronized Path createPath(IPathfindable entity, Vec3d pos0, Vec3d pos1, float targetRadius,
			float maxSearchRange, IBlockAccess iblockaccess, int searchDepth, int quickFailDepth) {
		return pathfinder.createEntityPathTo(entity, pos0, pos1, targetRadius, maxSearchRange, iblockaccess,
				searchDepth, quickFailDepth);
	}

	public PathfinderIM() {
		this.path = new NodeContainer();
		this.pointMap = new IntHashMap<>();
		this.pathOptions = new PathNode[32];
	}

	public Path createEntityPathTo(IPathfindable entity, Vec3d pos0, Vec3d pos1, float targetRadius,
			float maxSearchRange, IBlockAccess iblockaccess, int searchDepth, int quickFailDepth) {
		this.nodesOpened = 1;
		this.pointMap.clearMap();
		PathNode start = this.openPoint(pos0); // DarthXenon: For some odd reason, if I left start and target on
												// consecutive lines, they become the exact same instance of PathNode,
												// despite pos0 and pos1 being different.
		this.worldMap = iblockaccess;
		this.nodeLimit = searchDepth;
		this.searchRange = maxSearchRange;
		this.path.clearPath();
		PathNode target = this.openPoint(pos1);
		this.finalTarget = target;
		this.targetRadius = targetRadius;
		Path pathentity = this.addToPath(entity, start, target);

		return pathentity;
	}

	private Path addToPath(IPathfindable entity, PathNode start, PathNode target) {
		start.totalPathDistance = 0.0F;
		start.distanceToNext = start.distanceTo(target);
		start.distanceToTarget = start.distanceToNext;
		this.path.clearPath();
		this.path.addPoint(start);
		PathNode previousPoint = start;

		int loops = 0;
		long elapsed = 0L;
		while (!this.path.isPathEmpty()) {
			loops++;

			if (loops >= 500)
				break;

			if (this.nodesOpened > this.nodeLimit) { // If the path is too long, stop making more path nodes!
				return this.createEntityPath(start, previousPoint);
			}
			PathNode examiningPoint = this.path.dequeue(); // Get next path point
			if (examiningPoint.equals(target)) {
				previousPoint = target;
				break;
			}
			float distanceToTarget = examiningPoint.distanceTo(target);
			if (distanceToTarget < this.targetRadius + 0.1F) { // If the next path point is close enough to the entity
				return this.createEntityPath(start, examiningPoint);
			}
			if (distanceToTarget < previousPoint.distanceTo(target)) {
				previousPoint = examiningPoint;
			}
			examiningPoint.isFirst = true;

			int i = this.findPathOptions(entity, examiningPoint, target);

			int j = 0;
			while (j < i) {
				PathNode newPoint = this.pathOptions[j];

				float actualCost = examiningPoint.totalPathDistance
						+ entity.getBlockPathCost(examiningPoint, newPoint, this.worldMap);

				if ((!newPoint.isAssigned()) || (actualCost < newPoint.totalPathDistance)) {
					newPoint.setPrevious(examiningPoint);
					newPoint.totalPathDistance = actualCost;
					newPoint.distanceToNext = this.estimateDistance(newPoint, target);

					if (newPoint.isAssigned()) {
						this.path.changeDistance(newPoint, newPoint.totalPathDistance + newPoint.distanceToNext);
					} else {
						newPoint.distanceToTarget = (newPoint.totalPathDistance + newPoint.distanceToNext);
						this.path.addPoint(newPoint);
					}
				}
				j++;
			}
		}

		if (previousPoint == start) {
			return null;
		}
		return this.createEntityPath(start, previousPoint);
	}

	public void addNode(Vec3d pos, PathAction action) {
		PathNode node = this.openPoint(pos, action);
		if ((node != null) && (!node.isFirst) && (node.distanceTo(this.finalTarget) < this.searchRange)) {
			
			if (this.pathsIndex > this.pathOptions.length) {
				ModLogger.logDebug("Pathfinder.addNode: trying to access index" + (this.pathsIndex + 1) + " of " + this.pathOptions.length);
			} else {
				this.pathOptions[(this.pathsIndex++)] = node;
			}
			
			
		}
			
	}

	private double estimateDistance(PathNode start, PathNode target) {
		return Math.abs(target.pos.x - start.pos.x) + Math.abs(target.pos.y - start.pos.y)
				+ Math.abs(target.pos.z - start.pos.z) * 1.01d;
	}

	protected PathNode openPoint(double x, double y, double z) {
		return this.openPoint(new Vec3d(x, y, z), PathAction.NONE);
	}

	protected PathNode openPoint(Vec3d vec) {
		return this.openPoint(vec, PathAction.NONE);
	}

	protected PathNode openPoint(Vec3d pos, PathAction action) {
		int hash = PathNode.makeHash(pos, action);
		PathNode pathpoint = this.pointMap.lookup(hash);
		boolean makeNewPoint = pathpoint == null;
		if (!makeNewPoint)
			makeNewPoint = pathpoint.equals(pos);
		if (makeNewPoint) {
			pathpoint = new PathNode(pos, action);
			this.pointMap.addKey(hash, pathpoint);
			this.nodesOpened += 1;
		}
		return pathpoint;
	}

	private int findPathOptions(IPathfindable entity, PathNode pathpoint, PathNode target) {
		this.pathsIndex = 0;
		entity.getPathOptionsFromNode(this.worldMap, pathpoint, this);
		return this.pathsIndex;
	}

	private Path createEntityPath(PathNode pathpoint, PathNode pathpoint1) {
		int i = 1;
		for (PathNode pathpoint2 = pathpoint1; pathpoint2.getPrevious() != null; pathpoint2 = pathpoint2
				.getPrevious()) {
			i++;
		}

		PathNode[] apathpoint = new PathNode[i];
		PathNode pathpoint3 = pathpoint1;
		for (apathpoint[(--i)] = pathpoint3; pathpoint3.getPrevious() != null; apathpoint[(--i)] = pathpoint3) {
			pathpoint3 = pathpoint3.getPrevious();
		}

		return new Path(apathpoint, this.finalTarget);
	}
}