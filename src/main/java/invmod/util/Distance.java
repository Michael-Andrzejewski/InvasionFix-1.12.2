// `^`^`^`
// ```java
// /**
//  * This class provides utility methods to calculate the Euclidean distance between various types of positions
//  * in a Minecraft world. It includes methods to compute distances between block positions, entities, and vectors.
//  *
//  * Methods:
//  * - distanceBetween(BlockPos pos1, BlockPos pos2): Calculates the distance between two block positions.
//  * - distanceBetween(BlockPos pos1, Vec3d pos2): Calculates the distance between a block position and a vector.
//  * - distanceBetween(BlockPos pos1, double x2, double y2, double z2): Calculates the distance between a block position and x, y, z coordinates.
//  * - distanceBetween(double x1, double y1, double z1, double x2, double y2, double z2): Calculates the distance between two sets of x, y, z coordinates.
//  * - distanceBetween(Entity entity, Vec3d pos2): Calculates the distance between an entity's position and a vector.
//  * - distanceBetween(Vec3d vec0, Vec3d vec1): Calculates the distance between two vectors.
//  *
//  * Each method returns the distance as a double value representing the straight line distance between the two points in 3D space.
//  */
// package invmod.util;
// 
// // ... (rest of the code)
// ```
// `^`^`^`

package invmod.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Distance {
	public static double distanceBetween(BlockPos pos1, BlockPos pos2) {
		double dX = pos2.getX() - pos1.getX();
		double dY = pos2.getY() - pos1.getY();
		double dZ = pos2.getZ() - pos1.getZ();
		return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
	}

	public static double distanceBetween(BlockPos pos1, Vec3d pos2) {
		double dX = pos2.x - pos1.getX();
		double dY = pos2.y - pos1.getY();
		double dZ = pos2.z - pos1.getZ();
		return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
	}

	public static double distanceBetween(BlockPos pos1, double x2, double y2, double z2) {
		double dX = x2 - pos1.getX();
		double dY = y2 - pos1.getY();
		double dZ = z2 - pos1.getZ();
		return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
	}

	public static double distanceBetween(double x1, double y1, double z1, double x2, double y2, double z2) {
		double dX = x2 - x1;
		double dY = y2 - y1;
		double dZ = z2 - z1;
		return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
	}

	public static double distanceBetween(Entity entity, Vec3d pos2) {
		double dX = pos2.x - entity.posX;
		double dY = pos2.y - entity.posY;
		double dZ = pos2.z - entity.posZ;
		return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
	}

	public static double distanceBetween(Vec3d vec0, Vec3d vec1) {
		double dX = vec1.x - vec0.x;
		double dY = vec1.y - vec0.y;
		double dZ = vec1.z - vec0.z;
		return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
	}
}