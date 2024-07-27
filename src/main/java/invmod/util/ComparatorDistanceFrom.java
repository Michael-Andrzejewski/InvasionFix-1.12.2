// `^`^`^`
// ```java
// /**
//  * This class provides a comparator to sort SpawnPoint objects based on their distance from a specified point in 3D space.
//  * It implements the Comparator interface for the SpawnPoint type.
//  *
//  * Constructor:
//  * ComparatorDistanceFrom(double x, double y, double z): Initializes the comparator with a specific point (x, y, z) in 3D space.
//  *
//  * Methods:
//  * compare(SpawnPoint pos1, SpawnPoint pos2): Compares two SpawnPoint objects based on their distance from the specified point.
//  * It calculates the squared distance of each SpawnPoint from the point and returns -1, 1, or 0 if the first SpawnPoint is
//  * farther from, closer to, or at the same distance as the second SpawnPoint, respectively.
//  *
//  * Usage:
//  * This comparator can be used to sort a collection of SpawnPoint objects so that the closest or farthest points from a given
//  * location can be easily identified. It is useful in scenarios where spatial sorting is required, such as in game development
//  * for spawn management or proximity-based triggers.
//  */
// package invmod.util;
// 
// // Import statements and class definition...
// ```
// This summary provides an overview of the `ComparatorDistanceFrom` class, detailing its purpose, constructor, and method. It also gives context on how this class might be used in practical scenarios.
// `^`^`^`

package invmod.util;

import java.util.Comparator;

import invmod.nexus.SpawnPoint;

public class ComparatorDistanceFrom implements Comparator<SpawnPoint> {
	private double x;
	private double y;
	private double z;

	public ComparatorDistanceFrom(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public int compare(SpawnPoint pos1, SpawnPoint pos2) {
		double d1 = (this.x - pos1.getPos().getX()) * (this.x - pos1.getPos().getX())
				+ (this.y - pos1.getPos().getY()) * (this.y - pos1.getPos().getY())
				+ (this.z - pos1.getPos().getZ()) * (this.z - pos1.getPos().getZ());
		double d2 = (this.x - pos2.getPos().getX()) * (this.x - pos2.getPos().getX())
				+ (this.y - pos2.getPos().getY()) * (this.y - pos2.getPos().getY())
				+ (this.z - pos2.getPos().getZ()) * (this.z - pos2.getPos().getZ());
		if (d1 > d2)
			return -1;
		if (d1 < d2)
			return 1;
		return 0;
	}
}