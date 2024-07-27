// `^`^`^`
// ```java
// /**
//  * This class represents a spawn point in a Minecraft mod, encapsulating the location, angle, and type of spawn.
//  * It implements the IPolarAngle interface for polar angle functionality and Comparable for sorting by angle.
//  *
//  * Constructors:
//  * - SpawnPoint(BlockPos pos, int angle, SpawnType type): Initializes a spawn point using a Minecraft BlockPos object.
//  * - SpawnPoint(int x, int y, int z, int angle, SpawnType type): Initializes a spawn point using individual coordinates.
//  *
//  * Methods:
//  * - getPos(): Returns the spawn point's position as a BlockPos object.
//  * - getAngle(): Returns the polar angle associated with the spawn point.
//  * - getType(): Returns the type of spawn as a SpawnType enum value.
//  * - compareTo(IPolarAngle polarAngle): Compares this spawn point's angle to another, for sorting purposes.
//  * - toString(): Provides a string representation of the spawn point, including type, coordinates, and angle.
//  *
//  * The class also contains private fields for the x, y, and z coordinates, the spawn angle, and the spawn type.
//  * The commented-out methods (getXCoord, getYCoord, getZCoord) suggest potential extensions for direct coordinate access.
//  */
// package invmod.nexus;
// 
// // ... (rest of the code)
// ```
// `^`^`^`

package invmod.nexus;

import invmod.util.IPolarAngle;
import net.minecraft.util.math.BlockPos;

public class SpawnPoint implements IPolarAngle, Comparable<IPolarAngle> {
	private int xCoord;
	private int yCoord;
	private int zCoord;
	private int spawnAngle;
	private SpawnType spawnType;

	public SpawnPoint(BlockPos pos, int angle, SpawnType type) {
		this(pos.getX(), pos.getY(), pos.getZ(), angle, type);
	}

	public SpawnPoint(int x, int y, int z, int angle, SpawnType type) {
		this.xCoord = x;
		this.yCoord = y;
		this.zCoord = z;
		this.spawnAngle = angle;
		this.spawnType = type;
	}

	/*
	 * @Override public int getXCoord() { return this.xCoord; }
	 * 
	 * @Override public int getYCoord() { return this.yCoord; }
	 * 
	 * @Override public int getZCoord() { return this.zCoord; }
	 */

	public BlockPos getPos() {
		return new BlockPos(this.xCoord, this.yCoord, this.zCoord);
	}

	@Override
	public int getAngle() {
		return this.spawnAngle;
	}

	public SpawnType getType() {
		return this.spawnType;
	}

	@Override
	public int compareTo(IPolarAngle polarAngle) {
		if (this.spawnAngle < polarAngle.getAngle()) {
			return -1;
		}
		if (this.spawnAngle > polarAngle.getAngle()) {
			return 1;
		}

		return 0;
	}

	@Override
	public String toString() {
		return "Spawn#" + this.spawnType + "#" + this.xCoord + "," + this.yCoord + "," + this.zCoord + "#"
				+ this.spawnAngle;
	}
}