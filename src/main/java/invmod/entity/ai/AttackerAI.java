// `^`^`^`
// ```java
// /**
//  * This Java class, AttackerAI, is part of a package designed to manage the artificial intelligence of attackers in a game, specifically those targeting a Nexus entity. The class is responsible for pathfinding, scaffold generation, and entity density management to facilitate strategic movement and construction activities of AI entities.
// 
//  * Key methods include:
//  * - update(): Manages timers for scaffold generation and entity density updates.
//  * - wrapEntityData(IBlockAccess terrainMap): Wraps terrain data with entity density information.
//  * - getMinDistanceBetweenScaffolds(): Retrieves the minimum distance allowed between scaffolds.
//  * - getScaffolds(): Returns a list of current scaffolds.
//  * - askGenerateScaffolds(EntityIMMob entity): Determines if new scaffolds should be generated based on conditions.
//  * - findMinScaffolds(IPathfindable pather, BlockPos pos): Finds the minimum scaffolds required for a path.
//  * - addScaffoldDataTo(IBlockAccessExtended terrainMap): Adds scaffold data to the terrain map.
//  * - getScaffoldAt(BlockPos pos): Retrieves a scaffold at a specific position.
//  * - onResume(): Forces a status update on all scaffolds.
//  * - readFromNBT(NBTTagCompound nbttagcompound): Reads scaffold data from a saved state.
//  * - writeToNBT(NBTTagCompound nbttagcompound): Writes scaffold data to a saved state.
// 
//  * The class also contains private methods for path creation and chunk caching, which are used internally to manage pathfinding and terrain analysis.
//  */
// ```
// ```plaintext
// This code is part of a system designed to manage and update scaffolding structures within a simulated environment, likely a game or simulation software. The code includes several methods that interact with scaffold objects and possibly other entities within the environment.
// 
// 1. `determineScaffoldOrientation`: This method calculates the optimal orientation for a given scaffold based on the surrounding environment. It checks adjacent blocks at a certain height and determines the direction with the most supporting blocks, setting the scaffold's orientation accordingly.
// 
// 2. `addNewScaffolds`: This method integrates new scaffolds into the existing scaffold collection. It compares the positions of new and existing scaffolds, updating the height and position of existing scaffolds if the new ones overlap or extend beyond them in the vertical space.
// 
// 3. `updateScaffolds`: This method iterates through the current scaffolds, spawning a visual effect (particle) at their location and forcing a status update on each scaffold. It also removes scaffolds from the collection if their integrity falls below a certain threshold relative to their completion status.
// 
// 4. `updateDensityData`: This method updates a map that tracks the density of living entities (mobs) around the scaffolds. It records the number of entities at each location, ensuring that the density does not exceed a specified limit.
// 
// Overall, the code is responsible for maintaining the structural integrity and proper placement of scaffolds in relation to the environment and other entities, ensuring that they are correctly oriented, do not overlap in undesirable ways, and are removed when they are no longer structurally sound.
// ```
// `^`^`^`

package invmod.entity.ai;

import java.util.ArrayList;
import java.util.List;

//NOOB HAUS: Done

import invmod.IBlockAccessExtended;
import invmod.IPathfindable;
import invmod.TerrainDataLayer;
import invmod.entity.EntityIMLiving;
import invmod.entity.IPathSource;
import invmod.entity.Scaffold;
import invmod.entity.ai.navigator.Path;
import invmod.entity.ai.navigator.PathAction;
import invmod.entity.ai.navigator.PathCreator;
import invmod.entity.ai.navigator.PathNode;
import invmod.entity.monster.EntityIMMob;
import invmod.tileentity.TileEntityNexus;
import invmod.util.Coords;
import invmod.util.Distance;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;

public class AttackerAI {
	private TileEntityNexus nexus;
	private IPathSource pathSource;
	private IntHashMap entityDensityData;
	private List<Scaffold> scaffolds;
	private int scaffoldLimit;
	private int minDistanceBetweenScaffolds;
	private int nextScaffoldCalcTimer;
	private int updateScaffoldTimer;
	private int nextEntityDensityUpdate;

	public AttackerAI(TileEntityNexus nexus) {
		this.nexus = nexus;
		this.pathSource = new PathCreator();
		this.pathSource.setSearchDepth(8500);
		this.pathSource.setQuickFailDepth(8500);
		this.entityDensityData = new IntHashMap();
		this.scaffolds = new ArrayList();
	}

	public void update() {
		this.nextScaffoldCalcTimer -= 1;
		if (--this.updateScaffoldTimer <= 0) {
			this.updateScaffoldTimer = 40;
			this.updateScaffolds();

			this.scaffoldLimit = (2 + this.nexus.getCurrentWave() / 2);
			this.minDistanceBetweenScaffolds = (90 / (this.nexus.getCurrentWave() + 10));
		}

		if (--this.nextEntityDensityUpdate <= 0) {
			this.nextEntityDensityUpdate = 20;
			this.updateDensityData();
		}
	}

	public IBlockAccessExtended wrapEntityData(IBlockAccess terrainMap) {
		TerrainDataLayer newTerrain = new TerrainDataLayer(terrainMap);
		newTerrain.setAllData(this.entityDensityData);
		return newTerrain;
	}

	public int getMinDistanceBetweenScaffolds() {
		return this.minDistanceBetweenScaffolds;
	}

	public List<Scaffold> getScaffolds() {
		return this.scaffolds;
	}

	public boolean askGenerateScaffolds(EntityIMMob entity) {
		if ((this.nextScaffoldCalcTimer > 0) || (this.scaffolds.size() > this.scaffoldLimit)) {
			return false;
		}
		this.nextScaffoldCalcTimer = 200;
		List newScaffolds = this.findMinScaffolds(entity, new BlockPos(MathHelper.floor(entity.posX),
				MathHelper.floor(entity.posY), MathHelper.floor(entity.posZ)));
		if ((newScaffolds != null) && (newScaffolds.size() > 0)) {
			this.addNewScaffolds(newScaffolds);
			return true;
		}

		return false;
	}

	public List<Scaffold> findMinScaffolds(IPathfindable pather, BlockPos pos) {
		Scaffold scaffold = new Scaffold(this.nexus);
		scaffold.setPathfindBase(pather);
		Path basePath = this.createPath(scaffold, pos, this.nexus.getPos(), 12.0F);
		if (basePath == null) {
			return new ArrayList();
		}
		List scaffoldPositions = this.extractScaffolds(basePath);
		if (scaffoldPositions.size() > 1) {
			float lowestCost = (1.0F / 1.0F);
			int lowestCostIndex = -1;
			for (int i = 0; i < scaffoldPositions.size(); i++) {
				TerrainDataLayer terrainMap = new TerrainDataLayer(
						this.getChunkCache(pos.getX(), pos.getY(), pos.getZ(), this.nexus.getPos().getX(),
								this.nexus.getPos().getY(), this.nexus.getPos().getZ(), 12.0F));
				Scaffold s = (Scaffold) scaffoldPositions.get(i);
				terrainMap.setData(s.getPos().x, s.getPos().y, s.getPos().z, Integer.valueOf(200000));
				Path path = this.createPath(pather, pos, this.nexus.getPos(), terrainMap);
				if ((path.getTotalPathCost() < lowestCost) && (path.getFinalPathPoint().equals(this.nexus.getPos()))) {
					lowestCostIndex = i;
				}
			}

			if (lowestCostIndex >= 0) {
				List s = new ArrayList();
				s.add(scaffoldPositions.get(lowestCostIndex));
				return s;
			}

			List costDif = new ArrayList(scaffoldPositions.size());
			for (int i = 0; i < scaffoldPositions.size(); i++) {
				TerrainDataLayer terrainMap = new TerrainDataLayer(
						this.getChunkCache(pos.getX(), pos.getY(), pos.getZ(), this.nexus.getPos().getX(),
								this.nexus.getPos().getY(), this.nexus.getPos().getZ(), 12.0F));
				Scaffold s = (Scaffold) scaffoldPositions.get(i);
				for (int j = 0; j < scaffoldPositions.size(); j++) {
					if (j != i) {
						terrainMap.setData(s.getPos().x, s.getPos().y, s.getPos().z, Integer.valueOf(200000));
					}
				}
				Path path = this.createPath(pather, pos, this.nexus.getPos(), terrainMap);

				if (!path.getFinalPathPoint().equals(this.nexus.getPos().getX(), this.nexus.getPos().getY(),
						this.nexus.getPos().getZ())) {
					costDif.add(s);
				}

			}

			return costDif;
		}

		if (scaffoldPositions.size() == 1) {
			return scaffoldPositions;
		}

		return null;
	}

	public void addScaffoldDataTo(IBlockAccessExtended terrainMap) {
		for (Scaffold scaffold : this.scaffolds) {
			for (int i = 0; i < scaffold.getTargetHeight(); i++) {
				int data = terrainMap.getLayeredData(scaffold.getPos().x, scaffold.getPos().y + i, scaffold.getPos().z);
				terrainMap.setData(scaffold.getPos().x, scaffold.getPos().y + i, scaffold.getPos().z,
						Integer.valueOf(data | 0x4000));
			}
		}
	}

	public Scaffold getScaffoldAt(BlockPos pos) {
		return this.getScaffoldAt(pos.getX(), pos.getY(), pos.getZ());
	}

	public Scaffold getScaffoldAt(int x, int y, int z) {
		for (Scaffold scaffold : this.scaffolds) {
			if ((scaffold.getPos().x == x) && (scaffold.getPos().z == z)) {
				if ((scaffold.getPos().y <= y) && (scaffold.getPos().y + scaffold.getTargetHeight() >= y))
					return scaffold;
			}
		}
		return null;
	}

	public void onResume() {
		for (Scaffold scaffold : this.scaffolds) {
			scaffold.forceStatusUpdate();
		}
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		// Had to put extra int param in 1.7.2, used 0 not sure why
		NBTTagList nbtScaffoldList = nbttagcompound.getTagList("scaffolds", 0);
		for (int i = 0; i < nbtScaffoldList.tagCount(); i++) {
			Scaffold scaffold = new Scaffold(this.nexus);
			scaffold.readFromNBT(nbtScaffoldList.getCompoundTagAt(i));
			this.scaffolds.add(scaffold);
		}
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		NBTTagList nbttaglist = new NBTTagList();
		for (Scaffold scaffold : this.scaffolds) {
			NBTTagCompound nbtscaffold = new NBTTagCompound();
			scaffold.writeToNBT(nbtscaffold);
			nbttaglist.appendTag(nbtscaffold);
		}
		nbttagcompound.setTag("scaffolds", nbttaglist);
	}

	private Path createPath(IPathfindable pather, Vec3d pos0, Vec3d pos1, IBlockAccess terrainMap) {
		return this.pathSource.createPath(pather, pos0, pos1, 1.1F,
				12.0F + (float) Distance.distanceBetween(pos0, pos1), terrainMap);
	}

	private Path createPath(IPathfindable pather, Vec3d pos0, Vec3d pos1, float axisExpand) {
		TerrainDataLayer terrainMap = new TerrainDataLayer(
				this.getChunkCache(new BlockPos(pos0), new BlockPos(pos1), axisExpand));
		this.addScaffoldDataTo(terrainMap);
		return this.createPath(pather, pos0, pos1, terrainMap);
	}

	private Path createPath(IPathfindable pather, BlockPos pos0, BlockPos pos1, IBlockAccess terrainMap) {
		return this.createPath(pather, new Vec3d(pos0.getX(), pos0.getY(), pos0.getZ()),
				new Vec3d(pos1.getX(), pos1.getY(), pos1.getZ()), terrainMap);
	}

	private Path createPath(IPathfindable pather, BlockPos pos0, BlockPos pos1, float axisExpand) {
		return this.createPath(pather, new Vec3d(pos0.getX(), pos0.getY(), pos0.getZ()),
				new Vec3d(pos1.getX(), pos1.getY(), pos1.getZ()), axisExpand);
	}

	private ChunkCache getChunkCache(BlockPos pos0, BlockPos pos1, float axisExpand) {
		return this.getChunkCache(pos0.getX(), pos0.getY(), pos0.getZ(), pos1.getX(), pos1.getY(), pos1.getZ(),
				axisExpand);
	}

	private ChunkCache getChunkCache(int x1, int y1, int z1, int x2, int y2, int z2, float axisExpand) {
		int d = (int) axisExpand;
		int cX2;
		int cX1;
		if (x1 < x2) {
			cX1 = x1 - d;
			cX2 = x2 + d;
		} else {
			cX2 = x1 + d;
			cX1 = x2 - d;
		}
		int cY2;
		int cY1;
		if (y1 < y2) {
			cY1 = y1 - d;
			cY2 = y2 + d;
		} else {
			cY2 = y1 + d;
			cY1 = y2 - d;
		}
		int cZ2;
		int cZ1;
		if (z1 < z2) {
			cZ1 = z1 - d;
			cZ2 = z2 + d;
		} else {
			cZ2 = z1 + d;
			cZ1 = z2 - d;
		}
		BlockPos blockPos = new BlockPos(cX1, cY1, cZ1);
		BlockPos blockPos2 = new BlockPos(cX2, cY2, cZ2);
		return new ChunkCache(this.nexus.getWorld(), blockPos, blockPos2, 0);
	}

	private List<Scaffold> extractScaffolds(Path path) {
		List scaffoldPositions = new ArrayList();
		boolean flag = false;
		double startHeight = 0d;
		for (int i = 0; i < path.getCurrentPathLength(); i++) {
			PathNode node = path.getPathPointFromIndex(i);
			if (!flag) {
				if (node.action == PathAction.SCAFFOLD_UP) {
					flag = true;
					startHeight = node.pos.y - 1;
				}

			} else if (node.action != PathAction.SCAFFOLD_UP) {
				Scaffold scaffold = new Scaffold(node.getPrevious().pos.x, startHeight, node.getPrevious().pos.z,
						MathHelper.floor(node.pos.y - startHeight), this.nexus);
				this.orientScaffold(scaffold, this.nexus.getWorld());
				scaffold.setInitialIntegrity();
				scaffoldPositions.add(scaffold);
				flag = false;
			}
		}

		return scaffoldPositions;
	}

	private void orientScaffold(Scaffold scaffold, IBlockAccess terrainMap) {
		int mostBlocks = 0;
		int highestDirectionIndex = 0;
		for (int i = 0; i < 4; i++) {
			int blockCount = 0;
			for (int height = 0; height < scaffold.getPos().y; height++) {
				if (terrainMap
						.getBlockState(new BlockPos(
								scaffold.getPos().addVector(Coords.offsetAdjX[i], height, Coords.offsetAdjZ[i])))
						.isNormalCube()) {
					blockCount++;
				}
				if (terrainMap.getBlockState(new BlockPos(
						scaffold.getPos().addVector(Coords.offsetAdjX[i] * 2, height, Coords.offsetAdjZ[i] * 2)))
						.isNormalCube()) {
					blockCount++;
				}
			}
			if (blockCount > mostBlocks) {
				highestDirectionIndex = i;
			}
		}
		scaffold.setOrientation(highestDirectionIndex);
	}

	private void addNewScaffolds(List<Scaffold> newScaffolds) {
		for (Scaffold newScaffold : newScaffolds) {
			for (Scaffold existingScaffold : this.scaffolds) {
				if ((existingScaffold.getPos().x == newScaffold.getPos().x)
						&& (existingScaffold.getPos().z == newScaffold.getPos().z)) {
					if (newScaffold.getPos().y > existingScaffold.getPos().y) {
						if (newScaffold.getPos().y < existingScaffold.getPos().y + existingScaffold.getTargetHeight()) {
							existingScaffold.setHeight(MathHelper.floor(newScaffold.getPos().y
									+ newScaffold.getTargetHeight() - existingScaffold.getPos().y));
							break;
						}

					} else if (newScaffold.getPos().x + newScaffold.getTargetHeight() > existingScaffold.getPos().y) {
						existingScaffold.setPosition(newScaffold.getPos());
						existingScaffold.setHeight(MathHelper.floor(existingScaffold.getPos().y
								+ existingScaffold.getTargetHeight() - newScaffold.getPos().y));
						break;
					}
				}

			}

			this.scaffolds.add(newScaffold);
		}
	}

	private void updateScaffolds() {
		for (int i = 0; i < this.scaffolds.size(); i++) {
			Scaffold lol = this.scaffolds.get(i);
			this.nexus.getWorld().spawnParticle(EnumParticleTypes.HEART, lol.getPos().x + 0.2D, lol.getPos().y + 0.2D,
					lol.getPos().z + 0.2D, lol.getPos().x + 0.5D, lol.getPos().y + 0.5D, lol.getPos().z + 0.5D);

			this.scaffolds.get(i).forceStatusUpdate();
			if (this.scaffolds.get(i).getPercentIntactCached() + 0.05F < 0.4F
					* this.scaffolds.get(i).getPercentCompletedCached())
				this.scaffolds.remove(i);
		}
	}

	private void updateDensityData() {
		this.entityDensityData.clearMap();
		List<EntityIMLiving> mobs = this.nexus.getMobList();
		for (EntityIMLiving mob : mobs) {
			int coordHash = PathNode.makeHash(mob.posX, mob.posY, mob.posZ, PathAction.NONE);
			if (this.entityDensityData.containsItem(coordHash)) {
				Integer value = (Integer) this.entityDensityData.lookup(coordHash);
				if (value.intValue() < 7) {
					this.entityDensityData.addKey(coordHash, Integer.valueOf(value.intValue() + 1));
				}
			} else {
				this.entityDensityData.addKey(coordHash, Integer.valueOf(1));
			}
		}
	}
}