// `^`^`^`
// ```java
// /**
//  * This class, SpawnPointContainer, is designed to manage and organize spawn points within a Minecraft mod environment.
//  * It categorizes spawn points by type and provides methods to add, retrieve, and manipulate these points.
//  *
//  * Public Methods:
//  * - SpawnPointContainer(): Constructor that initializes the container with sorted status set to false, a new random number generator, a default angle, and an empty EnumMap for storing spawn points by type.
//  * - addSpawnPointXZ(SpawnPoint spawnPoint): Adds a new spawn point to the container. If a spawn point with the same X and Z coordinates exists, it replaces the old one if the new Y coordinate is lower.
//  * - getRandomSpawnPoint(SpawnType spawnType): Returns a random spawn point of the specified type, or null if no spawn points of that type exist.
//  * - getRandomSpawnPoint(SpawnType spawnType, int minAngle, int maxAngle): Returns a random spawn point within a specified angle range of the specified type, or null if no such spawn points exist.
//  * - getNumberOfSpawnPoints(SpawnType type): Returns the number of spawn points of a specified type.
//  * - getNumberOfSpawnPoints(SpawnType spawnType, int minAngle, int maxAngle): Returns the number of spawn points within a specified angle range of the specified type.
//  * - pointDisplayTest(Block block, World world): A test method that sets a specified block at the location of each humanoid spawn point in the world for visualization purposes.
//  *
//  * The class uses an EnumMap to store ArrayLists of SpawnPoint objects, categorized by SpawnType. It also includes a sorting mechanism to optimize angle-based queries.
//  */
// ```
// `^`^`^`

package invmod.nexus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Random;

import invmod.util.PolarAngle;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpawnPointContainer {
	private EnumMap<SpawnType, ArrayList<SpawnPoint>> spawnPoints;
	private boolean sorted;
	private Random random;
	private PolarAngle angleDesired;

	public SpawnPointContainer() {
		this.sorted = false;
		this.random = new Random();
		this.angleDesired = new PolarAngle(0);
		this.spawnPoints = new EnumMap(SpawnType.class);
		for (SpawnType type : SpawnType.values()) {
			this.spawnPoints.put(type, new ArrayList());
		}
	}

	public void addSpawnPointXZ(SpawnPoint spawnPoint) {
		boolean flag = false;
		ArrayList spawnList = this.spawnPoints.get(spawnPoint.getType());
		for (int i = 0; i < spawnList.size(); i++) {
			SpawnPoint oldPoint = (SpawnPoint) spawnList.get(i);
			if ((oldPoint.getPos().getX() == spawnPoint.getPos().getX())
					&& (oldPoint.getPos().getZ() == spawnPoint.getPos().getZ())) {
				if (oldPoint.getPos().getY() > spawnPoint.getPos().getY()) {
					spawnList.set(i, spawnPoint);
				}
				flag = true;
				break;
			}
		}

		if (!flag) {
			spawnList.add(spawnPoint);
		}
		this.sorted = false;
	}

	public SpawnPoint getRandomSpawnPoint(SpawnType spawnType) {
		ArrayList spawnList = this.spawnPoints.get(spawnType);
		if (spawnList.size() == 0) {
			return null;
		}
		return (SpawnPoint) spawnList.get(this.random.nextInt(spawnList.size()));
	}

	public SpawnPoint getRandomSpawnPoint(SpawnType spawnType, int minAngle, int maxAngle) {
		ArrayList spawnList = this.spawnPoints.get(spawnType);
		if (spawnList.size() == 0) {
			return null;
		}

		if (!this.sorted) {
			Collections.sort(spawnList);
			this.sorted = true;
		}

		this.angleDesired.setAngle(minAngle);
		int start = Collections.binarySearch(spawnList, this.angleDesired);
		if (start < 0) {
			start = -start - 1;
		}
		this.angleDesired.setAngle(maxAngle);
		int end = Collections.binarySearch(spawnList, this.angleDesired);
		if (end < 0) {
			end = -end - 1;
		}
		if (end > start) {
			return (SpawnPoint) spawnList.get(start + this.random.nextInt(end - start));
		}
		if ((start > end) && (end > 0)) {
			int r = start + this.random.nextInt(spawnList.size() + end - start);
			if (r >= spawnList.size()) {
				r -= spawnList.size();
			}
			return (SpawnPoint) spawnList.get(r);
		}
		return null;
	}

	public int getNumberOfSpawnPoints(SpawnType type) {
		return ((ArrayList) this.spawnPoints.get(type)).size();
	}

	public int getNumberOfSpawnPoints(SpawnType spawnType, int minAngle, int maxAngle) {
		ArrayList spawnList = this.spawnPoints.get(spawnType);
		if ((spawnList.size() == 0) || (maxAngle - minAngle >= 360)) {
			return spawnList.size();
		}

		if (!this.sorted) {
			Collections.sort(spawnList);
			this.sorted = true;
		}

		this.angleDesired.setAngle(minAngle);
		int start = Collections.binarySearch(spawnList, this.angleDesired);
		if (start < 0) {
			start = -start - 1;
		}
		this.angleDesired.setAngle(maxAngle);
		int end = Collections.binarySearch(spawnList, this.angleDesired);
		if (end < 0) {
			end = -end - 1;
		}
		if (end > start) {
			return end - start;
		}
		if ((start > end) && (end > 0)) {
			return end + spawnList.size() - start;
		}
		return 0;
	}

	public void pointDisplayTest(Block block, World world) {
		ArrayList points = this.spawnPoints.get(SpawnType.HUMANOID);
		SpawnPoint point = null;
		for (int i = 0; i < points.size(); i++) {
			point = (SpawnPoint) points.get(i);
			world.setBlockState(new BlockPos(point.getPos().getX(), point.getPos().getY(), point.getPos().getZ()),
					block.getDefaultState());
		}
	}
}