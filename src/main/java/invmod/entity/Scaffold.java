// `^`^`^`
// ```java
// /**
//  * This class represents a Scaffold structure used by entities for pathfinding and navigation in the game Minecraft.
//  * It implements the IPathfindable interface, allowing entities to interact with the scaffold as part of their pathfinding logic.
//  *
//  * Constructors:
//  * - Scaffold(TileEntityNexus nexus): Initializes a scaffold with a reference to a Nexus.
//  * - Scaffold(double x, double y, double z, int height, TileEntityNexus nexus): Initializes a scaffold at a specific position with a target height.
//  * - Scaffold(Vec3d vec, int height, TileEntityNexus nexus): Initializes a scaffold with a Vec3d position and target height.
//  *
//  * Public Methods:
//  * - setPosition(Vec3d vec): Sets the position of the scaffold.
//  * - setPosition(double x, double y, double z): Sets the position of the scaffold using individual coordinates.
//  * - setInitialIntegrity(): Sets the initial integrity of the scaffold based on its current state.
//  * - setOrientation(int i): Sets the orientation of the scaffold.
//  * - getOrientation(): Returns the orientation of the scaffold.
//  * - setHeight(int height): Sets the target height of the scaffold and recalculates platform positions.
//  * - getTargetHeight(): Returns the target height of the scaffold.
//  * - forceStatusUpdate(): Forces an update of the scaffold's integrity status.
//  * - getPercentIntactCached(): Returns the cached percentage of the scaffold's integrity.
//  * - getPercentCompletedCached(): Returns the cached percentage of the scaffold's completion.
//  * - getPos(): Returns the position of the scaffold as a Vec3d.
//  * - getNexus(): Returns the associated TileEntityNexus.
//  * - setPathfindBase(IPathfindable base): Sets the base pathfinding behavior for the scaffold.
//  * - isLayerPlatform(int height): Checks if a given height corresponds to a platform layer.
//  * - readFromNBT(NBTTagCompound nbttagcompound): Reads the scaffold's data from NBT.
//  * - writeToNBT(NBTTagCompound nbttagcompound): Writes the scaffold's data to NBT.
//  *
//  * Private Methods:
//  * - calcPlatforms(): Calculates the positions of platforms within the scaffold.
//  * - evaluateIntegrity(): Evaluates the current integrity of the scaffold based on its structure.
//  *
//  * Interface Implementation:
//  * - getBlockPathCost(PathNode prevNode, PathNode node, IBlockAccess terrainMap): Calculates the path cost for a block.
//  * - getPathOptionsFromNode(IBlockAccess terrainMap, PathNode currentNode, PathfinderIM pathFinder): Provides path options from a given node.
//  *
//  * The class also includes a private method to determine if a block should be avoided during pathfinding.
//  */
// ```
// `^`^`^`

package invmod.entity;

import java.util.List;

import invmod.IPathfindable;
import invmod.entity.ai.navigator.PathAction;
import invmod.entity.ai.navigator.PathNode;
import invmod.entity.ai.navigator.PathfinderIM;
import invmod.tileentity.TileEntityNexus;
import invmod.util.Coords;
import invmod.util.Distance;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Scaffold implements IPathfindable {

	private static final int MIN_SCAFFOLD_HEIGHT = 4;
	// private int xCoord;
	// private int yCoord;
	// private int zCoord;
	private Vec3d vec;
	private int targetHeight;
	private int orientation;
	private int[] platforms;
	private IPathfindable pathfindBase;
	private TileEntityNexus nexus;
	private float latestPercentCompleted;
	private float latestPercentIntact;
	private float initialCompletion;

	public Scaffold(TileEntityNexus nexus) {
		this.nexus = nexus;
		this.initialCompletion = 0.01F;
		this.calcPlatforms();
	}

	public Scaffold(double x, double y, double z, int height, TileEntityNexus nexus) {
		this(new Vec3d(x, y, z), height, nexus);
	}

	public Scaffold(Vec3d vec, int height, TileEntityNexus nexus) {
		this.vec = vec;
		this.targetHeight = height;
		this.latestPercentCompleted = 0.0F;
		this.latestPercentIntact = 0.0F;
		this.initialCompletion = 0.01F;
		this.nexus = nexus;
		this.calcPlatforms();
	}

	public void setPosition(Vec3d vec) {
		this.vec = vec;
	}

	public void setPosition(double x, double y, double z) {
		this.vec = new Vec3d(x, y, z);
	}

	public void setInitialIntegrity() {
		this.initialCompletion = this.evaluateIntegrity();
		if (this.initialCompletion == 0.0F)
			this.initialCompletion = 0.01F;
	}

	public void setOrientation(int i) {
		this.orientation = i;
	}

	public int getOrientation() {
		return this.orientation;
	}

	public void setHeight(int height) {
		this.targetHeight = height;
		this.calcPlatforms();
	}

	public int getTargetHeight() {
		return this.targetHeight;
	}

	public void forceStatusUpdate() {
		this.latestPercentIntact = ((this.evaluateIntegrity() - this.initialCompletion)
				/ (1.0F - this.initialCompletion));
		if (this.latestPercentIntact > this.latestPercentCompleted)
			this.latestPercentCompleted = this.latestPercentIntact;
	}

	public float getPercentIntactCached() {
		return this.latestPercentIntact;
	}

	public float getPercentCompletedCached() {
		return this.latestPercentCompleted;
	}

	public Vec3d getPos() {
		return this.vec;
	}

	public TileEntityNexus getNexus() {
		return this.nexus;
	}

	public void setPathfindBase(IPathfindable base) {
		this.pathfindBase = base;
	}

	public boolean isLayerPlatform(int height) {
		if (height == this.targetHeight - 1) {
			return true;
		}
		if (this.platforms != null) {
			for (int i : this.platforms) {
				if (i == height)
					return true;
			}
		}
		return false;
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		this.vec = new Vec3d(nbttagcompound.getDouble("xCoord"), nbttagcompound.getDouble("yCoord"),
				nbttagcompound.getDouble("zCoord"));
		this.targetHeight = nbttagcompound.getInteger("targetHeight");
		this.orientation = nbttagcompound.getInteger("orientation");
		this.initialCompletion = nbttagcompound.getFloat("initialCompletion");
		this.latestPercentCompleted = nbttagcompound.getFloat("latestPercentCompleted");
		this.calcPlatforms();
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setDouble("xCoord", this.vec.x);
		nbttagcompound.setDouble("yCoord", this.vec.y);
		nbttagcompound.setDouble("zCoord", this.vec.z);
		nbttagcompound.setInteger("targetHeight", this.targetHeight);
		nbttagcompound.setInteger("orientation", this.orientation);
		nbttagcompound.setFloat("initialCompletion", this.initialCompletion);
		nbttagcompound.setFloat("latestPercentCompleted", this.latestPercentCompleted);
	}

	private void calcPlatforms() {
		int spanningPlatforms = this.targetHeight < 16 ? this.targetHeight / 4 - 1 : this.targetHeight / 5 - 1;
		if (spanningPlatforms > 0) {
			int avgSpace = this.targetHeight / (spanningPlatforms + 1);
			int remainder = this.targetHeight % (spanningPlatforms + 1) - 1;
			this.platforms = new int[spanningPlatforms];
			for (int i = 0; i < spanningPlatforms; i++) {
				this.platforms[i] = (avgSpace * (i + 1) - 1);
			}

			int i = spanningPlatforms - 1;
			while (remainder > 0) {
				this.platforms[i] += 1;
				if (i-- < 0) {
					i = spanningPlatforms - 1;
					remainder--;
				}
				remainder--;
			}
		} else {
			this.platforms = new int[0];
		}
	}

	private float evaluateIntegrity() {
		if (this.nexus != null) {
			int existingMainSectionBlocks = 0;
			int existingMainLadderBlocks = 0;
			int existingPlatformBlocks = 0;
			World world = this.nexus.getWorld();
			for (int i = 0; i < this.targetHeight; i++) {
				// set bool true, donno why
				IBlockState blockState0 = world.getBlockState(new BlockPos(this.vec
						.addVector(Coords.offsetAdjX[this.orientation], i, Coords.offsetAdjZ[this.orientation])));
				if (blockState0.isOpaqueCube()/* isSolidFullCube() */) {
					existingMainSectionBlocks++;
				}
				if (world.getBlockState(new BlockPos(this.vec.addVector(0d, i, 0d))).getBlock() == Blocks.LADDER) {
					existingMainLadderBlocks++;
				}
				if (this.isLayerPlatform(i)) {
					for (int j = 0; j < 8; j++) {
						BlockPos pos = new BlockPos(
								this.vec.addVector(Coords.offsetRing1X[j], i, Coords.offsetRing1Z[j]));
						IBlockState blockState1 = world.getBlockState(pos);
						if (blockState1.isSideSolid(world, pos,
								EnumFacing.UP)/* .isFullyOpaque() *//* .isSolidFullCube() */) {
							existingPlatformBlocks++;
						}
					}
				}
			}

			float mainSectionPercent = this.targetHeight > 0 ? existingMainSectionBlocks / this.targetHeight : 0.0F;
			float ladderPercent = this.targetHeight > 0 ? existingMainLadderBlocks / this.targetHeight : 0.0F;

			return 0.7F * (0.7F * mainSectionPercent + 0.3F * ladderPercent)
					+ 0.3F * (existingPlatformBlocks / ((this.platforms.length + 1) * 8));
		}
		return 0.0F;
	}

	@Override
	public float getBlockPathCost(PathNode prevNode, PathNode node, IBlockAccess terrainMap) {
		IBlockState blockState = terrainMap.getBlockState(new BlockPos(node.pos));
		float materialMultiplier = blockState.getMaterial().isSolid() ? 2.2F : 1.0F;
		if (node.action == PathAction.SCAFFOLD_UP) {
			if (prevNode.action != PathAction.SCAFFOLD_UP) {
				materialMultiplier *= 3.4F;
			}
			return prevNode.distanceTo(node) * 0.85F * materialMultiplier;
		}
		if (node.action == PathAction.BRIDGE) {
			if (prevNode.action == PathAction.SCAFFOLD_UP) {
				materialMultiplier = 0.0F;
			}
			return prevNode.distanceTo(node) * 1.1F * materialMultiplier;
		}
		if ((node.action == PathAction.LADDER_UP_NX) || (node.action == PathAction.LADDER_UP_NZ)
				|| (node.action == PathAction.LADDER_UP_PX) || (node.action == PathAction.LADDER_UP_PZ)) {
			return prevNode.distanceTo(node) * 1.5F * materialMultiplier;
		}
		if (this.pathfindBase != null) {
			return this.pathfindBase.getBlockPathCost(prevNode, node, terrainMap);
		}
		return prevNode.distanceTo(node);
	}

	@Override
	public void getPathOptionsFromNode(IBlockAccess terrainMap, PathNode currentNode, PathfinderIM pathFinder) {
		if (this.pathfindBase != null) {
			this.pathfindBase.getPathOptionsFromNode(terrainMap, currentNode, pathFinder);
		}
		Block block = terrainMap
				.getBlockState(new BlockPos(currentNode.pos.x, currentNode.pos.y + 1, currentNode.pos.z)).getBlock();
		if ((currentNode.getPrevious() != null) && (currentNode.getPrevious().action == PathAction.SCAFFOLD_UP)
				&& (!this.avoidsBlock(block))) {
			pathFinder.addNode(currentNode.pos.addVector(0d, 1d, 0d), PathAction.SCAFFOLD_UP);
			return;
		}
		int minDistance;
		if (this.nexus != null) {
			List<Scaffold> scaffolds = this.nexus.getAttackerAI().getScaffolds();
			minDistance = this.nexus.getAttackerAI().getMinDistanceBetweenScaffolds();
			for (int sl = scaffolds.size() - 1; sl >= 0; sl--) {
				Scaffold scaffold = scaffolds.get(sl);
				if (Distance.distanceBetween(scaffold.getPos(), currentNode.pos) < minDistance) {
					return;
				}
			}
		}

		IBlockState blockState = terrainMap.getBlockState(new BlockPos(currentNode.pos.subtract(0d, 2d, 0d)));
		if ((block == Blocks.AIR) && (blockState.getMaterial().isSolid())) {
			boolean flag = false;
			for (int i = 1; i < 4; i++) {
				if (terrainMap.getBlockState(new BlockPos(currentNode.pos.addVector(0d, (double) i, 0d)))
						.getBlock() != Blocks.AIR) {
					flag = true;
					break;
				}
			}

			if (!flag)
				pathFinder.addNode(currentNode.pos.addVector(0d, 1d, 0d), PathAction.SCAFFOLD_UP);
		}
	}

	private boolean avoidsBlock(Block block) {
		if ((block == Blocks.FIRE) || (block == Blocks.BEDROCK) || (block == Blocks.WATER)
				|| (block == Blocks.FLOWING_WATER) || (block == Blocks.LAVA) || (block == Blocks.FLOWING_LAVA)) {
			return true;
		}
		return false;
	}

}
