// `^`^`^`
// ```java
// /**
//  * This Java class is part of the invmod.util package and is used to compare two Minecraft entities based on their distance from a specific point in the game world. It implements the Comparator interface for the Entity type.
//  *
//  * Class: ComparatorEntityDistance
//  * Purpose: Provide a comparator to sort entities based on their distance from a given point (x, y, z).
//  * Usage: Can be used to sort collections of entities or to find the closest/farthest entity from a point.
//  *
//  * Constructor:
//  * ComparatorEntityDistance(double x, double y, double z)
//  * - Initializes the comparator with a specific point in the world (x, y, z) to compare distances against.
//  *
//  * Methods:
//  * int compare(Entity entity1, Entity entity2)
//  * - Compares two entities based on their squared distance from the point initialized in the constructor.
//  * - Returns -1 if entity1 is farther than entity2, 1 if closer, and 0 if the distances are equal.
//  *
//  * Note: This comparator uses squared distances to avoid the computational cost of square root operations, which is acceptable when only the relative distance is needed for comparison.
//  */
// ```
// `^`^`^`

package invmod.util;

import java.util.Comparator;

import net.minecraft.entity.Entity;

public class ComparatorEntityDistance implements Comparator<Entity> {
	private double x;
	private double y;
	private double z;

	public ComparatorEntityDistance(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public int compare(Entity entity1, Entity entity2) {
		double d1 = (this.x - entity1.posX) * (this.x - entity1.posX)
				+ (this.y - entity1.posY) * (this.y - entity1.posY) + (this.z - entity1.posZ) * (this.z - entity1.posZ);
		double d2 = (this.x - entity2.posX) * (this.x - entity2.posX)
				+ (this.y - entity2.posY) * (this.y - entity2.posY) + (this.z - entity2.posZ) * (this.z - entity2.posZ);
		if (d1 > d2)
			return -1;
		if (d1 < d2) {
			return 1;
		}
		return 0;
	}
}