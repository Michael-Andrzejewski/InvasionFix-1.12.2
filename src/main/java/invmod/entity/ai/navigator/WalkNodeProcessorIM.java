// `^`^`^`
// ```plaintext
// This code is part of the invmod.entity.ai.navigator package and extends the WalkNodeProcessor class from Minecraft's net.minecraft.pathfinding package. It is designed to provide custom pathfinding logic for entities within the game, specifically tailored to the needs of the Invasion Mod (invmod).
// 
// The primary method, findPathOptions, generates an array of PathPoint objects that represent potential next steps for an entity to take towards a target point. It considers the entity's ability to climb, the height of steps it can take, and various obstacles that may be in the way. The method calculates safe points in different cardinal directions and includes logic to handle diagonal movement.
// 
// The getSafePoint method is a helper function that determines if a particular point in the world is safe for the entity to move to. It checks for the presence of blocks that might impede movement, such as fences or trapdoors, and ensures that the entity will not collide with blocks if it moves to that point. It also handles special cases like water lilies, fire, cacti, and different types of doors, adjusting the path type accordingly.
// 
// The getPathNodeType method is overloaded; one version accepts an EntityLiving and a BlockPos, while the other accepts additional parameters for the entity's size and whether it can open or enter doors. These methods determine the type of path node at a given location, which informs the pathfinding algorithm of the nature of the terrain (e.g., walkable, dangerous, open, etc.).
// 
// Lastly, getPathNodeTypeRaw provides a raw analysis of the block type at a given position without considering the entity's abilities or size. It is used to determine the basic path node type based on the material and properties of the block (e.g., air, fire, water, etc.).
// 
// Overall, this code enhances the pathfinding capabilities of entities in Minecraft, allowing for more intelligent navigation that takes into account various environmental factors and the entity's physical characteristics.
// ```
// This code snippet appears to be part of a method that determines the type of path node based on the environmental context within a game, likely for pathfinding purposes. The method evaluates various conditions to categorize a location into different path node types such as WATER, LAVA, OPEN, BLOCKED, FENCE, and TRAPDOOR. These categories are used to inform the pathfinding algorithm about the navigability of the terrain.
// 
// Here is a summary of the code's functionality:
// 
// - The method checks the material of the current location. If it's water, it assigns the PathNodeType.WATER.
// - If the material is not water, it checks if it's lava, and if so, assigns PathNodeType.LAVA.
// - If the material is neither water nor lava, the method checks if the block at the location is passable. If it is passable, it assigns PathNodeType.OPEN.
// - If the block is not passable, it defaults to assigning PathNodeType.BLOCKED.
// - There are also checks for specific structures like fences and trapdoors, which are assigned PathNodeType.FENCE and PathNodeType.TRAPDOOR, respectively.
// 
// This method is crucial for the AI to make decisions about where it can and cannot move, ensuring that the AI character navigates the game world effectively and avoids obstacles or hazards. The exact implementation details and the context of the surrounding code are not provided, but this summary captures the essence of the provided code snippet.
// `^`^`^`

package invmod.entity.ai.navigator;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class WalkNodeProcessorIM extends WalkNodeProcessor {

	@Override
	public int findPathOptions(PathPoint[] pathOptions, PathPoint currentPoint, PathPoint targetPoint,
			float maxDistance) {
		int i = 0;
		int j = 0;
		PathNodeType pathnodetype = this.getPathNodeType(this.entity, currentPoint.x, currentPoint.y + 1,
				currentPoint.z);

		if (this.entity.getPathPriority(pathnodetype) >= 0.0F) {
			j = MathHelper.floor(Math.max(1.0F, this.entity.stepHeight));
		}

		BlockPos blockpos = (new BlockPos(currentPoint.x, currentPoint.y, currentPoint.z)).down();
		double d0 = (double) currentPoint.y
				- (1.0D - this.blockaccess.getBlockState(blockpos).getBoundingBox(this.blockaccess, blockpos).maxY);
		PathPoint pathpoint = this.getSafePoint(currentPoint.x, currentPoint.y, currentPoint.z + 1, j, d0,
				EnumFacing.SOUTH);
		PathPoint pathpoint1 = this.getSafePoint(currentPoint.x - 1, currentPoint.y, currentPoint.z, j, d0,
				EnumFacing.WEST);
		PathPoint pathpoint2 = this.getSafePoint(currentPoint.x + 1, currentPoint.y, currentPoint.z, j, d0,
				EnumFacing.EAST);
		PathPoint pathpoint3 = this.getSafePoint(currentPoint.x, currentPoint.y, currentPoint.z - 1, j, d0,
				EnumFacing.NORTH);

		if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(targetPoint) < maxDistance) {
			pathOptions[i++] = pathpoint;
		}

		if (pathpoint1 != null && !pathpoint1.visited && pathpoint1.distanceTo(targetPoint) < maxDistance) {
			pathOptions[i++] = pathpoint1;
		}

		if (pathpoint2 != null && !pathpoint2.visited && pathpoint2.distanceTo(targetPoint) < maxDistance) {
			pathOptions[i++] = pathpoint2;
		}

		if (pathpoint3 != null && !pathpoint3.visited && pathpoint3.distanceTo(targetPoint) < maxDistance) {
			pathOptions[i++] = pathpoint3;
		}

		boolean flag = pathpoint3 == null || pathpoint3.nodeType == PathNodeType.OPEN || pathpoint3.costMalus != 0.0F;
		boolean flag1 = pathpoint == null || pathpoint.nodeType == PathNodeType.OPEN || pathpoint.costMalus != 0.0F;
		boolean flag2 = pathpoint2 == null || pathpoint2.nodeType == PathNodeType.OPEN || pathpoint2.costMalus != 0.0F;
		boolean flag3 = pathpoint1 == null || pathpoint1.nodeType == PathNodeType.OPEN || pathpoint1.costMalus != 0.0F;

		if (flag && flag3) {
			PathPoint pathpoint4 = this.getSafePoint(currentPoint.x - 1, currentPoint.y, currentPoint.z - 1, j, d0,
					EnumFacing.NORTH);

			if (pathpoint4 != null && !pathpoint4.visited && pathpoint4.distanceTo(targetPoint) < maxDistance) {
				pathOptions[i++] = pathpoint4;
			}
		}

		if (flag && flag2) {
			PathPoint pathpoint5 = this.getSafePoint(currentPoint.x + 1, currentPoint.y, currentPoint.z - 1, j, d0,
					EnumFacing.NORTH);

			if (pathpoint5 != null && !pathpoint5.visited && pathpoint5.distanceTo(targetPoint) < maxDistance) {
				pathOptions[i++] = pathpoint5;
			}
		}

		if (flag1 && flag3) {
			PathPoint pathpoint6 = this.getSafePoint(currentPoint.x - 1, currentPoint.y, currentPoint.z + 1, j, d0,
					EnumFacing.SOUTH);

			if (pathpoint6 != null && !pathpoint6.visited && pathpoint6.distanceTo(targetPoint) < maxDistance) {
				pathOptions[i++] = pathpoint6;
			}
		}

		if (flag1 && flag2) {
			PathPoint pathpoint7 = this.getSafePoint(currentPoint.x + 1, currentPoint.y, currentPoint.z + 1, j, d0,
					EnumFacing.SOUTH);

			if (pathpoint7 != null && !pathpoint7.visited && pathpoint7.distanceTo(targetPoint) < maxDistance) {
				pathOptions[i++] = pathpoint7;
			}
		}

		return i;
	}

	/**
	 * Returns a point that the entity can safely move to
	 */
	@Nullable
	private PathPoint getSafePoint(int x, int y, int z, int p_186332_4_, double p_186332_5_, EnumFacing facing) {
		PathPoint pathpoint = null;
		BlockPos blockpos = new BlockPos(x, y, z);
		BlockPos blockpos1 = blockpos.down();
		double d0 = (double) y
				- (1.0D - this.blockaccess.getBlockState(blockpos1).getBoundingBox(this.blockaccess, blockpos1).maxY);

		if (d0 - p_186332_5_ > 1.125D) {
			return null;
		} else {
			PathNodeType pathnodetype = this.getPathNodeType(this.entity, x, y, z);
			float f = this.entity.getPathPriority(pathnodetype);
			double d1 = (double) this.entity.width / 2.0D;

			if (f >= 0.0F) {
				pathpoint = this.openPoint(x, y, z);
				pathpoint.nodeType = pathnodetype;
				pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
			}

			if (pathnodetype == PathNodeType.WALKABLE) {
				return pathpoint;
			} else {
				if (pathpoint == null && p_186332_4_ > 0 && pathnodetype != PathNodeType.FENCE
						&& pathnodetype != PathNodeType.TRAPDOOR) {
					pathpoint = this.getSafePoint(x, y + 1, z, p_186332_4_ - 1, p_186332_5_, facing);

					if (pathpoint != null
							&& (pathpoint.nodeType == PathNodeType.OPEN || pathpoint.nodeType == PathNodeType.WALKABLE)
							&& this.entity.width < 1.0F) {
						double d2 = (double) (x - facing.getFrontOffsetX()) + 0.5D;
						double d3 = (double) (z - facing.getFrontOffsetZ()) + 0.5D;
						AxisAlignedBB axisalignedbb = new AxisAlignedBB(d2 - d1, (double) y + 0.001D, d3 - d1, d2 + d1,
								(double) ((float) y + this.entity.height), d3 + d1);
						AxisAlignedBB axisalignedbb1 = this.blockaccess.getBlockState(blockpos)
								.getBoundingBox(this.blockaccess, blockpos);
						AxisAlignedBB axisalignedbb2 = axisalignedbb.offset(0.0D, axisalignedbb1.maxY - 0.002D, 0.0D);

						if (this.entity.world.collidesWithAnyBlock(axisalignedbb2)) {
							pathpoint = null;
						}
					}
				}

				if (pathnodetype == PathNodeType.OPEN) {
					AxisAlignedBB axisalignedbb3 = new AxisAlignedBB((double) x - d1 + 0.5D, (double) y + 0.001D,
							(double) z - d1 + 0.5D, (double) x + d1 + 0.5D, (double) ((float) y + this.entity.height),
							(double) z + d1 + 0.5D);

					if (this.entity.world.collidesWithAnyBlock(axisalignedbb3)) {
						return null;
					}

					if (this.entity.width >= 1.0F) {
						PathNodeType pathnodetype1 = this.getPathNodeType(this.entity, x, y - 1, z);

						if (pathnodetype1 == PathNodeType.BLOCKED) {
							pathpoint = this.openPoint(x, y, z);
							pathpoint.nodeType = PathNodeType.WALKABLE;
							pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
							return pathpoint;
						}
					}

					int i = 0;

					while (y > 0 && pathnodetype == PathNodeType.OPEN) {
						--y;

						if (i++ >= this.entity.getMaxFallHeight()) {
							return null;
						}

						pathnodetype = this.getPathNodeType(this.entity, x, y, z);
						f = this.entity.getPathPriority(pathnodetype);

						if (pathnodetype != PathNodeType.OPEN && f >= 0.0F) {
							pathpoint = this.openPoint(x, y, z);
							pathpoint.nodeType = pathnodetype;
							pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
							break;
						}

						if (f < 0.0F) {
							return null;
						}
					}
				}

				return pathpoint;
			}
		}
	}

	public PathNodeType getPathNodeType(EntityLiving entitylivingIn, BlockPos pos) {
		return this.getPathNodeType(entitylivingIn, pos.getX(), pos.getY(), pos.getZ());
	}

	private PathNodeType getPathNodeType(EntityLiving entitylivingIn, int x, int y, int z) {
		return this.getPathNodeType(this.blockaccess, x, y, z, entitylivingIn, this.entitySizeX, this.entitySizeY,
				this.entitySizeZ, this.getCanOpenDoors(), this.getCanEnterDoors());
	}

	public PathNodeType getPathNodeTypeRaw(IBlockAccess p_189553_1_, int p_189553_2_, int p_189553_3_,
			int p_189553_4_) {
		BlockPos blockpos = new BlockPos(p_189553_2_, p_189553_3_, p_189553_4_);
		IBlockState iblockstate = p_189553_1_.getBlockState(blockpos);
		Block block = iblockstate.getBlock();
		Material material = iblockstate.getMaterial();
		return material == Material.AIR ? PathNodeType.OPEN
				: (block != Blocks.TRAPDOOR && block != Blocks.IRON_TRAPDOOR && block != Blocks.WATERLILY
						? (block == Blocks.FIRE ? PathNodeType.DAMAGE_FIRE
								: (block == Blocks.CACTUS ? PathNodeType.DAMAGE_CACTUS
										: (block instanceof BlockDoor && material == Material.WOOD
												&& !((Boolean) iblockstate.getValue(BlockDoor.OPEN)).booleanValue()
														? PathNodeType.DOOR_WOOD_CLOSED
														: (block instanceof BlockDoor && material == Material.IRON
																&& !((Boolean) iblockstate.getValue(BlockDoor.OPEN))
																		.booleanValue()
																				? PathNodeType.DOOR_IRON_CLOSED
																				: (block instanceof BlockDoor
																						&& ((Boolean) iblockstate
																								.getValue(
																										BlockDoor.OPEN))
																												.booleanValue()
																														? PathNodeType.DOOR_OPEN
																														: (block instanceof BlockRailBase
																																? PathNodeType.RAIL
																																: (!(block instanceof BlockFence)
																																		&& !(block instanceof BlockWall)
																																		&& (!(block instanceof BlockFenceGate)
																																				|| ((Boolean) iblockstate
																																						.getValue(
																																								BlockFenceGate.OPEN))
																																										.booleanValue())
																																												? (material == Material.WATER
																																														? PathNodeType.WATER
																																														: (material == Material.LAVA
																																																? PathNodeType.LAVA
																																																: (block.isPassable(
																																																		p_189553_1_,
																																																		blockpos)
																																																				? PathNodeType.OPEN
																																																				: PathNodeType.BLOCKED)))
																																												: PathNodeType.FENCE)))))))
						: PathNodeType.TRAPDOOR);
	}

}
