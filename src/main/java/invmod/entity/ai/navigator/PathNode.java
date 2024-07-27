// `^`^`^`
// ```java
// /**
//  * This class represents a node in a pathfinding algorithm for entities in Minecraft.
//  * It encapsulates the position of the node, the action required to reach this node,
//  * and various metrics used for pathfinding such as distances and previous node reference.
//  *
//  * Methods:
//  * - PathNode(int, int, int): Constructs a node with integer coordinates and default action.
//  * - PathNode(int, int, int, PathAction): Constructs a node with integer coordinates and specified action.
//  * - PathNode(double, double, double): Constructs a node with double coordinates and default action.
//  * - PathNode(double, double, double, PathAction): Constructs a node with double coordinates and specified action.
//  * - PathNode(Vec3d, PathAction): Constructs a node with a Vec3d position and specified action.
//  * - makeHash(Vec3d, PathAction): Generates a unique hash code for the node based on its position and action.
//  * - distanceTo(PathNode): Calculates the Euclidean distance to another node.
//  * - distanceTo(double, double, double): Calculates the Euclidean distance to specified coordinates.
//  * - equals(Object): Checks if another object is equal to this node based on position and action.
//  * - equals(double, double, double): Checks if the node's position matches the given coordinates.
//  * - equals(Vec3d): Checks if the node's position matches the given Vec3d.
//  * - hashCode(): Returns the hash code of the node.
//  * - isAssigned(): Checks if the node has been assigned an index in a path.
//  * - toString(): Returns a string representation of the node's position and action.
//  * - getPrevious(): Retrieves the previous node in the path.
//  * - setPrevious(PathNode): Sets the previous node in the path.
//  *
//  * The class provides essential functionality for pathfinding, such as node comparison,
//  * distance calculation, and hash code generation for efficient storage and retrieval.
//  */
// ```
// `^`^`^`

package invmod.entity.ai.navigator;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class PathNode {

	// DarthXenon: Changed the following fields to double for added precision:
	// xCoord, yCoord, zCoord, distanceToNext, ditanceToTarget
	// public final double xCoord;
	// public final double yCoord;
	// public final double zCoord;
	public final Vec3d pos;
	public final PathAction action;
	private final int hash;
	int index;
	float totalPathDistance;
	double distanceToNext;
	double distanceToTarget;
	private PathNode previous;
	public boolean isFirst;

	public PathNode(int i, int j, int k) {
		this(i, j, k, PathAction.NONE);
	}

	public PathNode(int i, int j, int k, PathAction pathAction) {
		this((double) i, (double) j, (double) k, pathAction);
	}

	public PathNode(double i, double j, double k) {
		this(i, j, k, PathAction.NONE);
	}

	public PathNode(double i, double j, double k, PathAction pathAction) {
		this(new Vec3d(i, j, k), pathAction);
	}

	public PathNode(Vec3d pos, PathAction pathAction) {
		this.index = -1;
		this.isFirst = false;
		this.pos = pos;
		this.action = pathAction;
		this.hash = makeHash(pos, this.action);
	}

	/*
	 * public static int makeHash(int x, int y, int z, PathAction action) { return y
	 * & 0xFF | (x & 0xFF) << 8 | (z & 0xFF) << 16 | (action.ordinal() & 0xFF) <<
	 * 24; }
	 */

	/*
	 * public static int makeHash(double x, double y, double z, PathAction action){
	 * return (Double.hashCode(y) & 0xFF) | (Double.hashCode(x) & 0xFF) << 8 |
	 * (Double.hashCode(z) & 0xFF) << 16 | (action.ordinal() & 0xFF) << 24; }
	 */

	// DarthXenon: I hope I'm doing this right.
	public static int makeHash(Vec3d vec, PathAction action) {
		long j = Integer.toUnsignedLong(action.ordinal());
		return 31 * vec.hashCode() + (int) (j ^ j >>> 32);
	}

	public static int makeHash(double x, double y, double z, PathAction action) {
		return makeHash(new Vec3d(x, y, z), action);
	}

	public float distanceTo(PathNode pathpoint) {
		double d0 = pathpoint.pos.x - this.pos.x;
		double d1 = pathpoint.pos.y - this.pos.y;
		double d2 = pathpoint.pos.z - this.pos.z;
		return MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
	}

	public float distanceTo(double x, double y, double z) {
		double d0 = x - this.pos.x;
		double d1 = y - this.pos.y;
		double d2 = z - this.pos.z;
		return MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj instanceof PathNode)) {
			PathNode node = (PathNode) obj;
			return (this.hash == node.hash) && this.equals(node.pos) && (node.action == this.action);
		}

		return false;
	}

	public boolean equals(double x, double y, double z) {
		return (this.pos.x == x) && (this.pos.y == y) && (this.pos.z == z);
	}

	public boolean equals(Vec3d pos) {
		return this.pos.equals(pos);
	}

	@Override
	public int hashCode() {
		return this.hash;
	}

	public boolean isAssigned() {
		return this.index >= 0;
	}

	@Override
	public String toString() {
		return this.pos.toString() + ", " + this.action.toString();
	}

	public PathNode getPrevious() {
		return this.previous;
	}

	public void setPrevious(PathNode previous) {
		this.previous = previous;
	}
}