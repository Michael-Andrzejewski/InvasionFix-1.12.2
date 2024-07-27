// `^`^`^`
// ```java
// /**
//  * This class is a comparator for sorting entities in Minecraft based on their distance from a specific point in the game world.
//  * It implements the Comparator interface for the Entity type, allowing it to be used to sort lists or arrays of entities.
//  *
//  * Constructor:
//  * - ComparatorEntityDistanceFrom(double x, double y, double z): Initializes the comparator with a specific point (x, y, z) in the world.
//  *
//  * Methods:
//  * - compare(Entity entity1, Entity entity2): Compares two entities based on their distance from the specified point. It calculates the squared distance
//  *   from the point to each entity and returns -1, 1, or 0 if the first entity is farther than, closer than, or at the same distance as the second entity, respectively.
//  *
//  * Usage:
//  * This comparator can be used to sort collections of entities, such as ArrayList<Entity>, to prioritize entities based on proximity to a point of interest.
//  * It is useful in scenarios where actions or decisions need to be made based on the distance of entities from a certain location, such as targeting systems or AI behavior.
//  */
// ```
// 
// `^`^`^`

package invmod.util;

import java.util.Comparator;

import net.minecraft.entity.Entity;

public class ComparatorEntityDistanceFrom implements Comparator<Entity> {
	private double posX;
	private double posY;
	private double posZ;

	public ComparatorEntityDistanceFrom(double x, double y, double z) {
		this.posX = x;
		this.posY = y;
		this.posZ = z;
	}

	@Override
	public int compare(Entity entity1, Entity entity2) {
		double d1 = (this.posX - entity1.posX) * (this.posX - entity1.posX)
				+ (this.posY - entity1.posY) * (this.posY - entity1.posY)
				+ (this.posZ - entity1.posZ) * (this.posZ - entity1.posZ);
		double d2 = (this.posX - entity2.posX) * (this.posX - entity2.posX)
				+ (this.posY - entity2.posY) * (this.posY - entity2.posY)
				+ (this.posZ - entity2.posZ) * (this.posZ - entity2.posZ);
		if (d1 > d2)
			return -1;
		if (d1 < d2) {
			return 1;
		}
		return 0;
	}
}