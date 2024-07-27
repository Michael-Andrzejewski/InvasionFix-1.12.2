// `^`^`^`
// ```java
// /**
//  * This class represents a container for managing path nodes in a navigation system, typically used for entity pathfinding.
//  * It provides methods to add, retrieve, and manage the order of path nodes based on their distance to a target.
//  *
//  * Methods:
//  * - NodeContainer(): Constructor that initializes the path points array and count.
//  * - addPoint(PathNode pathpoint): Adds a new path node to the container, expanding the array if necessary, and sorts it into the correct position.
//  * - clearPath(): Clears all path nodes from the container.
//  * - dequeue(): Retrieves and removes the first path node from the container, re-sorting the remaining nodes.
//  * - changeDistance(PathNode pathpoint, double d0): Updates the distance of a given path node and re-sorts the node in the container.
//  * - sortBack(int i): A private method that sorts a node backwards in the array based on its distance to the target.
//  * - sortForward(int i): A private method that sorts a node forwards in the array based on its distance to the target.
//  * - isPathEmpty(): Checks if the container is empty.
//  *
//  * The class uses a binary heap structure to maintain an ordered list of path nodes, allowing efficient retrieval and updating of nodes.
//  */
// package invmod.entity.ai.navigator;
// 
// public class NodeContainer {
//     // Class implementation...
// }
// ```
// `^`^`^`

package invmod.entity.ai.navigator;

public class NodeContainer {
	private PathNode[] pathPoints;
	private int count;

	public NodeContainer() {
		this.pathPoints = new PathNode[1024];
		this.count = 0;
	}

	public PathNode addPoint(PathNode pathpoint) {
		if (pathpoint.index >= 0) {
			throw new IllegalStateException("OW KNOWS!");
		}
		if (this.count == this.pathPoints.length) {
			PathNode[] apathpoint = new PathNode[this.count << 1];
			System.arraycopy(this.pathPoints, 0, apathpoint, 0, this.count);
			this.pathPoints = apathpoint;
		}
		this.pathPoints[this.count] = pathpoint;
		pathpoint.index = this.count;
		this.sortBack(this.count++);
		return pathpoint;
	}

	public void clearPath() {
		this.count = 0;
	}

	public PathNode dequeue() {
		PathNode pathpoint = this.pathPoints[0];
		this.pathPoints[0] = this.pathPoints[(--this.count)];
		this.pathPoints[this.count] = null;
		if (this.count > 0) {
			this.sortForward(0);
		}
		pathpoint.index = -1;
		return pathpoint;
	}

	public void changeDistance(PathNode pathpoint, double d0) {
		double d1 = pathpoint.distanceToTarget;
		pathpoint.distanceToTarget = d0;
		if (d0 < d1) {
			this.sortBack(pathpoint.index);
		} else {
			this.sortForward(pathpoint.index);
		}
	}

	private void sortBack(int i) {
		PathNode pathpoint = this.pathPoints[i];
		double d = pathpoint.distanceToTarget;

		while (i > 0) {
			int j = i - 1 >> 1;
			PathNode pathpoint1 = this.pathPoints[j];
			if (d >= pathpoint1.distanceToTarget) {
				break;
			}
			this.pathPoints[i] = pathpoint1;
			pathpoint1.index = i;
			i = j;
		}

		this.pathPoints[i] = pathpoint;
		pathpoint.index = i;
	}

	private void sortForward(int i) {
		PathNode pathpoint = this.pathPoints[i];
		double d0 = pathpoint.distanceToTarget;
		while (true) {
			int j = 1 + (i << 1);
			int k = j + 1;
			if (j >= this.count) {
				break;
			}
			PathNode pathpoint1 = this.pathPoints[j];
			double d1 = pathpoint1.distanceToTarget;
			double d2;
			PathNode pathpoint2;
			if (k >= this.count) {
				pathpoint2 = null;
				d2 = 1d;
			} else {
				pathpoint2 = this.pathPoints[k];
				d2 = pathpoint2.distanceToTarget;
			}
			if (d1 < d2) {
				if (d1 >= d0) {
					break;
				}
				this.pathPoints[i] = pathpoint1;

				pathpoint1.index = i;
				i = j;
			} else {

				if (d2 >= d0) {
					break;
				}
				// Unstoppable Custom Testcode
				// this seems to temp fix mobs not being able to spawn.
				if (pathpoint2 == null) {
					break;
				}
				// end Unstoppable Custom Testcode
				this.pathPoints[i] = pathpoint2;
				pathpoint2.index = i;
				i = k;
			}
		}
		this.pathPoints[i] = pathpoint;
		pathpoint.index = i;
	}

	public boolean isPathEmpty() {
		return this.count == 0;
	}
}